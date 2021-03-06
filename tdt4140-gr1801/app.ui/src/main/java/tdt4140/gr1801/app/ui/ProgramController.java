package tdt4140.gr1801.app.ui;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.fxml.FXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.time.LocalDateTime;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;



import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import tdt4140.gr1801.app.core.Client;
import tdt4140.gr1801.app.core.PersonalTrainer;
import tdt4140.gr1801.app.core.pdfcreator.PdfCreator;
import tdt4140.gr1801.app.core.DayProgram;
import tdt4140.gr1801.app.core.Exercise;

//This class controls the ProgramTab window, and makes sure things are correctly entered to all the fields.
public class ProgramController implements TabController {
	
	
	@FXML
	ListView<DayProgram> listview;
	
    @FXML
    TableView<Exercise> tableview;
    
    @FXML
    TableColumn<Exercise, String> colName, colWeight, colSets, colReps;
	
    @FXML
    TextField distance_field, duration_field, speed_field;
    
    @FXML 
    TextArea description_field;
    
    @FXML
	Button exportButton;
    
    
	private Client client;
	private PersonalTrainer pt;	
	
	
	//Constructor with a PT and a Client
	public ProgramController(PersonalTrainer pt, Client client) {
		this.client = client;
		this.pt = pt;
	}
	
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	
	// Function for exporting the program to a specific path on the running computer in PDF-format
	@FXML
	public void exportPdf() {
		//Get the stage to open DirectoryChooser in
		Stage stage = (Stage) exportButton.getScene().getWindow();
		
		//Open DirectoryChooser and save path
		final DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Choose Export Location");
		final File selectedDirectoryPath = directoryChooser.showDialog(stage);   
		
        //Create the new document if path not null
        if (selectedDirectoryPath != null) {
        	LocalDateTime date = LocalDateTime.now();
        	Document doc = PdfCreator.getNewDocument(selectedDirectoryPath.toString() + "/" 
        	+ client.getName() + "_" + date.getDayOfMonth() + "_" + date.getMonthValue() + "_" + date.getYear() + ".pdf");
        	try {
        		//Add all the data to the document
        		PdfCreator.addMetaData(doc, "TrainingProgram_" + client.getName(), "Training", new ArrayList<String>(Arrays.asList("Training")), "PTApp", "PTApp");
				PdfCreator.addFrontPage(doc, pt, client);
				PdfCreator.addContent(doc, listview.getItems());
			} catch (DocumentException e) {
				e.printStackTrace();
			}
        	finally {
        		//Always close the document
				doc.close();
			}
        }   
	}
	
	
	//Method for updating the information in the view
	@Override
	public void updateInfo() {
		fillListview();
		updateView(null);
		setFieldsEditable(false);
		
	}
	

	// Method for filling the listview containing dayprograms
	public void fillListview() {
		// Adding the client's programs in the listview
		ObservableList<DayProgram> days = FXCollections.observableArrayList ();
		List<DayProgram> dayPrograms = client.getDayProgramList();
		Collections.sort(dayPrograms);
		for (DayProgram dayprogram : dayPrograms) {
			days.add(dayprogram);
		}
		listview.setItems(days);
	}
	
	public void setFieldsEditable(boolean value) {
		//distance_field.setDisable(true);
		distance_field.setEditable(value);
		//duration_field.setDisable(true);
		duration_field.setEditable(value);
		//speed_field.setDisable(true);
		speed_field.setEditable(value);
		//description_field.setDisable(true);
		description_field.setEditable(value);
		//tableview.setDisable(true);
		tableview.setEditable(value);
	}
	
	public void setNavigationLogic() {
		// Adding logic for updating view when different days gets selected.
		// Mouseclick
		listview.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Getting the selected day
				DayProgram selected = listview.getSelectionModel().getSelectedItem();
				// Setting data in the view thereafter
				updateView(selected);
			}
		});

		// Keyboard (up- and down-arrows)
		listview.setOnKeyReleased(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				// Getting the selected day
				DayProgram selected = listview.getSelectionModel().getSelectedItem();
				// Setting data in the view thereafter
				updateView(selected);
			}
		});

	}
	

	public void updateView(DayProgram day) {
		if (day == null || day.getDistance() == null) {
			distance_field.setText("");
			duration_field.setText("");
			speed_field.setText("");
			description_field.setText("");
		}
		
		else if (day.getDistance() != null) {
			// It's endurance training
			distance_field.setText(String.valueOf(day.getDistance()));
			duration_field.setText(String.valueOf(day.getDuration()));
			speed_field.setText(String.valueOf(day.getAvgSpeed()));
			description_field.setText(day.getDescription());
		}
		
		if (day == null || day.getExercises() == null) {
			tableview.setItems(null);
		}
		
		else if (day.getExercises() != null) {
			// It's Strength training
			ObservableList<Exercise> items = FXCollections.observableArrayList ();
			for (Exercise exercise : day.getExercises()) {
				items.add(exercise);
			}
			tableview.setItems(items);
		}
	}

	
	@Override
	public void startup() {
		// Connecting the columns of the tableview to attributes in Exercise
		this.colName.setCellValueFactory(new PropertyValueFactory<Exercise, String>("Name"));
		this.colWeight.setCellValueFactory(new PropertyValueFactory<Exercise, String>("Weight"));
		
		// Sets and reps are not excplicit defined as attributes in Exercise, so they must be calculated
		this.colSets.setCellValueFactory(s -> new SimpleStringProperty(Integer.toString(s.getValue().getNumberOfSets())));
		this.colReps.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().getReps()));
		
		setNavigationLogic();
		updateView(null);
	}
	
	

}
