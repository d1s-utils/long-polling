/*
 * Copyright 2022 Mikhail Titov and other contributors (if even present)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.d1s.lp.commons.entity

import java.time.Instant

public data class LongPollingEvent<T : Any>(
    val group: String,
    val principal: String?,
    val data: T?,
    val satisfiedRecipients: MutableSet<String>,
    val timestamp: Instant,
) {
    lateinit var id: String

    override fun toString(): String =
        "LongPollingEvent(" +
                "id=${
                    if (::id.isInitialized) {
                        id
                    } else {
                        "not set"
                    }
                }, " +
                "group='$group', " +
                "principal=$principal, " +
                "satisfiedRecipients=$satisfiedRecipients, " +
                "timestamp=$timestamp)"
}