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
public class TextFont_Transaction implements jTPS_Transaction{
    private AppTemplate app;
    private golWorkspace workspace;
    
    private String fontSelected;
    private Integer fontSize;
    private boolean isBold;
    private boolean isItalics;
    
    private String previousFontSelected;
    private Integer previousFontSize;
    private boolean previousIsBold;
    private boolean previousIsItalics;
    
    DraggableText text;
    
    public TextFont_Transaction(AppTemplate app, DraggableText text, String pFont, String fontSelected, Integer pFontSize, Integer fontSize
        , boolean pBold, boolean bold, boolean pItalics, boolean italics){
        this.app = app;
        this.workspace = (golWorkspace)app.getWorkspaceComponent();
        
        this.previousFontSelected = pFont;
        this.previousFontSize = pFontSize;
        this.previousIsBold = pBold;
        this.previousIsItalics = pItalics;
        
        this.fontSelected = fontSelected;
        this.fontSize = fontSize;
        this.isBold = bold;
        this.isItalics = italics;
        
        this.text = text;
    }

    @Override
    public void doTransaction(){
        FontPosture fp = isItalics?FontPosture.ITALIC:FontPosture.REGULAR;
        FontWeight fw = isBold?FontWeight.BOLD:FontWeight.NORMAL;
        text.setFont(Font.font(fontSelected, fw, fp, fontSize));
        
        // Set the text font
        text.setBold1(isBold);
        text.setItalics1(isItalics);
        text.setFont1(fontSelected);
        text.setFontSize1(fontSize);
    }
    
    @Override 
    public void undoTransaction(){
        FontPosture fp = previousIsItalics?FontPosture.ITALIC:FontPosture.REGULAR;
        FontWeight fw = previousIsBold?FontWeight.BOLD:FontWeight.NORMAL;
        text.setFont(Font.font(previousFontSelected, fw, fp, previousFontSize));
        
        // Set the text font
        text.setBold1(previousIsBold);
        text.setItalics1(previousIsItalics);
        text.setFont1(previousFontSelected);
        text.setFontSize1(previousFontSize);
    }
}
