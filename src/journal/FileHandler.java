/*
 * FILEHANDLER_JAVA
 *    This will use a class that implements XML and file reading and
 *      writing.
 */

package journal;

import java.io.BufferedReader;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.ErrorHandler;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerException;
import javax.xml.transform.OutputKeys;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

/**
 * FILEHANDLER
 *    This will hold all the member variables and methods!
 * @author Samuel Hibbard
 */
public class FileHandler {
   /*
    * Member variables
    */

   /*
    * Member methods
    */

   /**
    * READXML
    *   This will read from an XML file and parse the contents within it.
    * @param entries
    * @param file
    * @return 
    */
   public List<JournalEntry> readXML(List<JournalEntry> entries, String file) {
      //set the right variables!
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      //do all this to make the factory do it correctly...
      factory.setNamespaceAware(true);
      factory.setIgnoringComments(true);
      
      File grabFile = new File(file);
      
      //check it if is valid...
      if (grabFile.exists()) {
          
      
            //now to build it!
            try {
               DocumentBuilder builder = factory.newDocumentBuilder();
               //now parse the file!
               Document doc = builder.parse(grabFile);
               doc.getDocumentElement().normalize();

               //now get the contents and make sure it is the right XML
               if (doc.getDocumentElement().getNodeName() == "journal") {
                  NodeList list = doc.getElementsByTagName("entry");

                  //now to read into the document!
                  for (int i = 0; i < list.getLength(); i++) {
                     Node tag = list.item(i);

                     NodeList childNodes = tag.getChildNodes();

                     //grab the content!
                     String content = "No Journal Entry";
                     String title = tag.getAttributes().getNamedItem("title").getNodeValue();
                     String date = tag.getAttributes().getNamedItem("date").getNodeValue();
                     List<String> topics = new ArrayList<>();
                     List<Scripture> scriptures = new ArrayList<>();

                     //for the lists only...
                     int topicCount = 0;
                     int scriptCount = 0;

                     //grab all the child nodes!
                     for (int j = 0; j < childNodes.getLength(); j++) {
                        Node child = childNodes.item(j);

                        //check the attributes
                        if (child.getNodeName() == "scripture") {
                           //grab the BOOK, CHAP, and VERSES
                           String book = child.getAttributes().getNamedItem("book").getNodeValue();
                           String chap = child.getAttributes().getNamedItem("chapter").getNodeValue();
                           //check to see if there are verses...
                           String startV = null;
                           String endV = null;
                           boolean hasVerses = false;

                           if (child.getAttributes().getNamedItem("startverse") != null) {
                              startV = child.getAttributes().getNamedItem("startverse").getNodeValue();
                              hasVerses = true;
                           }

                           if (child.getAttributes().getNamedItem("endverse") != null) {
                              endV = child.getAttributes().getNamedItem("endverse").getNodeValue();
                           }

                           if (startV != null && endV != null) {
                              startV += "-";
                              startV += endV;
                           }

                           //now put everything in!
                           Scripture script = new Scripture(book, chap);

                           if (hasVerses) {
                              script.setVerses(startV);
                           }

                           //and add to the list!
                           scriptures.add(scriptCount, script);
                           scriptCount++;
                        }
                        else if (child.getNodeName() == "topic" ) {
                           topics.add(topicCount, child.getTextContent());
                           topicCount++;
                        }
                        else if (child.getNodeName() == "content") {
                           content = child.getTextContent();
                        }
                     }

                     //now add to a NEW JOURNALENTRY!
                     JournalEntry entry = new JournalEntry(title, content, date, topics, scriptures);

                     entries.add(i, entry);
                  }
               } else {
                  System.out.println("ERROR: XML is not a type <journal>");
               }
            } catch (ParserConfigurationException pCEx) {
               pCEx.getMessage();
            } catch (SAXException sEx) {
               sEx.getException();
            } catch (IOException ex) {
               ex.getMessage();
            }
      } else {
          System.out.println("no such file!");
      }
      
      return entries;
   }

   /**
    * WRITEFILETOTXT
    *   This will write the file to a .txt
    * @param entries
    * @param fileName 
    */
   public void writeFileToTXT(List<JournalEntry> entries, String fileName) {
      try {
         //create the right variables!
         File file = new File(fileName);
         //check to make sure that that will work!
         if (!file.exists()) {
            file.createNewFile();
         }
         //now to write the file!
         FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
         //now to loop thru the entires!
         for (JournalEntry entry : entries) {
            //start a new entry!
            bufferedWriter.write("Entry: " + entry.getTitle() + "\n");
            //now for the date!
            bufferedWriter.write(entry.getDate() + "\n");
            //now for the content!
            bufferedWriter.write(entry.getContent() + "\n");
         }

         bufferedWriter.close();
      } catch (IOException ex) {
         ex.getMessage();
      }
   }

   /**
    * WRITEFILETOXML
    *   This will mainly call the functions!
    * @param entries
    * @param file 
    */
   public void writeFileToXML(List<JournalEntry> entries, String file) {
      //call the right files!
      try {
         //first build the document!
         Document doc = buildXMLDocument(entries);
         //then save it!
         saveXMLDocument(doc, file);
      } catch (ParserConfigurationException pCEx) {
         pCEx.getMessage();
      } catch (TransformerException tEx) {
         tEx.getMessage();
      }
   }
   
