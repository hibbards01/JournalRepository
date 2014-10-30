/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import journal.*;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author SamuelHibbard
 */
public class JournalTest {
    
    public JournalTest() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Test
    public void readTXT() {
        FileHandler handler = new FileHandler();
        Journal journal = new Journal();
        String file = "/Users/SamuelHibbard/Documents/BYU-I/Fall 2014/CS 246/Journal/src/journal/rsc/savedFiles.xml";
        
        journal.read(file);
    }
    
    @Test
    public void loadProperties() {
        Journal journal = new Journal();
        
        for (JournalEntry entry : journal.getEntries()) {
            entry.display();
        }
    }
    
    @Test
    public void displayScripture() {
        Scripture s = new Scripture("D&C", "18");
        
        s.display();
    }
    
    @Test
    public void fileHandler() {
        Journal journal = new Journal();
        FileHandler handler = new FileHandler();
        
        handler.readTXT(journal.getEntries(), "/Users/SamuelHibbard/Desktop/t.txt");
        
        for (JournalEntry entry : journal.getEntries()) {
            entry.display();
        }
    }
    
    @Test
    public void showTopics() {
        Journal journal = new Journal();
        FileHandler handler = new FileHandler();
        
        handler.readXML(journal.getEntries(), "/Users/SamuelHibbard/Desktop/t.xml");
        
        for (JournalEntry entry : journal.getEntries()) {
            for (String topic : entry.getTopic()) {
                System.out.println(topic);
            }
        }
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }
}
