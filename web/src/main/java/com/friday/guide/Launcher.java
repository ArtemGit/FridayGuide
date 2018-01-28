package com.friday.guide;

import org.springframework.boot.Banner;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@SpringBootApplication(exclude = ManagementWebSecurityAutoConfiguration.class)
@EnableJpaRepositories(basePackages = "com.friday.guide.api.data.repository")
@ComponentScan(basePackages = "com.friday.guide")
public class Launcher {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
            .listeners(new ApplicationPidFileWriter())
            .bannerMode(Banner.Mode.OFF)
            .sources(Launcher.class)
            .run(args);
    }
}
