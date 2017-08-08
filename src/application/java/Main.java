package application.java;

//File file = fileChooser.showOpenDialog(stage);
//if (file != null) {
//    try {
//        Desktop.getDesktop().open(file);
//    } catch (IOException ex) {
//        System.out.println(ex.getMessage());
//    }
//}

import javafx.scene.text.Font;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.font.PDFont;
//import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import javafx.scene.control.ScrollPane;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

import application.java.easytable.Row.RowBuilder;
import application.java.easytable.Table.TableBuilder;
import application.java.easytable.Cell;
import application.java.easytable.TableDrawer;

public class Main extends Application {

    String css = this.getClass().getResource("stylesheet.css").toExternalForm();
    String chartcss = this.getClass().getResource("application.css").toExternalForm();
    final Font segoeUIFont = Font.loadFont(
    	      this.getClass().getResourceAsStream("/resources/fonts/segoeui.ttf"), 
    	      20.0
    	    );

    private Stage stage = new Stage();

    private Image logo = new Image(this.getClass().getResourceAsStream("Trackmobile_wht_logo2.png"));
    private ImageView logoView = new ImageView(logo);

    private String password = "";
    
    Toggle diff = new Toggle(false);
    Toggle compSwitch = new Toggle(true);

//INSTANTIATE FLUID TABLE
    private TableView<Consumables> fluidTable = new TableView<>();

    private ObservableList<Consumables> consumablesData;
    ObservableList<Consumables> consumablesDataComp;

//INSTANTIATE PARTS TABLE
    private TableView<Part> partsTable = new TableView<>();

    private ObservableList<Part> partsData;
    ObservableList<Part> partsDataComp;

    private final Map<Double, Integer> swxLaborHours = new HashMap<Double, Integer>() {{
        //cycle 1
        put(500.0,6);
        put(1000.0,10);
        put(1500.0,6);
        put(2000.0,22);
        //cycle 2
        put(2500.0,6);
        put(3000.0,10);
        put(3500.0,6);
        put(4000.0,22);
        //cycle 3
        put(4500.0,6);
        put(5000.0,10);
        put(5500.0,6);
        put(6000.0,22);
    }};

    private final Map<Double, Integer> tmLaborHours = new HashMap<Double, Integer>() {{
        //break in
        put(100.0,6);
        //cycle 1
        put(500.0,4);
        put(1000.0,6);
        put(1500.0,7);
        put(2000.0,4);
        put(2500.0,6);
        put(3000.0,9);
        //cycle 2
        put(3500.0,4);
        put(4000.0,6);
        put(4500.0,7);
        put(5000.0,4);
        put(5500.0,6);
        put(6000.0,9);
    }};

//INSTANTIATE
    private TableView<PartResult> tmResultsTable = new TableView<>();
    private TableView<PartResult> swxResultsTable = new TableView<>();
    
    private String specialist = "";
    private String distributor = "";
    private String distStreet = "";
    private String distCity = "";
    private String distState = "";
    private String distZip = "";
    private String specPhone = "";
    private String customer = "";
    private String customerCompany = "";
    private String custStreet = "";
    private String custCity = "";
    private String custState = "";
    private String custZip = "";
    private String custPhone = "";
    private String date = "";
    private boolean rememberMe = false;
    

    Result tm;
    Result swx;

    private Label tmMaintCostNum;
    private Label swxMaintCostNum;
    private Label tmAdvantageNum;
    private Label tmcphlabel;
    private Label swxcphlabel;
    private Label cphadvlabel;
    private double tmcph;
    private double swxcph;
    private double cphadv;

    ComboBox<Integer> cbend = new ComboBox<Integer>();
    final ComboBox<Integer> cbstart = new ComboBox<Integer>();
    final Map combox2Map = new HashMap();

    private double laborCost = 125.00;
    private double tireHours = 6.0;
    private double tmTravelCost = 0;
    private double swxTravelCost = 0;
    private double timeperiodStart;
    private double timeperiodEnd;
    private double tmgph = 0.0;
    private double swxgph = 0.0;
    private double cpg = 0.0;

    private boolean step1Viewed = false;
    private boolean partsViewed = false;
    private boolean resultsViewed = false;
    private boolean cb2changing = false;
    private boolean selectViewed = false;

