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
public class MoveBack_Transaction implements jTPS_Transaction{
    private AppTemplate app;
    private golWorkspace workspace;
    private golData dataManager;
    
    private Node selectedShape;
    ObservableList<Node> shapes;
    private ArrayList<Node> temp;
    private int position = 0;
    
    public MoveBack_Transaction(AppTemplate app, Node selectedShape, ObservableList<Node> shapes){
        this.app = app;
        this.workspace = (golWorkspace)app.getWorkspaceComponent();
        this.dataManager = (golData) app.getDataComponent();
        
        this.selectedShape = selectedShape;
        this.shapes = shapes;
        this.temp = new ArrayList<>();
    }
    
    @Override
    public void doTransaction(){
        temp.clear();
        position = 0;
        boolean flag = true;

        temp.add(selectedShape);
        for (Node node : shapes){
            if(flag && node != selectedShape){
                temp.add(node);
                position++;
            }
            // Skip adding selectedShape
            else if(node == selectedShape){
                flag = false;
            }
            else if(node != selectedShape){
                temp.add(node);
            }
        }
        shapes.clear();
        for (Node node : temp){
            shapes.add(node);
        }
    }
    
    @Override 
    public void undoTransaction(){           
        shapes.clear();
        temp.remove(selectedShape);
        
        for (Node node : temp){
            if(position > 0){
                position--;
                shapes.add(node);
            }
            else if(position == 0){
                position--;
                shapes.add(selectedShape);
                shapes.add(node);
            }
            else
                shapes.add(node);
        }
        if(position == 0){
            shapes.add(selectedShape);
        }
    }
}
