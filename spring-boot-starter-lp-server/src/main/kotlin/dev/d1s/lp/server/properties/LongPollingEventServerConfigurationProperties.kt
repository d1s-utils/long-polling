package dev.d1s.lp.server.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import java.time.Duration
import javax.validation.constraints.NotNull

@Validated
@ConstructorBinding
@ConfigurationProperties("long-polling-server")
public data class LongPollingEventServerConfigurationProperties(
    @NotNull val eventLifeTime: Duration = Duration.ofSeconds(5)
)