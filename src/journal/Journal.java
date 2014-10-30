/*
 * JOURNAL_JAVA
 *
 * This will be the main driver of the program.
 */

package journal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;

/**
 * JOURNAL
 *   This will be the class that drives everything!
 * @author Samuel Hibbard
 */
public class Journal {
   /*
    * Member variables
    */
   private List<JournalEntry> entries;
   private StandardWorks scriptures;
   private GospelTopics topics;
   private FileHandler fileHandler;

   /*
    * Member functions
    */

   /**
    * CONSTRUCTOR
    *   This will construct the private members.
    */
   public Journal() {
       //create everything!
      entries = new ArrayList<>();
      scriptures = new StandardWorks();
      topics = new GospelTopics();
      fileHandler = new FileHandler();
      //and load the saved files!
      loadSavedFiles();
   }

   /**
    * GETTERS
    */
   public List<JournalEntry> getEntries() {
       return entries;
   }
   
   public List<String> getTopics(int index) {
       return entries.get(index).getTopic();
   }
   
   public List<Scripture> getScriptures(int index) {
       return entries.get(index).getScriptures();
   }
   
   /**
    * READ
    *   Read from the file in .xml or .txt
    * @param file 
    */
   public void read(String file) {
       //check for the type of file...
       if (file.contains(".txt")) {
           fileHandler.readTXT(entries, file);
       } else if (file.contains(".xml")) {
           fileHandler.readXML(entries, file);
       }
   }
   
   /**
    * LOADSAVEDFILES
    *   This will load the property file.
    */
   public void loadSavedFiles() {
       //grab the properties!
       LoadProperties properties = new LoadProperties("/journal/rsc/Journal.properties");
       //now grab it...
       String filePath = properties.grabProperties(2);
       //now read it!
       read(filePath);
   }
   
   /**
    * WRITE
    *   This will write to a file!
    * @param filePath 
    */
   public void write(String filePath) {
       //now to write to the right path!
       if (filePath.contains(".txt")) {
           fileHandler.writeFileToTXT(entries, filePath);
       } else if (filePath.contains(".xml")) {
           fileHandler.writeFileToXML(entries, filePath);
       }
   } 
   
   /**
    * CREATENEWENTRY
    *   This will add a new entry to the JOURNAL
    * @param title
    * @param content 
    */
   public void createNewEntry(String title, String content, String date) {
       //grab the scriptures and topics
       List<Scripture> grabScriptures = scriptures.scriptureFinder(content);
       List<String> grabTopics = topics.topicsFinder(content);
       
       //now create a new journal entry!
       JournalEntry entry = new JournalEntry(title, content, date, grabTopics, 
                                             grabScriptures);
       
       //add to the journal!
       //int index = entries.size();
       entries.add(0, entry);
   }
   
   /**
    * WRITE
    *   Does the same thing but with different parameters.
    * @param entry
    * @param file 
    */
   public void write(List<JournalEntry> entry, String file) {
       //now to write to the right path!
       if (file.contains(".txt")) {
           fileHandler.writeFileToTXT(entry, file);
       } else if (file.contains(".xml")) {
           fileHandler.writeFileToXML(entry, file);
       }
   }
}
