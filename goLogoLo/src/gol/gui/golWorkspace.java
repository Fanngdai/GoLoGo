package gol.gui;

import com.sun.javafx.embed.AbstractEvents;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import static gol.golLanguageProperty.ELLIPSE_ICON;
import static gol.golLanguageProperty.ELLIPSE_TOOLTIP;
import static gol.golLanguageProperty.MOVE_TO_BACK_ICON;
import static gol.golLanguageProperty.MOVE_TO_BACK_TOOLTIP;
import static gol.golLanguageProperty.MOVE_TO_FRONT_ICON;
import static gol.golLanguageProperty.MOVE_TO_FRONT_TOOLTIP;
import static gol.golLanguageProperty.RECTANGLE_ICON;
import static gol.golLanguageProperty.RECTANGLE_TOOLTIP;
import static gol.golLanguageProperty.REMOVE_ICON;
import static gol.golLanguageProperty.REMOVE_ELEMENT_TOOLTIP;
import static gol.golLanguageProperty.SELECTION_TOOL_ICON;
import static gol.golLanguageProperty.SELECTION_TOOL_TOOLTIP;
import static gol.golLanguageProperty.SNAPSHOT_ICON;
import static gol.golLanguageProperty.SNAPSHOT_TOOLTIP;
import static gol.golLanguageProperty.IMAGE_ICON;
import static gol.golLanguageProperty.IMAGE_TOOLTIP;
import static gol.golLanguageProperty.TEXT_ICON;
import static gol.golLanguageProperty.TEXT_TOOLTIP;
import static gol.golLanguageProperty.BOLD_ICON;
import static gol.golLanguageProperty.BOLD_TOOLTIP;
import static gol.golLanguageProperty.ITALICS_ICON;
import static gol.golLanguageProperty.ITALICS_TOOLTIP;
import static djf.settings.AppStartupConstants.APP_PROPERTIES_FILE_NAME;
import gol.data.golData;
import static gol.data.golData.BLACK_HEX;
import static gol.data.golData.WHITE_HEX;
import gol.data.golState;
import djf.ui.AppYesNoCancelDialogSingleton;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppGUI;
import djf.AppTemplate;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import static djf.settings.AppPropertyType.APP_LOGO;
import static djf.settings.AppPropertyType.LANGUAGE_CHANGED_MESSAGE;
import static djf.settings.AppPropertyType.LANGUAGE_CHANGED_TITLE;
import static djf.settings.AppPropertyType.LANGUAGE_NOT_CHANGED_MESSAGE;
import static djf.settings.AppPropertyType.LANGUAGE_NOT_CHANGED_TITLE;
import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import static djf.settings.AppStartupConstants.isEnglish;
import static gol.css.golStyle.*;
import gol.data.DraggableText;
import gol.transaction.StrokeSize_Transaction;
import gol.transaction.TextChange_Transaction;
import gol.transaction.TextFont_Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import jtps.jTPS;
import properties_manager.PropertiesManager;

/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 *
 * @author Richard McKenna
 * @author Fanng Dai
 * @version 1.0
 */
public class golWorkspace extends AppWorkspaceComponent {
    // HERE'S THE APP
    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;

    // HAS ALL THE CONTROLS FOR EDITING
    VBox editToolbar;
    
    // FIRST ROW
    HBox topRow;
    Button selectionToolButton;
    Button removeButton;
    Button rectButton;
    Button ellipseButton;
    
    // Addtional Row?
    HBox centerRow;
    Button addImageButton;
    Button addTextButton;
    Button boldButton;
    Button italicsButton;
    
    HBox bottomRow;
    ComboBox<String> fontFamily;
    ComboBox<Integer> fontSize;
    
    VBox row1Box;
    
    // SECOND ROW
    HBox row2Box;
    Button moveToBackButton;
    Button moveToFrontButton;

    // THIRD ROW
    VBox row3Box;
    Label backgroundColorLabel;
    ColorPicker backgroundColorPicker;

    // FORTH ROW
    VBox row4Box;
    Label fillColorLabel;
    ColorPicker fillColorPicker;
    
