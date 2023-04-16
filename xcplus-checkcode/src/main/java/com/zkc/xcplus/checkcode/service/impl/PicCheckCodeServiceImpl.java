package com.zkc.xcplus.checkcode.service.impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.zkc.xcplus.base.utils.EncryptUtil;
import com.zkc.xcplus.checkcode.model.CheckCodeParamsDto;
import com.zkc.xcplus.checkcode.model.CheckCodeResultDto;
import com.zkc.xcplus.checkcode.service.AbstractCheckCodeService;
import com.zkc.xcplus.checkcode.service.CheckCodeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service("PicCheckCodeService")
public class PicCheckCodeServiceImpl extends AbstractCheckCodeService implements CheckCodeService {
	
	@Autowired
	private DefaultKaptcha kaptcha;
	
	@Resource(name = "NumberLetterCheckCodeGenerator")
	@Override
	public void setCheckCodeGenerator(CheckCodeGenerator checkCodeGenerator) {
		this.checkCodeGenerator = checkCodeGenerator;
	}
	
	@Resource(name = "UUIDKeyGenerator")
	@Override
	public void setKeyGenerator(KeyGenerator keyGenerator) {
		this.keyGenerator = keyGenerator;
	}
	
	@Resource(name = "RedisCheckCodeStore")
	@Override
	public void setCheckCodeStore(CheckCodeStore checkCodeStore) {
		this.checkCodeStore = checkCodeStore;
	}
	
	@Override
	public CheckCodeResultDto generate(CheckCodeParamsDto dto) {
		GenerateResult generate = generate(dto, 4, "checkcode", 300);
		String key = generate.getKey();
		String code = generate.getCode();
		String pic = createPic(code);
		CheckCodeResultDto resultDto = new CheckCodeResultDto();
		resultDto.setKey(key);
		resultDto.setAliasing(pic);
		return resultDto;
	}
	
	/**
	 * 生成图片验证码
	 *
	 * @param code 内容
	 * @return 图片Base64字符串
	 */
	private String createPic(String code) {
		BufferedImage image = kaptcha.createImage(code);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String imageBase64EncodedStr = null;
		try {
			//字节数组Base64编码
			ImageIO.write(image, "png", outputStream);
			imageBase64EncodedStr = "data:image/png;base64," + EncryptUtil.encodeBase64(outputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return imageBase64EncodedStr;
	}
}
