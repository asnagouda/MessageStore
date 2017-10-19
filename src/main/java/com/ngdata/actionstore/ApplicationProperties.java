package com.ngdata.actionstore;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <ul>
 * <li>Title: ApplicationProperties</li>
 * <li>Description: Reads the application properties defined in the properties file.</li>
 * <li>Created: Oct 12, 2017 by: Anand N</li>
 * </ul>
 */

public class ApplicationProperties {
    /**
     * Legal copyright notice.
     */
    public static final String COPYRIGHT = "@Copyright NGDATA NV";

    /**
     * The LOGGER.
     */
    private final static Logger LOGGER = Logger.getLogger(ApplicationProperties.class.getName());

    /**
     * The PROPERTY_FILE_NAME.
     */
    public static final String PROPERTY_FILE_NAME = "messagestore.properties";

    /**
     * The appProperties.
     */
    private static Properties messagestoreProperties;

    /**
     * Initializes the appProperties by reading the properties file.
     */
    public static void initialize() {
        try {
        	System.out.println("Entered method initialize, will try reading the property file " + PROPERTY_FILE_NAME);
            LOGGER.info("Entered method initialize, will try reading the property file " + PROPERTY_FILE_NAME);
            messagestoreProperties = new Properties();

            InputStream inputStream = new ApplicationProperties().getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME);

            if (inputStream != null) {
            	messagestoreProperties.load(inputStream);
                inputStream.close();
            } else {
                LOGGER.info("Could not read property file " + PROPERTY_FILE_NAME);
            }
            System.out.println(getProperty("JEDIS_HOST"));
            LOGGER.info("ApplicationProperties.initialize() : END");
        } catch (Exception ex) {
            LOGGER.info("System will not work correctly. Error reading property file " + PROPERTY_FILE_NAME);
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    /**
     * @param strFlag
     * @param defaultRetVal
     * @return
     */
    public static Boolean convertStringToBoolean(String strFlag, Boolean defaultRetVal){
        Boolean flag = defaultRetVal;
        try {
            if(null != strFlag && strFlag.trim().length() > 0){
                strFlag = strFlag.trim();
                if(strFlag.equalsIgnoreCase("YES") || strFlag.equalsIgnoreCase("Y")){
                    flag = true;
                } else if (strFlag.equalsIgnoreCase("NO") || strFlag.equalsIgnoreCase("N")) {
                    flag = false;
                } else {
                    flag = Boolean.parseBoolean(strFlag);
                }
            }
        } catch (Exception e) {
            //suppress Exception and return default value
        }
        return flag;
    }
    



    /**
     * Returns a single property value for the mentioned key.
     * @param key
     * @return String
     */
    public static String getProperty(String key) {
        return messagestoreProperties.getProperty(key);
    }

    
}
