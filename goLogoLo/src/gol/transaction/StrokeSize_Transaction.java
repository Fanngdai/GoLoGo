package gol.transaction;
import djf.AppTemplate;
import gol.gui.golWorkspace;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;

/**
 * @author fannydai
 */
public class StrokeSize_Transaction implements jTPS_Transaction{
    private AppTemplate app;
    private golWorkspace workspace;
    
    private Node shape;
    private double previousStroke;
    private double stroke;
    
    public StrokeSize_Transaction(AppTemplate app, Node shape, double previousStroke, double stroke){
        this.app = app;
        this.workspace = (golWorkspace)app.getWorkspaceComponent(); 
        
        this.shape = shape;
        this.previousStroke = previousStroke;
        this.stroke = stroke;
    }

    @Override
    public void doTransaction(){
        ((Shape)shape).setStrokeWidth(stroke);
        
//        currentFillColor = initColor;
//	if (selectedShape != null)
//	    ((Shape)selectedShape).setFill(currentFillColor);
        
//        workspace.getCanvas().getChildren().add(previousShape);
//        workspace.getCanvas().getChildren().remove(shape);
    }
    
    @Override 
    public void undoTransaction(){
         ((Shape)shape).setStrokeWidth(previousStroke);
//        workspace.getCanvas().getChildren().remove(shape);
//        workspace.getCanvas().getChildren().add(previousShape);
    }
}
