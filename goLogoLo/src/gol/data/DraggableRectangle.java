package gol.data;

import static gol.data.golData.copiedShape;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * This is a draggable rectangle for our goLogoLo application.
 * 
 * @author Richard McKenna
 * @author Fanng Dai
 * @version 1.0
 */
public class DraggableRectangle extends Rectangle implements Draggable {
    double startX;
    double startY;
    private static double initX;
    private static double initY;
    
    private String imagePath = "";
    
    public DraggableRectangle() {
            // Don't need this
//        ((Node)this).setLayoutX(0.0);
//        ((Node)this).setLayoutY(0.0);
//        ((Node)this).minHeight(0.0);
//        ((Node)this).maxHeight(0.0);
//        ((Node)this).minWidth(0.0);
//        ((Node)this).maxWidth(0.0);
        ((Node)this).setOpacity(1.0);
//        startX = 0.0;
//        startY = 0.0;
    }
    
    @Override
    public golState getStartingState() {
        return golState.STARTING_RECTANGLE;
    }
    
    @Override
    public void start(int x, int y) {
        initX = getX();
        initY = getY();
	startX = x;
	startY = y;
        setX(x);
        setY(y);
    }
    /**
     * Drags the rectangle.
     * getX() = the top corner of the rectangle
     * x = where the mouse is
     * @param x
     * @param y 
     */
    @Override
    public void drag(int x, int y) {
            double newX = initX + x - startX;
            double newY = initY + y - startY;
            startX = x;
            startY = y;
            initX = newX;
            initY = newY;
            xProperty().set(newX);
            yProperty().set(newY);
    }
    
    public String cT(double x, double y) {
	return "(x,y): (" + x + "," + y + ")";
    }
    
    @Override
    public void size(int x, int y) {
	double width = x - getX();
	widthProperty().set(width);
	double height = y - getY();
	heightProperty().set(height);	
    }
    
    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
	xProperty().set(initX);
	yProperty().set(initY);
	widthProperty().set(initWidth);
	heightProperty().set(initHeight);
    }
    
    @Override
    public void setLocation(double initX, double initY){
        xProperty().set(initX);
	yProperty().set(initY);
    }
    
    @Override
    public String getShapeType() {
        if(imagePath == ""){
            return RECTANGLE;
        }
        else
            return IMAGE;
    }
    
    // The path to the image
    public void setImagePath(String a){
        this.imagePath = a;
    }
    
    public String getImagePath(){
        return this.imagePath;
    }
    
    public DraggableRectangle clone(){
        DraggableRectangle newRectangle = new DraggableRectangle();
        
        newRectangle.setFill(getFill());
        newRectangle.setStroke(getStroke());
        newRectangle.setStrokeWidth(getStrokeWidth());
            
//        newRectangle.startX = startX;
//        newRectangle.startX = startX;
        
        newRectangle.setLocationAndSize(getX(), getY(), getWidth(), getHeight());
        
        return newRectangle;
    }
}
