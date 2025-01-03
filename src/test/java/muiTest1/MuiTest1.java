package muiTest1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.theko.logger.GlobalLogger;
import org.theko.logger.LogLevel;
import org.theko.mui.MuiLocalization;

public class MuiTest1 {
    public static void main(String[] args) throws IOException, URISyntaxException {
        GlobalLogger.getOutputsWith(System.out).forEach(output -> {
            output.setPreferredLevel(LogLevel.DEBUG);
            output.setPattern("{colored}" + output.getPattern());
        });

        // Enable logging if necessary
        MuiLocalization.enableLogging(true);

        // Load the locales folder path
        URL localesUrl = MuiTest1.class.getResource("locales");
        String localesPath = Paths.get(localesUrl.toURI()).toString();
        
        // Initialize MuiLocalization with the path
        MuiLocalization localization = new MuiLocalization(localesPath);
        
        // Print all available locales
        System.out.println("Available locales: " + localization.getLocales());

        // Test existing locale and keys
        testLocale(localization, "en-UK");  // This should work
        testLocale(localization, "es-ES");  // This should work

        // Test non-existing locale
        testLocale(localization, "de-DE");  // This should work
        testLocale(localization, "xx-XX");  // This should return null
        
        // Test non-existing key for an existing locale
        testKey(localization, "en-UK", "unknownKey");  // Should return null
        testKey(localization, "en-UK", "greeting");  // Should return "Hello, mate!"
    }

    // Method to test a locale
    private static void testLocale(MuiLocalization localization, String locale) {
        System.out.println("Testing locale: " + locale);
        String greeting = localization.get("greeting", locale);
        if (greeting != null) {
            System.out.println("Greeting in " + locale + ": " + greeting);
        } else {
            System.out.println("Greeting not found for " + locale);
        }
    }

    // Method to test a key
    private static void testKey(MuiLocalization localization, String locale, String key) {
        System.out.println("Testing key: " + key + " in locale: " + locale);
        String value = localization.get(key, locale);
        if (value != null) {
            System.out.println("Value for " + key + " in " + locale + ": " + value);
        } else {
            System.out.println("Key not found for " + key + " in " + locale);
        }
    }
}
