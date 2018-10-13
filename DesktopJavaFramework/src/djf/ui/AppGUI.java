package djf.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import djf.controller.AppFileController;
import djf.AppTemplate;
import static djf.settings.AppPropertyType.*;
import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import java.net.URL;
import javafx.geometry.Pos;

/**
 * This class provides the basic user interface for this application,
 * including all the file controls, but not including the workspace,
 * which would be customly provided for each app.
 * 
 * @author Richard McKenna
 * @co-author Fanng Dai
 * @version 1.0
 */
public class AppGUI {
    // THIS HANDLES INTERACTIONS WITH FILE-RELATED CONTROLS
    protected AppFileController fileController;

    // THIS IS THE APPLICATION WINDOW
    protected Stage primaryStage;

    // THIS IS THE STAGE'S SCENE GRAPH
    protected Scene primaryScene;

    // THIS PANE ORGANIZES THE BIG PICTURE CONTAINERS FOR THE
    // APPLICATION AppGUI. NOTE THAT THE WORKSPACE WILL GO
    // IN THE CENTER REGION OF THE appPane
    protected BorderPane appPane;
    
    // THIS IS THE TOP PANE WHERE WE CAN PUT TOOLBAR
    protected BorderPane topToolbarPane;
    
    // THIS IS THE FILE TOOLBAR AND ITS CONTROLS
    protected FlowPane fileToolbarLeft;
    protected FlowPane fileToolbarCenter;
    protected FlowPane fileToolbarRight;

    // FILE TOOLBAR BUTTONS
    protected Button newButton;
    protected Button loadButton;
    protected Button saveButton;
    protected Button exitButton;
    // Homework 2
    private Button languageButton;
    private Button aboutButton;
    
    private Button cutButton;
    private Button copyButton;
    private Button pasteButton;
    private Button undoButton;
    private Button redoButton;
    
    // THIS DIALOG IS USED FOR GIVING FEEDBACK TO THE USER
    protected AppYesNoCancelDialogSingleton yesNoCancelDialog;
    
    // THIS TITLE WILL GO IN THE TITLE BAR
    protected String appTitle;
    
    /**
     * This constructor initializes the file toolbar for use.
     * 
     * @param initPrimaryStage The window for this application.
     * 
     * @param initAppTitle The title of this application, which
     * will appear in the window bar.
     * 
     * @param app The app within this gui is used.
     */
    public AppGUI(  Stage initPrimaryStage, 
		    String initAppTitle, 
		    AppTemplate app){
	// SAVE THESE FOR LATER
	primaryStage = initPrimaryStage;
	appTitle = initAppTitle;
	       
        // INIT THE TOOLBAR
        initTopToolbar(app);
        		
        // AND FINALLY START UP THE WINDOW (WITHOUT THE WORKSPACE)
        initWindow();
        
        // INIT THE STYLESHEET AND THE STYLE FOR THE FILE TOOLBAR
        initStylesheet(app);
        initFileToolbarStyle();        
    }
    
    /**
     * Accessor method for getting the file toolbar controller.
     */
    public AppFileController getFileController() { return fileController; }
    
    /**
     * Accessor method for getting the application pane, within which all
     * user interface controls are ultimately placed.
     * 
     * @return This application GUI's app pane.
     */
    public BorderPane getAppPane() { return appPane; }
    
    /**
     * Accessor method for getting the toolbar pane in the top, within which
     * other toolbars are placed.
     * 
     * @return This application GUI's topToolBar pane.
     */    
    private BorderPane getTopToolbarPane() {
        return topToolbarPane;
    }
    
    /**
     * Accessor method for getting the file toolbar pane, within which all
     * file controls are ultimately placed.
     * 
     * @return This application GUI's app pane.
     */    
    public FlowPane getFileToolbarLeft() {
        return fileToolbarLeft;
    }
    
