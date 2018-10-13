package djf.settings;

import com.sun.webkit.WebPage;
import static djf.settings.AppPropertyType.APP_LOGO;
import static djf.settings.AppPropertyType.LOAD_ERROR_MESSAGE;
import static djf.settings.AppPropertyType.LOAD_ERROR_TITLE;
import djf.ui.AppMessageDialogSingleton;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import properties_manager.PropertiesManager;

/**
 * This class provides a set of constants useful for setting up the app.
 * 
 * @author Richard McKenna
 * @co-author Fanng Dai
 * @version 1.0
 */
public class AppStartupConstants
{    
    // Checks if the program is English
    public static boolean isEnglish;
    // Check to see if the workspace is activated. If it's not.
    // User click cancel, the whole program ends.
    public static boolean isActivated = false;
    
    // WE NEED THESE CONSTANTS JUST TO GET STARTED
    // LOADING SETTINGS FROM OUR XML FILES
    // XML PROPERTIES FILE WHERE ALL LANGUAGE-SPECIFIC TEXT CAN BE FOUND
    public static String APP_PROPERTIES_FILE_NAME;

    // XML SCHEMA FOR VALIDATING THE XML PROPERTIES FILE
    public static final String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";    

    // PROTOCOLS AND PATHS NEEDED FOR LOADING CERTAIN FILES
    public static final String FILE_PROTOCOL = "file:";
    public static final String PATH_DATA = "./data/";
    public static final String PATH_WORK = "./work/";
    public static final String PATH_IMAGES = "./images/";
    public static final String PATH_TEMP = "./temp/";
    public static final String PATH_EMPTY = ".";
    public static final String PATH_LANGUAGE = "./language_default/";
  
    // ERROR MESSAGE ASSOCIATED WITH PROPERTIES FILE LOADING ERRORS.
    // NOTE THAT THE REASON WE CAN'T LOAD THIS FROM THE XML FILE IS
    // THAT WE DISPLAY IT WHEN THE LOADING OF THAT FILE FAILS
    public static String PROPERTIES_FILE_ERROR_MESSAGE = isEnglish? "Error Loading ":"错误加载 " + APP_PROPERTIES_FILE_NAME;

    // ERROR DIALOG CONTROL
    public static String CLOSE_BUTTON_LABEL  = isEnglish? "Close ":"关 " + APP_PROPERTIES_FILE_NAME;
    
    /**
     * Set the language of the program. Asks user which language they want.
     * 
     * @return 
     * The string of the file which has the language of the users choice.
     */
    public static void setLanguage(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    
        String appIcon = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(APP_LOGO);
            Image image = new Image(appIcon);
            ImageView imageView = new ImageView(image);
            alert.setGraphic(imageView);
            
        alert.setTitle("Language of program");
        alert.setHeaderText(null);
        alert.setContentText("\nChoose a language.\n\nNote: Cancel will exit the program! (At start up)");

        ButtonType buttonEnglish = new ButtonType("English");
        ButtonType buttonChinese = new ButtonType("Chinese");
        ButtonType buttonCancel = new ButtonType("Cancel");
        
        alert.getButtonTypes().setAll(buttonEnglish, buttonChinese, buttonCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonEnglish){
            isEnglish = true;
            isActivated = true;
            APP_PROPERTIES_FILE_NAME = "app_properties_EN.xml";
//            return "app_properties_EN.xml";
        }
        else if (result.get() == buttonChinese) {
            isEnglish = false;
            isActivated = true;
            APP_PROPERTIES_FILE_NAME = "app_properties_CH.xml";
//            return "app_properties_CH.xml";
        }
        else if(result.get() == buttonCancel && isActivated == false){
            System.exit(0);
        }
        else
            alert.close();
//        return "app_properties_EN.xml";
    }
    
    public static void changeLanguage(String languagesss){
        if(languagesss.equalsIgnoreCase("app_properties_EN.xml"))
            isEnglish = true;
        else
            isEnglish = false;
        
        isActivated = true;
        
        APP_PROPERTIES_FILE_NAME = languagesss;
        
        PROPERTIES_FILE_ERROR_MESSAGE = isEnglish? "Error Loading ":"错误加载 " + APP_PROPERTIES_FILE_NAME;
        // ERROR DIALOG CONTROL
        CLOSE_BUTTON_LABEL  = isEnglish? "Close ":"关 " + APP_PROPERTIES_FILE_NAME;
    }
}