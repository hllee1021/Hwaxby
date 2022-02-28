package spring.Hwaxby_back.util;

// Utility class to read property file

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileReader {
    private static final Logger logger = LogManager.getLogger(PropertyFileReader.class);

    private static Properties prop = new Properties();

    public static Properties readPropertyFile(String propertyFileName) throws Exception {
        if (prop.isEmpty()) {
            InputStream input = PropertyFileReader.class.getClassLoader().getResourceAsStream(propertyFileName);
            try {
                prop.load(input);
            } catch (IOException e) {
                logger.error(e);
                throw e;
            } finally {
                if (input != null) input.close();
            }
        }
        return prop;
    }
}