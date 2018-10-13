package gol.transaction;
import djf.AppTemplate;
import gol.data.golData;
import gol.gui.golWorkspace;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import jtps.jTPS_Transaction;

/**
 * @author fannydai
 */
public class MoveForward_Transaction implements jTPS_Transaction{
    private AppTemplate app;
    private golWorkspace workspace;
    private golData dataManager;
    
    private Node selectedShape;
    ObservableList<Node> shapes;
    private ArrayList<Node> temp = new ArrayList<>();
    private int position = 0;
    
    public MoveForward_Transaction(AppTemplate app, Node selectedShape, ObservableList<Node> shapes){
        this.app = app;
        this.workspace = (golWorkspace)app.getWorkspaceComponent();
        this.dataManager = (golData) app.getDataComponent();
        
        this.selectedShape = selectedShape;
        this.shapes = shapes;
    }
    
    @Override
    public void doTransaction(){
        // Just to make sure
        position = 0;
        temp.clear();
        
        // Look for the position selected shape is at
        for (Node node : shapes){
            if(node != selectedShape){
                position++;
            }
            else{
                break;
            }
        }
        // Remove and add to front
        shapes.remove(selectedShape);
        shapes.add(selectedShape);
    }
    
    @Override 
    public void undoTransaction(){   
        
        shapes.remove(selectedShape);
        for (Node node : shapes){
            if(position > 0){
                position--;
                temp.add(node);
            }
            else if(position == 0){
                position--;
                temp.add(selectedShape);
                temp.add(node);
            }
            else
                temp.add(node);
        }
        if(position == 0){
            temp.add(selectedShape);
        }
        
        shapes.clear();
        for (Node node : temp){
            shapes.add(node);
        }
    }
}
