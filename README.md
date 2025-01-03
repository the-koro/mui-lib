# mui-lib Mui Localization Library for Java

**mui-lib** is a Java library for handling localization data for different locales. It loads `.mui` localization files from a given folder and provides methods to retrieve localized strings for specific keys and locales.

## Features
- Load `.mui` localization files from a specified directory.
- Retrieve localized strings based on keys and locales.
- Supports fallback to default locale if a key is not found in the preferred locale.
- Optional logging functionality to trace the loading and access of localization files.

## Requirements
- Java 11 or higher.
- **Optional**: External Java logging library `org.theko.logger` for logging functionality.

## Installation

### Download JAR from Releases
You can download the latest JAR file from the [Releases page](https://github.com/the-koro/mui-lib/releases). Simply download the JAR and add it to your project dependencies.

Alternatively, you can include the JAR manually in your project's `lib` folder.

### Logging Library Dependency

If you want to enable logging functionality, **MuiLocalization** requires the external logging library `org.theko.logger`. You can add it as a dependency in your project.

You can find the library on GitHub: [theko-logger](https://github.com/the-koro/theko-logger).

## Usage

### 1. Import the library:
```java
import org.theko.mui.MuiLocalization;
```

### 2. Initialize the `MuiLocalization` object:
You can initialize the localization object either by providing a `File` representing the folder containing `.mui` files or by providing a folder path as a string.

```java
// Using a folder
MuiLocalization localization = new MuiLocalization(new File("path/to/mui/files"));

// Using a folder path string
MuiLocalization localization = new MuiLocalization("path/to/mui/files");
```

### 3. Enable logging (optional):
You can enable logging to track the localization loading and retrieval process. By default, logging is disabled.

```java
MuiLocalization.enableLogging(true);
```

### 4. Retrieve localized strings:
Use the `get` method to fetch the localized value for a specific key and locale.

```java
String greeting = localization.get("greeting", "en-UK");
System.out.println(greeting); // Output: "Hello, mate!"
```

### 5. Fallback to default locale:
If a key is not found in the preferred locale, you can use the `getOrDefault` method to fallback to a default locale.

```java
String greeting = localization.getOrDefault("greeting", "fr-FR", "en-UK");
System.out.println(greeting); // Output: "Bonjour, camarade!" (or the English greeting if French is missing)
```

### 6. Retrieve available locales:
You can retrieve a list of all available locales from the loaded `.mui` files:

```java
List<String> locales = localization.getLocales();
System.out.println(locales); // Output: List of locales e.g. [en-UK, fr-FR, es-ES, ...]
```

## Logging

Logging is optional and controlled via the `enableLogging` method. It helps you track the loading of `.mui` files, as well as the retrieval of localized keys.

To enable logging:
```java
MuiLocalization.enableLogging(true);
```

### Logging messages:
- **INFO**: Indicates that logging is enabled.
- **DEBUG**: Detailed messages about file loading and locale data.
- **WARNING**: Warnings when a key or locale is not found.

## Example

```java
import org.theko.mui.MuiLocalization;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize the localization object
            MuiLocalization localization = new MuiLocalization(new File("path/to/mui/files"));
            
            // Enable logging
            MuiLocalization.enableLogging(true);
            
            // Retrieve localized string
            String greeting = localization.get("greeting", "en-UK");
            System.out.println(greeting); // Output: "Hello, mate!"
            
            // Get fallback string from default locale if key not found
            String fallbackGreeting = localization.getOrDefault("greeting", "fr-FR", "en-UK");
            System.out.println(fallbackGreeting); // Output: "Bonjour, camarade!" (if French is missing)
            
            // Get list of available locales
            System.out.println(localization.getLocales());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
