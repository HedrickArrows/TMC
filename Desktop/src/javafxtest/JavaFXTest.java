/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxtest;

import java.awt.Dimension;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.imageio.ImageIO;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
//import org.openstreetmap.*;



/**
 *
 * @author Michal
 */
public class JavaFXTest extends Application {
    
    public List<String> labels;
    public List<List<String>> dataList;
    //public List<String> readingImage;
    public List<BufferedImage> imageList;
    
    public ObservableList<String> items;
    
    public MyBrowser map;
    
    @Override
    public void start(Stage primaryStage) throws IOException, ScriptException, NoSuchMethodException, URISyntaxException {
        
        Pane root = new Pane();
        
        map = new MyBrowser();
        
        map.setLayoutX(0);
        map.setLayoutY(250);
        map.setMaxSize(200,200);
        
        root.getChildren().add(map);
        
        Button btn = new Button();
        btn.setText("Importuj");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                try {
                    readFile();
                } catch (IOException ex) {
                    Logger.getLogger(JavaFXTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        
        btn.setLayoutX(10);
        btn.setLayoutY(10);
        
        root.getChildren().add(btn);
        
        Button btn2 = new Button();
        btn2.setText("Eksportuj");
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                try {
                    PrepForPrint();
                    //map.foo();
                    /*
                    try {
                    TestFoo();
                    } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(JavaFXTest.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                    Logger.getLogger(JavaFXTest.class.getName()).log(Level.SEVERE, null, ex);
                    }*/
                } catch (IOException ex) {
                    Logger.getLogger(JavaFXTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        btn2.setLayoutX(80);
        btn2.setLayoutY(10);
        
        //root.getChildren().add(btn2);
        
        Text t = new Text();
        t.setText("");
                
        t.setX(150);
        t.setY(55);
        //t.setTextAlignment(TextAlignment.RIGHT);
        root.getChildren().add(t);
        
        ImageView iv1 = new ImageView();
        iv1.setX(300);
        iv1.setY(0);
        root.getChildren().add(iv1);
        
        
        ListView<String> list = new ListView<String>();
        items = FXCollections.observableArrayList ( );
        list.setItems(items);
        list.setLayoutX(10);
        list.setLayoutY(45);
        
        list.setPrefWidth(100);
        list.setPrefHeight(200);
        
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String textForBox = "";
                int selID = list.getSelectionModel().getSelectedIndex();
                System.out.println("clicked on " + list.getSelectionModel().getSelectedItem());
                List<String> selList = dataList.get(selID);
                for(int i = 0; i< labels.size(); i++){
                    if(i==3 || i==0) continue;
                    textForBox += labels.get(i).replace("\"", "") +": " +
                            selList.get(i).replace("\"", "") + "\n";
                }
                t.setText(textForBox);
                iv1.setImage(SwingFXUtils.toFXImage(imageList.get(selID),null));
            }
        });
        
        root.getChildren().add(list);
        
        
        
        Scene scene = new Scene(root, 650, 750);
        
        primaryStage.setTitle("Drzewa");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
    
    public void readFile() throws UnsupportedEncodingException, IOException{
        
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);

        int i = 0;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            
            map.executeScript("remove_markers()");
            
            imageList = new ArrayList<BufferedImage>();
            dataList = new ArrayList<List<String>>();
            items.clear();
            
            File file = fc.getSelectedFile();
            String name = file.getAbsolutePath();
            
            try (Scanner scanner = new Scanner(new File(name));) {
                
                labels = Arrays.asList(scanner.nextLine().split(","));
                
                while (scanner.hasNextLine()) {
                    //tutaj będzie tragedia
                    String[] readingLine = scanner.nextLine().split(",");
                    List<String> line = new ArrayList(Arrays.asList(readingLine));
                    
                    int rlLength = readingLine.length;
                    
                    String lonS = readingLine[1].replace("\"", ""),
                            latS = readingLine[2].replace("\"", "");
                   
                    if(!lonS.isEmpty() && !latS.isEmpty()){
                        map.executeScript("add_marker("+lonS
                                +","+latS+")");
                    }
                    
                    if(rlLength == 4 && 
                            readingLine[rlLength - 1]
                            .charAt(readingLine[rlLength - 1].length() - 1) != '"'){
                        String tempStr = readingLine[rlLength - 1].substring(1);
                        
                         //System.out.print(tempStr + "\n");
                         
                        byte[] byteArr = Base64.getDecoder()
                                .decode(tempStr.getBytes("UTF-8"));
                        
                       
                        while(scanner.hasNextLine() && 
                                (tempStr = scanner.nextLine()).charAt(0) != '"' ){
                            byte[] tempByteArr = Base64.getDecoder()
                                .decode(tempStr.getBytes("UTF-8"));
                            //System.out.print(tempStr + "\n");
                            //byteList.add(tempByteArr);
                            
                            byte[] bigTemp = new byte[byteArr.length 
                                    + tempByteArr.length];
                            System.arraycopy(byteArr, 0, bigTemp, 
                                    0, byteArr.length);
                            System.arraycopy(tempByteArr, 0, bigTemp, 
                                    byteArr.length, tempByteArr.length);
                            
                            byteArr = bigTemp;
                            }
                        
                        System.out.print(tempStr + "\n");
                        
                        String[] remains = tempStr.split(",");
                        List<String> rems = new ArrayList(Arrays.asList(remains));
                        
                        rems.remove(0);
                        line.addAll(rems);
                        
                        
                        ByteArrayInputStream bis = new ByteArrayInputStream(byteArr);
                        BufferedImage bImage2 = ImageIO.read(bis);
                        
                        imageList.add(bImage2);
                        
                        
                        //System.out.print(" wyprodukowano obrazek");
                        //ImageIO.write(bImage2, "jpg", new File("output"+ ++i +".jpg") );
                    }
                    else{
                        imageList.add(null);
                    }
                    
                    line.set(3, "image");
                        
                        
                    dataList.add(line);
                        
                    items.add("Drzewo " + line.get(0).replace("\"", ""));
                    //records.add(getRecordFromLine(scanner.nextLine()));
                }
                
            }catch(FileNotFoundException e){
            }
            
            //This is where a real application would open the file.
            //log.append("Opening: " + file.getName() + "." + newline);
        } else {
            //log.append("Open command cancelled by user." + newline);
        }
    }
        
        class MyBrowser extends Region{
        
        HBox toolbar;

        WebView webView = new WebView();
        public WebEngine webEngine = webView.getEngine();
        
        public MyBrowser(){
            
            final URL urlGoogleMaps = getClass().getResource("openstreetmap.html");
            webEngine.load(urlGoogleMaps.toExternalForm());
            
            getChildren().add(webView);
        
        }
        
        void executeScript(String script){
            webEngine.executeScript(script);
        }
        
        void foo(){
            webEngine.executeScript("test()");
        }
    }
        
    void PrepForPrint() throws IOException{
        WritableImage image = map.webView.snapshot(null, null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        
    }    
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
