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

package dev.d1s.lp.commons.constant

private const val EVENTS_BASE = "/api/events"

public const val GET_EVENTS_BY_GROUP_MAPPING: String = "$EVENTS_BASE/{group}"
public const val GET_EVENTS_BY_PRINCIPAL_MAPPING: String = "$GET_EVENTS_BY_GROUP_MAPPING/{principal}"
public const val GET_EVENT_GROUPS_MAPPING: String = "$EVENTS_BASE/groups"