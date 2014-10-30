/*
 * SCRIPTURE_JAVA
 *
 *   This will keep track of the Scriptures that were
 *      used in the entry.
 */

package journal;

/**
 * SCRIPTURE
 */
public class Scripture {
   /*
    * MEMBER VARIABLES
    */
   String book;
   String chapters;
   String verses;

   /*
    * MEMBER FUNCTIONS
    */

   /**
    * CONSTRUCTOR
    */
   public Scripture() {
   }

   /**
    * NON-DEFAULT CONSTRUCTOR
     * @param b
     * @param c
    */
   public Scripture(String b, String c) {
      book = b;
      chapters = c;
      verses = "none";
   }
   
   /**
    * NON-DEFAULT CONSTRUCTOR
     * @param b
     * @param c
     * @param v
    */
   public Scripture(String b, String c, String v) {
      book = b;
      chapters = c;
      verses = v;
   }

   /**
    * ISVALID
    *   This will make sure it is a valid scripture
     * @return 
    */
   public boolean isValid() {
      return true;
   }

   /**
    * DISPLAY
    *   Display the scriptures!
    */
   public void display() {
      System.out.print(book + " " + chapters);

      if (verses != "none") {
         System.out.println(":" + verses); 
      } else {
         System.out.println();
      }
   }
   
   /**
    * GETTERS
    */
   public String getScripture() {
       String text = book + " " + chapters;
       
       if (!verses.equals("none")) {
         text += ":" + verses; 
       } 
       
       text += "\n";
       
       return text;
   }
   
   public String getBook() {
      return book;
   }

   public String getChapters() {
      return chapters;
   }

   public String getVerses() {
      return verses;
   }

   /**
    * SETTERS
    */
   public void setBook(String b) {
      book = b;
   }

   public void setChapters(String c) {
      chapters = c;
   }

   public void setVerses(String v) {
      verses = v;
   }
}