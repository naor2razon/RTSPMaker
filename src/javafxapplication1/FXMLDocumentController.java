/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.io.FileReader;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.json.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author naor
 */
public class FXMLDocumentController implements Initializable {
 
    
    //vars
    ObservableList<String> systemFunctunalltyOptinions = FXCollections.observableArrayList();
    JSONObject jsRootObject = new JSONObject();
    JSONParser jsParser = new JSONParser();
    JSONArray jsComponentsArray = new JSONArray();


    ObservableList<String> RtspFormatList = FXCollections.observableArrayList();
    ObservableList<String> CopmponentOptinions = FXCollections.observableArrayList();
    
    @FXML private ListView _csvListView;
    @FXML private Label optionsChoiceBoxLable;
    //@FXML private ChoiceBox optionsChoiceBox;
    @FXML private ComboBox optionsComboBox;
    @FXML private CheckBox PrimaryChannel,DnsName;
    @FXML private TextField streamingIp,RTSPStringLable,streamingPort,streamingPassword,streamingUserName,URLComponentFormat,componentName;
    @FXML private Button ipButton,portButton,passwordButton,usernameButton,_selectFileButton;//,pointsButton,slashButton,userInputButton,ResetComponentButton,saveComponentButton,createRtspButton ;

//***********************############### ADD RTSP TAB #############**************************************/

    
    //create the RSTP URL by use case of the components
    public void createRTSPUrlWhileButtonPushed(){
       String ip=streamingIp.getText();
       
        if(!DnsName.isSelected()){
        if (!validateIP(streamingIp.getText())) {
                streamingIp.setText("invalid ip address");
            }else{
               ip=streamingIp.getText();
        }
     }  
        String RTSPValue = 
                ("rtsp://"+streamingUserName.getText()+":"
                +streamingPassword.getText()+"@"+ip+":"+streamingPort.getText()
                +"/Streaming/Channels/");
        
        //AT THE END OF THE RTSP CREATION
        if(PrimaryChannel.isSelected()){
            //NEED TO FIX ACCORDING THE SPECIPIC RTSP
           RTSPValue+="101"; 
        }else{
           RTSPValue+="102";
        }
        
        this.RTSPStringLable.setText(RTSPValue);
        this.RTSPStringLable.selectAll();
        this.RTSPStringLable.requestFocus();
    }
    
    

