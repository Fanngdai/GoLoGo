package gol.gui;

import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import gol.data.golData;
import gol.data.golState;
import djf.AppTemplate;
import static djf.settings.AppPropertyType.LOAD_ERROR_MESSAGE;
import static djf.settings.AppPropertyType.LOAD_ERROR_TITLE;
import static djf.settings.AppPropertyType.LOAD_WORK_TITLE;
import static djf.settings.AppStartupConstants.APP_PROPERTIES_FILE_NAME;
import static djf.settings.AppStartupConstants.PATH_WORK;
import static djf.settings.AppStartupConstants.setLanguage;
import djf.ui.AppMessageDialogSingleton;
import gol.data.DraggableText;
import gol.transaction.RemoveShape_Transaction;
import gol.transaction.StrokeSize_Transaction;
import jtps.jTPS;
import properties_manager.PropertiesManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.scene.Node;

/**
 * This class responds to interactions with other UI logo editing controls.
 * 
 * @author Richard McKenna
 * @author Fanng Dai
 * @version 1.0
 */
public class LogoEditController {
    AppTemplate app;
    golData dataManager;
    private static jTPS jtps;
        
    public LogoEditController(AppTemplate initApp) {
	app = initApp;
	dataManager = (golData)app.getDataComponent();
        jtps = dataManager.getJTPS();
    }
    