    final FileChooser fileChooser = new FileChooser();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Trackmobile® Maintenance Cost Estimator");
        stage.setWidth(820);
        stage.setHeight(650);
        stage.setResizable(false);
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("square_logo.png")));
        this.stage = stage;
        
        List<Integer> combox1List = new ArrayList<Integer>();
        combox1List.add(0);
        combox1List.add(100);
        for (int i = 500; i <= 6000; i+=500) {
            combox1List.add(i);
        }
        
        for (int i = 0; i < combox1List.size(); i++) {
            List<Integer> l = new ArrayList<Integer>();
            for (int j = 0; j < (int) combox1List.get(i); j+=500) {
                l.add(j);
            }
            combox2Map.put(combox1List.get(i), l);
        }
        ObservableList<Integer> combox1 = FXCollections.observableList(combox1List);
        
        cbend.setValue(3000);
        ObservableList<Integer> combox2 = FXCollections.observableArrayList((List) combox2Map.get(3000));
        cbstart.setValue(0);
        cbstart.setItems(combox2);
        cbend.setItems(combox1);
        timeperiodStart = ((Integer) cbstart.getValue()).intValue();
        timeperiodEnd = ((Integer) cbend.getValue()).intValue();

        logoView.setFitHeight(50);
        logoView.setPreserveRatio(true);
        
        File personalInfo = new File("personalInfo.csv");
        if (personalInfo.exists()) {
        	rememberMe = true;
        }
        String csvFile = "personalInfo.csv";
        BufferedReader br = null;
        String line = "";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                String[] prepBy = line.split(",");
                specialist = prepBy[0];
                distributor = prepBy[1];
                distStreet = prepBy[2];
                distCity = prepBy[3];
                distState = prepBy[4];
                distZip = prepBy[5];
                specPhone = prepBy[6];
            }

        } catch (FileNotFoundException e) {
            System.out.println("No personal information to load.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.now();
        date = dtf.format(localDate); //2016/11/16

        prepare();
    }

    public void loadData() {
    	FileChooser filechooser = new FileChooser();
    	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        filechooser.getExtensionFilters().add(extFilter);
        
    	File updated = filechooser.showOpenDialog(stage);
    	
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            try {
            	FileReader csv = new FileReader(updated);//Paths.get(".").toAbsolutePath().normalize().toString()+"\\fluidDataUpdated.csv"
            	br = new BufferedReader(csv);
            	consumablesData = FXCollections.observableArrayList();
                boolean first = true;
                while ((line = br.readLine()) != null) {
                    String[] fluid = line.split(cvsSplitBy);
                    this.consumablesData.add(new Consumables(fluid[0],Double.parseDouble(fluid[1]),fluid[2],Double.parseDouble(fluid[3]),
                            Double.parseDouble(fluid[4]),Double.parseDouble(fluid[5]),Double.parseDouble(fluid[6]),
                            Boolean.valueOf(fluid[7]), Boolean.valueOf(fluid[8]), Boolean.valueOf(fluid[9])));
                    if (first) {
                		diff.setSwitchedOn(Boolean.valueOf(fluid[10]));
                		compSwitch.setSwitchedOn(Boolean.valueOf(fluid[11]));
                		laborCost = Double.valueOf(fluid[12]);
                		tmTravelCost = Double.valueOf(fluid[13]);
                		swxTravelCost = Double.valueOf(fluid[14]);
                		tireHours = Double.valueOf(fluid[15]);
                		tmgph = Double.valueOf(fluid[16]);
                		swxgph = Double.valueOf(fluid[17]);
                		cpg = Double.valueOf(fluid[18]);
                		first = false;
                	}
                }
            } catch (Exception e) {
            	if (!step1Viewed) {
	            	Alert a = new Alert(Alert.AlertType.INFORMATION);
	            	a.setHeaderText("Default Values will be used. This can be changed on the next screen.");
	            	a.showAndWait();
	            	defaultData();
            	}
            }
            
        }  finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void loadPartsData() {
    	FileChooser filechooser = new FileChooser();
    	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        filechooser.getExtensionFilters().add(extFilter);
        
    	File updated = filechooser.showOpenDialog(stage);
    	
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            try {
            	FileReader csv = new FileReader(updated);
            	br = new BufferedReader(csv);
                partsData = FXCollections.observableArrayList();
                while ((line = br.readLine()) != null) {
                    String[] part = line.split(cvsSplitBy);
                    this.partsData.add(new Part(part[0],Double.parseDouble(part[1]), Double.parseDouble(part[2]), part[3],
                            Double.parseDouble(part[4]),Double.parseDouble(part[5]),Double.parseDouble(part[6]),
                            Double.parseDouble(part[7]), Boolean.valueOf(part[8]),Boolean.valueOf(part[9]),Boolean.valueOf(part[10])));
                   
                }
            } catch (Exception e) {
            	if (!partsViewed) {
	            	Alert a = new Alert(Alert.AlertType.INFORMATION);
	            	a.setHeaderText("Default Values will be used.");
	            	a.showAndWait();
	            	defaultPartsData();
            	}
            }
            
        }  finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void defaultData() {
        consumablesData = FXCollections.observableArrayList();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
        	br = new BufferedReader(new InputStreamReader(
                    this.getClass().getResourceAsStream("consumablesDataOrig.csv")));
        	boolean first = true;
            while ((line = br.readLine()) != null) {
                String[] fluid = line.split(cvsSplitBy);
                this.consumablesData.add(new Consumables(fluid[0],Double.parseDouble(fluid[1]),fluid[2],Double.parseDouble(fluid[3]),
                        Double.parseDouble(fluid[4]),Double.parseDouble(fluid[5]),Double.parseDouble(fluid[6]),
                        Boolean.valueOf(fluid[7]), Boolean.valueOf(fluid[8]), Boolean.valueOf(fluid[9])));
                if (first) {
            		diff.setSwitchedOn(Boolean.valueOf(fluid[10]));
            		compSwitch.setSwitchedOn(Boolean.valueOf(fluid[11]));
            		laborCost = Double.valueOf(fluid[12]);
            		tmTravelCost = Double.valueOf(fluid[13]);
            		swxTravelCost = Double.valueOf(fluid[14]);
            		tireHours = Double.valueOf(fluid[15]);
            		tmgph = Double.valueOf(fluid[16]);
            		swxgph = Double.valueOf(fluid[17]);
            		cpg = Double.valueOf(fluid[18]);
            		first = false;
            	}
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void defaultPartsData() {
        partsData = FXCollections.observableArrayList();
        String partsData = "partsDataOrig.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new InputStreamReader(
                    this.getClass().getResourceAsStream(partsData)));
            while ((line = br.readLine()) != null) {
                String[] part = line.split(cvsSplitBy);
                this.partsData.add(new Part(part[0],Double.parseDouble(part[1]), Double.parseDouble(part[2]),part[3],Double.parseDouble(part[4]),
                        Double.parseDouble(part[5]),Double.parseDouble(part[6]),Double.parseDouble(part[7]), Boolean.valueOf(part[8]),
                        Boolean.valueOf(part[9]),Boolean.valueOf(part[10])));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void prepare() {
    	Scene scene = new Scene(new Group(), Color.rgb(0,93,170));
        scene.getStylesheets().add(css);

        VBox everything = new VBox();
        everything.setPadding(new Insets(10, 10, 10, 10));
        
        Button cont = new Button();
        cont.setText("Next");
        
        final Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinSize(10, 1);
        
        final HBox contBar = new HBox();
        contBar.setPadding(new Insets(10,0,0,0));
        contBar.getChildren().addAll( spacer, cont);
        
        Label prepBySectionLabel = new Label("Prepared By:");
        prepBySectionLabel.setId("header");
        VBox prepBySectionVBox = new VBox();

        TextField specialistInput = new TextField();
        specialistInput.setPromptText("Specialist Name");
        specialistInput.setText(specialist);
        

        TextField distributorInput = new TextField();
        distributorInput.setPromptText("Distributor Name");
        distributorInput.setText(distributor);
        
        HBox specialistAndDistributorBox = new HBox();
        specialistAndDistributorBox.getChildren().addAll(specialistInput,distributorInput);
        specialistAndDistributorBox.setSpacing(5);
        specialistAndDistributorBox.setPrefWidth(800);
        
        TextField byStreetInput = new TextField();
        byStreetInput.setPromptText("Street Address");
        byStreetInput.setText(distStreet);
        
        HBox byCSZBox = new HBox();
        TextField byCityInput = new TextField();
        byCityInput.setPromptText("City");
        byCityInput.setText(distCity);
        
        TextField byStateInput = new TextField();
        byStateInput.setPromptText("State");
        byStateInput.setText(distState);
        
        TextField byZipInput = new TextField();
        byZipInput.setPromptText("Zip");
        byZipInput.setText(distZip);
        byCSZBox.getChildren().addAll(byCityInput,byStateInput,byZipInput);
        byCSZBox.setSpacing(5);

        TextField specPhoneInput = new TextField();
        specPhoneInput.setPromptText("Specialist Phone");
        specPhoneInput.setText(specPhone);
        
        CheckBox rememberMeBox = new CheckBox("Remember Me");
        rememberMeBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                        if (rememberMeBox.isSelected()) {
                        	rememberMe = true;
                        } else {
                        	rememberMe = false;
                        }
                }
            });
        rememberMeBox.setSelected(rememberMe);
        
        prepBySectionVBox.getChildren().addAll(prepBySectionLabel,specialistAndDistributorBox,byStreetInput,byCSZBox,specPhoneInput,rememberMeBox);
        prepBySectionVBox.setSpacing(5);
        prepBySectionVBox.setPadding(new Insets(25,0,0,0));
        
        Label prepForSectionLabel = new Label("Prepared For: ");
        prepForSectionLabel.setId("header");
        VBox prepForSectionVBox = new VBox();
        
        TextField customerInput = new TextField();
        customerInput.setPromptText("Customer Name");
        customerInput.setText(customer);
        
        TextField customerCompanyInput = new TextField();
        customerCompanyInput.setPromptText("Customer Company");
        customerCompanyInput.setText(customerCompany);
        
        HBox custNameAndCompBox = new HBox();
        custNameAndCompBox.getChildren().addAll(customerInput,customerCompanyInput);
        custNameAndCompBox.setSpacing(5);
        custNameAndCompBox.setPrefWidth(800);
        
        TextField forStreetInput = new TextField();
        forStreetInput.setPromptText("Street Address");
        forStreetInput.setText(custStreet);
        
        HBox forCSZBox = new HBox();
        TextField forCityInput = new TextField();
        forCityInput.setPromptText("City");
        forCityInput.setText(custCity);
        
        TextField forStateInput = new TextField();
        forStateInput.setPromptText("State");
        forStateInput.setText(custState);
        
        TextField forZipInput = new TextField();
        forZipInput.setPromptText("Zip");
        forZipInput.setText(custZip);
        forCSZBox.getChildren().addAll(forCityInput,forStateInput,forZipInput);
        forCSZBox.setSpacing(5);
        
        TextField custPhoneInput = new TextField();
        custPhoneInput.setPromptText("Customer Phone");
        custPhoneInput.setText(custPhone);
        
        prepForSectionVBox.getChildren().addAll(prepForSectionLabel, custNameAndCompBox,forStreetInput,forCSZBox,custPhoneInput);
        prepForSectionVBox.setSpacing(5);
        prepForSectionVBox.setPadding(new Insets(25,0,0,0));
        
        HBox dateBox = new HBox();
        Label dateLabel = new Label("Date:");
        dateLabel.setId("dataentry");
        TextField dateInput = new TextField();
        dateInput.setPromptText("mm/dd/yyyy");
        dateInput.setText(date);
        dateBox.getChildren().addAll(dateLabel,dateInput);
        dateBox.setSpacing(5);
        dateBox.setAlignment(Pos.CENTER_RIGHT);
        dateBox.setPrefWidth(800);
        dateBox.setPadding(new Insets(50,0,0,0));
        
        cont.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if (specialistInput.getText().length() == 0 ||
                        distributorInput.getText().length() == 0 ||
                        byStreetInput.getText().length() == 0 ||
                        byCityInput.getText().length() == 0 ||
                		byStateInput.getText().length() == 0 ||
        				byZipInput.getText().length() == 0 ||
						specPhoneInput.getText().length() == 0 ||
						customerInput.getText().length() == 0 ||
                        customerCompanyInput.getText().length() == 0 ||
                        forStreetInput.getText().length() == 0 ||
                        forCityInput.getText().length() == 0 ||
                		forStateInput.getText().length() == 0 ||
        				forZipInput.getText().length() == 0 ||
						custPhoneInput.getText().length() == 0 ||
                        dateInput.getText().length() == 0) {
                    if (specialistInput.getText().length() == 0)
                        specialistInput.setStyle("-fx-border-color: red");
                    if (distributorInput.getText().length() == 0)
                        distributorInput.setStyle("-fx-border-color: red");
                    if (byStreetInput.getText().length() == 0)
                        byStreetInput.setStyle("-fx-border-color: red");
                    if (byCityInput.getText().length() == 0)
                        byCityInput.setStyle("-fx-border-color: red");
                    if (byStateInput.getText().length() == 0)
                        byStateInput.setStyle("-fx-border-color: red");
                    if (byZipInput.getText().length() == 0)
                        byZipInput.setStyle("-fx-border-color: red");
                    if (specPhoneInput.getText().length() == 0)
                        specPhoneInput.setStyle("-fx-border-color: red");
                    if (customerInput.getText().length() == 0)
                        customerInput.setStyle("-fx-border-color: red");
                    if (customerCompanyInput.getText().length() == 0)
                        customerCompanyInput.setStyle("-fx-border-color: red");
                    if (forStreetInput.getText().length() == 0)
                    	forStreetInput.setStyle("-fx-border-color: red");
                    if (forCityInput.getText().length() == 0)
                    	forCityInput.setStyle("-fx-border-color: red");
                    if (forStateInput.getText().length() == 0)
                    	forStateInput.setStyle("-fx-border-color: red");
                    if (forZipInput.getText().length() == 0)
                    	forZipInput.setStyle("-fx-border-color: red");
                    if (custPhoneInput.getText().length() == 0)
                        custPhoneInput.setStyle("-fx-border-color: red");
                    if (dateInput.getText().length() == 0)
                        dateInput.setStyle("-fx-border-color: red");
                } else {
                    specialist = specialistInput.getText();
                    distributor = distributorInput.getText();
                    distStreet = byStreetInput.getText();
                    distCity = byCityInput.getText();
                    distState = byStateInput.getText();
                    distZip = byZipInput.getText();
                    specPhone = specPhoneInput.getText();
                    customer = customerInput.getText();
                    customerCompany = customerCompanyInput.getText();
                    custStreet = forStreetInput.getText();
                    custCity = forCityInput.getText();
                    custState = forStateInput.getText();
                    custZip = forZipInput.getText();
                    custPhone = custPhoneInput.getText();
                    date = dateInput.getText();
                    if (!selectViewed) {
                    	selectData();
                    } else {
                    	step1();
                    }
                    if (rememberMe) {
                    	savePersonalInfo();
                    } else {
                    	File personalInfo = new File("personalInfo.csv");
                    	if (personalInfo.exists())
                    		personalInfo.delete();
                    }
                }
            }
        });
        stage.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER && ! step1Viewed) {
               cont.fire();
               ev.consume(); 
            }
        });
        
        everything.getChildren().addAll(logoView,prepBySectionVBox, 
        		prepForSectionVBox, dateBox, contBar);
        everything.setPrefSize(800,580);
        everything.setSpacing(5);
        ((Group) scene.getRoot()).getChildren().addAll(everything);
        stage.setScene(scene);
        stage.show();
        prepForSectionLabel.requestFocus();
    }
    
    private void savePersonalInfo() {
    	try {
            String csvFile = Paths.get(".").toAbsolutePath().normalize().toString()+"\\personalInfo.csv";
    		FileWriter writer = new FileWriter(csvFile);
            CSVUtils.writeLine(writer, Arrays.asList(
                    specialist,distributor,distStreet,distCity,distState,distZip,specPhone
                ),',');
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void selectData() {
    	Scene scene = new Scene(new Group(), Color.rgb(0,93,170));
        scene.getStylesheets().add(css);

        VBox everything = new VBox();
        everything.setPadding(new Insets(10, 10, 10, 10));
        
        VBox labelBox = new VBox();
        labelBox.setAlignment(Pos.CENTER);
        Label label1 = new Label("What data would you like to use");
        label1.setId("header");
        label1.setPadding(new Insets(100,0,0,0));
        label1.setAlignment(Pos.CENTER);
        Label label2 = new Label("\nto initialize the application?");
        label2.setId("header");
        labelBox.getChildren().addAll(label1, label2);
        labelBox.setSpacing(-15);
        
        Button def = new Button("Default Data");
        def.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                defaultData();
                defaultPartsData();
                step1();
            }
        });
        def.setId("dspage");
        
        Button load = new Button("Load Data");
        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadData();
                defaultPartsData();
                step1();
            }
        });
        load.setId("dspage");
        
        HBox buttons = new HBox();
        buttons.getChildren().addAll(def,load);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPrefWidth(800);
        buttons.setSpacing(100);
        buttons.setPadding(new Insets(50,0,0,0));
        
        everything.getChildren().addAll(logoView, labelBox, buttons);
        ((Group) scene.getRoot()).getChildren().addAll(everything);
        stage.setScene(scene);
        stage.show();
        selectViewed = true;
    }

    private void step1() {
        Scene scene = new Scene(new Group(), Color.rgb(0,93,170));
        scene.getStylesheets().add(css);

        final HBox step1HBox = new HBox();
        step1HBox.setSpacing(30);

        VBox everything = new VBox();
        everything.setPadding(new Insets(10, 10, 10, 10));
        
        final Pane logobarspacer = new Pane();
        HBox.setHgrow(logobarspacer, Priority.ALWAYS);
        logobarspacer.setMinSize(10, 1);
        
        HBox logobar = new HBox();
        Button editPrep = new Button("Edit Preparation Data");
        editPrep.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                prepare();
            }
        });
        logobar.getChildren().addAll(logoView, logobarspacer, editPrep);

        final HBox contBar = new HBox();
        contBar.setPadding(new Insets(10,0,0,0));

        final VBox fluidTableVBox = new VBox();
        fluidTableVBox.setSpacing(5);
        fluidTableVBox.setPadding(new Insets(10, 0, 0, 10));
        
        final HBox fluidTableHBox = new HBox();
        fluidTableHBox.setSpacing(10);
        fluidTableHBox.setAlignment(Pos.TOP_LEFT);

        final Label step1Label = new Label("Update any prices, quatntities, or maintenance intervals.");
        step1Label.setId("header");

        final HBox difficultyHBox = new HBox();
        difficultyHBox.setSpacing(5);
        difficultyHBox.setAlignment(Pos.CENTER_RIGHT);

        final Label difficultyLabel = new Label("Severe Conditions?");
        difficultyLabel.setId("smallheader");

        if (!step1Viewed) {
	        diff.switchedOnProperty().addListener(new ChangeListener<Boolean>() {
	            public void changed(ObservableValue<? extends Boolean> ov,
	                                Boolean old_val, Boolean new_val) {
	                updateMaintenanceIntervals();
	                fluidTable.getColumns().get(0).setVisible(false);
	                fluidTable.getColumns().get(0).setVisible(true);
	            }
	        });
        }
        Tooltip.install(diff, new Tooltip("Selecting sever conditions will increase the frequency of maintenance"));
        
        difficultyHBox.getChildren().addAll(difficultyLabel, diff);
        
        final HBox compressorHBox = new HBox();
        compressorHBox.setSpacing(5);
        compressorHBox.setAlignment(Pos.CENTER_RIGHT);

        final Label compressorLabel = new Label("Include Rotary \nCompressors?");
        compressorLabel.setId("smallheader");

        compSwitch.switchedOnProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                toggleCompressorData();
            }
        });
        Tooltip.install(compSwitch, new Tooltip("This includes compressor parts and related consumables"));
        
        compressorHBox.getChildren().addAll(compressorLabel, compSwitch);

        HBox labor = new HBox();
        labor.setPadding(new Insets(0,0,20,0));
        labor.setSpacing(5);
        labor.setAlignment(Pos.CENTER);
        Label laborLabel = new Label("Labor Cost:");
        laborLabel.setId("dataentry");
        TextField laborInput = new TextField();
        laborInput.setTooltip(new Tooltip("The cost per one hour of labor"));
        laborInput.setPrefHeight(15);
        laborInput.setPrefWidth(50);
        laborInput.setText(String.format("%.2f", laborCost));
        Label laborUnitLabel = new Label("/Hour");
        laborUnitLabel.setId("dataentry");
        labor.getChildren().addAll(laborLabel, laborInput, laborUnitLabel);

        Label travelCost = new Label("Travel Costs:");
        travelCost.setId("dataentry");
        travelCost.setAlignment(Pos.CENTER_LEFT);
        Label tmTravelCostLabel = new Label("TM:");
        tmTravelCostLabel.setId("dataentry");
        Label swxTravelCostLabel = new Label("SWX:");
        swxTravelCostLabel.setId("dataentry");
        TextField tmTravelInput = new TextField();
        tmTravelInput.setPrefHeight(15);
        tmTravelInput.setPrefWidth(50);
        tmTravelInput.setText(String.format("%.2f", tmTravelCost));
        tmTravelInput.setTooltip(new Tooltip("Travel costs include: distance to service, cost per mile, flat rate charges, etc."));
        TextField swxTravelInput = new TextField();
        swxTravelInput.setPrefHeight(15);
        swxTravelInput.setPrefWidth(50);
        swxTravelInput.setText(String.format("%.2f", swxTravelCost));
        swxTravelInput.setTooltip(new Tooltip("Travel costs include: distance to service, cost per mile, flat rate charges, etc."));
        HBox tmTravelBox = new HBox();
        tmTravelBox.getChildren().addAll(tmTravelCostLabel,tmTravelInput);
        tmTravelBox.setAlignment(Pos.CENTER_RIGHT);
        tmTravelBox.setSpacing(5);
        tmTravelBox.setPadding(new Insets(0,45,0,0));
        HBox swxTravelBox = new HBox();
        swxTravelBox.getChildren().addAll(swxTravelCostLabel,swxTravelInput);
        swxTravelBox.setAlignment(Pos.CENTER_RIGHT);
        swxTravelBox.setSpacing(5);
        swxTravelBox.setPadding(new Insets(0,45,0,0));
        
        Label tireHoursLabel = new Label("Labor hours for tires:");
        tireHoursLabel.setId("dataentry");
        TextField tireHoursInput = new TextField();
        tireHoursInput.setTooltip(new Tooltip("The number of hours of labor added to a service due to changing tires"));
        tireHoursInput.setPrefHeight(15);
        tireHoursInput.setPrefWidth(50);
        tireHoursInput.setText(String.format("%.2f", tireHours));
        HBox tireHoursBox = new HBox();
        tireHoursBox.getChildren().addAll(tireHoursLabel, tireHoursInput);
        tireHoursBox.setSpacing(5);
        tireHoursBox.setPadding(new Insets(0,0,10,0));
        
        Label fuelConsumptionLabel = new Label("Fuel Consumption:");
        fuelConsumptionLabel.setId("dataentry");
        fuelConsumptionLabel.setAlignment(Pos.CENTER_LEFT);
        Label tmGPHLabel = new Label("TM:");
        tmGPHLabel.setId("dataentry");
        Label swxGPHLabel = new Label("SWX:");
        swxGPHLabel.setId("dataentry");
        TextField tmGPHInput = new TextField();
        tmGPHInput.setTooltip(new Tooltip("How many gallons of fuel a Trackmobile in your conditions will consume"));
        tmGPHInput.setPrefHeight(15);
        tmGPHInput.setPrefWidth(50);
        tmGPHInput.setText(String.format("%.2f", tmgph));
        Label tmgphUnitLabel = new Label("Gal/Hr");
        tmgphUnitLabel.setId("dataentry");
        TextField swxGPHInput = new TextField();
        swxGPHInput.setTooltip(new Tooltip("How many gallons of fuel a Shuttlewagon in your conditions will consume"));
        swxGPHInput.setPrefHeight(15);
        swxGPHInput.setPrefWidth(50);
        swxGPHInput.setText(String.format("%.2f", swxgph));
        Label swxgphUnitLabel = new Label("Gal/Hr");
        swxgphUnitLabel.setId("dataentry");
        HBox tmGPHBox = new HBox();
        tmGPHBox.getChildren().addAll(tmGPHLabel,tmGPHInput,tmgphUnitLabel);
        tmGPHBox.setAlignment(Pos.CENTER_RIGHT);
        tmGPHBox.setSpacing(5);
        HBox swxGPHBox = new HBox();
        swxGPHBox.getChildren().addAll(swxGPHLabel,swxGPHInput,swxgphUnitLabel);
        swxGPHBox.setAlignment(Pos.CENTER_RIGHT);
        swxGPHBox.setSpacing(5);
  
        HBox cpgBox = new HBox();
        cpgBox.setSpacing(5);
        cpgBox.setAlignment(Pos.CENTER_RIGHT);
        TextField cpgInput = new TextField();
        cpgInput.setTooltip(new Tooltip("Cost per one gallon of fuel"));
        cpgInput.setPrefHeight(15);
        cpgInput.setPrefWidth(50);
        cpgInput.setText(String.format("%.2f", cpg));
        Label cpgUnitLabel = new Label("Cost/Gallon: ");
        cpgUnitLabel.setId("dataentry");
        cpgBox.getChildren().addAll(cpgUnitLabel, cpgInput);
        

        Button cont = new Button();
        cont.setText("Results");
        cont.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (laborInput.getText().length() == 0 ||
                		tireHoursInput.getText().length() == 0 ||
                		tmGPHInput.getText().length() == 0 ||
                		swxGPHInput.getText().length() == 0 ||
                		cpgInput.getText().length() == 0 ||
                        tmTravelInput.getText().length() == 0 ||
                        swxTravelInput.getText().length() == 0) {
                    if (laborInput.getText().length() == 0)
                        laborInput.setStyle("-fx-border-color: red");
                    if (tireHoursInput.getText().length() == 0)
                        tireHoursInput.setStyle("-fx-border-color: red");
                    if (tmGPHInput.getText().length() == 0)
                        tmGPHInput.setStyle("-fx-border-color: red");
                    if (swxGPHInput.getText().length() == 0)
                        swxGPHInput.setStyle("-fx-border-color: red");
                    if (cpgInput.getText().length() == 0)
                        cpgInput.setStyle("-fx-border-color: red");
                    if (tmTravelInput.getText().length() == 0)
                        tmTravelInput.setStyle("-fx-border-color: red");
                    if (swxTravelInput.getText().length() == 0)
                        swxTravelInput.setStyle("-fx-border-color: red");
                } else {
                    laborCost = Double.parseDouble(laborInput.getText());
                    tireHours = Double.parseDouble(tireHoursInput.getText());
                    tmgph = Double.parseDouble(tmGPHInput.getText());
                    swxgph = Double.parseDouble(swxGPHInput.getText());
                    cpg = Double.parseDouble(cpgInput.getText());
                    tmTravelCost = Double.parseDouble(tmTravelInput.getText());
                    swxTravelCost = Double.parseDouble(swxTravelInput.getText());
                    results();
                }
            }
        });

        final Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinSize(10, 1);

        final Pane spacer2 = new Pane();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        spacer2.setMinSize(10, 1);

        VBox viewPartsVBox = new VBox();
        Label distrLabel = new Label("Distributors:");
        distrLabel.setId("subtext");
        Button viewParts = new Button("View Parts");
        viewParts.setId("viewparts");
        viewParts.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PasswordDialog admin = new PasswordDialog();
                Optional<String> result = admin.showAndWait();
                if (result.isPresent()) {
                    String entered = result.get();
                    if (isValid(entered)) {
                        viewPartsTable();
                    } else {
                        viewParts.fire();
                    }
                }
            }
        });

        Button infoButton = new Button();
        infoButton.setStyle("-fx-background-image: url('/application/java/information.png'); -fx-padding: 3 10 3 10;");
        infoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Disclaimers");
                a.setHeaderText("Disclaimers");
                a.setResizable(true);
                a.setContentText("1. Calculations done assuming the labor cost is the same for both companies." +
                        "\n2. All prices for other companies are estimated.\n" +
                        "\nCopyright 2017 Trackmobile, LLC\n" +
                        "\n" +
                        "Licensed under the Apache License, Version 2.0 (the \"License\"); " +
                        "you may not use this file except in compliance with the License. " +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software " +
                        "distributed under the License is distributed on an \"AS IS\" BASIS, " +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. " +
                        "See the License for the specific language governing permissions and " +
                        "limitations under the License.");
                a.showAndWait();
            }
        });

        Button revert = new Button("Revert Data");
        revert.setId("sidebar");
        revert.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            	alert.setTitle("Confirmation Dialog");
            	alert.setHeaderText("You are about to revert to the default data.");
            	alert.setContentText("Any unsaved changes will be lost. Would you like to continue?");
