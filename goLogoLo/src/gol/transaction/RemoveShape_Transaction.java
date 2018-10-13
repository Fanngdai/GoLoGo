package gol.transaction;
import djf.AppTemplate;
import gol.gui.golWorkspace;
import javafx.scene.Node;
import jtps.jTPS_Transaction;

/**
 * @author fannydai
 */
public class RemoveShape_Transaction implements jTPS_Transaction{
    private AppTemplate app;
    private golWorkspace workspace;
    private Node shape;
    
    public RemoveShape_Transaction(AppTemplate app, Node shape){
        this.app = app;
        workspace = (golWorkspace)app.getWorkspaceComponent();
        
        this.shape = shape;
    }
    
    @Override
    public void doTransaction(){
        workspace.getCanvas().getChildren().remove(shape);
        
        // Disable the buttons when you remove
        app.getGUI().getCopyButton().setDisable(true);
        app.getGUI().getCutButton().setDisable(true);
    }
    
    @Override 
    public void undoTransaction(){
        workspace.getCanvas().getChildren().add(shape);
    }
}
