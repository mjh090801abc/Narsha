package com.spring.dishcovery.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TempFileStore {
    public static final Map<String, File> files = new ConcurrentHashMap<>();
}