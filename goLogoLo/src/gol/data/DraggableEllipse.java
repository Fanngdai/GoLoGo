package gol.data;

import static gol.data.golData.copiedShape;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

/**
 * This is a draggable ellipse for our goLogoLo application.
 * 
 * @author Richard McKenna
 * @author Fanng Dai
 * @version 1.0
 */
public class DraggableEllipse extends Ellipse implements Draggable {
    double startCenterX;
    double startCenterY;
    
    public DraggableEllipse() {
	setCenterX(0.0);
	setCenterY(0.0);
	setRadiusX(0.0);
	setRadiusY(0.0);
	setOpacity(1.0);
	startCenterX = 0.0;
	startCenterY = 0.0;
    }
    
    @Override
    public golState getStartingState() {
	return golState.STARTING_ELLIPSE;
    }
    
    @Override
    public void start(int x, int y) {
	startCenterX = x;
	startCenterY = y;
//        setCenterX(x);
//        setCenterY(y);
    }
    
    @Override
    public void drag(int x, int y) {
	double diffX = x - startCenterX;
	double diffY = y - startCenterY;
	double newX = getCenterX() + diffX;
	double newY = getCenterY() + diffY;
	setCenterX(newX);
	setCenterY(newY);
	startCenterX = x;
	startCenterY = y;
    }
    
    @Override
    public void size(int x, int y) {
	double width = x - startCenterX;
	double height = y - startCenterY;
	double centerX = startCenterX + (width / 2);
	double centerY = startCenterY + (height / 2);
	setCenterX(centerX);
	setCenterY(centerY);
	setRadiusX(width / 2);
	setRadiusY(height / 2);
    }
        
    @Override
    public double getX() {
	return getCenterX() - getRadiusX();
    }

    @Override
    public double getY() {
	return getCenterY() - getRadiusY();
    }

    @Override
    public double getWidth() {
	return getRadiusX() * 2;
    }

    @Override
    public double getHeight() {
	return getRadiusY() * 2;
    }
        
    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
	setCenterX(initX + (initWidth/2));
	setCenterY(initY + (initHeight/2));
	setRadiusX(initWidth/2);
	setRadiusY(initHeight/2);
    }
    
    @Override
    public void setLocation(double initX, double initY) {
	setCenterX(initX + (getWidth()/2));
	setCenterY(initY + (getHeight()/2));
    }
    
    @Override
    public String getShapeType() {
	return ELLIPSE;
    }
    
    public DraggableEllipse clone(){
        DraggableEllipse newEllipse = new DraggableEllipse();
//        newEllipse.startCenterX = startCenterX;
//        newEllipse.startCenterY = startCenterY;
        
        newEllipse.setFill(getFill());
        newEllipse.setStroke(getStroke());
        newEllipse.setStrokeWidth(getStrokeWidth());
        
        newEllipse.setLocationAndSize(getCenterX(), getCenterY(), getWidth(), getHeight());
        newEllipse.setLocation(getCenterX(), getCenterY());
            
        return newEllipse;
    }
}
