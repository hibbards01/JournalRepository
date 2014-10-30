/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 