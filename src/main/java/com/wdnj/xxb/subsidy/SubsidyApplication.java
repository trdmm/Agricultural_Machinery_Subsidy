package com.wdnj.xxb.subsidy;

import com.thebeastshop.forest.springboot.annotation.ForestScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ForestScan("com.wdnj.xxb.subsidy.http")
public class SubsidyApplication {

  public static void main(String[] args) {
    SpringApplication.run(SubsidyApplication.class, args);
  }

}
