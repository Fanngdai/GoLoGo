package gol.transaction;
import djf.AppTemplate;
import gol.gui.golWorkspace;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;

/**
 * @author fannydai
 */
public class OutlineColor_Transaction implements jTPS_Transaction{
    private AppTemplate app;
    private golWorkspace workspace;
    
    private Node shape;
    private Color previousColor;
    private Color color;
    
    public OutlineColor_Transaction(AppTemplate app, Node shape, Color previousColor, Color color){
        this.app = app;
        this.workspace = (golWorkspace)app.getWorkspaceComponent();
        
        this.shape = shape;
        this.previousColor = previousColor;
        this.color = color;
    }

    @Override
    public void doTransaction(){
        ((Shape)shape).setStroke(color);
        
//        currentFillColor = initColor;
//	if (selectedShape != null)
//	    ((Shape)selectedShape).setFill(currentFillColor);
        
//        workspace.getCanvas().getChildren().add(previousShape);
//        workspace.getCanvas().getChildren().remove(shape);
    }
    
    @Override 
    public void undoTransaction(){
         ((Shape)shape).setStroke(previousColor);
//        workspace.getCanvas().getChildren().remove(shape);
//        workspace.getCanvas().getChildren().add(previousShape);
    }
}