//            	alert.initStyle(StageStyle.UTILITY);
            	Optional<ButtonType> result = alert.showAndWait();
            	if (result.get() == ButtonType.OK){
            		defaultData();
                	step1();
            	}
            	
            }
        });
        revert.setTooltip(new Tooltip("Restores the data to Trackmobile's default MSRP values"));
        Button load = new Button("Load Data");
        load.setId("sidebar");
        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	loadData();
            	step1();
            }
        });
        load.setTooltip(new Tooltip("Select a save file to load \nNOTE: Only save files generated from this application will work"));
        Button save = new Button("Save Data");
        save.setId("sidebar");
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	saveData(laborInput.getText(),tmTravelInput.getText(),swxTravelInput.getText(),tireHoursInput.getText(),
            			tmGPHInput.getText(),swxGPHInput.getText(),cpgInput.getText());
            }
        });
        save.setTooltip(new Tooltip("Create a file on your computer with the data on this screen that can be loaded later"));
        
        VBox buttons = new VBox();
        buttons.getChildren().addAll(revert,load,save);
        buttons.setAlignment(Pos.BASELINE_CENTER);
        buttons.setSpacing(10);

        viewPartsVBox.getChildren().addAll(distrLabel,viewParts);

        contBar.getChildren().addAll(viewPartsVBox,spacer,infoButton,spacer2,cont);
        contBar.setAlignment(Pos.CENTER);

        //CREATE FLUID TABLE
        fluidTable.setPrefSize(630,276);

        fluidTable.setEditable(true);
        Callback<TableColumn, TableCell> cellFactoryPrice =
                new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn p) {
                        return new EditingCell("price");
                    }
                };
        Callback<TableColumn, TableCell> cellFactoryNumber =
                new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn p) {
                        return new EditingCell("number");
                    }
                };
        Callback<TableColumn, TableCell> cellFactoryDouble =
                new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn p) {
                        return new EditingCell("double");
                    }
                };

        TableColumn nameCol = new TableColumn("Consumables");
        nameCol.setMinWidth(50);
        nameCol.setPrefWidth(130);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Consumables, String>("name"));

        TableColumn priceCol = new TableColumn("Price");
        priceCol.setMinWidth(75);
        priceCol.setCellValueFactory(
                new PropertyValueFactory<Consumables, String>("price"));
        priceCol.setCellFactory(cellFactoryPrice);
        priceCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Consumables, Double>>() {
                    @Override
                    public void handle(CellEditEvent<Consumables, Double> t) {
                        ((Consumables) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setPrice(t.getNewValue());
                    }
                }
        );
        
        TableColumn qtys = new TableColumn("Qty");

        TableColumn swxQtyCol = new TableColumn("SWX");
        swxQtyCol.setMinWidth(50);
        swxQtyCol.setCellValueFactory(
                new PropertyValueFactory<Consumables, String>("swxqty"));
        swxQtyCol.setCellFactory(cellFactoryDouble);
        swxQtyCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Consumables, Double>>() {
                    @Override
                    public void handle(CellEditEvent<Consumables, Double> t) {
                        ((Consumables) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setSWXQty(t.getNewValue());
                    }
                }
        );

        TableColumn tmQtyCol = new TableColumn("TM");
        tmQtyCol.setMinWidth(50);
        tmQtyCol.setCellValueFactory(
                new PropertyValueFactory<Consumables, String>("tmqty"));
        tmQtyCol.setCellFactory(cellFactoryDouble);
        tmQtyCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Consumables, Double>>() {
                    @Override
                    public void handle(CellEditEvent<Consumables, Double> t) {
                        ((Consumables) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setTMQty(t.getNewValue());
                    }
                }
        );
        
        qtys.getColumns().addAll(tmQtyCol, swxQtyCol);

        TableColumn unitCol = new TableColumn("Unit");
        unitCol.setMinWidth(50);
        unitCol.setCellValueFactory(
                new PropertyValueFactory<Consumables, String>("unit"));

        TableColumn maintIntCol = new TableColumn("Maintenance \nInterval: Hrs");

        TableColumn tmMaintIntCol = new TableColumn("TM");
        tmMaintIntCol.setMinWidth(50);
        tmMaintIntCol.setCellValueFactory(
                new PropertyValueFactory<Consumables, String>("tmMaintInterval"));
        tmMaintIntCol.setCellFactory(cellFactoryNumber);
        tmMaintIntCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Consumables, Double>>() {
                    @Override
                    public void handle(CellEditEvent<Consumables, Double> t) {
                        ((Consumables) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setTMMaintInterval(t.getNewValue());
                    }
                }
        );

        TableColumn swxMaintIntCol = new TableColumn("SWX");
        swxMaintIntCol.setMinWidth(50);
        swxMaintIntCol.setCellValueFactory(
                new PropertyValueFactory<Consumables, String>("swxMaintInterval"));
        swxMaintIntCol.setCellFactory(cellFactoryNumber);
        swxMaintIntCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Consumables, Double>>() {
                    @Override
                    public void handle(CellEditEvent<Consumables, Double> t) {
                        ((Consumables) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setSwxMaintInterval(t.getNewValue());
                    }
                }
        );
        
        TableColumn submitColumn = new TableColumn();
        submitColumn.setId("submitCol");
        submitColumn.setMinWidth(50);
        Callback<TableColumn<Consumables, String>, TableCell<Consumables, String>> buttonFactory
		        = //
		        new Callback<TableColumn<Consumables, String>, TableCell<Consumables, String>>() {
		    @Override
		    public TableCell call(final TableColumn<Consumables, String> param) {
		        final TableCell<Consumables, String> cell = new TableCell<Consumables, String>() {
		
		            final Button btn = new Button();
		            
		            @Override
		            public void updateItem(String item, boolean empty) {
		                super.updateItem(item, empty);
		                if (empty) {
		                    setGraphic(null);
		                    setText(null);
		                } else {
		                	btn.setId("tableBtn");
		                	btn.setStyle("-fx-background-image: url('/application/java/check.png');");
		                	btn.setTooltip(new Tooltip("Accept changes to table values"));
		                    setGraphic(btn);
		                    setText(null);
		                }
		            }
		        };
		        return cell;
		    }
		};
        submitColumn.setCellFactory(buttonFactory);
        
        TableColumn breakInCols = new TableColumn("Break In");
        
        TableColumn tmBreakInCol = new TableColumn("TM");
        tmBreakInCol.setId("submitCol");
        tmBreakInCol.setMinWidth(50);
		tmBreakInCol.setCellFactory(col -> {
            CheckBoxTableCell<Consumables, Boolean> cell = new CheckBoxTableCell<>(index -> {
                BooleanProperty active = new SimpleBooleanProperty(fluidTable.getItems().get(index).gettmBreakIn());
                active.addListener((obs, wasActive, isNowActive) -> {
                    Consumables item = fluidTable.getItems().get(index);
                    item.settmBreakIn(isNowActive);
                });
                return active ;
            });
            cell.setTooltip(new Tooltip("Include in 100 Hr break in?"));
            return cell ;
        });
		
		TableColumn swxBreakInCol = new TableColumn("SWX");
		swxBreakInCol.setId("submitCol");
		swxBreakInCol.setMinWidth(50);
		swxBreakInCol.setCellFactory(col -> {
            CheckBoxTableCell<Consumables, Boolean> cell = new CheckBoxTableCell<>(index -> {
                BooleanProperty active = new SimpleBooleanProperty(fluidTable.getItems().get(index).getswxBreakIn());
                active.addListener((obs, wasActive, isNowActive) -> {
                    Consumables item = fluidTable.getItems().get(index);
                    item.setswxBreakIn(isNowActive);
                });
                return active ;
            });
            cell.setTooltip(new Tooltip("Include in 50 Hr break in?"));
            return cell;
        });
		
		breakInCols.getColumns().addAll(tmBreakInCol, swxBreakInCol);

        maintIntCol.getColumns().addAll(tmMaintIntCol,swxMaintIntCol);

        consumablesDataComp = FXCollections.observableArrayList();
        for (Consumables c : consumablesData){
        	if (!c.getIsCompressor() || (c.getIsCompressor() && compSwitch.switchedOnProperty().get())) {
        		consumablesDataComp.add(c);
        	}
        }
        partsDataComp = FXCollections.observableArrayList();
    	for (Part p : partsData){
        	if (!p.getIsCompressor() || (p.getIsCompressor() && compSwitch.switchedOnProperty().get())) {
        		partsDataComp.add(p);
        	}
        }
        fluidTable.setItems(consumablesDataComp);
        if (!step1Viewed) {
            fluidTable.getColumns().addAll(nameCol, priceCol, qtys, unitCol, maintIntCol, breakInCols, submitColumn);
        }
        
        VBox altCostsVBox = new VBox();
        altCostsVBox.getChildren().addAll(labor, travelCost,tmTravelBox,swxTravelBox);
        altCostsVBox.setPadding(new Insets(20,0,10,0));
        
        VBox altCostsVBox2 = new VBox();
        altCostsVBox2.getChildren().addAll(fuelConsumptionLabel, tmGPHBox, swxGPHBox,cpgBox);
        altCostsVBox2.setSpacing(5);
        altCostsVBox2.setPadding(new Insets(20,0,10,0));
        tireHoursBox.setPadding(new Insets(20,0,10,0));
        
        VBox switchesVBox = new VBox();
        switchesVBox.getChildren().addAll(difficultyHBox,compressorHBox);
        switchesVBox.setAlignment(Pos.CENTER_RIGHT);
        switchesVBox.setSpacing(10);
        
        step1HBox.getChildren().addAll(switchesVBox, altCostsVBox, altCostsVBox2, tireHoursBox);

        fluidTableHBox.getChildren().addAll(fluidTable, buttons);
        fluidTableVBox.getChildren().addAll(step1Label, fluidTableHBox, step1HBox);
        everything.getChildren().addAll(logobar, fluidTableVBox,contBar);

        ((Group) scene.getRoot()).getChildren().addAll(everything);
        stage.setScene(scene);
        stage.show();
        step1Viewed = true;
    }
    
    private void toggleCompressorData() {
    	consumablesDataComp = FXCollections.observableArrayList();
    	for (Consumables c : consumablesData){
        	if (!c.getIsCompressor() || (c.getIsCompressor() && compSwitch.switchedOnProperty().get())) {
        		consumablesDataComp.add(c);
        	}
        }
        fluidTable.setItems(consumablesDataComp);
        
        partsDataComp = FXCollections.observableArrayList();
    	for (Part p : partsData){
        	if (!p.getIsCompressor() || (p.getIsCompressor() && compSwitch.switchedOnProperty().get())) {
        		partsDataComp.add(p);
        	}
        }
        partsTable.setItems(partsDataComp);
    }

    private void viewPartsTable() {
        Scene scene = new Scene(new Group(), Color.rgb(0,93,170));
        scene.getStylesheets().add(css);

        VBox everything = new VBox();
        everything.setPadding(new Insets(10, 10, 10, 10));

        final Label header = new Label("Parts Table");
        header.setId("header");

        final HBox contBar = new HBox();
        Button back = new Button();
        final Pane spacer3 = new Pane();
        HBox.setHgrow(spacer3, Priority.ALWAYS);
        spacer3.setMinSize(10, 1);
        contBar.getChildren().addAll(back,spacer3);
        back.setText("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                step1();
            }
        });
        
        Button revert = new Button("Revert Data");
        revert.setId("sidebar");
        revert.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            	alert.setTitle("Confirmation Dialog");
            	alert.setHeaderText("You are about to revert to the default data.");
            	alert.setContentText("Any unsaved changes will be lost. Would you like to continue?");