    // FIFTH ROW
    VBox row5Box;
    Label outlineColorLabel;
    ColorPicker outlineColorPicker;
        
    // SIXTH ROW
    VBox row6Box;
    Label outlineThicknessLabel;
    Slider outlineThicknessSlider;
    
    // SEVENTH ROW
    HBox row7Box;
    Button snapshotButton;
    
    // THIS IS WHERE WE'LL RENDER OUR DRAWING, NOTE THAT WE
    // CALL THIS A CANVAS, BUT IT'S REALLY JUST A Pane
    Pane canvas;
    
    // HERE ARE THE CONTROLLERS
    CanvasController canvasController;
    LogoEditController logoEditController;    

    // HERE ARE OUR DIALOGS
    AppMessageDialogSingleton messageDialog;
    AppYesNoCancelDialogSingleton yesNoCancelDialog;
    
    // FOR DISPLAYING DEBUG STUFF
    Text debugText;

    private Button languageButton;
    private Button aboutButton;
    private Button cutButton;
    private Button copyButton;
    private Button pasteButton;
    private Button undoButton;
    private Button redoButton;
    
    private static jTPS jtps;
    
    /**
     * Constructor for initializing the workspace, note that this constructor
     * will fully setup the workspace user interface for use.
     *
     * @param initApp The application this workspace is part of.
     *
     * @throws IOException Thrown should there be an error loading application
     * data for setting up the user interface.
     */
    public golWorkspace(AppTemplate initApp){
	// KEEP THIS FOR LATER
	app = initApp;

	// KEEP THE GUI FOR LATER
	gui = app.getGUI();

        // LAYOUT THE APP
        initLayout();
        
        // HOOK UP THE CONTROLLERS
        initControllers();
        
        // AND INIT THE STYLE FOR THE WORKSPACE
        initStyle();
        
    }
    
    /**
     * Note that this is for displaying text during development.
     */
    public void setDebugText(String text) {
	debugText.setText(text);
    }
    
    // ACCESSOR METHODS FOR COMPONENTS THAT EVENT HANDLERS
    // MAY NEED TO UPDATE OR ACCESS DATA FROM
    
    public ColorPicker getFillColorPicker() {
	return fillColorPicker;
    }
    
    public ColorPicker getOutlineColorPicker() {
	return outlineColorPicker;
    }
    
    public ColorPicker getBackgroundColorPicker() {
	return backgroundColorPicker;
    }
    
    public Slider getOutlineThicknessSlider() {
	return outlineThicknessSlider;
    }

    public Pane getCanvas() {
	return canvas;
    }
        
