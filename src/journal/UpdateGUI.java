/*
* UPDATEGUI_JAVA
*   This will update the GUI.
*/
package journal;

import java.util.List;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * UPDATEJOURNALGUI
 *  This will be the journal updater...
 * @author SamuelHibbard
 */
public class UpdateGUI implements Runnable {
    /*
     * Member variables
     */
    private Updater updater;
    private List<JournalEntry> entries;
    
    /*
     * Member methods
     */
    
    /**
     * CONSTRUCTOR
     * @param updater 
     */
    public UpdateGUI(Updater updater,
                     List<JournalEntry> entries) {
        this.updater = updater;
        this.entries = entries;
    }
    
    @Override
    public void run() {
        //now update it!
        int count = 0;
        for (JournalEntry entry : entries) {
            //pause for a second..
            try {
                Thread.sleep(1000); // wait for 1 second

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            //grab everything!
            String title = entry.getTitle();
            String date = entry.getDate();
               
            title = title + " " + date;
          
            updater.update(entry.getScriptListSize(), entry.getTopicSize(), title, false);
        }
        
        try {
            Thread.sleep(1000); // wait for 1 second
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        updater.update(0, 0, "", true);
    }
}

