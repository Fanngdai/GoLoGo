package gol.data;

import static djf.settings.AppPropertyType.APP_LOGO;
import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import static djf.settings.AppStartupConstants.isEnglish;
import static gol.data.golData.selectedShape;
import gol.gui.golWorkspace;
import java.awt.font.TextAttribute;
import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import properties_manager.PropertiesManager;

/**
 * This is a draggable rectangle for our goLogoLo application.
 * 
 * @author Richard McKenna
 * @author Fanng Dai
 * @version 1.0
 */
public class DraggableText extends Text implements Draggable {
    double startX;
    double startY;
    double initX;
    double initY;
    
    private String fontSelected;
    private Integer fontSize;
    private boolean isBold = false;
    private boolean isItalics = false;
    
    public DraggableText() {
        ((Node)this).setOpacity(1.0);
        // Set to empty String
//        setFont(Font.font("sansserif", FontWeight.NORMAL, FontPosture.REGULAR, 12));
        this.setText("");
    }
    
    @Override
    public golState getStartingState() {
        return golState.STARTING_TEXT;
    }
    
    @Override
    public void start(int x, int y) {
        initX = getX();
        initY = getY();
	startX = x;
	startY = y;
        setX(x);
        setY(y);
    }
    /**
     * Drags the rectangle.
     * getX() = the top corner of the rectangle
     * x = where the mouse is
     * @param x
     * @param y 
     */
    @Override
    public void drag(int x, int y) {
            double newX = initX + x - startX;
            double newY = initY + y - startY;
            startX = x;
            startY = y;
            initX = newX;
            initY = newY;
            xProperty().set(newX);
            yProperty().set(newY);
    }
    
    public String cT(double x, double y) {
	return "(x,y): (" + x + "," + y + ")";
    }
    
    @Override
    public void size(int x, int y) {
	double width = x - getX();
        setX(x+width/100000);
//	widthProperty().set(width);
	double height = y - getY();
        setY(y+height/100000);
//	heightProperty().set(height);	
    }

    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
	xProperty().set(initX);
	yProperty().set(initY);
        setX(initX+initWidth/100000);
        setY(initY+initHeight/100000);
//	widthProperty().set(initWidth);
//	heightProperty().set(initHeight);
    }
    
    @Override
    public void setLocation(double initX, double initY) {
	xProperty().set(initX);
	yProperty().set(initY);
    }
    
    @Override
    public String getShapeType() {
	return TEXT;
    }

    @Override
    public double getWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public String getTextFromUser(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        TextInputDialog dialog = new TextInputDialog(getText());
        
        // Set the logo
        String appIcon = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(APP_LOGO);
            Image image = new Image(appIcon);
            ImageView imageView = new ImageView(image);
        dialog.setGraphic(imageView);
        
        dialog.setTitle(isEnglish?"Text Input Dialog":"文本输入对话框");
        dialog.setHeaderText(isEnglish?"Please enter your text":"请输入您的文字");

        Optional<String> result = dialog.showAndWait();


        if(!result.isPresent()){
            return null;
        }
        if(getText() != result.get() && !getText().trim().equals("")){
            return result.get();
        }
        // Adding in a new text object
        else if (result.isPresent()){
            setText(result.get());
            return getText();
        }
        
        return null;
    }
    
    public void setFont1(String font){
        this.fontSelected = font;
    }
    public void setFontSize1(Integer integer){
        this.fontSize = integer;
    }
    public void setBold1(){
        if(isBold){
            isBold = false;
        }
        else
            isBold = true;
    }
    public void setBold1(boolean flag){
        isBold = flag;
    }
    public void setItalics1(){
        if(isItalics){
            isItalics = false;
        }
        else
            isItalics = true;
    }
    public void setItalics1(boolean flag){
        this.isItalics = flag;
    }
    
    public String getFont1(){
        return fontSelected;
    }
    public Integer getFontSize1(){
        return fontSize;
    }
    public boolean getBold1(){
        return isBold;
    }
    public boolean getItalics1(){
        return isItalics;
    }
    
    public void setFontt(){
        try{
            FontPosture fp = isItalics?FontPosture.ITALIC:FontPosture.REGULAR;
            FontWeight fw = isBold?FontWeight.BOLD:FontWeight.NORMAL;
            setFont(Font.font(fontSelected, fw, fp, fontSize));
        }
        catch(Exception ex){
            
        }
    }
    
    public DraggableText clone(){
        DraggableText newText = new DraggableText();
        newText.setText(this.getText());
        // Set the fonts
        newText.setBold1(isBold);
        newText.setItalics1(isItalics);
        newText.setFont1(fontSelected);
        newText.setFontSize1(fontSize);
        newText.setFontt();
        
        newText.setFill(getFill());
        newText.setStroke(getStroke());
        newText.setStrokeWidth(getStrokeWidth());
        
        // For where the paste shape is at
        newText.setLocation((int)xProperty().get(), (int)yProperty().get());
//        newText.start((int)getX(), (int)getY());
            
        return newText;
    }
}
