package com.zkc.xcplus.search.service.impl;

import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch._types.aggregations.Buckets;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.zkc.xcplus.base.exception.CustomException;
import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.search.dto.CourseIdxSearchParamDto;
import com.zkc.xcplus.search.dto.IdxSearchResultDto;
import com.zkc.xcplus.search.po.CourseIndexInfo;
import com.zkc.xcplus.search.service.CourseIdxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CourseIdxServiceImpl implements CourseIdxService {
	
	@Value("${elasticsearch.course.idxname}")
	private String courseIdxName;
	
	@Value("${elasticsearch.course.source_fields}")
	private String sourceFields;
	
	@Autowired
	private ElasticsearchOperations operations;
	
	@Override
	public boolean addCourseIdx(CourseIndexInfo courseIndexInfo) {
		if (courseIndexInfo == null) {
			CustomException.cast("课程为空");
		}
		Long id = courseIndexInfo.getId();
		if (id == null) {
			CustomException.cast("课程id为空");
		}
		IndexQueryBuilder builder = new IndexQueryBuilder();
		builder.withIndex(courseIdxName).withId(String.valueOf(id)).withOpType(IndexQuery.OpType.CREATE)
				.withObject(courseIndexInfo);
		
		String docId = operations.index(builder.build(), IndexCoordinates.of(courseIdxName));
		if (!StringUtils.hasText(docId)) {
			CustomException.cast("添加课程索引失败");
		}
		return StringUtils.hasText(docId);
	}
	
	@Override
	public boolean updateCourseIdx(CourseIndexInfo courseIndexInfo) {
		if (courseIndexInfo == null) {
			CustomException.cast("课程为空");
		}
		Long id = courseIndexInfo.getId();
		if (id == null) {
			CustomException.cast("课程id为空");
		}
		IndexQueryBuilder builder = new IndexQueryBuilder();
		builder.withId(String.valueOf(id)).withIndex(courseIdxName).withOpType(IndexQuery.OpType.INDEX)
				.withObject(courseIndexInfo);
		String docId = operations.index(builder.build(), IndexCoordinates.of(courseIdxName));
		return StringUtils.hasText(docId);
	}
	
	@Override
	public boolean deleteCourseIdx(String id) {
		if (!StringUtils.hasText(id)) {
			CustomException.cast("课程id为空");
		}
		String docId = operations.delete(id, IndexCoordinates.of(courseIdxName));
		return StringUtils.hasText(docId);
	}
	
	@Override
	public IdxSearchResultDto<CourseIndexInfo> queryCoursePublishIndex(PageParams pageParams, CourseIdxSearchParamDto searchCourseParamDto) {
		NativeQueryBuilder queryBuilder = NativeQuery.builder();
		//包含字段
		String[] sourceFieldsArr = sourceFields.split(",");
		SourceFilter sourceFilter = new FetchSourceFilter(sourceFieldsArr, new String[]{});
		queryBuilder.withSourceFilter(sourceFilter);
		//分页
		Long pageNo = pageParams.getPageNo();
		Long pageSize = pageParams.getPageSize();
		PageRequest pageRequest = PageRequest.of(pageNo.intValue() - 1, pageSize.intValue());
		queryBuilder.withPageable(pageRequest);
		//关键词匹配
		String keyword = searchCourseParamDto.getKeyword();
		String grade = searchCourseParamDto.getGrade();
		if (StringUtils.hasText(keyword)) {
			Query matchQuery1 = Query.of(f -> f.match(v -> v.field("name").query(keyword).boost(10f).minimumShouldMatch("70%")));
			Query matchQuery2 = Query.of(f -> f.match(v -> v.field("description").query(keyword).minimumShouldMatch("70%")));
			queryBuilder.withQuery(q -> q.disMax(
					m -> m.queries(Arrays.asList(matchQuery1, matchQuery2))
			));
		}
		//过滤
		String mtName = searchCourseParamDto.getMt();
		String stName = searchCourseParamDto.getSt();
		if (StringUtils.hasText(grade)) {
			queryBuilder.withFilter(f -> f.term(v -> v.field("grade").value(grade)));
		}
		if (StringUtils.hasText(mtName)) {
			queryBuilder.withFilter(f -> f.term(v -> v.field("mtName").value(mtName)));
		}
		if (StringUtils.hasText(stName)) {
			queryBuilder.withFilter(f -> f.term(v -> v.field("stName").value(stName)));
		}
		//分类聚合
		queryBuilder.withAggregation("mtAgg", AggregationBuilders.terms(f -> f.field("mtName").size(100)));
		queryBuilder.withAggregation("stAgg", AggregationBuilders.terms(f -> f.field("stName").size(100)));
		//关键字高亮
		HighlightParameters parameters = HighlightParameters.builder().withPreTags("<font class='eslight'>").withPostTags("</font>").build();
		Highlight highlight = new Highlight(parameters, List.of(new HighlightField("name")));
		HighlightQuery highlightQuery = new HighlightQuery(highlight, null);
		queryBuilder.withHighlightQuery(highlightQuery);
		//索引
		IndexCoordinates indexCoordinates = IndexCoordinates.of(courseIdxName);
		
		//查询
		SearchHits<CourseIndexInfo> searchHits = operations.search(queryBuilder.build(), CourseIndexInfo.class, indexCoordinates);
		
		//总数
		long totalHits = searchHits.getTotalHits();
		//数据列表
		List<CourseIndexInfo> courseIndexLst = new ArrayList<>();
		//取出高亮字段
		for (SearchHit<CourseIndexInfo> hit : searchHits) {
			CourseIndexInfo indexInfo = hit.getContent();
			List<String> field = hit.getHighlightField("name");
			StringBuffer sb = new StringBuffer();
			for (String s : field) {
				sb.append(s);
			}
			indexInfo.setName(sb.toString());
			courseIndexLst.add(indexInfo);
		}
		//分类聚合结果处理
		List<String> mtNameLst = new ArrayList<>();
		List<String> stNameLst = new ArrayList<>();
		if (searchHits.getAggregations() != null) {
			Map<String, ElasticsearchAggregation> aggMap = ((ElasticsearchAggregations) searchHits.getAggregations()).aggregationsAsMap();
			Buckets<StringTermsBucket> mtAggBuckets = ((StringTermsAggregate) aggMap.get("mtAgg").aggregation().getAggregate()._get()).buckets();
			for (StringTermsBucket bucket : mtAggBuckets.array()) {
				mtNameLst.add(bucket.key().stringValue());
			}
			Buckets<StringTermsBucket> stAggBuckets = ((StringTermsAggregate) aggMap.get("stAgg").aggregation().getAggregate()._get()).buckets();
			for (StringTermsBucket bucket : stAggBuckets.array()) {
				stNameLst.add(bucket.key().stringValue());
			}
		}
		//填充结果
		IdxSearchResultDto<CourseIndexInfo> ret = new IdxSearchResultDto<>(courseIndexLst, totalHits, pageNo, pageSize);
		ret.setMtList(mtNameLst);
		ret.setStList(stNameLst);
		return ret;
	}
}
