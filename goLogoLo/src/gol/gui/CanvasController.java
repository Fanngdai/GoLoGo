package gol.gui;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;
import gol.data.golData;
import gol.data.Draggable;
import gol.data.golState;
import static gol.data.golState.DRAGGING_NOTHING;
import static gol.data.golState.DRAGGING_SHAPE;
import static gol.data.golState.SELECTING_SHAPE;
import static gol.data.golState.SIZING_SHAPE;
import djf.AppTemplate;
import gol.data.DraggableText;
import gol.transaction.DragShape_Transaction;
import gol.transaction.BackgroundColor_Transaction;
import javafx.scene.Node;
import jtps.jTPS;
/**
 * This class responds to interactions with the rendering surface.
 *
 * @author Richard McKenna
 * @co-author Fanng Dai
 * @version 1.0
 */
public class CanvasController {

    AppTemplate app;
    golData dataManager;
    private static jTPS jtps;
    private double initX, initY;

    public CanvasController(AppTemplate initApp) {
        app = initApp;
        dataManager = (golData)app.getDataComponent();
        jtps = dataManager.getJTPS();
    }

    /**
     * Respond to mouse presses on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
    public void processCanvasMousePress(int x, int y) {
        golData dataManager = (golData) app.getDataComponent();
        
        if (dataManager.isInState(SELECTING_SHAPE)) {
            // SELECT THE TOP SHAPE
            Node shape = (Node)dataManager.selectTopShape(x, y);
            Scene scene = app.getGUI().getPrimaryScene();
                            
            // AND START DRAGGING IT
            if ((Node)shape != null) {
                scene.setCursor(Cursor.MOVE);
                dataManager.setState(golState.DRAGGING_SHAPE);
                Draggable selectedDraggableShape = (Draggable) dataManager.getSelectedShape();
                initX = selectedDraggableShape.getX();
                initY = selectedDraggableShape.getY();
                selectedDraggableShape.start(x, y);
                selectedDraggableShape.drag(x, y);
                app.getGUI().updateToolbarControls(false);
            } else {
                scene.setCursor(Cursor.DEFAULT);
                dataManager.setState(DRAGGING_NOTHING);
                app.getWorkspaceComponent().reloadWorkspace(dataManager);
            }
        } 
        else if (dataManager.isInState(golState.STARTING_RECTANGLE)) {
            dataManager.startNewRectangle(x, y);
        } 
        else if (dataManager.isInState(golState.STARTING_ELLIPSE)) {
            dataManager.startNewEllipse(x, y);
        }
        golWorkspace workspace = (golWorkspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
    }

    /**
     * Respond to mouse dragging on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
    public void processCanvasMouseDragged(int x, int y) {
        try{
            golData dataManager = (golData) app.getDataComponent();
            if (dataManager.isInState(SIZING_SHAPE)) {
                Draggable newDraggableShape = (Draggable) dataManager.getNewShape();
                newDraggableShape.size(x, y);
            } else if (dataManager.isInState(DRAGGING_SHAPE)) {
                Draggable selectedDraggableShape = (Draggable) dataManager.getSelectedShape();
                selectedDraggableShape.drag(x, y);
                app.getGUI().updateToolbarControls(false);
            }
        }
        catch(Exception ex){
            
        }
    }

    /**
     * Respond to mouse button release on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
    public void processCanvasMouseRelease(int x, int y) {
        golData dataManager = (golData) app.getDataComponent();
        if (dataManager.isInState(SIZING_SHAPE)) {
            dataManager.selectSizedShape();
            app.getGUI().updateToolbarControls(false);
        } else if (dataManager.isInState(golState.DRAGGING_SHAPE)) {
            dataManager.setState(SELECTING_SHAPE);
            Scene scene = app.getGUI().getPrimaryScene();
            scene.setCursor(Cursor.DEFAULT);
            app.getGUI().updateToolbarControls(false);
            
            Draggable selectedDraggableShape = (Draggable) dataManager.getSelectedShape();
            if(initX != selectedDraggableShape.getX() || initY != selectedDraggableShape.getY()){
                DragShape_Transaction temp = new DragShape_Transaction(app, dataManager.getSelectedShape(),
                    initX, initY, selectedDraggableShape.getX(), selectedDraggableShape.getY());
                jtps.addTransaction(temp);
            }
        } else if (dataManager.isInState(golState.DRAGGING_NOTHING)) {
            dataManager.setState(SELECTING_SHAPE);
        }
    }
}