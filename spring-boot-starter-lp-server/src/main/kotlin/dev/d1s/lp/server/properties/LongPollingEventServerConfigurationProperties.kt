package dev.d1s.lp.server.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration
import javax.validation.constraints.NotNull

@ConstructorBinding
@ConfigurationProperties("long-polling-server")
public data class LongPollingEventServerConfigurationProperties(
    @NotNull val eventLifeTime: Duration = Duration.ofSeconds(5)
)