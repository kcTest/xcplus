package com.zkc.xcplus.search.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

/**
 * To use the imperative (non-reactive) client, a configuration bean must be configured like this:
 */
@Configuration
@Slf4j
public class ESClientConfig extends ElasticsearchConfiguration {
	
	@Override
	public ClientConfiguration clientConfiguration() {
		return ClientConfiguration.builder()
				.connectedTo("https://esnode:1016")
				.usingSsl(buildSSLContext())
				.withBasicAuth("elastic", "123456xxx")
				.build();
	}
	
	private static SSLContext buildSSLContext() {
		ClassPathResource resource = new ClassPathResource("http_ca.crt");
		SSLContext sslContext = null;
		try {
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			Certificate trustedCa;
			try (InputStream is = resource.getInputStream()) {
				trustedCa = factory.generateCertificate(is);
			}
			KeyStore trustStore = KeyStore.getInstance("pkcs12");
			trustStore.load(null, null);
			trustStore.setCertificateEntry("ca", trustedCa);
			SSLContextBuilder sslContextBuilder = SSLContexts.custom()
					.loadTrustMaterial(trustStore, null);
			sslContext = sslContextBuilder.build();
		} catch (Exception e) {
			log.error("ES连接认证失败", e);
		}
		
		return sslContext;
	}
}
