package com.springboot.architecture.config;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 直播-配置
 * 
 * @author Jiangyang
 */
@ConfigurationProperties(prefix = "lss")
@Component
public class LSSConfig {

	private static Logger logger = LoggerFactory.getLogger(LSSConfig.class);

	private String accessKeyId;
	private String accessKeySecret;
	private String playDomain;
	private String pushDomain;
	private String vodEndpoint;

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getPlayDomain() {
		return playDomain;
	}

	public void setPlayDomain(String playDomain) {
		this.playDomain = playDomain;
	}

	public String getPushDomain() {
		return pushDomain;
	}

	public void setPushDomain(String pushDomain) {
		this.pushDomain = pushDomain;
	}
	

	public String getVodEndpoint() {
		return vodEndpoint;
	}

	public void setVodEndpoint(String vodEndpoint) {
		this.vodEndpoint = vodEndpoint;
	}

	@PostConstruct
	public void init() {
		logger.debug(JSON.toJSONString(this));
	}

}
