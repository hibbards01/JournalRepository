/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journal;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.text.Font;

/**
 *
 * @author SamuelHibbard
 */
public class JournalGUI extends Application {
    /*
    * Member variables
    */
    private Journal journal;           //we need the journal! 
    private ListView<String> list;     //also a list!
    private TextField inputTitle;      //for the title the user gives
    private TextArea input;            //and content....
    private Text scriptureText;        //for the list of scriptures...
    private Text topicsText;           //for the list of topics...
    private boolean newEntry = false;  //do we have a new entry?
    private boolean isSaved = false;   //checking to make sure we saved everything
    private static int grabInput = -1; //for the purpose of the dialog.
    private Text dialogTextUpdater;
    private Stage updaterStage;
    
    /*
    * These member variables will be used if the user wants to switch
    *   between his new entry and other entries.
    */
    private String newEntryTitle;
    private String newEntryInput;
    
    /**
     * START
     *  Overriding this method
     * @param primaryStage 
     */
    @Override
    public void start(Stage primaryStage) {
        //create a new journal!
        journal = new Journal();
        //and some other variables...
        list = new ListView<>();
        input = new TextArea();
        inputTitle = new TextField();
        scriptureText = new Text();
        topicsText = new Text();
        dialogTextUpdater = new Text();
        updaterStage = new Stage();
        
        //set the title!
        primaryStage.setTitle("Scripture Insight Journal");
        //show the very first scene when it starts!
        BorderPane root = new BorderPane();
        //now set everything!
        root.setTop(mainMenu(primaryStage));
        root.setLeft(sideMenu());
        root.setCenter(inputField());
        
        //create the scene!
        Scene scene = new Scene(root, 750, 600);
       
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * DISPLAYTEXT
     *  Display the content and title of the journal entry!
     * @param title 
     */
    public void displayText(String title) {
        //make sure it is not null...
        if (title != null) {
            //do this only if it is not a new entry...
            if (!title.contains("New Entry")) {
                //now find it!
                for (JournalEntry entry : journal.getEntries()) {
                    
                    //check the title!
                    if (title.contains(entry.getTitle()) &&
                            title.contains(entry.getDate())) {
                    
                         //now display it!
                        inputTitle.setText(entry.getTitle());
                        input.setText(entry.getContent());
                
                        break;
                    }
                }
            } else {
                inputTitle.setText(newEntryTitle);
                input.setText(newEntryInput);
            }
        }    
    }
    
    /**
     * INPUTFIELD
     *  Make the inputs look nice!
     * @return 
     */
    public BorderPane inputField() {
        //create the variables!
        BorderPane border = new BorderPane();
        GridPane root = new GridPane();
        GridPane grid = new GridPane();
        GridPane grid2 = new GridPane();
        Label label = new Label("Title");
        TabPane pane = new TabPane();
        Tab entry = new Tab("Entry");
        Tab script = new Tab("Scriptures");
        Tab topics = new Tab("Topics");
        
        //set the sizes!
        root.setPrefSize(520, 600);
        grid.setVgap(5);
        grid.setHgap(7);
        grid2.setVgap(1);
        grid2.setHgap(1);
        root.setVgap(0);
        root.setHgap(1);
        input.setPrefSize(495, 490);
        input.setWrapText(true);
        inputTitle.setPrefWidth(250);
        
        //this one is special...
        border.layoutBoundsProperty().addListener((ObservableValue<? extends Bounds> observable, 
                Bounds oldValue, Bounds newValue) -> {
            //add to the INPUT!
            //check horizontally!
            if (newValue.getHeight() > oldValue.getHeight()) {
                input.setPrefHeight(input.getPrefHeight() + 3);
            } else if (newValue.getHeight() < oldValue.getHeight()) {
                input.setPrefHeight(input.getPrefHeight() - 3);
            }
            
            //then vertically!
            if (newValue.getWidth() > oldValue.getWidth()) {
                input.setPrefWidth(input.getPrefWidth() + 3);
            } else if (newValue.getWidth() < oldValue.getWidth()) {
                input.setPrefWidth(input.getPrefWidth() - 3);
            }
        });
        
        //now put things in the right place!
        grid.add(label, 10, 0);
        grid.add(inputTitle, 13, 0);
        grid2.add(input, 1, 5);
      
        root.add(grid, 11, 2);
        root.add(grid2, 11, 8);
        
        //do the tabs!
        entry.setContent(root);
        script.setContent(scriptureText);
        topics.setContent(topicsText);
        
        //put everything in the borderpane!
        pane.getTabs().addAll(entry, script, topics);
        border.setTop(pane);
      
        return border;
    }
    
    /**
     * UPDATELIST
     *  This will mainly update the list!
     */
    public void updateList() {
        //clear everything!
        list.getItems().clear();
        
        //now update it!
        int count = 0;
        for (JournalEntry entry : journal.getEntries()) {
            //grab everything!
            String title = entry.getTitle();
            String date = entry.getDate();
                
            title = title + " " + date;
                
            list.getItems().add(count, title);
            count++;
        }
    }
    
    /**
     * SIDEMENU
     *  This will display the entries that the user has done
     * @return 
     */
    public TabPane sideMenu() {
        //create one tab...
        TabPane pane = new TabPane();
        VBox box = new VBox();
        TextField searchBar = new TextField();
        Tab entries = new Tab("All Entries");
        Tab search = new Tab("Search");
        ListView<String> searchList = new ListView<String>();
        
        //create the variables!
        list.setPrefWidth(230);
        searchList.setPrefWidth(230);
        searchList.setPrefHeight(500);
        searchBar.setText("Search");
        
        if (!journal.getEntries().isEmpty()) {
            //update the list!
            updateList();
            
            //also load the first entry to the INPUT and INPUTFIELD
            updateList();
            displayText(list.getItems().get(0));
            
            //now load the scriptures and topics
            loadScriptures(list.getItems().get(0));
            loadTopics(list.getItems().get(0));
        }
            
        
        //create an action.
        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, 
                                String oldValue, String newValue) {
                //grab the new entry...
                if (oldValue == "New Entry" && newEntry) {
                    newEntryTitle = inputTitle.getText();
                    newEntryInput = input.getText();
                } else {
                    //update the old list...
                    updateJournalEntry(oldValue);
                }
               
                //now display it!
                displayText(newValue);
                
                //upload the new scriptures and topics...
                loadScriptures(newValue);
                loadTopics(newValue);
                
            }
        });
        
        //now for the search bar action....
        searchBar.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                //now find the topic.. or scriptures!
                if (event.getCode().equals(KeyCode.ENTER)) {
                    //now find them!
                    search(searchList, searchBar.getText());
                }
            }
        });
        //just to make it easier for the user...
        searchBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //select and focus the searchbar!
                searchBar.selectAll();
            }            
        });
        
        //now to select the create things!
        searchList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, 
                    String oldValue, String newValue) {
                
                if (!newValue.contains("Entries found:")) {
                    //do the same idea as the other list...
                    //update the old list...
                    updateJournalEntry(oldValue);
                
                    //now display it!
                    displayText(newValue);
                
                    //upload the new scriptures and topics...
                    loadScriptures(newValue);
                    loadTopics(newValue);
                }
            }
        });
        
        //add to VBOX
        box.getChildren().add(searchBar);
        box.getChildren().add(searchList);
        
        //add them to the tabs!
        entries.setContent(list);
        search.setContent(box);
        
        //add to the pane
        pane.getTabs().addAll(entries, search);
        
        return pane;
    }
    
    /**
     * UPDATEJOUNRALENTRY
     *  Update the journal entries!
     * @param title 
     */
    private void updateJournalEntry(String title) {
        //update the entry!
        if (title != null) {
            for (JournalEntry entry : journal.getEntries()) {
                
                //grab the entry!
                if (title.contains(entry.getTitle()) 
                        && title.contains(entry.getDate())) {
                    //then do this!
                    String input = this.input.getText();
                    String grabTitle = inputTitle.getText();
                    this.input.clear();
                    inputTitle.clear();
            
                    //now update it....
                    entry.setContent(input);
                    entry.setTitle(grabTitle);
                }
            }
        }
    }
    
    /**
     * OPENINGSCENE
     *  This will contain the main menu of options
     * @return 
     */
    public VBox mainMenu(Stage stage) {
        //create everything!
        VBox container = new VBox();  //Creates a container to hold all Menu Objects.
        MenuBar mainMenu = new MenuBar();  //Creates our main menu to hold our Sub-Menus.
 
        //now add to the VBox
        container.getChildren().add(mainMenu);

        //now add to the menu!
        Menu file = new Menu("File");
        MenuItem newEntry = new MenuItem("New Entry");
        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");
        Menu export = new Menu("Export as...");
        MenuItem exit = new MenuItem("Quit");
        file.getItems().addAll(newEntry, open, save, export, exit);
        //now for the export!
        MenuItem saveTXT = new MenuItem("To .txt");
        MenuItem saveXML = new MenuItem("To .xml");
        export.getItems().addAll(saveTXT, saveXML);
        //now add the the menu!
        mainMenu.getMenus().add(file);
        
        //
        //now give them an action and handlers to all of them!!
        //
        
        //create a new entry!
        newEntry.setOnAction((ActionEvent event) -> {
            //make sure there are no duplicates!
            if (!this.newEntry) {
                //add a new list view...
                list.getItems().add(0, "New Entry");
           
                //update the old journal entry...
                updateJournalEntry(inputTitle.getText());
                
                scriptureText.setText("");
                topicsText.setText("");
            
                //set the focus and select!
                list.getFocusModel().focus(0);
                list.getSelectionModel().select(0);
                this.newEntry = true;
            }
        });
        
        //open a file!
        open.setOnAction((ActionEvent event) -> {
            //use the file chooser to grab the file path!
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            File grabFile = fileChooser.showOpenDialog(stage);
            
            //check it!
            if (grabFile != null) {
                //open it!
                journal.read(grabFile.getAbsolutePath());
                
                //create a new thread!
                list.getItems().clear();
                ListUpdater updater = new ListUpdater(dialogTextUpdater, list, updaterStage);
                UpdateGUI update = new UpdateGUI(updater, journal.getEntries());
                Thread thread = new Thread(update);
                thread.start();
                uploadDialog();
                
                //change this...
                isSaved = false;
                this.newEntry = false;
            }
        });
        
        //exit the program!
        exit.setOnAction((ActionEvent event) -> {
            //exit out of the program!
            if (this.newEntry || !isSaved) {
                //ask the user if they want to save...
                showDialog();
                
                if (grabInput == 0) {
                    //do this only if there is a new entry...
                    if (this.newEntry) {
                        saveNewEntry();
                    }
                    
                    //grab properties!
                    LoadProperties loadProp = new LoadProperties("/journal/rsc/Journal.properties");
                    //now grab the properties!
                    String filePath = loadProp.grabProperties(2);
                    //save all the ENTRIES!
                    journal.write(filePath); 
                }
            }
            
            //check this also...
            if (grabInput != 2) {
                Platform.exit();
            }
        });
        
        //save the file!
        save.setOnAction((ActionEvent event) -> {
            //check for New Entries!
            if (this.newEntry) {
                saveNewEntry();
            }
            //grab properties!
            LoadProperties loadProp = new LoadProperties("/journal/rsc/Journal.properties");
            //now grab the properties!
            String filePath = loadProp.grabProperties(2);
            //save all the ENTRIES!
            
            //update the selected item...
            updateJournalEntry(list.getSelectionModel().getSelectedItem());
            
            journal.write(filePath);
            //now save it!
            isSaved = true;
        });
        
        //save as an .txt file
        saveTXT.setOnAction((ActionEvent event) -> {
            //use the file chooser to grab the file path!
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");
            File grabFile = fileChooser.showSaveDialog(stage);
            String setFile = grabFile.getAbsolutePath() + ".txt";
            
            //grab the right entry...
            List<JournalEntry> entry = new ArrayList<>();
            entry.add(0, findEntry());
            
            //now write it!
            journal.write(entry, setFile);
        });
        
        //save as an .xml
        saveXML.setOnAction((ActionEvent event) -> {
            //use the file chooser to grab the file path!
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");
            File grabFile = fileChooser.showSaveDialog(stage);
            String setFile = grabFile.getAbsolutePath() + ".xml";
            
            //grab the right entry...
            List<JournalEntry> entry = new ArrayList<>();
            entry.add(0, findEntry());
            
            //now write it!
            journal.write(entry, setFile);
        });
        
        return container;
    }
    
    /**
     * SEARCH
     *  This will search for the topics and scriptures!
     * @param list
     * @param word 
     */
    public void search(ListView<String> list, String word) {
        //now to find them!
        list.getItems().clear();
        for (JournalEntry entry : journal.getEntries()) {
            //look thru topics first...
            boolean found = false;
            for (String topic : entry.getTopic()) {
                if (word.contains(topic)) {
                    found = true;
                }
            }
            
            //then at scriptures!
            for (Scripture s : entry.getScriptures()) {
                if (word.contains(s.getBook())) {
                    if (word.contains(s.getChapters())) {
                        found = true;
                    }
                }
            }
            
            //now see if we have found it!
            if (found) {
                //now add to the list!
                list.getItems().add(entry.getTitle() + " " + entry.getDate());
            }
        }
        
        //if list is empty then tell the user... 
        if (list.getItems().size() == 0) {
            list.getItems().add("No entries found");
        } else {
            list.getItems().add(0, "Entries found:");
        }
    }

    /**
     * LOADSCRIPTURES
     *  This will load the scriptures into the tab!
     * @param title
     */
    public void loadScriptures(String title) {
        
        //now load it!
        if (title != null && !title.contains("New Entry")) {
            JournalEntry entry = findEntry(title);
            String text = "\n\tScriptures Referenced:\n\n";
            //loop thru all the scriptures
            for (Scripture script : entry.getScriptures()) {
                //grab it!
                text += "\t\t";
                text += script.getScripture();
            }
            
            //now put it in!
            scriptureText.setText(text);
        } else {
            scriptureText.setText("");
        }
    }
    
    /**
     * LOADTOPICS
     *  Load all the topics to the text!
     * @param title
     */
    public void loadTopics(String title) {       
        //now load it!
        if (title != null && !title.contains("New Entry")) {
            JournalEntry entry = findEntry(title);
            String text = "\n\tTopics Referenced:\n\n";
            
            //loop thru the topics!
            for (String index : entry.getTopic()) {
                //grab everything!
                text += "\t\t" + index + "\n";
            }
            
            //now put it in!
            topicsText.setText(text);
        } else {
            topicsText.setText("");
        }
    }
    
    /**
     * FINDENTRY
     *  Find the entry based on title!
     * @param title
     * @return 
     */
    public JournalEntry findEntry(String title) {
        JournalEntry grabEntry = null;
        
        //now grab it!
        for (JournalEntry entry : journal.getEntries()) {
            if (title.contains(entry.getTitle()) 
                    && title.contains(entry.getDate())) {
                //then do this!
                grabEntry = entry;
            }
        }
        
        return grabEntry;
    }
    
    /**
     * FINDENTRY
     *  Find the entry with list!    
     * @return 
     */
    public JournalEntry findEntry() {
        //find the entry!
        String title = list.getSelectionModel().getSelectedItem();
        JournalEntry grabEntry = null;
        
        //now grab it!
        for (JournalEntry entry : journal.getEntries()) {
            if (title.contains(entry.getTitle()) 
                    && title.contains(entry.getDate())) {
                //then do this!
                grabEntry = entry;
            }
        }
        
        return grabEntry;
    }
    
    /**
     * SAVENEWENTRY
     *  Save the new entry!
     */
    public void saveNewEntry() {
        //create the format!
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //get current date time with Date()
        Date date = new Date();
        String grabDate = dateFormat.format(date);
            
        //now create a new journal!
        journal.createNewEntry(inputTitle.getText(), 
                               input.getText(), grabDate);
        
        //update the list!
        updateList();
        
        //and change this...
        this.newEntry = false;
    }
    
    /**
     * SHOWDIALOG
     *  This will prompt the user!
     */
    private void showDialog() {
        //create the dialog!
        Stage dialog = new Stage();
        BorderPane panel = new BorderPane();
        GridPane grid = new GridPane();
        Button yes = new Button("Yes and quit");
        Button no = new Button("No and quit");
        Button cancel = new Button("Cancel");
        Text question = new Text("Do you want to save?");
        HBox buttons = new HBox();
        Font font = new Font(20);
        
        //set everything!
        dialog.setTitle("Do you want to save?");
        question.setFont(font);
        buttons.setSpacing(20);
        buttons.getChildren().addAll(yes, no, cancel);
        
        //now set up the grid!
        grid.setHgap(5);
        grid.setVgap(10);
        grid.add(question, 10, 5);
        grid.add(buttons, 10, 10);
        
        //now the panel!
        panel.setCenter(grid);
        
        //now create all the handlers!
        
        //first the yes one...
        yes.setOnAction((ActionEvent event) -> {
            grabInput = 0;
            dialog.close(); 
        });
        
        //now the no...
        no.setOnAction((ActionEvent event) -> {
            grabInput = 1;
            dialog.close();
        });
        
        //now the cancel...
        cancel.setOnAction((ActionEvent event) -> {
            grabInput = 2;
            dialog.close();
        });
        
        //now create the scene!
        Scene scene = new Scene(panel, 400, 200);
        
        //now show it and wait!
        dialog.setScene(scene);
        dialog.showAndWait();
    }
   
    public void uploadDialog() {
        //create the scene and dialog!
        BorderPane pane = new BorderPane();
        pane.setCenter(dialogTextUpdater);
                
        //create the scene!
        Scene scene = new Scene(pane, 300, 200);
        updaterStage.setScene(scene);
                
        //now show it!
        updaterStage.show();
    }
    
    /**
     * MAIN
     *  Driver program.
     * @param args 
     */
    public static void main(String [] args) {
        //launch the GUI!
        launch(args);
    }
}
