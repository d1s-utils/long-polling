[![](https://jitpack.io/v/d1snin/long-polling.svg)](https://jitpack.io/#d1snin/long-polling)

# long-polling

A complex of libraries and starters for organizing long-polling-based interaction between the client and the server.

### Installation

```kotlin
repositories {
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("dev.d1s.long-polling:{module}:{long-polling version}")
}
```

### Modules

| Module                              | Description                                                  |
|-------------------------------------|--------------------------------------------------------------|
| `lp-client`                         | Coroutines-powered Kotlin client for the server.             |
| `lp-commons`                        | Common components shared between modules.                    |
| `spring-boot-starter-lp-server`     | Server implementation with ready-to-use logic.               |
| `spring-boot-starter-lp-server-web` | Ready-to-use server implementation with configured REST API. |