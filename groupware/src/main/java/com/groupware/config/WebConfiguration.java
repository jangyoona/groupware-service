package com.groupware.config;

import com.groupware.security.AuthInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.Properties;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	@Value("naver.mail-id")
	private String senderEmailId;

	@Value("naver.mail-pw")
	private String senderEmailPassword ;

	// Mail Config
	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.naver.com");
		mailSender.setPort(465);
		mailSender.setUsername(senderEmailId);
		mailSender.setPassword(senderEmailPassword );
		mailSender.setDefaultEncoding("UTF-8");
		
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.debug", "false");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.trust", "smtp.naver.com");
		
		return mailSender;
	}
	
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasenames("message.label");
		
		return source;
	}
	
	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

	@Bean
	public AuthInterceptor authInterceptor() {
		return new AuthInterceptor();
	}



	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor())
				.addPathPatterns("/**")
//				.excludePathPatterns("/account/login", "/static/**");
				.excludePathPatterns("/account/login","/account/user-check/**", "/account/email-check",
									"/account/email-check", "/account/email-checkAuthCode",
									"/account/reset-password", "/account/id-check", "/account/email-message",
									"/bulid/**", "/dist/**", "/docs/**", "/plugins/**");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		File directory = new File("/home/ubuntu/employee-photo/");
		if (!directory.exists()) {
			directory.mkdirs(); // 디렉토리 생성
		}

		File directory2 = new File("/home/ubuntu/user-filebox/");
		if (!directory2.exists()) {
			directory2.mkdirs(); // 디렉토리 생성
		}

		registry
				.addResourceHandler("/employee-photo/**") // 웹 요청 경로
				.addResourceLocations("file:/home/ubuntu/employee-photo/"); // 실제 파일 경로

		registry
				.addResourceHandler("/user-filebox/**") // 웹 요청 경로
				.addResourceLocations("file:/home/ubuntu/user-filebox/"); // 실제 파일 경로
	}


}