    /**
     * This method handles the response for selecting either the
     * selection or removal tool.
     */
    public void processSelectSelectionTool() {
	// CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.DEFAULT);
	
	// CHANGE THE STATE
	dataManager.setState(golState.SELECTING_SHAPE);	
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	golWorkspace workspace = (golWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
    }
    
    /**
     * This method handles a user request to remove the selected shape.
     */
    public void processRemoveSelectedShape() {
        RemoveShape_Transaction temp = new RemoveShape_Transaction(app, dataManager.getSelectedShape());
        jtps.addTransaction(temp);
        
	// REMOVE THE SELECTED SHAPE IF THERE IS ONE
//	dataManager.removeSelectedShape();
        
// Put into transaction class
        // Disable the buttons when you remove
//        app.getGUI().getCopyButton().setDisable(true);
//        app.getGUI().getCutButton().setDisable(true);
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	golWorkspace workspace = (golWorkspace)app.getWorkspaceComponent();
        workspace.italicsButton.setDisable(true);
        workspace.boldButton.setDisable(true);
	workspace.reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * This method processes a user request to start drawing a rectangle.
     */
    public void processSelectRectangleToDraw() {
	// CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.CROSSHAIR);
	
	// CHANGE THE STATE
	dataManager.setState(golState.STARTING_RECTANGLE);
        
	// ENABLE/DISABLE THE PROPER BUTTONS
	golWorkspace workspace = (golWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
    }
    
    /**
     * Used when making rectangle without image
     */
//    public void processSelectRectangleToDrawHelper() {
//	dataManager.setBackgroundImage(null);
//    }
    
    /**
     * This method provides a response to the user requesting to start
     * drawing an ellipse.
     */
    public void processSelectEllipseToDraw() {
	// CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.CROSSHAIR);
	
	// CHANGE THE STATE
	dataManager.setState(golState.STARTING_ELLIPSE);
        
	// ENABLE/DISABLE THE PROPER BUTTONS
	golWorkspace workspace = (golWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
    }
    
    /**
     * This method processes a user request to move the selected shape
     * down to the back layer.
     */
    public void processMoveSelectedShapeToBack() {
	dataManager.moveSelectedShapeToBack();
	app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * This method processes a user request to move the selected shape
     * up to the front layer.
     */
    public void processMoveSelectedShapeToFront() {
	dataManager.moveSelectedShapeToFront();
	app.getGUI().updateToolbarControls(false);
    }
        
    /**
     * This method processes a user request to select a fill color for
     * a shape.
     */
    public void processSelectFillColor() {
	golWorkspace workspace = (golWorkspace)app.getWorkspaceComponent();
	Color selectedColor = workspace.getFillColorPicker().getValue();
	if (selectedColor != null) {
	    dataManager.setCurrentFillColor(selectedColor);
	    app.getGUI().updateToolbarControls(false);
	}
    }
    
    /**
     * This method processes a user request to select the outline
     * color for a shape.
     */
    public void processSelectOutlineColor() {
	golWorkspace workspace = (golWorkspace)app.getWorkspaceComponent();
	Color selectedColor = workspace.getOutlineColorPicker().getValue();
	if (selectedColor != null) {
	    dataManager.setCurrentOutlineColor(selectedColor);
	    app.getGUI().updateToolbarControls(false);
	}    
    }
    
    /**
     * This method processes a user request to select the 
     * background color.
     */
    public void processSelectBackgroundColor() {
	golWorkspace workspace = (golWorkspace)app.getWorkspaceComponent();
	Color selectedColor = workspace.getBackgroundColorPicker().getValue();
	if (selectedColor != null) {
	    dataManager.setBackgroundColor(selectedColor);
	    app.getGUI().updateToolbarControls(false);
	}
    }
    
    /**
     * This method processes a user request to select the outline
     * thickness for shape drawing.
     */
    public void processSelectOutlineThickness() {
	golWorkspace workspace = (golWorkspace)app.getWorkspaceComponent();
	int outlineThickness = (int)workspace.getOutlineThicknessSlider().getValue();
	dataManager.setCurrentOutlineThickness(outlineThickness);
	app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * This method processes a user request to take a snapshot of the
     * current scene.
     */
    public void processSnapshot() {
	golWorkspace workspace = (golWorkspace)app.getWorkspaceComponent();
	Pane canvas = workspace.getCanvas();
	WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
	File file = new File("Logo.png");
	try {
	    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
	}
	catch(IOException ioe) {
//	    ioe.printStackTrace();
	}
    }
    
    public void handleLanguageRequest() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        try {
            // Ask user to change language. The popup
            setLanguage();
            
            // Load the Json file
            app.getGUI().getFileController().writeLanguageJson();
            
            app.loadProperties(APP_PROPERTIES_FILE_NAME);
            
            // NOW RELOAD THE WORKSPACE WITH THE RESET DATA
            app.getWorkspaceComponent().reloadWorkspace(app.getDataComponent());
            
            golWorkspace workspace = (golWorkspace)app.getWorkspaceComponent();
            workspace.reloadWorkspace(dataManager);
        }
        catch (Exception ioe) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(LOAD_ERROR_TITLE), props.getProperty(LOAD_ERROR_MESSAGE));
        }
    }
        
    /**
     * Only gets the image.
     */
    public boolean handleImageButton(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // AND NOW ASK THE USER FOR THE FILE TO OPEN       
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_WORK));
	fc.setTitle(props.getProperty(LOAD_WORK_TITLE));
            
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fc.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        //Show open file dialog
        File selectedFile = fc.showOpenDialog(app.getGUI().getWindow());

        try {
            if (selectedFile != null) {
                BufferedImage bufferedImage = ImageIO.read(selectedFile);
                // Get the path for the image
                String imagePath = selectedFile.getCanonicalPath();
                dataManager.setBackgroundImage(bufferedImage, imagePath);
                return true;
            }
            else{
                return false;
            }
        } catch (IOException ex) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(LOAD_ERROR_TITLE), props.getProperty(LOAD_ERROR_MESSAGE));
            return false;
        }
    }
    
    public void handleAddTextButton(){
        dataManager = (golData) app.getDataComponent();
        
        if(!dataManager.startNewTextbox()){
            return;
        }
        dataManager.setState(golState.STARTING_TEXT);
        golWorkspace workspace = (golWorkspace)app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
    }
    
    // If you copied something, able the paste button
    public void handleCopyRequest(){
        dataManager.setCopiedShape(dataManager.getSelectedShape());
        app.getGUI().getPasteButton().setDisable(false);
    }
    
    public void handlePasteRequest(){  
        golWorkspace workspace = (golWorkspace)app.getWorkspaceComponent();
        
        dataManager.unhighlightShape();
        
        Node temp = dataManager.getCopiedShape();
        
        if(temp instanceof DraggableText){
            workspace.italicsButton.setDisable(false);
            workspace.boldButton.setDisable(false);
            workspace.fontFamily.setDisable(false);
            workspace.fontSize.setDisable(false);
        }
        else{
            workspace.italicsButton.setDisable(true);
            workspace.boldButton.setDisable(true);
            workspace.fontFamily.setDisable(true);
            workspace.fontSize.setDisable(true);
        }
        dataManager.setState(golState.SELECTING_SHAPE);
        workspace.reloadWorkspace(dataManager);
    }
    
    public void handleCutRequest(){
        handleCopyRequest();
        processRemoveSelectedShape();
    }
    /**
     * Return the JTPS
     * Homework2 part9
     */
    public jTPS getJTPS(){
        return this.jtps;
    }
    /**
     * Handles do and undo
     * Homework2 part9
     */
    
    public void handleDoTransaction(){
        jtps.doTransaction();  
    }
    public void handleUndoTransaction(){
        jtps.undoTransaction();
//        if(jtps.getAmtTransactions() <= 0){
//            app.getGUI().getUndoButton().setDisable(true);
//        }
    }
}
