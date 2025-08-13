package com.rookies4.myspringbootlab.Config;

import com.rookies4.myspringbootlab.Config.vo.MyEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Configuration
public class ProdConfig {
    @Bean
    public MyEnvironment myEnvironment() {
        return MyEnvironment.builder()
                .mode("운영환경")
                .rate(1.5)
                .build();
    }
}
