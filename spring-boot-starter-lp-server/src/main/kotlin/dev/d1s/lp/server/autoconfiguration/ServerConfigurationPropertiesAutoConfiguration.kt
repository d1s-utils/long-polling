package dev.d1s.lp.server.autoconfiguration

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan(basePackages = ["dev.d1s.lp.server.properties"])
public class ServerConfigurationPropertiesAutoConfiguration