package com.zkc.xcplus.base.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * LocalDateTime和json之间的转换格式
 *
 * @author pczkc
 */
@AutoConfigureBefore(JacksonAutoConfiguration.class)
@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
public class LocalDateTimeConfig {
	
	
	/**
	 * 序列化
	 */
	@Bean
	public LocalDateTimeSerializer localDateTimeSerializer() {
		return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
	
	/**
	 * 反序列化
	 */
	@Bean
	public LocalDateTimeDeserializer localDateTimeDeserializer() {
		return new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
	
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return new Jackson2ObjectMapperBuilderCustomizer() {
			@Override
			public void customize(Jackson2ObjectMapperBuilder builder) {
				builder.locale(Locale.CHINA);
				builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
				builder.serializerByType(LocalDateTime.class, localDateTimeSerializer());
				builder.deserializerByType(LocalDateTime.class, localDateTimeDeserializer());
			}
		};
	}
}
