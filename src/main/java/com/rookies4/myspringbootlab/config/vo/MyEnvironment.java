package com.rookies4.myspringbootlab.config.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class MyEnvironment {
    private String mode;
    private Double rate;
}
