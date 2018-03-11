package com.cyrof.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "main")
public class MainProperties {

    @Value("${main.trackperlist}")
    private int trackPerList;

    public int getTrackPerList() {
        return trackPerList;
    }

    public void setTrackPerList(int trackPerList) {
        this.trackPerList = trackPerList;
    }

}