    // HELPER SETUP METHOD
    private void initLayout() {
	// THIS WILL GO IN THE LEFT SIDE OF THE WORKSPACE
	editToolbar = new VBox();
        
	// ROW 1
	topRow = new HBox();
	selectionToolButton = gui.initChildButton(topRow, SELECTION_TOOL_ICON.toString(), SELECTION_TOOL_TOOLTIP.toString(), true);
	removeButton = gui.initChildButton(topRow, REMOVE_ICON.toString(), REMOVE_ELEMENT_TOOLTIP.toString(), true);
	rectButton = gui.initChildButton(topRow, RECTANGLE_ICON.toString(), RECTANGLE_TOOLTIP.toString(), false);
	ellipseButton = gui.initChildButton(topRow, ELLIPSE_ICON.toString(), ELLIPSE_TOOLTIP.toString(), false);

        // Additional Row
        centerRow = new HBox();
        addImageButton = gui.initChildButton(centerRow, IMAGE_ICON.toString(), IMAGE_TOOLTIP.toString(), false);
        addTextButton = gui.initChildButton(centerRow, TEXT_ICON.toString(), TEXT_TOOLTIP.toString(), false);
        boldButton = gui.initChildButton(centerRow, BOLD_ICON.toString(), BOLD_TOOLTIP.toString(), true);
        italicsButton = gui.initChildButton(centerRow, ITALICS_ICON.toString(), ITALICS_TOOLTIP.toString(), true);
        
        // Combo box
        ObservableList<String> font = FXCollections.observableArrayList("sansserif", "Monospaced", "Times Roman", "Courier", "Helvetica");
        fontFamily = new ComboBox<String>(font);
        fontFamily.setPromptText(isEnglish? "Font": "字体");
//        fontFamily.setMinWidth(95);
        fontFamily.setPrefSize(120,20);
        fontFamily.setDisable(true);
        
        ObservableList<Integer> fontSizee = FXCollections.observableArrayList(8,10,12,14,16,18,20,22,24,26,28,30,40,45,50,55,65,72,85,100,1);
        fontSize = new ComboBox<Integer>(fontSizee);
        fontSize.setPromptText(isEnglish? "Size": "大小");
//        fontSize.setMinWidth(95);
        fontSize.setDisable(true);
        
        fontSize.setPrefSize(70,20);
        bottomRow = new HBox();
        bottomRow.setSpacing(10);
        bottomRow.getChildren().addAll(fontFamily, fontSize);
        
        
        // Add topRow and bottomRow into a vbox.
        row1Box = new VBox();
        row1Box.getChildren().addAll(topRow,centerRow, bottomRow);
        
	// ROW 2
	row2Box = new HBox();
	moveToBackButton = gui.initChildButton(row2Box, MOVE_TO_BACK_ICON.toString(), MOVE_TO_BACK_TOOLTIP.toString(), true);
	moveToFrontButton = gui.initChildButton(row2Box, MOVE_TO_FRONT_ICON.toString(), MOVE_TO_FRONT_TOOLTIP.toString(), true);

	// ROW 3
	row3Box = new VBox();
	backgroundColorLabel = new Label(isEnglish? "Background Color":"背景颜色");
	backgroundColorPicker = new ColorPicker(Color.valueOf(WHITE_HEX));
	row3Box.getChildren().add(backgroundColorLabel);
	row3Box.getChildren().add(backgroundColorPicker);

	// ROW 4
	row4Box = new VBox();
	fillColorLabel = new Label(isEnglish? "Fill Color":"填色");
	fillColorPicker = new ColorPicker(Color.valueOf(WHITE_HEX));
	row4Box.getChildren().add(fillColorLabel);
	row4Box.getChildren().add(fillColorPicker);
	
	// ROW 5
	row5Box = new VBox();
	outlineColorLabel = new Label(isEnglish? "Outline Color":"轮廓颜色");
	outlineColorPicker = new ColorPicker(Color.valueOf(BLACK_HEX));
	row5Box.getChildren().add(outlineColorLabel);
	row5Box.getChildren().add(outlineColorPicker);
	
	// ROW 6
	row6Box = new VBox();
	outlineThicknessLabel = new Label(isEnglish? "Outline Thickness":"轮廓厚度");
	outlineThicknessSlider = new Slider(0, 10, 1);
	row6Box.getChildren().add(outlineThicknessLabel);
	row6Box.getChildren().add(outlineThicknessSlider);
	
	// ROW 7
	row7Box = new HBox();
	snapshotButton = gui.initChildButton(row7Box, SNAPSHOT_ICON.toString(), SNAPSHOT_TOOLTIP.toString(), false);
	
	// NOW ORGANIZE THE EDIT TOOLBAR
	editToolbar.getChildren().add(row1Box);
	editToolbar.getChildren().add(row2Box);
	editToolbar.getChildren().add(row3Box);
	editToolbar.getChildren().add(row4Box);
	editToolbar.getChildren().add(row5Box);
	editToolbar.getChildren().add(row6Box);
	editToolbar.getChildren().add(row7Box);
	
	// WE'LL RENDER OUR STUFF HERE IN THE CANVAS
	canvas = new Pane();
	debugText = new Text();
	canvas.getChildren().add(debugText);
	debugText.setX(100);
	debugText.setY(100);
	
	// AND MAKE SURE THE DATA MANAGER IS IN SYNCH WITH THE PANE
	golData data = (golData)app.getDataComponent();
	data.setShapes(canvas.getChildren());        

        final Rectangle outputClip = new Rectangle();
        canvas.setClip(outputClip);

        canvas.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            outputClip.setWidth(newValue.getWidth());
            outputClip.setHeight(newValue.getHeight());
        });    

	// AND NOW SETUP THE WORKSPACE
	workspace = new BorderPane();
        ((BorderPane)workspace).setCenter(canvas);
	((BorderPane)workspace).setLeft(editToolbar);
        
        
    }
    
    public int clicks = 0;
    // HELPER SETUP METHOD
    private void initControllers() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        golData dataManager = (golData) app.getDataComponent();
        jtps = dataManager.getJTPS();
        
        languageButton = gui.getLanguageButton();
        aboutButton = gui.getAboutButton();
        cutButton = gui.getCutButton();
        copyButton = gui.getCopyButton();
        pasteButton = gui.getPasteButton();
        undoButton = gui.getUndoButton();
        redoButton = gui.getRedoButton();
        
        jtps.getMostRecentProperty().addListener( e ->{
            Node node = dataManager.getSelectedShape();
//            System.out.println("GetMostRecentLIstener " + jtps.getMostRecentTransaction());
            // At the beginning of the transaction?
            if(jtps.getMostRecentTransaction() == -1){
                undoButton.setDisable(true);
            }
            else
               undoButton.setDisable(false);
            
            if(jtps.getMostRecentTransaction() == jtps.getSizeOfJTPS()-1){
                redoButton.setDisable(true);
            }
            else
                redoButton.setDisable(false);
            
            if(node != null && !(node instanceof DraggableText)){
                    boldButton.setDisable(true);
                    italicsButton.setDisable(true);
            }
        });

	// MAKE THE EDIT CONTROLLER
	logoEditController = new LogoEditController(app);
	
	// NOW CONNECT THE BUTTONS TO THEIR HANDLERS
	selectionToolButton.setOnAction(e->{
            clicks = 0;
	    logoEditController.processSelectSelectionTool();
	});
	removeButton.setOnAction(e->{
            clicks = 0;
	    logoEditController.processRemoveSelectedShape();
	});
	rectButton.setOnAction(e->{
            clicks = 0;
            // Delete the image
            logoEditController.dataManager.setBackgroundImage(null, null);
	    logoEditController.processSelectRectangleToDraw();
	});
	ellipseButton.setOnAction(e->{
            clicks = 0;
	    logoEditController.processSelectEllipseToDraw();
	});
	
        // FANNY'S WORK. HOMEWORK 2
        addImageButton.setOnAction(e->{
            clicks = 0;
            // Only make rectangles if image is selected
	    if(logoEditController.handleImageButton()){
                logoEditController.processSelectRectangleToDraw();
            }
	});
        addTextButton.setOnAction(e->{
            clicks = 0;
	    logoEditController.handleAddTextButton();
            Node node = dataManager.getSelectedShape();
            
            // Cancelled or anything which we cannot get the text.
            if(node == null || (node instanceof DraggableText) && ((DraggableText)node).getText() == null){
                boldButton.setDisable(true);
                italicsButton.setDisable(true);
                fontFamily.setDisable(true);
                fontSize.setDisable(true);
            }
            else{
                fontFamily.setValue("sansserif");
                fontSize.setValue(12);
                dataManager.setState(golState.SELECTING_SHAPE);
                
                DraggableText text = ((DraggableText)node);
                text.setFontSize1(12);
                text.setFont1("sansserif");
                text.setFontt();       
            }
            // Activate the save button
            gui.updateToolbarControls(false);
	});
        
        boldButton.setOnAction(e->{
            clicks = 0;
	    Node node = dataManager.getSelectedShape();
            
            if(node != null && node instanceof DraggableText){
                DraggableText text = ((DraggableText)node);
                text.setBold1();
                
                TextFont_Transaction temp = new TextFont_Transaction(app, text, text.getFont1(),
                        text.getFont1(), text.getFontSize1(),text.getFontSize1() ,
                        !text.getBold1(), text.getBold1(),text.getItalics1(), text.getItalics1());
                jtps.addTransaction(temp);
            }
	});
        italicsButton.setOnAction(e->{
            clicks = 0;
	    Node node = dataManager.getSelectedShape();
            
            if(node != null && node instanceof DraggableText){
                DraggableText text = ((DraggableText)node);
                text.setItalics1();
                
                TextFont_Transaction temp = new TextFont_Transaction(app, text, text.getFont1(),
                        text.getFont1(), text.getFontSize1(),text.getFontSize1() ,
                        text.getBold1(), text.getBold1(),!text.getItalics1(), text.getItalics1());
                jtps.addTransaction(temp);
            }
	});
        fontFamily.setOnAction(e ->{
            clicks = 0;
            Node node = dataManager.getSelectedShape();

            // DraggableText the font family value is not the same as the previous one
            if(node != null && node instanceof DraggableText &&
                    ((DraggableText)node).getFont1() != null &&
                    !((DraggableText)node).getFont1().equals(fontFamily.getSelectionModel().getSelectedItem())
                    && !dataManager.getState().equals(golState.STARTING_TEXT)){
                DraggableText text = ((DraggableText)node);
                String pFont = text.getFont1();
                ((DraggableText)node).setFont1(fontFamily.getSelectionModel().getSelectedItem());

                TextFont_Transaction temp = new TextFont_Transaction(app, text, pFont,
                        text.getFont1(), text.getFontSize1(),text.getFontSize1() ,
                        text.getBold1(), text.getBold1(),text.getItalics1(), text.getItalics1());
                jtps.addTransaction(temp);
            }
        });
        fontSize.setOnAction(e ->{
            clicks = 0;
            Node node = dataManager.getSelectedShape();
            
            if(node != null && node instanceof DraggableText &&
                    ((DraggableText)node).getFont1() != null &&
                    // the previous text and current is not the same
                    !((DraggableText)node).getFontSize1().equals(fontSize.getSelectionModel().getSelectedItem())
                    && !dataManager.getState().equals(golState.STARTING_TEXT)){
                DraggableText text = ((DraggableText)node);
                Integer previousSize = text.getFontSize1();
                ((DraggableText)node).setFontSize1(fontSize.getSelectionModel().getSelectedItem());
                
                TextFont_Transaction temp = new TextFont_Transaction(app, text, text.getFont1(), text.getFont1(), 
                        previousSize, text.getFontSize1(), text.getBold1(), text.getBold1(), text.getItalics1(), text.getItalics1());
                jtps.addTransaction(temp);
            }
        });

	moveToBackButton.setOnAction(e->{
            clicks = 0;
	    logoEditController.processMoveSelectedShapeToBack();
	});
	moveToFrontButton.setOnAction(e->{
            clicks = 0;
	    logoEditController.processMoveSelectedShapeToFront();
	});

	backgroundColorPicker.setOnAction(e->{
            clicks = 0;
	    logoEditController.processSelectBackgroundColor();
	});
	fillColorPicker.setOnAction(e->{ 
            clicks = 0;
	    logoEditController.processSelectFillColor();
	});
	outlineColorPicker.setOnAction(e->{
            clicks = 0;
	    logoEditController.processSelectOutlineColor();
	});
	outlineThicknessSlider.addEventHandler(MouseEvent.MOUSE_RELEASED, e-> {
            clicks = 0;
            Node selectedShape = dataManager.getSelectedShape();
            // Only if the mouse is released
                if (selectedShape != null) {
                    logoEditController.processSelectOutlineThickness();
                    double previousStroke = ((Shape)selectedShape).getStrokeWidth();
                    double currentBorderWidth = dataManager.getCurrentBorderWidth();
                    StrokeSize_Transaction temp = new StrokeSize_Transaction(app, selectedShape, previousStroke, currentBorderWidth);
                    jtps.addTransaction(temp);
                }
	});
	snapshotButton.setOnAction(e->{
            clicks = 0;
	    logoEditController.processSnapshot();
	});
	
	// MAKE THE CANVAS CONTROLLER	
	canvasController = new CanvasController(app);
	canvas.setOnMousePressed(e->{
            Node topShape = (Node)dataManager.getTopShape((int)e.getX(), (int)e.getY());
            if(topShape != null && topShape instanceof DraggableText && topShape == dataManager.getSelectedShape()){
                clicks++;
            }
            else
                clicks = 0;
            
            // Set text
            Node shape = (Node)dataManager.selectTopShape((int)e.getX(), (int)e.getY());
            if(topShape != null && clicks == 0 && shape instanceof DraggableText ){
                clicks++;
            }
            
            // Change text
            if(shape instanceof DraggableText && clicks == 2){
                String editedString = ((DraggableText)shape).getTextFromUser();
                
                if(editedString != null && !editedString.equals(((DraggableText)shape).getText())){
                    TextChange_Transaction temp = new TextChange_Transaction(app,
                            (DraggableText)shape, ((DraggableText)shape).getText(), editedString);
                    jtps.addTransaction(temp);
                }
                clicks = 0;
            }
            // Is a draggable that is not a text
            else if(!(((Node)shape) instanceof DraggableText)){
                // Drag
                canvasController.processCanvasMousePress((int)e.getX(), (int)e.getY());
                clicks = 0;
            }
            // Is a Draggable text
            else if(((Node)shape) instanceof DraggableText){
                canvasController.processCanvasMousePress((int)e.getX(), (int)e.getY());
            }
            
            Node node = dataManager.getSelectedShape();
            if(node != null && node instanceof DraggableText){
                boldButton.setDisable(false);
                italicsButton.setDisable(false);   
                fontFamily.setDisable(false);
                fontSize.setDisable(false);
                fontFamily.setValue(((DraggableText)shape).getFont1());
                fontSize.setValue(((DraggableText)shape).getFontSize1());
            }
            else{
                boldButton.setDisable(true);
                italicsButton.setDisable(true);
                fontFamily.setDisable(true);
                fontSize.setDisable(true);
            }
            
            // A shape is selected
            if(node != null){
                dataManager.highlightShape(node);
                gui.getCopyButton().setDisable(false);
                gui.getCutButton().setDisable(false);
            }
            else{
                gui.getCopyButton().setDisable(true);
                gui.getCutButton().setDisable(true);
            }
	});
	canvas.setOnMouseReleased(e->{
	    canvasController.processCanvasMouseRelease((int)e.getX(), (int)e.getY());
            
	});
	canvas.setOnMouseDragged(e->{
	    canvasController.processCanvasMouseDragged((int)e.getX(), (int)e.getY());
            clicks = 0;
	});
        
        languageButton.setOnAction(e->{
            clicks = 0;
            String tempXML = APP_PROPERTIES_FILE_NAME;
	    logoEditController.handleLanguageRequest();
            
            // Only do these if the user changed the language. Too much to process if otherwise
            if(!tempXML.equalsIgnoreCase(APP_PROPERTIES_FILE_NAME)){
                // Reload the tooltips left side
                reloadLabels();
                // Reload the tooltips located at toolbar
                app.getGUI().reloadLabels();
                
                // Popup change those 
                app.loadProperties(APP_PROPERTIES_FILE_NAME);
            
                // Let user know the language has been changed
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                // Change the close button language
                dialog.reloadLabels();
                dialog.show(props.getProperty(LANGUAGE_CHANGED_TITLE),props.getProperty(LANGUAGE_CHANGED_MESSAGE));
            }
            else{
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.reloadLabels();
                dialog.show(props.getProperty(LANGUAGE_NOT_CHANGED_TITLE),props.getProperty(LANGUAGE_NOT_CHANGED_MESSAGE));
            }
        });
        aboutButton.setOnAction(e->{
            clicks = 0;
            String appIcon = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(APP_LOGO);
            Image image = new Image(appIcon);
            ImageView imageView = new ImageView(image);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setGraphic(imageView);
            alert.setTitle(isEnglish?"goLogoLo Logo Information": "goLogoLo Logo 信息");
            alert.setHeaderText(null);
            if(isEnglish){
                alert.setContentText("Welcome to GoLogoLoLogo! \n"
                        + "The creators of this program is \n "
                        + "Richard McKenna and Fanng Dai. \n "
                        + "Created in 2017.");
            }
            else{
                alert.setContentText("欢迎来到GoLogoLoLogo！ \n"
                        + "这个程序的创造者是 \n "
                        + "Richard McKenna 和 Fanng Dai. \n "
                        + "创建于 2017.");
            }

            alert.showAndWait();
	});
        cutButton.setOnAction(e->{
            clicks = 0;
	    logoEditController.handleCutRequest();
            gui.getCopyButton().setDisable(true);
            gui.getCutButton().setDisable(true);
	});
        copyButton.setOnAction(e->{
            clicks = 0;
	    logoEditController.handleCopyRequest();
	});
        pasteButton.setOnAction(e->{
            clicks = 0;
	    logoEditController.handlePasteRequest();
                    
            gui.getCopyButton().setDisable(false);
            gui.getCutButton().setDisable(false);
	});
        undoButton.setOnAction(e->{
            clicks = 0;
	    logoEditController.handleUndoTransaction();
            
            // To make sure that whatever the selected value was,
            // the buttons which were enabled should be disabled
            // If selected values are changed because of
            // Undo and redo
            dataManager.unhighlightShape();
            italicsButton.setDisable(true);
            boldButton.setDisable(true);
            copyButton.setDisable(true);
            cutButton.setDisable(true);
            removeButton.setDisable(true);
            moveToBackButton.setDisable(true);
            moveToFrontButton.setDisable(true);
            fontFamily.setDisable(true);
            fontSize.setDisable(true);
	});
        redoButton.setOnAction(e->{
            clicks = 0;
	    logoEditController.handleDoTransaction();
            
            dataManager.unhighlightShape();
            italicsButton.setDisable(true);
            boldButton.setDisable(true);
            copyButton.setDisable(true);
            cutButton.setDisable(true);
            removeButton.setDisable(true);
            moveToBackButton.setDisable(true);
            moveToFrontButton.setDisable(true);
            fontFamily.setDisable(true);
            fontSize.setDisable(true);
	});
        
    }
    
    
    public void reloadLabels(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        backgroundColorLabel.setText(isEnglish? "Background Color":"背景颜色");
        fillColorLabel.setText(isEnglish? "Fill Color":"填色");
        outlineColorLabel.setText(isEnglish? "Outline Color":"轮廓颜色");
        outlineThicknessLabel.setText(isEnglish? "Outline Thickness":"轮廓厚度");
        
        Tooltip button = new Tooltip(props.getProperty(SELECTION_TOOL_TOOLTIP.toString()));
        selectionToolButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(REMOVE_ELEMENT_TOOLTIP.toString()));
        removeButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(RECTANGLE_TOOLTIP.toString()));
        rectButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(ELLIPSE_TOOLTIP.toString()));
        ellipseButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(IMAGE_TOOLTIP.toString()));
        addImageButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(TEXT_TOOLTIP.toString()));
        addTextButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(BOLD_TOOLTIP.toString()));
        boldButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(ITALICS_TOOLTIP.toString()));
        italicsButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(MOVE_TO_BACK_TOOLTIP.toString()));
        moveToBackButton.setTooltip(button);

        button = new Tooltip(props.getProperty(MOVE_TO_FRONT_TOOLTIP.toString()));
        moveToFrontButton.setTooltip(button);
        
        button = new Tooltip(props.getProperty(SNAPSHOT_TOOLTIP.toString()));
        snapshotButton.setTooltip(button);
        
        fontFamily.setPromptText(isEnglish? "Font": "字体");
        fontSize.setPromptText(isEnglish? "Size": "大小");
      
        // FOR COLORPICKER IF NEEDED
