package gol.data;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import static gol.data.golState.SELECTING_SHAPE;
import static gol.data.golState.SIZING_SHAPE;
import gol.gui.golWorkspace;
import djf.components.AppDataComponent;
import djf.AppTemplate;
import gol.transaction.AddShape_Transaction;
import gol.transaction.BackgroundColor_Transaction;
import gol.transaction.FillColor_Transaction;
import gol.transaction.MoveBack_Transaction;
import gol.transaction.MoveForward_Transaction;
import gol.transaction.OutlineColor_Transaction;
import gol.transaction.StrokeSize_Transaction;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import jtps.jTPS;

/**
 * This class serves as the data management component for this application.
 *
 * @author Richard McKenna
 * @author Fanng Dai
 * @version 1.0
 */
public class golData implements AppDataComponent {
    // FIRST THE THINGS THAT HAVE TO BE SAVED TO FILES
    
    // THESE ARE THE SHAPES TO DRAW
    ObservableList<Node> shapes;
    
    // THE BACKGROUND COLOR
    Color backgroundColor;
    
    // AND NOW THE EDITING DATA

    // THIS IS THE SHAPE CURRENTLY BEING SIZED BUT NOT YET ADDED
    static Node newShape;

    // THIS IS THE SHAPE CURRENTLY SELECTED
    static Node selectedShape;
    
    static Node copiedShape;

    // FOR FILL AND OUTLINE
    Color currentFillColor;
    Color currentOutlineColor;
    double currentBorderWidth;

    // CURRENT STATE OF THE APP
    static golState state;

    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    
    // USE THIS WHEN THE SHAPE IS SELECTED
    Effect highlightedEffect;

    public static final String WHITE_HEX = "#FFFFFF";
    public static final String BLACK_HEX = "#000000";
    public static final String YELLOW_HEX = "#EEEE00";
    public static final Paint DEFAULT_BACKGROUND_COLOR = Paint.valueOf(WHITE_HEX);
    public static final Paint HIGHLIGHTED_COLOR = Paint.valueOf(YELLOW_HEX);
    public static final int HIGHLIGHTED_STROKE_THICKNESS = 3;

    static BufferedImage backgroundImage;
    private String imagePath;
    private boolean isimage = false;
    
    static jTPS jtps;
    ArrayList<Integer> transactionSize = new ArrayList<Integer>();
    
    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public golData(AppTemplate initApp) {
	// KEEP THE APP FOR LATER
	app = initApp;

	// NO SHAPE STARTS OUT AS SELECTED
	newShape = null;
	selectedShape = null;

	// INIT THE COLORS
	currentFillColor = Color.web(WHITE_HEX);
	currentOutlineColor = Color.web(BLACK_HEX);
	currentBorderWidth = 1;
	
	// THIS IS FOR THE SELECTED SHAPE
	DropShadow dropShadowEffect = new DropShadow();
	dropShadowEffect.setOffsetX(0.0f);
	dropShadowEffect.setOffsetY(0.0f);
	dropShadowEffect.setSpread(1.0);
	dropShadowEffect.setColor(Color.YELLOW);
	dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
	dropShadowEffect.setRadius(15);
	highlightedEffect = dropShadowEffect;
        
        jtps = new jTPS();
    }
    
    public jTPS getJTPS(){
        return this.jtps;
    }
    
    public ObservableList<Node> getShapes() {
	return shapes;
    }

    public Color getBackgroundColor() {
	return backgroundColor;
    }
    
    public Color getCurrentFillColor() {
	return currentFillColor;
    }

    public Color getCurrentOutlineColor() {
	return currentOutlineColor;
    }

    public double getCurrentBorderWidth() {
	return currentBorderWidth;
    }
    
    public void setShapes(ObservableList<Node> initShapes) {
	shapes = initShapes;
    }
    
    public BufferedImage getBackgroundImage(){
        return backgroundImage;
    }
    
