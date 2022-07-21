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

### License
```
   Copyright 2022 Mikhail Titov

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```