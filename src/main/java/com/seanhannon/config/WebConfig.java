package com.seanhannon.config;

import com.seanhannon.util.LoggingInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addInterceptors(InterceptorRegistry registry)
  {
    registry.addInterceptor(new LoggingInterceptor()).addPathPatterns("/**");
  }
}
