package tdt4140.gr1801.app.ui;


import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import jersey.repackaged.com.google.common.collect.Lists;
import tdt4140.gr1801.app.core.Client;
import tdt4140.gr1801.app.core.Endurance;


//This controller controls the Endurance Tab in the program 
//Implements TabController and has all the required methods.

public class EnduranceController implements TabController {
	
	
	@FXML
	TextField distance_field, duration_field, avg_speed_field, cal_burned_field, max_pulse_field, avg_pulse_field;
	
	@FXML
	ListView<Endurance> listview;
	
	Client client;
	
	public EnduranceController(Client client) {
		setClient(client);
	}
	
	@Override
	public void setClient(Client client) {
		this.client = client;
		
	}
	
	@Override
	public void updateInfo() {
		fillListview();
		updateView(null);
		
	}
	
	public void fillListview() {
		// Adding the client's endurance-trainings to the listview
		ObservableList<Endurance> items = FXCollections.observableArrayList ();
		Collections.sort(client.getEnduranceList()); // Sorting after date
		for (Endurance endurance : Lists.reverse(client.getEnduranceList())) {
			items.add(endurance);
		}
		listview.setItems(items);
	}
	
	public void setNavigationLogic() {
		// Adding logic for updating view when different trainings gets selected.
		// Mouseclick
		listview.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Getting the selected endurance training
				Endurance selected = listview.getSelectionModel().getSelectedItem();
				// Setting data in the view thereafter
				updateView(selected);
			}
		});

		// Keyboard (up- and down-arrows)
		listview.setOnKeyReleased(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				// Getting the selected endurance training
				Endurance selected = listview.getSelectionModel().getSelectedItem();
				// Setting data in the view thereafter
				updateView(selected);
			}
		});
	}
	
	public void updateView(Endurance e) {
		if (e != null) {
			distance_field.setText(String.valueOf(e.getDistance()));
			duration_field.setText(String.valueOf(e.getDuration()));
			avg_speed_field.setText(String.valueOf(e.getAverageSpeed()));
			cal_burned_field.setText(String.valueOf(e.getCaloriesBurned()));
			max_pulse_field.setText(String.valueOf(e.getMaxPulse()));
			avg_pulse_field.setText(String.valueOf(e.getAvgPulse()));
		}
		else {
			distance_field.setText("");
			duration_field.setText("");
			avg_speed_field.setText("");
			cal_burned_field.setText("");
			max_pulse_field.setText("");
			avg_pulse_field.setText("");
		}
	}

	@Override
	public void startup() {
		setNavigationLogic();
		updateView(null);
		
	}
	

}
