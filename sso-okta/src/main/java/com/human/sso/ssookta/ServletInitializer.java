package com.human.sso.ssookta;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SsoOktaApplication.class);
	}
	
	@Bean
	OAuth2ClientProperties oAuth2ClientProperties() {
	    return new OAuth2ClientProperties();
	}

}
