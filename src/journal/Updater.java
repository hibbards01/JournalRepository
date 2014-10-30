/*
*  UPADTER
*   This will be an interface tha will be implmented by other classes.
*/

package journal;

import javafx.scene.control.ListView;
import javafx.scene.text.Text;

/**
 * UPDATER
 *  Other classes will implement this.     
 * @author SamuelHibbard
 */
public interface Updater {
    public void update(int numScripts, int numTopics, String title, boolean isDone);
}
 