    public String getImagePath(){
        return this.imagePath;
    }
    
    public boolean getIsImage(){
        return this.isimage;
    }
    
    public void setBackgroundColor(Color initBackgroundColor) {
        backgroundImage= null;
        golWorkspace workspace = (golWorkspace)app.getWorkspaceComponent();
	Pane canvas = workspace.getCanvas();
        if(backgroundColor == null){
            backgroundColor = initBackgroundColor;
            BackgroundFill fill = new BackgroundFill(backgroundColor, null, null);
            Background background = new Background(fill);
            canvas.setBackground(background);
            return;
        }
        Paint previousBackground = canvas.getBackground().getFills().get(0).getFill();
        backgroundColor = initBackgroundColor;
        
        if(backgroundColor.equals((Color)previousBackground))
            return;
        
        BackgroundColor_Transaction temp = new BackgroundColor_Transaction(app, 
                (Color)previousBackground, backgroundColor);
            jtps.addTransaction(temp);
    }

    public void setCurrentFillColor(Color initColor) {
//        try{
            backgroundImage= null;
            currentFillColor = initColor;
            if (selectedShape != null){
                if(!(selectedShape instanceof DraggableRectangle) ||
                    (selectedShape instanceof DraggableRectangle && 
                    ((DraggableRectangle)selectedShape).getImagePath().equals(""))){
                    
                    Color previousColor = (Color)((Shape)selectedShape).getFill();
                    // Don't go to next adding to stack if the colors are the same
                    if(currentFillColor.equals(previousColor))
                        return;
                    FillColor_Transaction temp = new FillColor_Transaction(app, selectedShape, previousColor, currentFillColor);
                    jtps.addTransaction(temp);
                }
            }
//        }
//        catch(ClassCastException ex){
//            // Program is trying to set the paint color to a color. That won't work.
//            // Don't need to do anything
//        }
    }

    public void setCurrentOutlineColor(Color initColor) {
        backgroundImage= null;
        currentOutlineColor = initColor;
	if (selectedShape != null) {
            Color previousColor = (Color)((Shape)selectedShape).getStroke();
            if(currentOutlineColor.equals(previousColor))
                return;
            OutlineColor_Transaction temp = new OutlineColor_Transaction(app, selectedShape, previousColor, currentOutlineColor);
            jtps.addTransaction(temp);
	}
    }

    public void setCurrentOutlineThickness(int initBorderWidth) {
        backgroundImage= null;
        currentBorderWidth = initBorderWidth;
    }
    
    public void removeSelectedShape() {
        backgroundImage = null;
	if (selectedShape != null) {
	    shapes.remove(selectedShape);
	    selectedShape = null;
	}
    }
    
    public void moveSelectedShapeToBack() {
	if (selectedShape != null && shapes.size() > 2) {
            MoveBack_Transaction temp = new MoveBack_Transaction(app, selectedShape, shapes);
            jtps.addTransaction(temp);
        }
    }
    
    public void moveSelectedShapeToFront() {
        backgroundImage = null;
	if (selectedShape != null && shapes.size() > 2) {
	    MoveForward_Transaction temp = new MoveForward_Transaction(app, selectedShape, shapes);
            jtps.addTransaction(temp);
	}
    }
 
