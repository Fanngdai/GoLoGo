package gol.transaction;
import djf.AppTemplate;
import jtps.jTPS_Transaction;
import gol.gui.golWorkspace;
import javafx.scene.Node;

/**
 * @author fannydai
 */
public class AddShape_Transaction implements jTPS_Transaction{
    private AppTemplate app;
    private golWorkspace workspace;
    
    private Node shape;
    
    public AddShape_Transaction(AppTemplate app, Node shape){
        this.app = app;
        this.workspace = (golWorkspace)app.getWorkspaceComponent();
        
        this.shape = shape;
    }
    
    @Override
    public void doTransaction(){
        workspace.getCanvas().getChildren().add(shape);
    }
    
    @Override 
    public void undoTransaction(){
        workspace.getCanvas().getChildren().remove(shape);
        
        // Disable the buttons when you remove
        app.getGUI().getCopyButton().setDisable(true);
        app.getGUI().getCutButton().setDisable(true);
    }
}