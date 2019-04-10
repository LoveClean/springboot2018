package com.springboot.architecture.config;

import com.aliyun.oss.OSSClient;
import com.baidubce.BceClientConfiguration;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.lss.LssClient;
import com.baidubce.services.vod.VodClient;
import com.springboot.architecture.interceptor.CrossDomainFilter;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 生成特殊bean的工厂bean, 有些第三方的Bean没有标注@component, 需要手工初始化.
 */
@Configuration
@EnableConfigurationProperties
public class AppBeanFactary {
	@Resource
	private AppConfig appConfig;
	@Resource
	private OSSConfig ossConfig;
	@Resource
	private BOSConfig bosConfig;
	@Resource
	private LSSConfig lssConfig;

	@Resource
	private MemcachedConfig memcachedConfig;

	
	
	/**
	 * LSSClient
	 * 
	 * @return
	 */
	@Bean(name = "LssClient")
	public LssClient lssClient() {
		BceClientConfiguration config = new BceClientConfiguration();
		config.setCredentials(new DefaultBceCredentials(lssConfig.getAccessKeyId(), lssConfig.getAccessKeySecret()));
		return new LssClient(config);
	}	
	
	
	/**
	 * VODClient
	 * 
	 * @return
	 */
	@Bean(name = "VodClient")
	public VodClient vodClient() {
		BceClientConfiguration config = new BceClientConfiguration();
		config.setCredentials(new DefaultBceCredentials(lssConfig.getAccessKeyId(), lssConfig.getAccessKeySecret()));
		config.setEndpoint(lssConfig.getVodEndpoint());
		return new VodClient(config);
	}		
	
	/**
	 * BOS存储-下载
	 * 
	 * @return
	 */
	@Bean(name = "downloadBOSClient")
	public BosClient downloadBOSClient() {
		BosClientConfiguration config = new BosClientConfiguration();
		config.setCredentials(new DefaultBceCredentials(bosConfig.getAccessKeyId(), bosConfig.getAccessKeySecret()));
		return new BosClient(config);
	}

	/**
	 * BOS存储-上传
	 * 
	 * @return
	 */
	@Bean(name = "uploadBOSClient")
	public BosClient uploadBOSClient() {
		BosClientConfiguration config = new BosClientConfiguration();
		config.setCredentials(new DefaultBceCredentials(bosConfig.getAccessKeyId(), bosConfig.getAccessKeySecret()));
		return new BosClient(config);
	}

	/**
	 * 存储-下载
	 * 
	 * @return
	 */
	@Bean(name = "downloadOSSClient")
	public OSSClient downloadOSSClient() {
		return new OSSClient(ossConfig.getDownloadEndpoint(), ossConfig.getAccessKeyId(),
				ossConfig.getAccessKeySecret());
	}

	/**
	 * 存储-上传
	 * 
	 * @return
	 */
	@Bean(name = "uploadOSSClient")
	public OSSClient uploadOSSClient() {
		return new OSSClient(ossConfig.getUploadEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
	}

	/**
	 * 注册跨域支持过滤器
	 */
	@Bean
	public FilterRegistrationBean registerCrossDomainFilter() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		CrossDomainFilter crossDomainFilter = new CrossDomainFilter();
		// 设置是否允许跨域访问
		crossDomainFilter.setAllowCrossDomain(appConfig.getAllowCrossDomainAccess());
		registrationBean.setFilter(crossDomainFilter);
		registrationBean.setOrder(1);
		return registrationBean;
	}

	/**
	 * 缓存
	 *
	 * @return
	 * @throws IOException
	 */
	@Bean
	public MemcachedClient memcachedClient() throws IOException {
		MemcachedClient memcachedClient = null;
		if (memcachedConfig.isNeedAuth()) {
			AuthDescriptor ad = new AuthDescriptor(new String[] { "PLAIN" },
					new PlainCallbackHandler(memcachedConfig.getUsername(), memcachedConfig.getPassword()));
			memcachedClient = new MemcachedClient(new ConnectionFactoryBuilder()
					.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY).setAuthDescriptor(ad).build(),
					AddrUtil.getAddresses(memcachedConfig.getServers()));
		} else {
			memcachedClient = new MemcachedClient(
					new ConnectionFactoryBuilder().setProtocol(ConnectionFactoryBuilder.Protocol.BINARY).build(),
					AddrUtil.getAddresses(memcachedConfig.getServers()));
		}

		
		//return
		return memcachedClient;
	}

}
