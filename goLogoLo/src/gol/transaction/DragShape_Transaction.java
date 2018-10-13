package gol.transaction;
import djf.AppTemplate;
import gol.data.Draggable;
import gol.data.golData;
import gol.gui.golWorkspace;
import javafx.scene.Node;
import jtps.jTPS_Transaction;

/**
 * @author fannydai
 */
public class DragShape_Transaction implements jTPS_Transaction{
    private AppTemplate app;
    private golWorkspace workspace;
    private golData dataManager;
    
    private Node shape;
    private double initX, initY, endX, endY;
    
    
    public DragShape_Transaction(AppTemplate app, Node shape, double initX, double initY, double endX, double endY){
        this.app = app;
        this.workspace = (golWorkspace)app.getWorkspaceComponent();
        this.dataManager = (golData) app.getDataComponent();
        
        this.shape = shape;
        this.initX = initX;
        this.initY = initY;
        this.endX = endX;
        this.endY = endY;
    }
    
    @Override
    public void doTransaction(){
//        workspace.getCanvas().getChildren().remove(shape);
        ((Draggable)shape).setLocation(endX,endY);
//        workspace.getCanvas().getChildren().add(shape);
    }
    
    @Override 
    public void undoTransaction(){
//        workspace.getCanvas().getChildren().remove(shape);
        ((Draggable)shape).setLocation(initX,initY);
//        workspace.getCanvas().getChildren().add(shape);
    }
}