//            	alert.initStyle(StageStyle.UTILITY);
            	Optional<ButtonType> result = alert.showAndWait();
            	if (result.get() == ButtonType.OK){
            		defaultPartsData();
                	viewPartsTable();
            	}
            	
            }
        });
        revert.setTooltip(new Tooltip("Restores the data to Trackmobile's default MSRP values"));
        Button load = new Button("Load Data");
        load.setId("sidebar");
        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	loadPartsData();
            	viewPartsTable();
            }
        });
        load.setTooltip(new Tooltip("Select a save file to load \nNOTE: Only save files generated from this application will work"));
        Button save = new Button("Save Data");
        save.setId("sidebar");
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	savePartsData();
            }
        });
        save.setTooltip(new Tooltip("Create a file on your computer with the data on this screen that can be loaded later"));
        
        VBox buttons = new VBox();
        buttons.getChildren().addAll(revert,load,save);
        buttons.setAlignment(Pos.BASELINE_CENTER);
        buttons.setSpacing(10);

        //CREATE PARTS TABLE
        partsTable.setPrefSize(630,276);

        partsTable.setEditable(true);

        Callback<TableColumn, TableCell> cellFactoryPrice =
                new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn p) {
                        return new EditingCell("price");
                    }
                };
        Callback<TableColumn, TableCell> cellFactoryNumber =
                new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn p) {
                        return new EditingCell("number");
                    }
                };

        TableColumn partsNameCol = new TableColumn("Parts");
        partsNameCol.setMinWidth(100);
        partsNameCol.setCellValueFactory(
                new PropertyValueFactory<Part, String>("name"));
        
        TableColumn prices = new TableColumn("Price");

        TableColumn swxpriceCol = new TableColumn("SWX");
        swxpriceCol.setMinWidth(75);
        swxpriceCol.setCellValueFactory(
                new PropertyValueFactory<Part, String>("swxprice"));
        swxpriceCol.setCellFactory(cellFactoryPrice);
        swxpriceCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Part, Double>>() {
                    @Override
                    public void handle(CellEditEvent<Part, Double> t) {
                        ((Part) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setSWXPrice(t.getNewValue());
                    }
                }
        );

        TableColumn tmpriceCol = new TableColumn("TM");
        tmpriceCol.setMinWidth(75);
        tmpriceCol.setCellValueFactory(
                new PropertyValueFactory<Part, String>("tmprice"));
        tmpriceCol.setCellFactory(cellFactoryPrice);
        tmpriceCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Part, Double>>() {
                    @Override
                    public void handle(CellEditEvent<Part, Double> t) {
                        ((Part) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setTMPrice(t.getNewValue());
                    }
                }
        );
        
        prices.getColumns().addAll(tmpriceCol, swxpriceCol);

        TableColumn idCol = new TableColumn("ID");
        idCol.setPrefWidth(100);
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Part, String>("id"));

        TableColumn maintIntCol = new TableColumn("Maintenance \nInterval: Hrs");

        TableColumn tmMaintIntCol = new TableColumn("TM");
        tmMaintIntCol.setMinWidth(50);
        tmMaintIntCol.setCellValueFactory(
                new PropertyValueFactory<Consumables, String>("tmMaintInterval"));
        tmMaintIntCol.setCellFactory(cellFactoryNumber);
        tmMaintIntCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Part, Double>>() {
                    @Override
                    public void handle(CellEditEvent<Part, Double> t) {
                        ((Part) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setTMMaintInterval(t.getNewValue());
                    }
                }
        );

        TableColumn swxMaintIntCol = new TableColumn("SWX");
        swxMaintIntCol.setMinWidth(50);
        swxMaintIntCol.setCellValueFactory(
                new PropertyValueFactory<Consumables, String>("swxMaintInterval"));
        swxMaintIntCol.setCellFactory(cellFactoryNumber);
        swxMaintIntCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Part, Double>>() {
                    @Override
                    public void handle(CellEditEvent<Part, Double> t) {
                        ((Part) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setSwxMaintInterval(t.getNewValue());
                    }
                }
        );

        maintIntCol.getColumns().addAll(tmMaintIntCol,swxMaintIntCol);
        
        TableColumn breakInCols = new TableColumn("Break In");
        
        TableColumn tmBreakInCol = new TableColumn("TM");
        tmBreakInCol.setId("submitCol");
        tmBreakInCol.setMinWidth(50);
		tmBreakInCol.setCellFactory(col -> {
            CheckBoxTableCell<Part, Boolean> cell = new CheckBoxTableCell<>(index -> {
                BooleanProperty active = new SimpleBooleanProperty(partsTable.getItems().get(index).gettmBreakIn());
                active.addListener((obs, wasActive, isNowActive) -> {
                    Part item = partsTable.getItems().get(index);
                    item.settmBreakIn(isNowActive);
                });
                return active ;
            });
            return cell ;
        });
		
		TableColumn swxBreakInCol = new TableColumn("SWX");
		swxBreakInCol.setId("submitCol");
		swxBreakInCol.setMinWidth(50);
		swxBreakInCol.setCellFactory(col -> {
            CheckBoxTableCell<Part, Boolean> cell = new CheckBoxTableCell<>(index -> {
                BooleanProperty active = new SimpleBooleanProperty(partsTable.getItems().get(index).getswxBreakIn());
                active.addListener((obs, wasActive, isNowActive) -> {
                    Part item = partsTable.getItems().get(index);
                    item.setswxBreakIn(isNowActive);
                });
                return active ;
            });
            return cell ;
        });
		
		breakInCols.getColumns().addAll(tmBreakInCol, swxBreakInCol);
		
		TableColumn submitColumn = new TableColumn();
        submitColumn.setId("submitCol");
        submitColumn.setMinWidth(50);
        Callback<TableColumn<Consumables, String>, TableCell<Consumables, String>> buttonFactory
		        = //
		        new Callback<TableColumn<Consumables, String>, TableCell<Consumables, String>>() {
		    @Override
		    public TableCell call(final TableColumn<Consumables, String> param) {
		        final TableCell<Consumables, String> cell = new TableCell<Consumables, String>() {
		
		            final Button btn = new Button();
		            
		            @Override
		            public void updateItem(String item, boolean empty) {
		                super.updateItem(item, empty);
		                if (empty) {
		                    setGraphic(null);
		                    setText(null);
		                } else {
		                	btn.setId("tableBtn");
		                	btn.setStyle("-fx-background-image: url('/application/java/check.png');");
		                	btn.setTooltip(new Tooltip("Accept changes to table values"));
		                    setGraphic(btn);
		                    setText(null);
		                }
		            }
		        };
		        return cell;
		    }
		};
        submitColumn.setCellFactory(buttonFactory);
        
        partsDataComp = FXCollections.observableArrayList();
        for (Part p : partsData){
        	if (!p.getIsCompressor() || (p.getIsCompressor() && compSwitch.switchedOnProperty().get())) {
        		partsDataComp.add(p);
        	}
        }
        partsTable.setItems(partsDataComp);
        if (!partsViewed) {
            partsTable.getColumns().addAll(partsNameCol, prices, idCol, maintIntCol, breakInCols, submitColumn);
        }

        final HBox partsAddBar = new HBox();
        final HBox partsAddBar2 = new HBox();

        double w = 108;
        final TextField addPartsName = new TextField();
        addPartsName.setPromptText("Part Name");
        addPartsName.setMaxWidth(w);
        final TextField addSWXPrice = new TextField();
        addSWXPrice.setMaxWidth(w);
        addSWXPrice.setPromptText("SWX Price");
        final TextField addTMPrice = new TextField();
        addTMPrice.setMaxWidth(w);
        addTMPrice.setPromptText("TM Price");
        final TextField addId = new TextField();
        addId.setMaxWidth(w);
        addId.setPromptText("ID");

        final TextField addSWXQty = new TextField();
        addSWXQty.setPromptText("SWX Quantity");
        addSWXQty.setMaxWidth(w);
        final TextField addTMQty = new TextField();
        addTMQty.setMaxWidth(w);
        addTMQty.setPromptText("TM Quantity");
        final TextField addSWXMaintInt = new TextField();
        addSWXMaintInt.setPrefWidth(160);
        addSWXMaintInt.setPromptText("SWX Maintenance Interval");
        final TextField addTMMaintInt = new TextField();
        addTMMaintInt.setPrefWidth(160);
        addTMMaintInt.setPromptText("TM Maintenance Interval");
        
        final CheckBox addComp = new CheckBox("Compressor?");
        final CheckBox addTMBreakIn = new CheckBox("TM Break In?");
        final CheckBox addSWXBreakIn = new CheckBox("SWX Break In?");

        final Button addPartsButton = new Button("Add");
        addPartsButton.setId("add");
        addPartsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	if (addPartsName.getText().length() == 0 ||
            			addSWXPrice.getText().length() == 0 ||
            			addTMPrice.getText().length() == 0 ||
            			addId.getText().length() == 0 ||
            			addSWXQty.getText().length() == 0 ||
            			addTMQty.getText().length() == 0 ||
            			addSWXMaintInt.getText().length() == 0 || 
            			addTMMaintInt.getText().length() == 0) {
            		if (addPartsName.getText().length() == 0) {
            			addPartsName.setStyle("-fx-border-color: red");
            		}
            		if (addSWXPrice.getText().length() == 0) {
            			addSWXPrice.setStyle("-fx-border-color: red");
            		}
            		if (addTMPrice.getText().length() == 0) {
            			addTMPrice.setStyle("-fx-border-color: red");
            		}
            		if (addId.getText().length() == 0) {
            			addId.setStyle("-fx-border-color: red");
            		}
            		if (addSWXQty.getText().length() == 0) {
            			addSWXQty.setStyle("-fx-border-color: red");
            		}
            		if (addTMQty.getText().length() == 0) {
            			addTMQty.setStyle("-fx-border-color: red");
            		}
            		if (addTMMaintInt.getText().length() == 0) {
            			addTMMaintInt.setStyle("-fx-border-color: red");
            		}
            		if (addSWXMaintInt.getText().length() == 0) {
            			addSWXMaintInt.setStyle("-fx-border-color: red");
            		}
            	} else {
            		try {
            			addPartsName.setStyle("-fx-border-color: null");
            			addSWXPrice.setStyle("-fx-border-color: null");
            			addTMPrice.setStyle("-fx-border-color: null");
            			addId.setStyle("-fx-border-color: null");
            			addSWXQty.setStyle("-fx-border-color: null");
            			addTMQty.setStyle("-fx-border-color: null");
            			addSWXMaintInt.setStyle("-fx-border-color: null");
            			addTMMaintInt.setStyle("-fx-border-color: null");
                        partsData.add(new Part(
                                addPartsName.getText(),
                                Double.parseDouble(addSWXPrice.getText()),
                                Double.parseDouble(addTMPrice.getText()),
                                addId.getText(),
                                Double.parseDouble(addSWXQty.getText()),
                                Double.parseDouble(addTMQty.getText()),
                                Double.parseDouble(addSWXMaintInt.getText()),
                                Double.parseDouble(addTMMaintInt.getText()),
                                addComp.isSelected(),
                                addTMBreakIn.isSelected(),
                                addSWXBreakIn.isSelected()
                                ));
                        addPartsName.clear();
                        addSWXPrice.clear();
                        addTMPrice.clear();
                        addId.clear();
                        addSWXQty.clear();
                        addTMQty.clear();
                        addSWXMaintInt.clear();
                        addTMMaintInt.clear();
                        addComp.setSelected(false);
                        addTMBreakIn.setSelected(false);
                        addSWXBreakIn.setSelected(false);
//                        printTotal();
                        viewPartsTable();
                    } catch (NumberFormatException nfe) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText("Make sure your numeric entries have only numbers!");
                        alert.setContentText("No symbols (ex. $) or letters. Decimal is fine.");

                        alert.showAndWait();
                    }
                }
            }
                
        });

        partsAddBar.getChildren().addAll(addPartsName, addSWXPrice, addTMPrice, addId, addSWXQty, addTMQty);
        partsAddBar.setSpacing(3);
        
        partsAddBar2.getChildren().addAll(addSWXMaintInt, addTMMaintInt, addComp, addTMBreakIn, addSWXBreakIn, addPartsButton);
        partsAddBar2.setSpacing(3);

        HBox partshbox = new HBox();
        partshbox.setSpacing(10);
        partshbox.getChildren().addAll(partsTable, buttons);
        
        final VBox partsvbox = new VBox();
        partsvbox.setSpacing(5);
        partsvbox.setPadding(new Insets(10, 0, 0, 10));
        partsvbox.getChildren().addAll(partshbox, partsAddBar, partsAddBar2);

        everything.getChildren().addAll(logoView,header,partsvbox,contBar);
        everything.setSpacing(5);
        ((Group) scene.getRoot()).getChildren().addAll(everything);
        stage.setScene(scene);
        stage.show();
        partsViewed = true;
    }

    private void results() {
        Scene scene = new Scene(new Group(), Color.rgb(0,93,170));
        scene.getStylesheets().add(css);

        VBox everything = new VBox();
        everything.setPadding(new Insets(10, 10, 10, 10));
        final Label label = new Label("Results");
        label.setId("header");

        final HBox contBar = new HBox();
        contBar.setPadding(new Insets(10, 0, 0, 0));

        Button cont = new Button();
        cont.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                detailedResults();
            }
        });
        cont.setText("View Chart");

        Button back = new Button();
        back.setText("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                step1();
            }
        });

        Button saveButton = new Button("Save PDF");
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                save();
            }
        });

        final Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinSize(10, 1);

        final Pane spacer2 = new Pane();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        spacer2.setMinSize(10, 1);

        contBar.getChildren().addAll(back,spacer,saveButton,spacer2,cont);


        HBox selectorRow = new HBox();
        selectorRow.setAlignment(Pos.BOTTOM_LEFT);
        
        Label selectorLabel = new Label("Time Period (Work Hrs): ");
        selectorLabel.setId("subtext");
        Label selectorDashLabel = new Label(" - ");
        selectorDashLabel.setId("header");
