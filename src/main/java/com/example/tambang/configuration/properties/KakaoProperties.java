package com.example.tambang.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties
public class KakaoProperties {
    private String restapi;
}
