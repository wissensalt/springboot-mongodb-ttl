package com.wissensalt.springbootmongodbttl;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "ttl")
@Configuration
public class TtlProperty {

  private Collection employee;

  @Data
  public static class Collection {

    private String name;
    private long expireAfter;
  }
}
