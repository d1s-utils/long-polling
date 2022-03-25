package dev.d1s.lp.server.exception

public class IncompatibleEventDataTypeException :
    RuntimeException("Event group must contain events with the same type of data.")