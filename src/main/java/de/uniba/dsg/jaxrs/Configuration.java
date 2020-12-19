package de.uniba.dsg.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import de.uniba.dsg.jaxrs.API.ExamplesApi;

public class Configuration {
    private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());

    public static Properties loadProperties() {
        try (InputStream stream = ExamplesApi.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(stream);   
            Set<String> keys = properties.stringPropertyNames();
            for (String key : keys) {
            	Optional<String> environmentVariable = Optional.ofNullable(System.getenv(toUppercase(key)));
            	environmentVariable.ifPresent(newValue -> properties.setProperty(key, newValue));
            }
            return properties;
        } catch (IOException e) {
            LOGGER.severe("Cannot load configuration file.");
            return null;
        }
    }

	private static String toUppercase(String key) {
		String[] splitOnUppercase = key.split("(?=[A-Z])");
		return Arrays.stream(splitOnUppercase).map(substring -> substring.toUpperCase()).collect(Collectors.joining("_"));
	}
	
	
}
