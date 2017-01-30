package sample;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.eventusermodel.examples.XLS2CSVmra;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by work on 26/01/17.
 */
public class Main extends Application {
    private boolean inputStatus1;
    private boolean inputStatus2;
    private String[] _args;
    private String inputAddress;
    private String outputAddress;
    private TextArea statusText;
    private TextArea inputText;
    private TextArea outputText;
    private Button inputButton;

    private int fileCount;


    @Override
    public void start(Stage primaryStage) throws Exception{

        final DirectoryChooser inputChooser = new DirectoryChooser();
        final DirectoryChooser outputChooser = new DirectoryChooser();


        primaryStage.setTitle(".xls to .csv");
        BorderPane masterLayout = new BorderPane();
        HBox directoryLayout = new HBox();
        directoryLayout.setAlignment(Pos.CENTER);
        VBox inputLayout = new VBox();
        inputLayout.setAlignment(Pos.CENTER);
        inputLayout.setPadding(new Insets(40,50,20,50));
        VBox outputLayout = new VBox();
        outputLayout.setAlignment(Pos.CENTER);
        outputLayout.setPadding(new Insets(40,50,20,50));
        VBox textLayout = new VBox();
        textLayout.setAlignment(Pos.CENTER);

        //set the file name count to 0 on startup
        fileCount=0;
        Button goButton = new Button("Convert");
        goButton.setDisable(true);
        goButton.setOnAction(e->{
            File folder = new File(inputAddress);
            StringBuilder sb = new StringBuilder();

            for (File DMFile : folder.listFiles()) {
                sb.append("Converting "+DMFile.getName()+"\n");
                int minColumns = 75;//not sure what mincolumns does
                String fileName = DMFile.getName();
                fileName = fileName.substring(0,fileName.length()-4);
                System.out.println(fileName);

                try {
                    XLS2CSVmra xls2csv = new XLS2CSVmra(inputAddress + "/" + DMFile.getName(), minColumns, fileName, outputAddress);
                    xls2csv.process();
                } catch (IOException io){
                    io.printStackTrace();
                }
                sb.append("Conversion successful.\n");
            }
            sb.append("All files have been converted.");
            statusText.appendText(sb.toString());
            goButton.setDisable(true);
        });
        inputButton = new Button("Choose Input Folder");
        inputButton.setOnAction(e->{
            File file = inputChooser.showDialog(primaryStage);
            if (file != null) {
                inputAddress = file.getAbsolutePath();
                inputText.setText(inputAddress);
                inputStatus1=true;
            }
            if (inputStatus1 && inputStatus2){
                goButton.setDisable(false);
            }
        });


        Button outputButton = new Button("Choose Output Folder");
        outputButton.setOnAction(e->{
            File file = outputChooser.showDialog(primaryStage);
            if (file != null) {
                outputAddress = file.getAbsolutePath();
                outputText.setText(outputAddress);
                inputStatus2=true;
            }
            if (inputStatus1 && inputStatus2){
                goButton.setDisable(false);
            }
        });

        statusText = new TextArea();
        statusText.setPrefSize(400, 300);

        inputText = new TextArea();
        inputText.setDisable(true);
        inputText.setPrefSize(300,30);
        outputText = new TextArea();
        outputText.setDisable(true);
        outputText.setPrefSize(300,30);
        inputButton.setPrefSize(300, 30);
        outputButton.setPrefSize(300, 30);

        //set scrollpane
        ScrollPane textScroll = new ScrollPane();
        textScroll.setContent(statusText);
        textScroll.setFitToWidth(true);
        textScroll.setFitToHeight(true);
        textScroll.setPrefSize(400, 300);

        textLayout.getChildren().addAll(textScroll, goButton);
        outputLayout.getChildren().addAll(outputText, outputButton);
        inputLayout.getChildren().addAll(inputText, inputButton);
        directoryLayout.getChildren().addAll(inputLayout, outputLayout);
        masterLayout.setTop(directoryLayout);
        masterLayout.setCenter(textLayout);
        masterLayout.setStyle("-fx-background-color: #4d8ab0");


        primaryStage.setScene(new Scene(masterLayout, 600, 450));
        primaryStage.show();


    }

    public static void main(String[] args) throws Exception {

        launch(args);



    }
}
