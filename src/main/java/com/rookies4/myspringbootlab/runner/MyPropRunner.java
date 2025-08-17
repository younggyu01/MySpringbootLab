package com.rookies4.myspringbootlab.runner;

import com.rookies4.myspringbootlab.config.vo.MyEnvironment;
import com.rookies4.myspringbootlab.property.MyPropProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MyPropRunner implements ApplicationRunner {
    @Value("${myprop.username}")
    private String username;

    @Value("${myprop.port}")
    private int port;

    @Autowired
    private Environment environment;

    @Autowired
    private MyPropProperties properties;

    @Autowired
    private MyEnvironment myEnvironment;

    private Logger logger = LoggerFactory.getLogger(MyPropRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.debug("Logger 구현 객체명 = {}", logger.getClass().getName());
        logger.info("현재 활성화된 MyEnvironment Bean = {}", myEnvironment);

        logger.info("MyBootProperties.getUsername() = {}", properties.getUsername());
        logger.info("MyBootProperties.getPort() = {}", properties.getPort());

        logger.info("Properties myprop.username = {}", username);
        logger.info("Properties myprop.port = {}", port);

        logger.debug("VM Arguments = {}", args.containsOption("foo")); //false
        logger.debug("Program Arguments = {}", args.containsOption("bar")); //true

    }
}
