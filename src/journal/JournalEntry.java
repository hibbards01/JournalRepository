/*
 * JOURNALENTRY_JAVA
 * 
 * This will save the entries and search for both
 *      topics and scriptures.
 */

package journal;

import java.util.List;
import java.util.ArrayList;

/**
 * JOURNALENTRY
 * @author Samuel Hibbard
 */
public class JournalEntry {
   /*
    * Member variables
    */
   private String content;
   private String title;
   private String date;
   private List<Scripture> scriptures;
   private List<String> topics;

   /*
    * Member methods
    */

   /**
    * CONSTRUCTOR
    *   This will construct the members.
     * @param title
     * @param content
     * @param date
     * @param topics
     * @param scriptures
    */
   public JournalEntry(String title, String content, String date, List<String> topics,
                       List<Scripture> scriptures) {
      //first trim the leading and trailing edges...
      content = content.trim();
      //now construct it!
      this.title = title;
      this.content = content;
      this.date = date;
      this.topics = new ArrayList<>(topics);
      this.scriptures = new ArrayList<>(scriptures);
   }
   
   /**
    * DISPLAY
    *  Display the entry!
    */
   public void display() {
       //first display the date!
       System.out.println("Entry: " + date);
       //now the content!
       System.out.println("\n" + content);
       //now for the topics!
       System.out.println("Topics:");
       for (String topic : topics) {
           System.out.println("\t" + topic);
       }
       //now the scripture references!
       System.out.println("Scripture References:");
       for (Scripture scripture : scriptures) {
           System.out.print("\t");
           scripture.display();
       }
       System.out.println("\n");
   }
   
   /**
    * GETTERS
    */
   public String getTitle() {
       return title;
   }
   
   public String getContent() {
      return content;
   }

   public String getDate() {
      return date;
   }

   public List<String> getTopic() {
      return topics;
   }
   
   public String getTopic(int index) {
      return topics.get(index); 
   }

   public int getTopicSize() {
      return topics.size();
   }

   public int getScriptListSize() {
      return scriptures.size();
   }

   public Scripture getScripture(int index) {
      return scriptures.get(index);
   }
   
   public List<Scripture> getScriptures() {
       return scriptures;
   }

   /**
    * SETTERS
    */
   public void setContent(String content) {
      this.content = content;
   }

   public void setDate(String date) {
      this.date = date;
   }
   
   public void setTitle(String title) {
       this.title = title;
   }
}