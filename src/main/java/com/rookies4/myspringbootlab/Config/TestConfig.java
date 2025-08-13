package com.rookies4.myspringbootlab.Config;

import com.rookies4.myspringbootlab.Config.vo.MyEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TestConfig {
    @Bean
    public MyEnvironment myEnvironment() {
        return MyEnvironment.builder()
                .mode("개발환경")
                .rate(0.5)
                .build();
    }
}
