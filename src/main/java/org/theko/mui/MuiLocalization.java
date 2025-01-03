package org.theko.mui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.theko.logger.GlobalLogger;

public class MuiLocalization {
    private final Map<String, Properties> localeData = new HashMap<>();
    private final List<String> locales = new ArrayList<>();
    private static boolean enableLogging = false;

    public MuiLocalization(File folder) throws IOException {
        loadMuiFiles(folder);
    }

    public MuiLocalization(String folderPath) throws IOException {
        loadMuiFiles(new File(folderPath));
    }

    public static void enableLogging(boolean b) {
        try {
            Class.forName("org.theko.logger.GlobalLogger");
            GlobalLogger.info("Logging enabled");
        } catch (ClassNotFoundException | NoClassDefFoundError ex) {
            System.err.println("Cannot use logging. No logging library installed: org.theko.logger");
            System.err.println("Download it from GitHub https://github.com/the-koro/theko-logger");
            b = false;
        }
        MuiLocalization.enableLogging = b;
    }

    private void loadMuiFiles(File folder) throws IOException {
        if (enableLogging) GlobalLogger.debug("Loading localization files.");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".mui"));

        if (files == null || files.length == 0) {
            if (enableLogging) GlobalLogger.error("No .mui files found in folder: " + folder.getAbsolutePath());
            throw new FileNotFoundException("No .mui files found in folder: " + folder.getAbsolutePath());
        }

        for (File file : files) {
            String locale = file.getName().replace(".mui", "");
            if (enableLogging) GlobalLogger.debug("Locale found: " + locale);
            locales.add(locale);
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
                properties.load(isr);
            }
            if (enableLogging) GlobalLogger.debug("Locale " + locale + " added.");
            localeData.put(locale, properties);
        }
    }

    public List<String> getLocales() {
        return locales;
    }

    public String get(String key, String locale) {
        Properties properties = localeData.get(locale);
        
        if (properties == null) {
            if (enableLogging) GlobalLogger.warn("Locale '" + locale + "' not found.");
            return null;
        }
    
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        }
    
        if (enableLogging) GlobalLogger.warn("Key '" + key + "' for the locale '" + locale + "' not found.");
        return null; // Key not found
    }

    public String getOrDefault(String key, String locale, String defaultLocale) {
        String value = get(key, locale);
        if (value != null) {
            return value;
        }

        return get(key, defaultLocale);
    }
}