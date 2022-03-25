package dev.d1s.lp.commons.constant

private const val EVENTS_BASE = "/events"

public const val GET_EVENTS_BY_GROUP_MAPPING: String = "$EVENTS_BASE/{group}"
public const val GET_EVENTS_BY_PRINCIPAL_MAPPING: String = "$GET_EVENTS_BY_GROUP_MAPPING/{principal}"