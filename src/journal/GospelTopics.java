/*
 * GOSPELTOPICS_JAVA
 *
 *    This will keep track of all the valid topics
 */

package journal;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * GOSPELTOPICS
 */
public class GospelTopics {
   /*
    * MEMBER VARIABLES
    */
   private HashMap<String, String> topics;
   
   /*
    * MEMBER FUNCTIONS
    */

   /**
    * CONSTRUCTOR
    */
   public GospelTopics() {
      topics = new HashMap<>();
      //now load them from the properties file!
      loadTopics();
   }

   /**
    * LOADTOPICS
    *   This will call the Properties class and grab the content.
    */
   public void loadTopics() {
       LoadProperties loadProp = new LoadProperties("/journal/rsc/Journal.properties");
      //now grab the properties!
      String filePath = loadProp.grabProperties(0);
      //now read the file!
      readFile(filePath);
   }

   /**
    * READFILE
    *   Read the file from the file.
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
            String [] split = line.split(":");
            String topic = split[0];
            String [] terms = split[1].split(",");
            //now for the HASHMAP!
            for (String term : terms) {
                topics.put(term, topic);
            }
         }
         
         reader.close();
      } catch (IOException error) {
         error.getMessage();
      }
   }

   /**
    * SEARCH
    *   Search in the entries.
     * @param entries
    */
   public void search(List<JournalEntry> entries) {
       //now find the topics!
       for (String topicList : topics.keySet()) {
           boolean firstTime = true;
           for (JournalEntry entry : entries) {
               for (String topic : entry.getTopic()) {
                   //now check it!
                   if (topicList.equals(topic.toLowerCase())) {
                       //for the first time!
                       if (firstTime) {
                           System.out.println(topic);
                           firstTime = false;
                       }
                       
                       //print out the date!
                       System.out.println("\t" + entry.getDate());
                   }
               }
           }
       }
   }
   
   /**
    * TOPICSFINDER
    *   Find all the topics!
     * @param line
     * @return 
    */
   public List<String> topicsFinder(String line) {
       //create variables!
       List<String> found = new ArrayList<>();
       //now loop thru the list!
       for (String term : topics.keySet()) {
           //now ckeck the line!
           if (line.contains(term)) {
               //now add it to the list!
               //also don't duplicate it!
               if (!found.contains(topics.get(term))) {
                   found.add(topics.get(term));
               }
           }
       }
       
       return found;
   }
}