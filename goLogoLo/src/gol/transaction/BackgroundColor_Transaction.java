package gol.transaction;
import djf.AppTemplate;
import gol.data.golData;
import gol.gui.golWorkspace;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import jtps.jTPS_Transaction;

/**
 * @author fannydai
 */
public class BackgroundColor_Transaction implements jTPS_Transaction{
    private AppTemplate app;
    private golWorkspace workspace;
    private golData dataManager;
    
    private Color backgroundColor;
    private Color previousBackgroundColor;
    
    public BackgroundColor_Transaction(AppTemplate app, Color previousBackgroundColor, Color backgroundColor){
        this.app = app;
        this.workspace = (golWorkspace)app.getWorkspaceComponent();
        this.dataManager = (golData)app.getDataComponent();
        
        this.backgroundColor = backgroundColor;
        this.previousBackgroundColor = previousBackgroundColor;
    }
    
    @Override
    public void doTransaction(){
        Pane canvas = workspace.getCanvas();
        BackgroundFill fill = new BackgroundFill(backgroundColor, null, null);
        Background background = new Background(fill);
	canvas.setBackground(background);
    }
    
    @Override 
    public void undoTransaction(){
        Pane canvas = workspace.getCanvas();
        BackgroundFill fill = new BackgroundFill(previousBackgroundColor, null, null);
        Background background = new Background(fill);
	canvas.setBackground(background);
    }
}