//      backgroundColorPicker = new ColorPicker(Color.valueOf(WHITE_HEX));
//	fillColorPicker = new ColorPicker(Color.valueOf(WHITE_HEX));
//	outlineColorPicker = new ColorPicker(Color.valueOf(BLACK_HEX));
//      Locale.setDefault(isEnglish?Locale.US:Locale.CHINA);
//      ColorPicker a = new ColorPicker(backgroundColorPicker.getValue());
//      backgroundColorPicker = a;
    }

    // HELPER METHOD
    public void loadSelectedShapeSettings(Shape shape) {
	if (shape != null && shape.getFill().isOpaque()) {
	    Color fillColor = (Color)shape.getFill();
	    Color strokeColor = (Color)shape.getStroke();
	    double lineThickness = shape.getStrokeWidth();
	    fillColorPicker.setValue(fillColor);
	    outlineColorPicker.setValue(strokeColor);
	    outlineThicknessSlider.setValue(lineThickness);	    
	}
    }

    /**
     * This function specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed. Note that the
     * tag editor controls are added and removed dynamicaly as the application
     * runs so they will have their style setup separately.
     */
    public void initStyle() {
	// NOTE THAT EACH CLASS SHOULD CORRESPOND TO
	// A STYLE CLASS SPECIFIED IN THIS APPLICATION'S
	// CSS FILE
	canvas.getStyleClass().add(CLASS_RENDER_CANVAS);
	
	// COLOR PICKER STYLE
	fillColorPicker.getStyleClass().add(CLASS_BUTTON);
	outlineColorPicker.getStyleClass().add(CLASS_BUTTON);
	backgroundColorPicker.getStyleClass().add(CLASS_BUTTON);
	
	editToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR);
	row1Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	row2Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	row3Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	backgroundColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	
	row4Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	fillColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	row5Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	outlineColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	row6Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	outlineThicknessLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	row7Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
    }
    
    /**
     * This function reloads all the controls for editing logos
     * the workspace.
     */
    @Override
    public void reloadWorkspace(AppDataComponent data) {
	golData dataManager = (golData)data;
	if (dataManager.isInState(golState.STARTING_RECTANGLE)) {
            boldButton.setDisable(true);
            italicsButton.setDisable(true);
	    selectionToolButton.setDisable(false);
	    removeButton.setDisable(true);
	    rectButton.setDisable(true);
            addImageButton.setDisable(false);
	}
	else if (dataManager.isInState(golState.STARTING_ELLIPSE)) {
            boldButton.setDisable(true);
            italicsButton.setDisable(true);
	    selectionToolButton.setDisable(false);
	    removeButton.setDisable(true);
	    rectButton.setDisable(false);
            addImageButton.setDisable(false);
	    ellipseButton.setDisable(true);
	}
        else if (dataManager.isInState(golState.STARTING_TEXT)) {
            fontFamily.setDisable(false);
            fontSize.setDisable(false);
            boldButton.setDisable(false);
            italicsButton.setDisable(false);
	    selectionToolButton.setDisable(false);
	    removeButton.setDisable(true);
	    rectButton.setDisable(false);
            addImageButton.setDisable(false);
	    ellipseButton.setDisable(false);
	}
	else if (dataManager.isInState(golState.SELECTING_SHAPE) 
		|| dataManager.isInState(golState.DRAGGING_SHAPE)
		|| dataManager.isInState(golState.DRAGGING_NOTHING)) {
	    boolean shapeIsNotSelected = dataManager.getSelectedShape() == null;
	    selectionToolButton.setDisable(false);
	    removeButton.setDisable(shapeIsNotSelected);
	    rectButton.setDisable(false);
	    ellipseButton.setDisable(false);
            addImageButton.setDisable(false);
	    moveToFrontButton.setDisable(shapeIsNotSelected);
	    moveToBackButton.setDisable(shapeIsNotSelected);
	}
	
	removeButton.setDisable(dataManager.getSelectedShape() == null);
	backgroundColorPicker.setValue(dataManager.getBackgroundColor());
    }
    
    @Override
    public void resetWorkspace() {
        // WE ARE NOT USING THIS, THOUGH YOU MAY IF YOU LIKE
    }
}