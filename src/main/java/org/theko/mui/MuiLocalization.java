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

/**
 * This class is responsible for handling localization data for different locales.
 * It loads localization files (".mui") from a given folder and provides methods
 * to retrieve localized strings for a specific key and locale.
 */
public class MuiLocalization {
    
    // Map to store locale data where the key is the locale and the value is the Properties object
    private final Map<String, Properties> localeData = new HashMap<>();
    // List to store all available locales
    private final List<String> locales = new ArrayList<>();
    // Static flag to enable or disable logging
    private static boolean enableLogging = false;

    /**
     * Constructor that initializes the MuiLocalization object by loading localization files from the specified folder.
     * 
     * @param folder The folder containing the ".mui" localization files.
     * @throws IOException If there is an error reading the files.
     */
    public MuiLocalization(File folder) throws IOException {
        loadMuiFiles(folder);
    }

    /**
     * Constructor that initializes the MuiLocalization object by loading localization files from the folder path.
     * 
     * @param folderPath The path to the folder containing the ".mui" localization files.
     * @throws IOException If there is an error reading the files.
     */
    public MuiLocalization(String folderPath) throws IOException {
        loadMuiFiles(new File(folderPath));
    }

    /**
     * Enables or disables logging for the MuiLocalization class.
     * 
     * @param b A boolean value indicating whether to enable logging (true) or disable it (false).
     */
    public static void enableLogging(boolean b) {
        try {
            // Try to load the logging class
            Class.forName("org.theko.logger.GlobalLogger");
            GlobalLogger.info("Logging enabled");
        } catch (ClassNotFoundException | NoClassDefFoundError ex) {
            // If logging class is not available, print error message
            System.err.println("Cannot use logging. No logging library installed: org.theko.logger");
            System.err.println("Download it from GitHub https://github.com/the-koro/theko-logger");
            b = false;
        }
        // Set the static logging flag based on the argument
        MuiLocalization.enableLogging = b;
    }

    /**
     * Loads the ".mui" files from the given folder and populates the localeData map with the locale and its corresponding properties.
     * 
     * @param folder The folder containing the ".mui" files.
     * @throws IOException If there is an error reading the files.
     */
    private void loadMuiFiles(File folder) throws IOException {
        // Log the process of loading files
        if (enableLogging) GlobalLogger.debug("Loading localization files.");
        
        // Get all files ending with ".mui" in the folder
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".mui"));

        // If no .mui files are found, log the error and throw an exception
        if (files == null || files.length == 0) {
            if (enableLogging) GlobalLogger.error("No .mui files found in folder: " + folder.getAbsolutePath());
            throw new FileNotFoundException("No .mui files found in folder: " + folder.getAbsolutePath());
        }

        // Process each file in the folder
        for (File file : files) {
            // Extract locale from the file name
            String locale = file.getName().replace(".mui", "");
            // Log the found locale
            if (enableLogging) GlobalLogger.debug("Locale found: " + locale);
            // Add the locale to the list
            locales.add(locale);

            // Load the properties from the file
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream(file);
                 InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
                properties.load(isr);
            }
            // Log that the locale has been added
            if (enableLogging) GlobalLogger.debug("Locale " + locale + " added.");
            // Add the locale's properties to the localeData map
            localeData.put(locale, properties);
        }
    }

    /**
     * Returns a list of all available locales.
     * 
     * @return A list of locales.
     */
    public List<String> getLocales() {
        return locales;
    }

    /**
     * Retrieves the localized value for a given key and locale.
     * 
     * @param key    The key for which the value is to be retrieved.
     * @param locale The locale to look for the key.
     * @return The localized value, or null if the key or locale is not found.
     */
    public String get(String key, String locale) {
        // Retrieve properties for the given locale
        Properties properties = localeData.get(locale);
        
        // If properties are null, log the issue and return null
        if (properties == null) {
            if (enableLogging) GlobalLogger.warn("Locale '" + locale + "' not found.");
            return null;
        }
    
        // If the key is found, return the value
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        }
    
        // If the key is not found, log the issue and return null
        if (enableLogging) GlobalLogger.warn("Key '" + key + "' for the locale '" + locale + "' not found.");
        return null; // Key not found
    }

    /**
     * Retrieves the localized value for a given key and locale. If the key is not found for the given locale,
     * it will try to get the value from a default locale.
     * 
     * @param key          The key for which the value is to be retrieved.
     * @param locale       The locale to look for the key.
     * @param defaultLocale The fallback locale to use if the key is not found for the given locale.
     * @return The localized value, or null if neither locale has the key.
     */
    public String getOrDefault(String key, String locale, String defaultLocale) {
        // Try to get the value for the given locale
        String value = get(key, locale);
        
        // If the value is not found for the locale, try the default locale
        if (value != null) {
            return value;
        }

        // Return the value for the default locale, if found
        return get(key, defaultLocale);
    }
}