     public void optionsComboBoxButtonPushed() throws JSONException{
        //optionsChoiceBoxLable.setText(optionsChoiceBox.getValue().toString());
        String selectedValue = (String) optionsComboBox.getSelectionModel().getSelectedItem();
        readJsonFile(selectedValue);
        System.out.println(selectedValue);
        
    }
    
    
    //VALIDATE THE IP ADRESS
    public boolean validateIP(final String ip) {
        Pattern pattern;
        Matcher matcher;
        String IPADDRESS_PATTERN
                = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        pattern = Pattern.compile(IPADDRESS_PATTERN);
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }
    
    
    //***********************############### ADD COMPONENT TAB #############**************************************/

   
    @FXML
    private void AddParameterToComponentURL() { 
   
        portButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
           // this.URL.setDisable(true);
            this.URLComponentFormat.setText(this.URLComponentFormat.getText()+"{PORT}");
            this.URLComponentFormat.requestFocus();
            this.URLComponentFormat.positionCaret(this.URLComponentFormat.getText().length());
        });
        passwordButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
           // this.URL.setDisable(true);
            this.URLComponentFormat.setText(this.URLComponentFormat.getText()+"{PASSWORD}");
            this.URLComponentFormat.requestFocus();
            this.URLComponentFormat.positionCaret(this.URLComponentFormat.getText().length());
            
        });
        usernameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
           // this.URL.setDisable(true);
            this.URLComponentFormat.setText(this.URLComponentFormat.getText()+"{USERNAME}");
            this.URLComponentFormat.requestFocus();
            this.URLComponentFormat.positionCaret(this.URLComponentFormat.getText().length());
            
        });
        ipButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            //this.URL.setDisable(true);
            this.URLComponentFormat.setText(this.URLComponentFormat.getText()+"{IP}");
            this.URLComponentFormat.requestFocus();
            this.URLComponentFormat.positionCaret(this.URLComponentFormat.getText().length());
        });
      /*  pointsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            this.URL.setDisable(true);
            this.URL.setText(this.URL.getText()+"{:}");
            this.URL.requestFocus();
            this.URL.positionCaret(this.URL.getText().length());
        });  
        slashButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            this.URL.setDisable(true);
            this.URL.setText(this.URL.getText()+"{/}");
            this.URL.requestFocus();
            this.URL.positionCaret(this.URL.getText().length());
        }); 
        
         //user input to store in the json   
         userInputButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> { 
             this.URL.setDisable(false);
            this.URL.setText(this.URL.getText()+"{PLEASE ENTER YOUR INPUT HERE!}");
            //try to select the (please enter..) only for user comfortable           
            this.URL.requestFocus();
            this.URL.selectRange(this.URL.getText().length()-30,this.URL.getText().length()-2);
            this.URL.selectNextWord();
            this.URL.setDisable(false);
            
        }); 
        */
        
    }
   public void readJsonFile(String componentName) throws JSONException{
       //String pageName = jsRootObject.getJSONObject("components").getString(componentName);
       //String pageName = (String) jsObject.get(componentName);
       //System.out.println(pageName);
         
       
   }

   
    //write the components to a json file after adding and saving 
      public void addComponentSaveButtonPushed() throws JSONException, IOException{
           String ComponentName = componentName.getText();
           String componentFormat = URLComponentFormat.getText();
           RtspFormatList.add(componentFormat);
           CopmponentOptinions.add(ComponentName);
           //jsObject.put(ComponentName,RtspFormatList.get(RtspFormatList.size()-1));
           
           JSONObject jsonInsideWriteJSFunc = new JSONObject();
           jsonInsideWriteJSFunc.append(ComponentName, RtspFormatList.get(RtspFormatList.size()-1));
           jsComponentsArray.put(jsonInsideWriteJSFunc);
           jsRootObject.put("components", jsComponentsArray);
           
           try(FileWriter file = new FileWriter("tr.json")){
               file.write(jsRootObject.toString());
               //file.append(jsObject.toString());
               file.flush();
              
           }catch(IOException e){
               e.printStackTrace();
           }
           System.out.println(jsRootObject);
           clearAddComponentTabFields();
       }
      
        public void clearAddComponentTabFields(){
        this.componentName.setText(null);
        this.URLComponentFormat.setText(null);
        
          }
    
    
    //***********************############### TABLE LIST TAB #############**************************************/

//load the xl/csv file
    public void selectFilePressed(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx"),
                new FileChooser.ExtensionFilter("XLS files (*.xls)", "*.xls"),
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
        
        if(selectedFile !=null){
           // _csvListView.getItems().add(selectedFile.getName());
      
        }else{
            System.out.println("file is not valid");
        }
                
    }
    
    public void JavaReadCSV() {
     
    String CsvFile = "/home/buddy/test/test.csv";
    String FieldDelimiter = ",";
 
        BufferedReader br;
         
        try {
            br = new BufferedReader(new FileReader(CsvFile));
             
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(FieldDelimiter, -1);
                System.out.print(fields.length + "-");
                for(String s : fields){
                    System.out.print(s);
                    System.out.print(":");
                }
                System.out.println();
            }
             
        } catch (FileNotFoundException ex) {
           // Logger.getLogger(JavaReadCSV.class.getName())
             //       .log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
       //     Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
 

    
    //***********************###############   Init   #############**************************************/

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        optionsComboBox.setTooltip(new Tooltip("Select Component"));
        optionsComboBox.setItems(CopmponentOptinions);
        //this.URL.setDisable(true);
        AddParameterToComponentURL();
    }    

}  

