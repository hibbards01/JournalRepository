/*
 * STANDARDWORKS_JAVA
 *
 *    This will hold the class that will compare the scriptures
 *      to valid date the entries that the user inputs.
 */

package journal;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * STANDARDWORKS
 */
public class StandardWorks {
   /*
    * Member variables
    */
   private List<Scripture> scriptures;
   
   /*
    * Member functions
    */

   /**
    * CONSTRUCTOR
    */
   public StandardWorks() {
      scriptures = new ArrayList<>();
      //now grab the scriptures!
      loadScriptures();
   }

   /**
    * LOADSCRIPTURES
    */
   public void loadScriptures() {
      LoadProperties loadProp = new LoadProperties("/journal/rsc/Journal.properties");
      //now grab the properties!
      String filePath = loadProp.grabProperties(1);
      //now read the file!
      readFile(filePath);
   }

   /**
    * READFILE
    *   Read the file and put them into SCRIPTURES
    * @param file 
    */
   public void readFile(String file) {
      //read the file
      try {
         FileReader fileReader = new FileReader(file);
         BufferedReader reader = new BufferedReader(fileReader);
         String line;
         int count = 0;
         
         //now read it!
         while ((line = reader.readLine()) != null) {
            String [] splitLine = line.split(":");
            //now add them to the list of scriptures!
            Scripture script = new Scripture(splitLine[0], splitLine[1]);
            scriptures.add(count, script);
            count++;
         }
         
         reader.close();
      } catch (IOException error) {
         error.getMessage();
      }
   }

   /**
    * SCRIPTUREFINDER
    *   This will use a pattern to find a scripture!
     * @param line
     * @return 
    */
   public List<Scripture> scriptureFinder(String line) {
       //create a new list!
       List<Scripture> found = new ArrayList<>();
       /*
       * Find these cases:
       * Alma Chapter 2
       * 2 Nephi 3:4-7
       * D&C 50:14
       */
       Pattern pattern = Pattern.compile("(\\d\\s)?\\w+ chapter \\d+|"
                                        + "(\\d\\s)?[\\w&]+ \\d+[:]\\d+(-\\d+)?"
                                        , Pattern.CASE_INSENSITIVE);
       //now to match it!
       Matcher matcher = pattern.matcher(line);
       int count = 0;
       String foundScript;
       
       //now find it!
       while (matcher.find()) {
           //only do this for a specail case....
           foundScript = matcher.group();
           foundScript = foundScript.replaceAll("(?i)chapter ", "");
           //now make it a scripture!
           Scripture scripture = splitScripture(foundScript);
           //now validate it!
          
           if (isValid(scripture)) {
               //now add to the list!
               found.add(scripture);
           }
       }
       
       return found;
   } 
   
   /**
    * ISVALID
    *   This make sure the it is a valid scripture!
     * @param scripture
     * @return 
    */
   public boolean isValid(Scripture scripture) {
       boolean valid = false;
       //loop thru the list!

       for (Scripture index : scriptures) {
           //check the book first!
           if (index.getBook().equals(scripture.getBook())) {
               //now the chapters!
               int scriptChap = Integer.parseInt(scripture.getChapters());
               int indexChap = Integer.parseInt(index.getChapters());
               //now validate it!
               if (scriptChap <= indexChap) {
                   valid = true;
               }
           } 
       }
       
       return valid;
   }
   
   /**
    * SPLITSCRIPTURE
    *  This will split the string to individual variables.
     * @param scripture
     * @return 
    */
   public Scripture splitScripture(String scripture) {
       //first split the scripture to the individual variables!
       String book = "none";
       String chapter = "none";
       String verses = "none";
       
       //split verses first!
       String [] splitScript = null;
       if (scripture.contains(":")) {
           String [] split = scripture.split(":");
           //now set them!'
           verses = split[1];
           scripture = split[0];
       }
       
       //now for book and chapter!
       splitScript = scripture.split(" ");
       
       //now check the size!
       if (splitScript.length == 2) {
           //set the variables!
           book = splitScript[0];
           chapter = splitScript[1];
       } else if (splitScript.length == 3) {
           //first the book!
           book = splitScript[0] + " " + splitScript[1];
           chapter = splitScript[2];
       }
       
       Scripture newScripture = new Scripture(book, chapter, verses);
       
       return newScripture;
   }
   
   /**
    * SEARCH
     * @param entries
    */
   public void search(List<JournalEntry> entries) {
      //find common scriptures!
      for (int i = 0; i < scriptures.size(); i++) {
         boolean firstTime = true;
         for (int j = 0; j < entries.size(); j++) {
            for (int s = 0; s < entries.get(j).getScriptListSize(); s++) {
               String stand = scriptures.get(i).getBook();
               String entScript = entries.get(j).getScripture(s).getBook();
               
               if (stand.equals(entScript)) {
                  //if for the first time!
                  if (firstTime) {
                     System.out.println(scriptures.get(i).getBook());
                     firstTime = false;
                  }

                  System.out.println("\t" + entries.get(j).getDate());
               }
            }
         }
      }
   }
}