    /**
     * Accessor method for getting the file toolbar pane, within which all
     * file controls are ultimately placed.
     * 
     * @return This application GUI's app pane.
     */    
    public FlowPane getFileToolbarRight() {
        return fileToolbarRight;
    }
    
    /**
     * Accessor method for getting this application's primary stage's,
     * scene.
     * 
     * @return This application's window's scene.
     */
    public Scene getPrimaryScene() { return primaryScene; }
    
    /**
     * Accessor method for getting this application's window,
     * which is the primary stage within which the full GUI will be placed.
     * 
     * @return This application's primary stage (i.e. window).
     */    
    public Stage getWindow() { return primaryStage; }

    // The buttons I made for hw2. Actions are held in the goLogoLo package
    public Button getCutButton(){ return cutButton; }
    public Button getCopyButton(){ return copyButton; }
    public Button getPasteButton(){ return pasteButton; }
    public Button getUndoButton(){ return undoButton; }
    public Button getRedoButton(){ return redoButton; }
    public Button getLanguageButton(){ return languageButton; }
    public Button getAboutButton(){ return aboutButton; }
    
    /**
     * This method is used to activate/deactivate toolbar buttons when
     * they can and cannot be used so as to provide foolproof design.
     * 
     * @param saved Describes whether the loaded Page has been saved or not.
     */
    public void updateToolbarControls(boolean saved) {
        // THIS TOGGLES WITH WHETHER THE CURRENT COURSE
        // HAS BEEN SAVED OR NOT
        saveButton.setDisable(saved);

        // ALL THE OTHER BUTTONS ARE ALWAYS ENABLED
        // ONCE EDITING THAT FIRST COURSE BEGINS
	newButton.setDisable(false);
        loadButton.setDisable(false);
	exitButton.setDisable(false);

        // NOTE THAT THE NEW, LOAD, AND EXIT BUTTONS
        // ARE NEVER DISABLED SO WE NEVER HAVE TO TOUCH THEM
    }

    /****************************************************************************/
    /* BELOW ARE ALL THE PRIVATE HELPER METHODS WE USE FOR INITIALIZING OUR AppGUI */
    /****************************************************************************/
    
    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initTopToolbar(AppTemplate app) {
        fileToolbarLeft = new FlowPane();
        fileToolbarLeft.setMinWidth(300);
        fileToolbarLeft.setHgap(10);
        // Set buttons to center
        fileToolbarLeft.setAlignment(Pos.CENTER);
        
        fileToolbarCenter = new FlowPane();
        fileToolbarCenter.setMinWidth(300);
        fileToolbarCenter.setHgap(10);
        // Set buttons to center
        fileToolbarCenter.setAlignment(Pos.CENTER);
        
        fileToolbarRight = new FlowPane();
        fileToolbarRight.setMinWidth(300);
        fileToolbarRight.setHgap(10);
        // Set buttons to center
        fileToolbarRight.setAlignment(Pos.CENTER);

        // HERE ARE OUR FILE TOOLBAR BUTTONS, NOTE THAT SOME WILL
        // START AS ENABLED (false), WHILE OTHERS DISABLED (true)
        newButton = initChildButton(fileToolbarLeft,	NEW_ICON.toString(),	    NEW_TOOLTIP.toString(),	false);
        loadButton = initChildButton(fileToolbarLeft,	LOAD_ICON.toString(),	    LOAD_TOOLTIP.toString(),	false);
        saveButton = initChildButton(fileToolbarLeft,	SAVE_ICON.toString(),	    SAVE_TOOLTIP.toString(),	true);
        exitButton = initChildButton(fileToolbarLeft,	EXIT_ICON.toString(),	    EXIT_TOOLTIP.toString(),	false);
        // Homework 2
        cutButton = initChildButton(fileToolbarCenter,	CUTTOOLBAR_ICON.toString(), CUTTOOLBAR_TOOLTIP.toString(),true);
        copyButton = initChildButton(fileToolbarCenter,	COPYTOOLBAR_ICON.toString(), COPYTOOLBAR_TOOLTIP.toString(),true);
        pasteButton = initChildButton(fileToolbarCenter,PASTETOOLBAR_ICON.toString(), PASTETOOLBAR_TOOLTIP.toString(),true);
        undoButton = initChildButton(fileToolbarCenter,	UNDO_ICON.toString(), UNDO_TOOLTIP.toString(),true);
        redoButton = initChildButton(fileToolbarCenter,	REDO_ICON.toString(), REDO_TOOLTIP.toString(),true);
        // Homework2
        languageButton = initChildButton(fileToolbarRight,LANGUAGE_ICON.toString(), LANGUAGE_TOOLTIP.toString(),false);
        aboutButton = initChildButton(fileToolbarRight,	ABOUT_ICON.toString(),	    ABOUT_TOOLTIP.toString(),	false);

	// AND NOW SETUP THEIR EVENT HANDLERS
        fileController = new AppFileController(app);
        newButton.setOnAction(e -> {
            fileController.handleNewRequest();
        });
        loadButton.setOnAction(e -> {
            fileController.handleLoadRequest();
        });
        saveButton.setOnAction(e -> {
            fileController.handleSaveRequest();
        });
        exitButton.setOnAction(e -> {
            fileController.handleExitRequest();
        });	
        
        // NOW PUT THE FILE TOOLBAR IN THE TOP TOOLBAR, WHICH COULD
        // ALSO STORE OTHER TOOLBARS
        topToolbarPane = new BorderPane();
        topToolbarPane.setLeft(fileToolbarLeft);
        topToolbarPane.setCenter(fileToolbarCenter);
        topToolbarPane.setRight(fileToolbarRight);
    }

