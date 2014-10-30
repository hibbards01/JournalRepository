/*
 * LOADPROPERTIES_JAVA
 *
 *    This will load the properties for these classes: STANDARDWORKS
 *      and GOSPELTOPICS.
 */

package journal;

import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

/**
 * LOADPROPERTIES
 */
public class LoadProperties {
   /*
    * Member variables
    */
   Properties properties;
   
   /*
    * Member functions
    */

   /**
    * CONSTRUCTOR
    *   This will load the properties.
    * @param filePath
    */
   public LoadProperties(String filePath) {
      properties = new Properties();

      InputStream inputStream = getClass().getResourceAsStream(filePath);

      //make sure it worked!
      try {
         properties.load(inputStream);
      } catch (IOException ex) {
         ex.getMessage();
      }
   }

   /**
    * GRABPROPERTIES
    *   This will grab the file paths!
    * @param type
    * @return 
    */
   public String grabProperties(int type) {
      String filePath;
      //now to grab the path!
      if (type == 0) {
         filePath = properties.getProperty("topics");
      } else if (type == 1) {
         filePath = properties.getProperty("scriptures");
      } else {
          filePath = properties.getProperty("savedFiles");
      }

      return filePath;
   }
}