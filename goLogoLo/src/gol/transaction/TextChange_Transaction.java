package gol.transaction;
import djf.AppTemplate;
import gol.data.DraggableText;
import gol.gui.golWorkspace;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import jtps.jTPS_Transaction;

/**
 * @author fannydai
 */
public class TextChange_Transaction implements jTPS_Transaction{
    private AppTemplate app;
    private golWorkspace workspace;
    
    DraggableText text;
    String prevString;
    String newString;
    
    public TextChange_Transaction(AppTemplate app, DraggableText text,String prevString, String newString){
        this.app = app;
        this.workspace = (golWorkspace)app.getWorkspaceComponent();
        
        this.text = text;
        this.prevString = prevString;
        this.newString = newString;
    }

    @Override
    public void doTransaction(){
        text.setText(newString);
//        // Set the text font
//        text.setBold1(isBold);
//        text.setItalics1(isItalics);
//        text.setFont1(fontSelected);
//        text.setFontSize1(fontSize);
    }
    
    @Override 
    public void undoTransaction(){
        text.setText(prevString);
//        // Set the text font
//        text.setBold1(previousIsBold);
//        text.setItalics1(previousIsItalics);
//        text.setFont1(previousFontSelected);
//        text.setFontSize1(previousFontSize);
    }
}