//combo box test                
        cbend.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
            	cb2changing = true;
            	timeperiodEnd = ((Integer) cbend.getValue()).doubleValue();
            	int temp = ((Integer) cbstart.getValue()).intValue();
                ObservableList combox2 = FXCollections.observableArrayList((List) combox2Map.get(t1));
                cbstart.setItems(combox2);//makes value null
                if (temp < timeperiodEnd) {
                	cbstart.setValue(temp);
                	timeperiodStart = temp;
                } else {
                	cbstart.setValue(0);
                	timeperiodStart = 0;
                }
                tm = calculateTMResults(((Integer)cbstart.getValue()).intValue(), ((Integer)cbend.getValue()).intValue());
                tmResultsTable.setItems(tm.getParts());
                tmResultsTable.refresh();
                swx = calculateSWXResults(((Integer)cbstart.getValue()).intValue(), ((Integer)cbend.getValue()).intValue());
                swxResultsTable.setItems(swx.getParts());
                swxResultsTable.refresh();
                tmMaintCostNum.setText("$" + NumberFormat.getNumberInstance().format(tm.getT4()));
                swxMaintCostNum.setText("$" + NumberFormat.getNumberInstance().format(swx.getT4()));
                tmAdvantageNum.setText("$" + NumberFormat.getNumberInstance().format(swx.getT4() - tm.getT4()));
                tmcph = tm.getT4()/(timeperiodEnd-timeperiodStart);
                swxcph = swx.getT4()/(timeperiodEnd-timeperiodStart);
                cphadv = swxcph - tmcph;
                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMinimumFractionDigits(2);
                nf.setMaximumFractionDigits(2);
                tmcphlabel.setText("$" + nf.format(tmcph) + "/Hr");
                swxcphlabel.setText("$" + nf.format(swxcph) + "/Hr");
                cphadvlabel.setText("$" + nf.format(cphadv) + "/Hr");
                cb2changing = false;
//                System.out.println("cbend changed and timeperiod is: " + timeperiodStart + " to " + timeperiodEnd);
            }
        });
        
        cbstart.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
            	if (!cb2changing) {
//	            	System.out.println("cb2 Selected: " + ((Integer)cbstart.getValue()));
	                timeperiodStart = ((Integer) cbstart.getValue()).doubleValue();
	                tm = calculateTMResults(((Integer)cbstart.getValue()).intValue(), ((Integer)cbend.getValue()).intValue());
	                tmResultsTable.setItems(tm.getParts());
	                tmResultsTable.refresh();
	                swx = calculateSWXResults(((Integer)cbstart.getValue()).intValue(), ((Integer)cbend.getValue()).intValue());
	                swxResultsTable.setItems(swx.getParts());
	                swxResultsTable.refresh();
	                tmMaintCostNum.setText("$" + NumberFormat.getNumberInstance().format(tm.getT4()));
	                swxMaintCostNum.setText("$" + NumberFormat.getNumberInstance().format(swx.getT4()));
	                tmAdvantageNum.setText("$" + NumberFormat.getNumberInstance().format(swx.getT4() - tm.getT4()));
	                tmcph = tm.getT4()/(timeperiodEnd-timeperiodStart);
	                swxcph = swx.getT4()/(timeperiodEnd-timeperiodStart);
	                cphadv = swxcph - tmcph;
	                NumberFormat nf = NumberFormat.getNumberInstance();
	                nf.setMinimumFractionDigits(2);
	                nf.setMaximumFractionDigits(2);
	                tmcphlabel.setText("$" + nf.format(tmcph) + "/Hr");
	                swxcphlabel.setText("$" + nf.format(swxcph) + "/Hr");
	                cphadvlabel.setText("$" + nf.format(cphadv) + "/Hr");
//	                System.out.println("cbstart changed and timeperiod is: " + timeperiodStart + " to " + timeperiodEnd);
            	}
            }
        });

//end combo box test
        selectorRow.getChildren().addAll(selectorLabel, cbstart, selectorDashLabel, cbend);

        tmResultsTable.setPrefSize(250,350);
        swxResultsTable.setPrefSize(250,350);

        //CALCULATE MAINTENANCE COST
//        System.out.println("Trackmobile");
        tm = calculateTMResults(timeperiodStart, timeperiodEnd);
//        tm.print();
        
//        System.out.println("\nShuttlewagon");
        swx = calculateSWXResults(timeperiodStart, timeperiodEnd);