    // INITIALIZE THE WINDOW (i.e. STAGE) PUTTING ALL THE CONTROLS
    // THERE EXCEPT THE WORKSPACE, WHICH WILL BE ADDED THE FIRST
    // TIME A NEW Page IS CREATED OR LOADED
    private void initWindow() {
	PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // SET THE WINDOW TITLE
        primaryStage.setTitle(appTitle);

        // START FULL-SCREEN OR NOT, ACCORDING TO PREFERENCES
        primaryStage.setMaximized("true".equals(props.getProperty(START_MAXIMIZED)));

        // ADD THE TOOLBAR ONLY, NOTE THAT THE WORKSPACE
        // HAS BEEN CONSTRUCTED, BUT WON'T BE ADDED UNTIL
        // THE USER STARTS EDITING A COURSE
        appPane = new BorderPane();
        appPane.setTop(topToolbarPane);
        primaryScene = new Scene(appPane);

        // SET THE APP PANE PREFERRED SIZE ACCORDING TO THE PREFERENCES
        double prefWidth = Double.parseDouble(props.getProperty(PREF_WIDTH));
        double prefHeight = Double.parseDouble(props.getProperty(PREF_HEIGHT));
        appPane.setPrefWidth(prefWidth);
        appPane.setPrefHeight(prefHeight);

        // SET THE APP ICON
        String appIcon = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(APP_LOGO);
        primaryStage.getIcons().add(new Image(appIcon));

        // NOW TIE THE SCENE TO THE WINDOW
        primaryStage.setScene(primaryScene);
    }
    