    /**
     * This function clears out the HTML tree and reloads it with the minimal
     * tags, like html, head, and body such that the user can begin editing a
     * page.
     */
    @Override
    public void resetData() {
        app.getGUI().getRedoButton().setDisable(true);
        app.getGUI().getUndoButton().setDisable(true);
        jtps.resetJTPS();
        
        backgroundImage = null;
        state = SELECTING_SHAPE;
	newShape = null;
	selectedShape = null;

	// INIT THE COLORS
	currentFillColor = Color.web(WHITE_HEX);
	currentOutlineColor = Color.web(BLACK_HEX);
	
	shapes.clear();
	((golWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().clear();
    }

    public void selectSizedShape() {
	if (selectedShape != null)
	    unhighlightShape();
	selectedShape = newShape;
	highlightShape(((Shape)selectedShape));
	newShape = null;
	if (state == SIZING_SHAPE) {
	    state = ((Draggable)selectedShape).getStartingState();
            app.getGUI().getCopyButton().setDisable(false);
            app.getGUI().getCutButton().setDisable(false);
	}
    }
    
    public void unhighlightShape() {
        if(selectedShape != null){
            selectedShape.setEffect(null);
        }
    }
    
    public void highlightShape(Node shape) {
	shape.setEffect(highlightedEffect);
    }

    public void startNewRectangle(int x, int y) {
	DraggableRectangle newRectangle = new DraggableRectangle();
	newRectangle.start(x, y);
	newShape = newRectangle;
	initNewShape();
    }
    
    public boolean startNewTextbox(){
        DraggableText dragTextbox = new DraggableText();
        String userInput = dragTextbox.getTextFromUser();
        if(userInput == null || userInput.trim().equals("")){
            return false;
        }
        // DESELECT THE SELECTED SHAPE IF THERE IS ONE
        else if (selectedShape != null) {
	    unhighlightShape();
	}

//        shapes.add(dragTextbox);
	selectedShape = dragTextbox;
	highlightShape((DraggableText)selectedShape);
        ((DraggableText)selectedShape).start(50,50);
        
        AddShape_Transaction temp = new AddShape_Transaction(app, dragTextbox);
        jtps.addTransaction(temp);
        
        return true;
//        state = ((Draggable)selectedShape).getStartingState();
    }

    public void startNewEllipse(int x, int y) {
        backgroundImage = null;
	DraggableEllipse newEllipse = new DraggableEllipse();
	newEllipse.start(x, y);
	newShape = newEllipse;
	initNewShape();
    }

    public void setBackgroundImage(BufferedImage image, String imagePath){
        this.backgroundImage = image;
        this.imagePath = imagePath;
    }
    
//    public void setBackgroundText(String userInput){
//        this.backgroundImage = null;
//        this.text = new Text(userInput);
//    }
    
    public void initNewShape() {
	// DESELECT THE SELECTED SHAPE IF THERE IS ONE
	if (selectedShape != null) {
	    unhighlightShape();
	    selectedShape = null;
	}

	// USE THE CURRENT SETTINGS FOR THIS NEW SHAPE
	golWorkspace workspace = (golWorkspace)app.getWorkspaceComponent();
        
        // background to color? no color? Rectangle & withbackground color?
        if(isInState(golState.STARTING_RECTANGLE) && backgroundImage != null){
            Image image = SwingFXUtils.toFXImage(backgroundImage, null);
            ((DraggableRectangle)newShape).setFill(new ImagePattern(image));
            ((DraggableRectangle)newShape).setImagePath(this.imagePath);
//            System.out.println(imagePath);
            // This is a image
            isimage = true;
        }
        else{
            backgroundImage = null;
            ((Shape)newShape).setFill(workspace.getFillColorPicker().getValue());
        }

	// ADD THE SHAPE TO THE CANVAS
//	shapes.add(newShape);
        
        ((Shape)newShape).setStroke(workspace.getOutlineColorPicker().getValue());
        ((Shape)newShape).setStrokeWidth(workspace.getOutlineThicknessSlider().getValue());
        
        AddShape_Transaction temp = new AddShape_Transaction(app, newShape);
        jtps.addTransaction(temp);
        
        // GO INTO SHAPE SIZING MODE
        state = golState.SIZING_SHAPE;
    }

    public Node getNewShape() {
	return newShape;
    }

    public Node getSelectedShape() {
	return selectedShape;
    }

    public void setSelectedShape(Node initSelectedShape) {
	selectedShape = initSelectedShape;
    }

    public Node selectTopShape(int x, int y) {
	Node shape = getTopShape(x, y);
	if ((Node)shape == selectedShape)
	    return shape;
	
	if (selectedShape != null) {
	    unhighlightShape();
	}
	if (shape != null) {

	    highlightShape((Node)shape);
	    golWorkspace workspace = (golWorkspace)app.getWorkspaceComponent();
            if(shape instanceof Shape){
                workspace.loadSelectedShapeSettings((Shape)shape);
            }
	}
	selectedShape = shape;
	return shape;
    }

    public Node getTopShape(int x, int y) {
	for (int i = shapes.size() - 1; i >= 0; i--) {
	    Node shape = shapes.get(i);
	    if (shape.contains(x, y)) {
		return shape;
	    }
	}
	return null;
    }

    public void addShape(Node shapeToAdd) {
	shapes.add(shapeToAdd);
    }

    public void removeShape(Node shapeToRemove) {
	shapes.remove(shapeToRemove);
    }

    public golState getState() {
	return state;
    }

    public void setState(golState initState) {
	state = initState;
    }
    
    /**
     * Have to clone the original copy first.
     * @param shape 
     */
    public void setCopiedShape(Node shape){
        this.copiedShape = shape;
        this.copiedShape = cloneCopied();
    }
    
    public Node getCopiedShape(){
        imagePath = null;
        newShape = null;
        // if selected shape is highlighted, unhighlight it
        unhighlightShape();
        
        if(copiedShape instanceof DraggableRectangle){
            DraggableRectangle newRectangle = (DraggableRectangle)copiedShape;
            newRectangle.setLocationAndSize(newRectangle.getX()+35, newRectangle.getY()+35,
                    newRectangle.getWidth(), newRectangle.getHeight());
        }
        else if(copiedShape instanceof DraggableText){
            ((DraggableText)copiedShape).setLocation(((DraggableText)copiedShape).xProperty().get() + 15,
                    ((DraggableText)copiedShape).yProperty().get() + 15);
        }
        else if(copiedShape instanceof DraggableEllipse){
//            DraggableEllipse dragEllipse = (DraggableEllipse)copiedShape;
//           //  Does not work...
//            if(dragEllipse.getWidth() < 65 || dragEllipse.getHeight() < 65){
//                dragEllipse.setLocationAndSize(dragEllipse.getCenterX()+10, 
//                    dragEllipse.getCenterY()+10, dragEllipse.getWidth(),
//                    dragEllipse.getHeight());
//            }
//            else{
//                dragEllipse.setLocationAndSize(dragEllipse.getCenterX()-15,
//                    dragEllipse.getCenterY()-15, dragEllipse.getWidth(),
//                    dragEllipse.getHeight());
//            }
        }
        
        selectedShape = copiedShape;
//        shapes.add(selectedShape);
        highlightShape(selectedShape);
        
        AddShape_Transaction temp = new AddShape_Transaction(app, selectedShape);
        jtps.addTransaction(temp);
        
        copiedShape = cloneCopied();
        return selectedShape;
    }

    public boolean isInState(golState testState) {
	return state == testState;
//        return state.equals(testState);
    }
    
    public Node cloneCopied(){
        imagePath = null;
        newShape = null;
        
        // Text
        if(copiedShape instanceof DraggableText){            
            return ((DraggableText)copiedShape).clone();
        }
        // Ellipse
        else if(copiedShape instanceof DraggableEllipse){
            return ((DraggableEllipse)copiedShape).clone();
        }
        // Rectangle & image
        else{
            return ((DraggableRectangle)copiedShape).clone();
        }
    }
    
    public Node cloneSelected(){
        imagePath = null;
        newShape = null;
        
        // Text
        if(selectedShape instanceof DraggableText){            
            return ((DraggableText)selectedShape).clone();
        }
        // Ellipse
        else if(selectedShape instanceof DraggableEllipse){
            return ((DraggableEllipse)selectedShape).clone();
        }
        // Rectangle & image
        else{
            return ((DraggableRectangle)selectedShape).clone();
        }
    }
}