   /**
    * BUILDXMLDOCUMENT
    *   This will first build the XML document before saving to file
    * @param entries
    * @return
    * @throws ParserConfigurationException 
    */
   private Document buildXMLDocument(List<JournalEntry> entries) throws ParserConfigurationException {
      //create variables
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      
      //create elements
      Document doc = builder.newDocument();
      Element rootElement = doc.createElement("journal");
      doc.appendChild(rootElement);

      //now for the entries!
      for (JournalEntry entry : entries) {
         //create a new entry!
         Element entryElement = doc.createElement("entry");
         rootElement.appendChild(entryElement);

         //create date attribute for entry and title!
         entryElement.setAttribute("title", entry.getTitle());
         entryElement.setAttribute("date", entry.getDate());

         //now for the scriptures!
         for (int i = 0; i < entry.getScriptListSize(); i++) {
            //now for the indiviual nodes!
            Element scripture = doc.createElement("scripture");
            entryElement.appendChild(scripture);

            //set the attributes!
            scripture.setAttribute("book", entry.getScripture(i).getBook());
            scripture.setAttribute("chapter", entry.getScripture(i).getChapters());

            //verses are special....
            String [] verses;
            String verse = entry.getScripture(i).getVerses();

            if (verse.contains("-")) {
               //do this then!
               verses = entry.getScripture(i).getVerses().split("-");
               scripture.setAttribute("startverse", verses[0]);
               scripture.setAttribute("endverse", verses[1]);
            } else if (verse != "none") {
               scripture.setAttribute("startverse", entry.getScripture(i).getVerses());
            }
         }

         //now for the topics!
         for (String topic : entry.getTopic()) {
            Element topicElement = doc.createElement("topic");
            topicElement.appendChild(doc.createTextNode(topic));
            entryElement.appendChild(topicElement);
         }

         //now for the content!
         Element content = doc.createElement("content");
         content.appendChild(doc.createTextNode(entry.getContent()));
         entryElement.appendChild(content);
      }
      
      //finally return the document!
      return doc;
   }

   /**
    * SAVEXMLDOCUMENT
    *   Save the document
    * @param doc
    * @param file
    * @throws TransformerException 
    */
   private void saveXMLDocument(Document doc, String file) throws TransformerException {
      //now lets write this document to a file
      TransformerFactory factory = TransformerFactory.newInstance();
      Transformer transformer = factory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(file));

      //set some things for transformer
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

      //now transform it!
      transformer.transform(source, result);
   }

   /**
    * READTXT
    *   This will read the entry and parse all the scriptures and topics.
    * @param entries
    * @param file
    * @return 
    */
   public List<JournalEntry> readTXT(List<JournalEntry> entries, String file) {
       //pull these classes to our aid!
       StandardWorks scriptures = new StandardWorks();
       GospelTopics topics = new GospelTopics();
       //now to read the TXT!
       try {
           //set the variables!
           FileReader fileReader = new FileReader(file);
           BufferedReader reader = new BufferedReader(fileReader);
           String line; //for reading line by line.
           int numEntries = 0; //keep track of entries
           boolean newEntry = false; //when it is a new entry!
           boolean grabDate = false;
           //find the scriptures and topics!
           String date = "";
           String title = "";
           List<Scripture> foundScriptures = new ArrayList<>();
           Set<String> foundTopics = new HashSet<>();
           String content = "";
           //now to read line by line!
           while ((line = reader.readLine()) != null) {
               //check if it is a new entry!
               if (line.contains("Entry:")) {
                   newEntry = true;
                   grabDate = true;
                   numEntries++;
               } else {
                   newEntry = false;
               }
               
               //grab the content
               if (!newEntry) {
                   if (grabDate) {
                       date = line;
                       grabDate = false;
                   } else {
                       //grab the scriptures
                       foundScriptures.addAll(scriptures.scriptureFinder(line));
                       //grab the topics
                       foundTopics.addAll(topics.topicsFinder(line));
                       //now the content!
                       content += line;
                   }
               } else {
                   //make sure it is not the first time!
                   if (numEntries != 1) {
                       //now add to the list of entries
                       List<String> topicList = new ArrayList<>(foundTopics);
                       JournalEntry entry = new JournalEntry(title, content, date, topicList, foundScriptures);
                       //now add it!
                       entries.add(entry);
                       //reinitailize everything!
                       title = "";
                       content = "";
                       date = "";
                       foundScriptures = new ArrayList<>();
                       foundTopics = new HashSet<>();
                   }
                    //grab the title!
                    if (line.contains(" ")) {
                        String [] split = line.split(" ");
                        title = split[1];
                    }
               }
           }
           //now add the last of the list...
           List<String> topicList = new ArrayList<>(foundTopics);
           JournalEntry entry = new JournalEntry(title, content, date, topicList, foundScriptures);
           //now add it!
           entries.add(entry);
           //close it!
           reader.close();
       } catch (IOException ex) {
           ex.getMessage();
       }
       
       return entries;
   }
}