    /**
     * This is a public helper method for initializing a simple button with
     * an icon and tooltip and placing it into a toolbar.
     * 
     * @param toolbar Toolbar pane into which to place this button.
     * 
     * @param icon Icon image file name for the button.
     * 
     * @param tooltip Tooltip to appear when the user mouses over the button.
     * 
     * @param disabled true if the button is to start off disabled, false otherwise.
     * 
     * @return A constructed, fully initialized button placed into its appropriate
     * pane container.
     */
    public Button initChildButton(Pane toolbar, String icon, String tooltip, boolean disabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
	
	// LOAD THE ICON FROM THE PROVIDED FILE
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(icon);
        Image buttonImage = new Image(imagePath);
	
	// NOW MAKE THE BUTTON
        Button button = new Button();
        button.setDisable(disabled);
        button.setGraphic(new ImageView(buttonImage));
        Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip));
        button.setTooltip(buttonTooltip);
	
	// PUT THE BUTTON IN THE TOOLBAR
        toolbar.getChildren().add(button);
	
	// AND RETURN THE COMPLETED BUTTON
        return button;
    }
    
   /**
     *  Note that this is the default style class for the top file toolbar
     * and that style characteristics for this type of component should be 
     * put inside app_properties.xml.
     */
    public static final String CLASS_BORDERED_PANE = "bordered_pane";

   /**
     *  Note that this is the default style class for the file buttons in
     * the top file toolbar and that style characteristics for this type
     * of component should be put inside app_properties.xml.
     */
    public static final String CLASS_FILE_BUTTON = "file_button";
    
    /**
     * This function sets up the stylesheet to be used for specifying all
     * style for this application. Note that it does not attach CSS style
     * classes to controls, that must be done separately.
     */
    private void initStylesheet(AppTemplate app) {
	// SELECT THE STYLESHEET
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	String stylesheet = props.getProperty(APP_PATH_CSS);
	stylesheet += props.getProperty(APP_CSS);
        Class appClass = app.getClass();
	URL stylesheetURL = appClass.getResource(stylesheet);
	String stylesheetPath = stylesheetURL.toExternalForm();
	primaryScene.getStylesheets().add(stylesheetPath);	
    }
    
    /**
     * Changes the languages of blah blah.
     */
    public void reloadLabels(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        Tooltip button = new Tooltip(props.getProperty(NEW_TOOLTIP.toString()));
        newButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(LOAD_TOOLTIP.toString()));
        loadButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(SAVE_TOOLTIP.toString()));
        saveButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(EXIT_TOOLTIP.toString()));
        exitButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(CUTTOOLBAR_TOOLTIP.toString()));
        cutButton.setTooltip(button);

        button = new Tooltip(props.getProperty(COPYTOOLBAR_TOOLTIP.toString()));
        copyButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(PASTETOOLBAR_TOOLTIP.toString()));
        pasteButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(UNDO_TOOLTIP.toString()));
        undoButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(REDO_TOOLTIP.toString()));
        redoButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(LANGUAGE_TOOLTIP.toString()));
        languageButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(ABOUT_TOOLTIP.toString()));
        aboutButton.setTooltip(button);
    }
    /**
     * This function specifies the CSS style classes for the controls managed
     * by this framework.
     */
    private void initFileToolbarStyle() {
	topToolbarPane.getStyleClass().add(CLASS_BORDERED_PANE);
        
        fileToolbarLeft.getStyleClass().add(CLASS_BORDERED_PANE);
	newButton.getStyleClass().add(CLASS_FILE_BUTTON);
	loadButton.getStyleClass().add(CLASS_FILE_BUTTON);
	saveButton.getStyleClass().add(CLASS_FILE_BUTTON);
	exitButton.getStyleClass().add(CLASS_FILE_BUTTON);
        
        fileToolbarCenter.getStyleClass().add(CLASS_BORDERED_PANE);
        cutButton.getStyleClass().add(CLASS_FILE_BUTTON);
	copyButton.getStyleClass().add(CLASS_FILE_BUTTON);
        pasteButton.getStyleClass().add(CLASS_FILE_BUTTON);
	undoButton.getStyleClass().add(CLASS_FILE_BUTTON);
        redoButton.getStyleClass().add(CLASS_FILE_BUTTON);
        
        fileToolbarRight.getStyleClass().add(CLASS_BORDERED_PANE);
        languageButton.getStyleClass().add(CLASS_FILE_BUTTON);
	aboutButton.getStyleClass().add(CLASS_FILE_BUTTON);
    }
}
