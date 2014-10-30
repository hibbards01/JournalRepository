/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journal;

import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * TEXTFIELDUPDATER
 *  This will implement the UPDATER class
 * @author SamuelHibbard
 */
public class ListUpdater implements Updater {
    /*
     * Member variables
     */
    private Text text;      //the text field we want to update
    private ListView<String> list;
    private int scriptures; //the number of scriptures,
    private int topics;     //topics,
    private int numEntries;    //and entries that were uploaded.
    private Stage stage;    //to show the stage!
    private List<JournalEntry> entries;
    private FileHandler handler;
    
    /*
     * Member methods
     */
    
    /**
     * CONSTRUCTOR
     * @param text 
     */
    public ListUpdater(Text text, ListView<String> list, Stage stage) {
        this.text = text;
        scriptures = 0;
        topics = 0;
        numEntries = 0;
        this.list = list;
        this.stage = stage;
        handler = new FileHandler();
    }
    
    /**
     * UPDATE
     *  This will actually implement the update function 
     *      from the update class.
     * @param numScripts
     * @param numTopics 
     */
    @Override
    public void update(int numScripts, int numTopics, String title, boolean isDone) {
        //make it run later...
        Platform.runLater(new Runnable () {
            @Override
            public void run() {
                if (!isDone) {
                    //make sure it works!
                    if (!list.getItems().add(title)) {
                        System.out.print("List could not be updated!\n");
                    }
                
                    //update everything!
                    ++numEntries;
                    scriptures += numScripts;
                    topics += numTopics;
                
                    //now put it into the TEXT!
                    String newText = "Entries Uploaded: " 
                            + Integer.toString(numEntries) +
                            "\n\n\tScriptures found: " 
                            + Integer.toString(scriptures) +
                            "\n\n\tTopics found: " 
                            + Integer.toString(topics);
                
                    text.setText(newText);
                } else {
                    //close the stage!
                    stage.close();
                }
            }
        }); 
    }
}