//        swx.print();

        HBox resultsTables = new HBox();

        //TM COLS
        TableColumn tmCol = new TableColumn("Trackmobile");
        TableColumn tmNameCol = new TableColumn("Item");
        tmNameCol.setMinWidth(120);
        tmNameCol.setCellValueFactory(
                new PropertyValueFactory<Consumables, String>("name"));

        TableColumn t4Col = new TableColumn("Cumulative Cost");
        t4Col.setMinWidth(40);
        t4Col.setCellValueFactory(
                new PropertyValueFactory<Consumables, String>("t4"));

        //SWX COLS
        TableColumn swxCol = new TableColumn("Shuttlewagon");
        TableColumn swxNameCol = new TableColumn("Item");
        swxNameCol.setMinWidth(120);
        swxNameCol.setCellValueFactory(
                new PropertyValueFactory<Consumables, String>("name"));

        TableColumn t4sCol = new TableColumn("Cumulative Cost");
        t4sCol.setMinWidth(40);
        t4sCol.setCellValueFactory(
                new PropertyValueFactory<Consumables, String>("t4"));

        tmResultsTable.setItems(tm.getParts());
        swxResultsTable.setItems(swx.getParts());
        if (!resultsViewed) {
            tmCol.getColumns().addAll(tmNameCol, t4Col);
            tmResultsTable.getColumns().addAll(tmCol);
            swxCol.getColumns().addAll(swxNameCol, t4sCol);
            swxResultsTable.getColumns().addAll(swxCol);
        }

        VBox labels = new VBox();
        labels.setAlignment(Pos.CENTER);
        VBox tmresultbox = new VBox();
        tmresultbox.setAlignment(Pos.CENTER);
        Label tmMaintCostLabel = new Label("Trackmobile");
        tmMaintCostLabel.setId("tmheader");
        Label tmMaintCostLabel2 = new Label("  Maintenance Cost  ");
        tmMaintCostLabel2.setId("smallheader");
        tmMaintCostLabel2.setUnderline(true);
        tmMaintCostNum = new Label("$" + NumberFormat.getNumberInstance().format(tm.getT4()));
        tmMaintCostNum.setId("smallheader");
        tmresultbox.getChildren().addAll(tmMaintCostLabel, tmMaintCostLabel2, tmMaintCostNum);
        VBox swxresultbox = new VBox();
        swxresultbox.setAlignment(Pos.CENTER);
        Label swxMaintCostLabel = new Label("\nShuttlewagon");
        swxMaintCostLabel.setId("swxheader");
        Label swxMaintCostLabel2 = new Label("  Maintenance Cost  ");
        swxMaintCostLabel2.setId("smallheader");
        swxMaintCostLabel2.setUnderline(true);
        swxMaintCostNum = new Label("$" + NumberFormat.getNumberInstance().format(swx.getT4()));
        swxMaintCostNum.setId("smallheader");
        swxresultbox.getChildren().addAll(swxMaintCostLabel, swxMaintCostLabel2, swxMaintCostNum);
        VBox advantagebox = new VBox();
        advantagebox.setAlignment(Pos.CENTER);
        Label tmAdvantage = new Label("\nTrackmobile");
        tmAdvantage.setId("smallheader");
        Label tmAdvantage2 = new Label("        Advantage        ");
        tmAdvantage2.setId("smallheader");
        tmAdvantage2.setUnderline(true);
        tmAdvantageNum = new Label("$" + NumberFormat.getNumberInstance().format(swx.getT4() - tm.getT4()));
        tmAdvantageNum.setId("header");
        advantagebox.getChildren().addAll(tmAdvantage, tmAdvantage2, tmAdvantageNum);
        VBox tmcphbox = new VBox();
        tmcphbox.setAlignment(Pos.BOTTOM_CENTER);
        VBox swxcphbox = new VBox();
        swxcphbox.setAlignment(Pos.BOTTOM_CENTER);
        VBox advcphbox = new VBox();
        advcphbox.setAlignment(Pos.BOTTOM_CENTER);
        Label tmcphheader = new Label("Cost per Hour");
        tmcphheader.setId("smallheader");
        tmcphheader.setUnderline(true);
        Label swxcphheader = new Label("Cost per Hour");
        swxcphheader.setId("smallheader");
        swxcphheader.setUnderline(true);
        Label advcphheader = new Label("Cost per Hour");
        advcphheader.setId("smallheader");
        advcphheader.setUnderline(true);
        tmcph = tm.getT4()/(timeperiodEnd-timeperiodStart);
        swxcph = swx.getT4()/(timeperiodEnd-timeperiodStart);
        cphadv = swxcph - tmcph;
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        tmcphlabel = new Label("$" + nf.format(tmcph) + "/Hr");
        tmcphlabel.setId("smallheader");
        swxcphlabel = new Label("$" + nf.format(swxcph) + "/Hr");
        swxcphlabel.setId("smallheader");
        cphadvlabel = new Label("$" + nf.format(cphadv) + "/Hr");
        cphadvlabel.setId("smallheader");
        tmcphbox.getChildren().addAll(tmcphheader,tmcphlabel);
        swxcphbox.getChildren().addAll(swxcphheader, swxcphlabel);
        advcphbox.getChildren().addAll(advcphheader, cphadvlabel);
        final Pane sp1 = new Pane();
        HBox.setHgrow(sp1, Priority.ALWAYS);
        sp1.setMinSize(10, 1);
        final Pane sp2 = new Pane();
        HBox.setHgrow(sp2, Priority.ALWAYS);
        sp2.setMinSize(10, 1);
        final Pane sp3 = new Pane();
        HBox.setHgrow(sp3, Priority.ALWAYS);
        sp3.setMinSize(10, 1);
        HBox tmbox = new HBox(tmresultbox, sp1, tmcphbox);
        tmbox.setAlignment(Pos.BOTTOM_LEFT);
        HBox swxbox = new HBox(swxresultbox, sp2, swxcphbox);
        swxbox.setAlignment(Pos.BOTTOM_LEFT);
        HBox advbox = new HBox(advantagebox, sp3, advcphbox);
        advbox.setAlignment(Pos.BOTTOM_LEFT);
        labels.getChildren().addAll(tmbox, swxbox, advbox);     
        labels.setPadding(new Insets(0,10,0,30));
        resultsTables.getChildren().addAll(tmResultsTable,swxResultsTable,labels);

        HBox viewSchedules = new HBox();
        Button viewTmSchedule = new Button("Trackmobile Maintenance Schedule");
        viewTmSchedule.setId("maint");
        viewTmSchedule.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               viewTmSchedule();
            }
        });

        Button viewSwxSchedule = new Button("Shuttlewagon Maintenance Schedule");
        viewSwxSchedule.setId("maint");
        viewSwxSchedule.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                viewSwxSchedule();
            }
        });
        viewSchedules.getChildren().addAll(viewTmSchedule,viewSwxSchedule);

        everything.getChildren().addAll(logoView,label,selectorRow, resultsTables, viewSchedules, contBar);
        ((Group) scene.getRoot()).getChildren().addAll(everything);
        stage.setScene(scene);
        stage.show();
        resultsViewed = true;
    }

    private Result calculateTMResults(double timePeriodStart, double timePeriodEnd) {
        Result result = new Result(timePeriodStart ,timePeriodEnd, tmLaborHours, laborCost, tireHours);
        for (Consumables consumables : consumablesDataComp) {
            if (consumables.getTMQty() != 0) {
                result.add(consumables.getName(), consumables.getPrice(), consumables.getTMQty(), consumables.getTMMaintInterval(), 
                		consumables.gettmBreakIn());
            }
        }
        for (Part part : partsDataComp) {
            if (part.getTMQty() != 0) {
                result.add(part.getName(), part.getTMPrice(), part.getTMQty(), part.getTMMaintInterval(), part.gettmBreakIn());
            }
        }
        result.add("Travel Costs", tmTravelCost, 1,250, false);
        result.add("Fuel Costs", cpg, tmgph, 1, false);
        result.add("Labor Cost",0,0,0, false);
        result.calculate(timePeriodStart, timePeriodEnd);
        return result;
    }

    private Result calculateSWXResults(double timePeriodStart, double timePeriodEnd) {
        Result result = new Result(timePeriodStart, timePeriodEnd, swxLaborHours, laborCost, tireHours);
        for (Consumables consumables : consumablesDataComp) {
            if (consumables.getSWXQty() != 0) {
                result.add(consumables.getName(), consumables.getPrice(), consumables.getSWXQty(), 
                		consumables.getSwxMaintInterval(), consumables.getswxBreakIn());
            }
        }
        for (Part part : partsDataComp) {
            if (part.getSWXQty() != 0) {
                result.add(part.getName(), part.getSWXPrice(), part.getSWXQty(), part.getSwxMaintInterval(), part.getswxBreakIn());
            }
        }
        result.add("Travel Costs", swxTravelCost, 1,500, false);
        result.add("Fuel Costs", cpg, swxgph, 1, false);
        result.add("Labor Cost",0,0,0, false);
        result.calculate(timePeriodStart, timePeriodEnd);
        return result;
    }
    
    private void saveData(String labor, String tmTravel, String swxTravel, String tires, String tmgph, String swxgph, String cpg) {
    	try {
            
            FileChooser filechooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            filechooser.getExtensionFilters().add(extFilter);
            filechooser.setInitialFileName("consumablesData.csv");
        	File outputFile = filechooser.showSaveDialog(stage);
        	if(outputFile != null){
        		FileWriter writer = new FileWriter(outputFile);
        		boolean first = true;
                for (Consumables consumables : consumablesData) {
                	if (first){
                		CSVUtils.writeLine(writer, Arrays.asList(
	                            consumables.getName(),
	                            String.valueOf(consumables.getPrice()),
	                            consumables.getUnit(),
	                            String.valueOf(consumables.getSWXQty()),
	                            String.valueOf(consumables.getTMQty()),
	                            String.valueOf(consumables.getSwxMaintInterval()),
	                            String.valueOf(consumables.getTMMaintInterval()),
	                            String.valueOf(consumables.isCompressor.get()),
	                            String.valueOf(diff.switchedOnProperty().get()),
	                            String.valueOf(compSwitch.switchedOnProperty().get()),
	                            String.valueOf(consumables.gettmBreakIn()),
	                            String.valueOf(consumables.getswxBreakIn()),
	                            labor,
	                            tmTravel,
	                            swxTravel,
	                            tires,
	                            tmgph,
	                            swxgph,
	                            cpg
	                    ),',');
                		first = false;
                	} else {
	                    CSVUtils.writeLine(writer, Arrays.asList(
	                            consumables.getName(),
	                            String.valueOf(consumables.getPrice()),
	                            consumables.getUnit(),
	                            String.valueOf(consumables.getSWXQty()),
	                            String.valueOf(consumables.getTMQty()),
	                            String.valueOf(consumables.getSwxMaintInterval()),
	                            String.valueOf(consumables.getTMMaintInterval()),
	                            String.valueOf(consumables.isCompressor.get()),
	                            String.valueOf(consumables.gettmBreakIn()),
	                            String.valueOf(consumables.getswxBreakIn())
	                    ),',');
	                }
                }
                writer.flush();
                writer.close();
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
    }
    
    public void savePartsData() {
    	try {
          FileChooser filechooser = new FileChooser();
          FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
          filechooser.getExtensionFilters().add(extFilter);
          filechooser.setInitialFileName("partsData.csv");
      	  File outputFile = filechooser.showSaveDialog(stage);
      	  if(outputFile != null){
      		  FileWriter writer = new FileWriter(outputFile);
              for (Part part : partsData) {
            	  CSVUtils.writeLine(writer, Arrays.asList(
                            part.getName(),
                            String.valueOf(part.getSWXPrice()),
                            String.valueOf(part.getTMPrice()),
                            part.getId(),
                            String.valueOf(part.getSWXQty()),
                            String.valueOf(part.getTMQty()),
                            String.valueOf(part.getSwxMaintInterval()),
                            String.valueOf(part.getTMMaintInterval()),
                            String.valueOf(part.getIsCompressor()),
                            String.valueOf(part.gettmBreakIn()),
                            String.valueOf(part.getswxBreakIn())
                  ),',');
              }
              writer.flush();
              writer.close();
      	  }
      } catch (Exception e) {
          e.printStackTrace();
          Alert a = new Alert(Alert.AlertType.INFORMATION);
          a.setContentText(e.getMessage());
          a.showAndWait();
      }
    }

    private void save() {
        //Apache
        try {
            // Create a document and add a page to it
            PDDocument docTemplate = PDDocument.load(this.getClass().getResourceAsStream("/resources/template.pdf"));
            PDPage template = (PDPage) docTemplate.getPage(0);

            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage( page );
            
// Create a new font object selecting one of the PDF base fonts
//            File segoeUIFile = new File(this.getClass().getResource("/resources/fonts/segoeui.ttf").getFile());
//            PDType0Font segoeUI = PDType0Font.load(doc, segoeUIFile);
            PDType1Font helvetica = PDType1Font.HELVETICA;
            PDType1Font helvetica_bold = PDType1Font.HELVETICA_BOLD;
            
            BufferedImage tmLogo = ImageIO.read(this.getClass().getResourceAsStream("/resources/Trackmobile_3col_logo2.png"));
            PDImageXObject  pdImageXObject = LosslessFactory.createFromImage(doc, tmLogo);
            
            WritableImage snapshot = stage.getScene().snapshot(null);
            BufferedImage results = SwingFXUtils.fromFXImage(snapshot, null);
            PDImageXObject  resultsObject = LosslessFactory.createFromImage(doc, results);
            
// Start a new content stream which will "hold" the to be created content
            PDPageContentStream contentStream = new PDPageContentStream(doc, page);
            contentStream.drawImage(pdImageXObject, 50, 762-tmLogo.getHeight()/12, tmLogo.getWidth() / 12, tmLogo.getHeight() / 12);
//            contentStream.drawImage(resultsObject, 50, 642-results.getHeight(), results.getWidth(), results.getHeight());

            contentStream.beginText();
            contentStream.newLineAtOffset(400, 762-20);
            contentStream.setFont(helvetica, 20);
            contentStream.showText("Operational Cost");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.newLineAtOffset(400, 762-42);
            contentStream.setFont(helvetica, 20);
            contentStream.showText("Estimation");
            contentStream.endText();
            
            int prepTopPad = 10;
            int prepSize = 10;
            
            contentStream.moveTo(50,698-prepTopPad);
            contentStream.lineTo(545, 698-prepTopPad);
            contentStream.stroke();
            
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700-prepTopPad);
            contentStream.setFont(helvetica_bold, prepSize + 2);
            contentStream.showText("Prepared For:");
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700 - 1*(prepSize+2)-prepTopPad);
            contentStream.setFont(helvetica, prepSize);
            contentStream.showText("Name: " + customer);
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700 - 2*(prepSize+2)-prepTopPad);
            contentStream.setFont(helvetica, prepSize);
            contentStream.showText("From: " + customerCompany);
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700 - 3*(prepSize+2)-prepTopPad);
            contentStream.setFont(helvetica, prepSize);
            contentStream.showText(custStreet);
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700 - 4*(prepSize+2)-prepTopPad);
            contentStream.setFont(helvetica, prepSize);
            contentStream.showText(custCity + ", " + custState + " " + custZip);
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700 - 5*(prepSize+2)-prepTopPad);
            contentStream.setFont(helvetica, prepSize);
            contentStream.showText("Phone: " + custPhone);
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.newLineAtOffset(300, 700-prepTopPad);
            contentStream.setFont(helvetica_bold, prepSize + 2);
            contentStream.showText("Prepared By:");
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(300, 700 - 1*(prepSize+2)-prepTopPad);
            contentStream.setFont(helvetica, prepSize);
            contentStream.showText("Name: " + specialist);
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(300, 700 - 2*(prepSize+2)-prepTopPad);
            contentStream.setFont(helvetica, prepSize);
            contentStream.showText("From: " + distributor);
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(300, 700 - 3*(prepSize+2)-prepTopPad);
            contentStream.setFont(helvetica, prepSize);
            contentStream.showText(distStreet);
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(300, 700 - 4*(prepSize+2)-prepTopPad);
            contentStream.setFont(helvetica, prepSize);
            contentStream.showText(distCity + ", " + distState + " " + distZip);
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(300, 700 - 5*(prepSize+2)-prepTopPad);
            contentStream.setFont(helvetica, prepSize);
            contentStream.showText("Phone: " + specPhone);
            contentStream.endText();
            
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);
            
            AreaChart ac = getChart(425*2,175*2);
            ac.getXAxis().setStyle("-fx-font-size: 12px;");
            ac.getXAxis().setLabel(null);
            ac.getYAxis().setStyle("-fx-font-size: 12px;");
            ac.getYAxis().setLabel(null);
            ac.setTitle(null);
            Scene chartScene = new Scene(ac, 425*2,175*2);
            
            int chartTopPad = 5;
            
            chartScene.getStylesheets().add(chartcss);
            WritableImage chart = chartScene.snapshot(null);
            BufferedImage chartImage = SwingFXUtils.fromFXImage(chart, null);
            PDImageXObject  chartObject = LosslessFactory.createFromImage(doc, chartImage);
            contentStream.drawImage(chartObject, 75, (float) (700-5*(prepSize+2)-prepTopPad-chartTopPad-chartImage.getHeight()/2), (float) (chartImage.getWidth()/2), (float) (chartImage.getHeight()/2));
            
            float cphsize = 10;
            float cphheadersize = cphsize + 2;
            float cphheaderline = cphheadersize + 2;
            float cphline = cphsize + 4;
            
            contentStream.beginText();
            contentStream.newLineAtOffset(430, 700-5*(prepSize+2)-prepTopPad-chartTopPad);
            contentStream.setFont(helvetica, cphheadersize);
            contentStream.showText("Trackmobile Cost/Hr");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.newLineAtOffset(460, 700-5*(prepSize+2)-prepTopPad-chartTopPad-cphheaderline);
            contentStream.setFont(helvetica, cphsize);
            contentStream.showText(nf.format(tmcph) + "/Hr");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.newLineAtOffset(425, 700-5*(prepSize+2)-prepTopPad-chartTopPad-cphheaderline-cphline);
            contentStream.setFont(helvetica, cphheadersize);
            contentStream.showText("Shuttlewagon Cost/Hr");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.newLineAtOffset(460, 700-5*(prepSize+2)-prepTopPad-chartTopPad-cphheaderline-cphline-cphheaderline);
            contentStream.setFont(helvetica, cphsize);
            contentStream.showText(nf.format(swxcph) + "/Hr");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.newLineAtOffset(420, 700-5*(prepSize+2)-prepTopPad-chartTopPad-cphheaderline-cphline-cphheaderline-cphline);
            contentStream.setFont(helvetica, cphheadersize);
            contentStream.showText("Trackmobile Advantage");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.newLineAtOffset(460, 700-5*(prepSize+2)-prepTopPad-chartTopPad-cphheaderline-cphline-cphheaderline-cphline-cphheaderline);
            contentStream.setFont(helvetica, cphsize);
            contentStream.showText(nf.format(cphadv) + "/Hr");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.newLineAtOffset(425, 700-5*(prepSize+2)-prepTopPad-chartTopPad+10-chartImage.getHeight()/2);
            contentStream.setFont(helvetica, 10);
            contentStream.showText("- Work Hours");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700-5*(prepSize+2)-prepTopPad-chartTopPad-8-chartImage.getHeight()/2);
            contentStream.setFont(helvetica_bold, 12);
            contentStream.showText("Cumulative cost of each input from " + (int) timeperiodStart + " to " + (int) timeperiodEnd + " Hours");
            contentStream.endText();
            
            int numParts = 0;
            ArrayList<PartResult> tmParts = new ArrayList<PartResult>(tm.getParts());
            ArrayList<PartResult> swxParts = new ArrayList<PartResult>(swx.getParts());
            
            ArrayList<ArrayList<String>> combinedList = new ArrayList<ArrayList<String>>();
            for (PartResult tmPart : tm.getParts()) {
            	combinedList.add(new ArrayList<String>(Arrays.asList(
            			tmPart.getName(),
            			String.format("$%,.2f", tmPart.getT4()),
            			String.format("$%,.2f",0.00))));
            }
            for (PartResult swxPart : swx.getParts()) {
            	boolean there = false;
            	for (ArrayList<String> part : combinedList) {
            		if (part.get(0).equals(swxPart.getName())) {
            			part.set(2,String.format("$%,.2f", swxPart.getT4()));
            			there = true;
            		}  
            	}
            	if (!there) {
            		combinedList.add(new ArrayList<String>(Arrays.asList(
            				swxPart.getName(),
            				String.format("$%,.2f",0.00),
            				String.format("$%,.2f", swxPart.getT4()))));
            	}
            }
            
            ArrayList<ArrayList<String>> combinedListPt1 = new ArrayList<ArrayList<String>>();
            ArrayList<ArrayList<String>> combinedListPt2 = new ArrayList<ArrayList<String>>();
            
            int count = 0;
            for (ArrayList<String> part : combinedList) {
            	if (count < combinedList.size()/2) {
            		combinedListPt1.add(part);
            	} else {
            		combinedListPt2.add(part);
            	}
            	count++;
            }
            
            // Define the table structure first
            TableBuilder tableBuilder = new TableBuilder()
                    .addColumnOfWidth(120)
                    .addColumnOfWidth(60)
                    .addColumnOfWidth(60)
                    .setFontSize(8)
                    .setFont(PDType1Font.HELVETICA);

            // Header ...
            tableBuilder.addRow(new RowBuilder()
            		.add(Cell.withText("Input Assumption").setBackgroundColor(new java.awt.Color(31, 73, 125)).withTextColor(new java.awt.Color(255,255,255)))
                    .add(Cell.withText("TM Cost").setBackgroundColor(new java.awt.Color(31, 73, 125)).withTextColor(new java.awt.Color(255,255,255)))
                    .add(Cell.withText("SWX Cost").setBackgroundColor(new java.awt.Color(31, 73, 125)).withTextColor(new java.awt.Color(255,255,255)))
                    .build());

            // ... and some cells
            for (ArrayList<String> part : combinedListPt1) {
                tableBuilder.addRow(new RowBuilder()
                        .add(Cell.withText(part.get(0)).withAllBorders())
                        .add(Cell.withText(part.get(1)).withAllBorders())
                        .add(Cell.withText(part.get(2)).withAllBorders())
                        .setBackgroundColor(numParts % 2 == 0 ? new java.awt.Color(220, 230, 241) : new java.awt.Color(184, 204, 228))
                        .build());
                numParts++;
            }
            
            (new TableDrawer(contentStream, tableBuilder.build(), 50, 700-5*(prepSize+2)-prepTopPad-chartTopPad-3-chartImage.getHeight()/2)).draw();
            
            // Define the table structure first
            TableBuilder tableBuilder2 = new TableBuilder()
                    .addColumnOfWidth(120)
                    .addColumnOfWidth(60)
                    .addColumnOfWidth(60)
                    .setFontSize(8)
                    .setFont(PDType1Font.HELVETICA);

            // Header ...
            tableBuilder2.addRow(new RowBuilder()
            		.add(Cell.withText("Input Assumption").setBackgroundColor(new java.awt.Color(31, 73, 125)).withTextColor(new java.awt.Color(255,255,255)))
                    .add(Cell.withText("TM Cost").setBackgroundColor(new java.awt.Color(31, 73, 125)).withTextColor(new java.awt.Color(255,255,255)))
                    .add(Cell.withText("SWX Cost").setBackgroundColor(new java.awt.Color(31, 73, 125)).withTextColor(new java.awt.Color(255,255,255)))
                    .build());

            // ... and some cells
            for (ArrayList<String> part : combinedListPt2) {
                tableBuilder2.addRow(new RowBuilder()
                        .add(Cell.withText(part.get(0)).withAllBorders())
                        .add(Cell.withText(part.get(1)).withAllBorders())
                        .add(Cell.withText(part.get(2)).withAllBorders())
                        .setBackgroundColor(numParts % 2 == 0 ? new java.awt.Color(220, 230, 241) : new java.awt.Color(184, 204, 228))
                        .build());
                numParts++;
            }
            
            (new TableDrawer(contentStream, tableBuilder2.build(), 300, 700-5*(prepSize+2)-prepTopPad-chartTopPad-3-chartImage.getHeight()/2)).draw();
            
            Paragraph boiler = new Paragraph(50,176, "Trackmobile improves the productivity and safety of rail operations by "
            		+ "manufacturing the longest lasting, most efficient mobile rail movers in the world.  Since we invented "
            		+ "the mobile rail car mover industry in 1948, we have produced over 10,000 units for over 54 countries.  "
            		+ "We enable you to move more cars at once, for lower cost per unit and higher rail yard productivity.  "
            		+ "Our technology delivers the most consistent performance, regardless of the weather, and our unmatched "
            		+ "network of authorized distributors ensures that trained service technicians are available 24x7x365.  "
            		, doc).withFont(helvetica, 11);
            
            java.util.List<String> boilerLines = boiler.getLines();
            int boilerCount = 0;
            for (Iterator<String> i = boilerLines.iterator(); i.hasNext(); boilerCount++) {
            	contentStream.beginText();
            	contentStream.newLineAtOffset(boiler.getX(), boiler.getY()-boilerCount*14);
                contentStream.setFont(boiler.getFont(), boiler.getFontSize());
                contentStream.showText(i.next());
                contentStream.endText();
            }
            
            Paragraph cta = new Paragraph(50,81,"To learn more about how Trackmobile leads the industry, contact the "
            		+ "Trackmobile Specialist above or visit www.trackmobile.com.", doc).withFont(helvetica, 11);
            
            java.util.List<String> ctaLines = cta.getLines();
            int ctaCount = 0;
            for (Iterator<String> i = ctaLines.iterator(); i.hasNext(); ctaCount++) {
            	contentStream.beginText();
            	contentStream.newLineAtOffset(cta.getX(), cta.getY()-ctaCount*14);
                contentStream.setFont(cta.getFont(), cta.getFontSize());
                contentStream.showText(i.next());
                contentStream.endText();
            }
            
            Paragraph footer = new Paragraph(50,50,"Note: This is not a guarantee of performance nor a quotation for sale.  "
            		+ "Exact performance depends on a variety of factors, including but not limited to track condition, "
            		+ "operational conditions, and operator training.",doc).withFont(helvetica, 8);
            
            java.util.List<String> footerLines = footer.getLines();
            int footerCount = 0;
            for (Iterator<String> i = footerLines.iterator(); i.hasNext(); footerCount++) {
            	contentStream.beginText();
            	contentStream.newLineAtOffset(footer.getX(), footer.getY()-footerCount*10);
                contentStream.setFont(footer.getFont(), footer.getFontSize());
                contentStream.showText(i.next());
                contentStream.endText();
            }
            contentStream.beginText();
            contentStream.newLineAtOffset(410, 20);
            contentStream.setFont(helvetica, 8);
            contentStream.showText("© Copyright Trackmobile® LLC, 2017");
            contentStream.endText();
            
            
// Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"


// Make sure that the content stream is closed:
            contentStream.close();

// Save the results and ensure that the document is properly closed:
            FileChooser filechooser = new FileChooser();
            filechooser.setInitialFileName("results" + date.replace("/", "-") + ".pdf");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
            filechooser.getExtensionFilters().add(extFilter);
            filechooser.setInitialDirectory(new File(System.getProperty("user.home")));
        	File outputFile = filechooser.showSaveDialog(stage);
        	if(outputFile != null){
        		doc.save(outputFile);
        		doc.close();
            }
            
        } catch (IOException e) {
        	Alert alert = new Alert(Alert.AlertType.ERROR);
        	alert.setTitle("Exception Dialog");
        	alert.setHeaderText("IO");
        	alert.setContentText(e.getMessage());
        	// Create expandable Exception.
        	StringWriter sw = new StringWriter();
        	PrintWriter pw = new PrintWriter(sw);
        	e.printStackTrace(pw);
        	String exceptionText = sw.toString();
        	Label label = new Label("The exception stacktrace was:");
        	TextArea textArea = new TextArea(exceptionText);
        	textArea.setEditable(false);
        	textArea.setWrapText(true);
        	textArea.setMaxWidth(Double.MAX_VALUE);
        	textArea.setMaxHeight(Double.MAX_VALUE);
        	GridPane.setVgrow(textArea, Priority.ALWAYS);
        	GridPane.setHgrow(textArea, Priority.ALWAYS);
        	GridPane expContent = new GridPane();
        	expContent.setMaxWidth(Double.MAX_VALUE);
        	expContent.add(label, 0, 0);
        	expContent.add(textArea, 0, 1);
        	// Set expandable Exception into the dialog pane.
        	alert.getDialogPane().setExpandableContent(expContent);
        	alert.showAndWait();
        	e.printStackTrace();
        } catch (Exception e) {
        	Alert alert = new Alert(Alert.AlertType.ERROR);
        	alert.setTitle("Exception Dialog");
        	alert.setHeaderText("Ex");
        	alert.setContentText(e.getMessage());
        	// Create expandable Exception.
        	StringWriter sw = new StringWriter();
        	PrintWriter pw = new PrintWriter(sw);
        	e.printStackTrace(pw);
        	String exceptionText = sw.toString();
        	Label label = new Label("The exception stacktrace was:");
        	TextArea textArea = new TextArea(exceptionText);
        	textArea.setEditable(false);
        	textArea.setWrapText(true);
        	textArea.setMaxWidth(Double.MAX_VALUE);
        	textArea.setMaxHeight(Double.MAX_VALUE);
        	GridPane.setVgrow(textArea, Priority.ALWAYS);
        	GridPane.setHgrow(textArea, Priority.ALWAYS);
        	GridPane expContent = new GridPane();
        	expContent.setMaxWidth(Double.MAX_VALUE);
        	expContent.add(label, 0, 0);
        	expContent.add(textArea, 0, 1);
        	// Set expandable Exception into the dialog pane.
        	alert.getDialogPane().setExpandableContent(expContent);
        	alert.showAndWait();
        	e.printStackTrace();
        }
    }

    private void printTotal() {
//CALCULATE TOTAL
        double fluidTotal = 0.00, swxTotal = 0.00, tmTotal = 0.00;
        for (Consumables consumables : consumablesData) {
            fluidTotal += consumables.getPrice();
        }
        for (Part part : partsData) {
            swxTotal += part.getSWXPrice();
            tmTotal += part.getTMPrice();
        }

    }

    private boolean isValid(String input) {
        return input.equals(password);
    }

    private void updateMaintenanceIntervals() {
    	double scale = 0.5;
    	if (diff.switchedOnProperty().get()) {
    		scale = 2.0;
    	}
        for (Consumables consumables : consumablesData) {
            consumables.setTMMaintInterval(consumables.getTMMaintInterval()/scale);
            consumables.setSwxMaintInterval(consumables.getSwxMaintInterval()/scale);
        }
        for (Part part : partsData) {
            part.setTMMaintInterval(part.getTmMaintIntervalOrig()/scale);
            part.setSwxMaintInterval(part.getSwxMaintIntervalOrig()/scale);
        }
    }

    private void viewTmSchedule() {
    	Stage stage = new Stage();
    	stage.setTitle("Trackmobile Maintenance Schedule");
        Scene scene = new Scene(new Group(), Color.rgb(0,93,170));
        scene.getStylesheets().add(css);
        
        Image logo = new Image(this.getClass().getResourceAsStream("Trackmobile_wht_logo2.png"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(50);
        logoView.setPreserveRatio(true);

        VBox everything = new VBox();
        everything.setPadding(new Insets(10, 10, 10, 10));
        final Label label = new Label("Trackmobile Maintenance Schedule");
        label.setId("header");
        Button back = new Button("Close");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        Image schedule = new Image(this.getClass().getResourceAsStream("TM_MaintenanceSchedule.png"));
        ImageView scheduleView = new ImageView(schedule);
        scheduleView.setPreserveRatio(true);
        
        ScrollPane imagePane = new ScrollPane();
        DoubleProperty zoomProperty = new SimpleDoubleProperty(200);
        
        zoomProperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable arg0) {
            	scheduleView.setFitWidth(zoomProperty.get() * schedule.getWidth()/200);
            	scheduleView.setFitHeight(zoomProperty.get() * schedule.getHeight()/200);
            }
        });

        imagePane.setContent(scheduleView);
        imagePane.setPrefHeight(700);
        imagePane.setPrefWidth(1000);
        
        Button in = new Button("+");
        in.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	zoomProperty.set(zoomProperty.get() * 1.1);
            }
        });
        in.setId("zoom");
        Button out = new Button("-");
        out.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	zoomProperty.set(zoomProperty.get() / 1.1);
            }
        });
        out.setId("zoom");
        
        final Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinSize(10, 1);
        
        HBox topbar = new HBox();
        topbar.getChildren().addAll(label,spacer,out,in);
        
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        Label laborlabel = new Label("Labor Hours");
        laborlabel.setPadding(new Insets(2,5,2,5));
        gridPane.add(laborlabel, 0, 0, 1, 2);
        gridPane.getStyleClass().add("labor-grid");
        
        int c = 1;
        Iterator it = sortByKey(tmLaborHours).entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Double,Integer> pair = (Map.Entry)it.next();
            
            HBox time = new HBox();
            time.getChildren().add(new Label(String.valueOf(pair.getKey())));
            time.getStyleClass().add("labor-grid-cell");
            gridPane.add(time, c, 0, 1, 1);
            
            HBox hours = new HBox();
            hours.getChildren().add(new Label(String.valueOf(pair.getValue())));
            hours.getStyleClass().add("labor-grid-cell");
            gridPane.add(hours, c, 1, 1, 1);
            
            c++;
        }

        everything.setSpacing(5);
        everything.getChildren().addAll(logoView,topbar, imagePane, gridPane, back);
        ((Group) scene.getRoot()).getChildren().addAll(everything);
        stage.setScene(scene);
        stage.show();
    }
    
    private void viewSwxSchedule() {
    	Stage stage = new Stage();
    	stage.setTitle("Trackmobile Maintenance Schedule");
        Scene scene = new Scene(new Group(), Color.rgb(0,93,170));
        scene.getStylesheets().add(css);
        
        Image logo = new Image(this.getClass().getResourceAsStream("Trackmobile_wht_logo2.png"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(50);
        logoView.setPreserveRatio(true);

        VBox everything = new VBox();
        everything.setPadding(new Insets(10, 10, 10, 10));
        final Label label = new Label("Shuttlewagon Maintenance Schedule");
        label.setId("header");
        Button back = new Button("Close");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        Image schedule = new Image(this.getClass().getResourceAsStream("SWX_MaintenanceSchedule.png"));
        ImageView scheduleView = new ImageView(schedule);
        scheduleView.setPreserveRatio(true);
        
        ScrollPane imagePane = new ScrollPane();
        DoubleProperty zoomProperty = new SimpleDoubleProperty(200);
        
        zoomProperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable arg0) {
            	scheduleView.setFitWidth(zoomProperty.get() * schedule.getWidth()/200);
            	scheduleView.setFitHeight(zoomProperty.get() * schedule.getHeight()/200);
            }
        });

        imagePane.setContent(scheduleView);
        imagePane.setPrefHeight(700);
        imagePane.setPrefWidth(1000);
        
        Button in = new Button("+");
        in.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	zoomProperty.set(zoomProperty.get() * 1.1);
            }
        });
        in.setId("zoom");
        Button out = new Button("-");
        out.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	zoomProperty.set(zoomProperty.get() / 1.1);
            }
        });
        out.setId("zoom");
        
        final Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinSize(10, 1);
        
        HBox topbar = new HBox();
        topbar.getChildren().addAll(label,spacer,out,in);
        
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        Label laborlabel = new Label("Labor Hours");
        laborlabel.setPadding(new Insets(2,5,2,5));
        gridPane.add(laborlabel, 0, 0, 1, 2);
        gridPane.getStyleClass().add("labor-grid");
        
        int c = 1;
        Iterator it = sortByKey(swxLaborHours).entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Double,Integer> pair = (Map.Entry)it.next();
            
            HBox time = new HBox();
            time.getChildren().add(new Label(String.valueOf(pair.getKey())));
            time.getStyleClass().add("labor-grid-cell");
            gridPane.add(time, c, 0, 1, 1);
            
            HBox hours = new HBox();
            hours.getChildren().add(new Label(String.valueOf(pair.getValue())));
            hours.getStyleClass().add("labor-grid-cell");
            gridPane.add(hours, c, 1, 1, 1);
            
            c++;
        }

        everything.setSpacing(5);
        everything.getChildren().addAll(logoView,topbar, imagePane, gridPane, back);
        ((Group) scene.getRoot()).getChildren().addAll(everything);
        stage.setScene(scene);
        stage.show();
    }

    public void detailedResults() {
        Scene scene = new Scene(new Group(), Color.rgb(0,93,170));
        scene.getStylesheets().add(css);

        VBox everything = new VBox();
        everything.setPadding(new Insets(10, 10, 10, 10));
        Button back = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                results();
            }
        });

        HBox chartBox = new HBox();
        chartBox.setAlignment(Pos.CENTER);
        chartBox.setPrefWidth(800);
        chartBox.getChildren().addAll(getChart(600,400));

        everything.getChildren().addAll(logoView, chartBox, back);
        ((Group) scene.getRoot()).getChildren().addAll(everything);
        stage.setScene(scene);
        stage.show();
        resultsViewed = true;
    }
    
    public AreaChart getChart(double width, double height) {
        final NumberAxis xAxis = new NumberAxis(timeperiodStart, timeperiodEnd, 500);
        xAxis.setLabel("Hours");
        final NumberAxis yAxis = new NumberAxis();
        final AreaChart<Number,Number> ac =
                new AreaChart<Number,Number>(xAxis,yAxis);
        ac.setTitle("Maintenance Cost Comparison");
        ac.setAnimated(false);
        yAxis.setTickUnit(1000);
        yAxis.setLabel("Cost");
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis,"$ ",null));
        ac.setPrefSize(width,height);
        ac.setMinSize( width,height);
        ac.setMaxSize( width,height);
        
        xAxis.setId("axis");
        yAxis.setId("axis");

        XYChart.Series tmSeries= new XYChart.Series();
        tmSeries.setName("Trackmobile");
        double interval = 500;
        tmSeries.getData().add(new XYChart.Data(0,0));
        tmSeries.getData().add(new XYChart.Data(100,tm.getValue(100)));
        System.out.println("TM");
        for (double i = interval; i <= tm.getTimePeriod(); i+=interval) {
            tmSeries.getData().add(new XYChart.Data(i,tm.getValue(i)));
            System.out.println(i + ": " + tm.getValue(i));
        }

        XYChart.Series swxSeries= new XYChart.Series();
        swxSeries.setName("Shuttlewagon");
        swxSeries.getData().add(new XYChart.Data(0,0));
        swxSeries.getData().add(new XYChart.Data(100,swx.getValue(100)));
        System.out.println("SWX");
        for (double i = interval; i <= swx.getTimePeriod(); i+=interval) {
            swxSeries.getData().add(new XYChart.Data(i,swx.getValue(i)));
            System.out.println(i + ": " + swx.getValue(i));
        }
        ac.getData().addAll(tmSeries, swxSeries);
        return ac;
    }
    
    public Map sortByKey(Map map) { 
    	List list = new LinkedList(map.entrySet()); 
    	Collections.sort(list, new Comparator() { 
    		public int compare(Object o2, Object o1) { 
    			return ((Comparable) ((Map.Entry) (o2)).getKey()) .compareTo(((Map.Entry) (o1)).getKey()); 
    			} 
    		}); 
    	Map result = new LinkedHashMap(); 
    	for (Iterator it = list.iterator(); it.hasNext();) {
    		Map.Entry entry = (Map.Entry)it.next(); 
    		result.put(entry.getKey(), entry.getValue()); 
    		} 
    	return result; 

    }
}


