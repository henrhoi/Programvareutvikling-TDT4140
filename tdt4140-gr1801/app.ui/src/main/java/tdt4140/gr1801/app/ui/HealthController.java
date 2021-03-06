package tdt4140.gr1801.app.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import tdt4140.gr1801.app.core.Client;
import tdt4140.gr1801.app.core.Nutrition;


//This controller controls the nutrition tab in the application
//Keeps the pie chart updated according to the client's food intake for the day.


public class HealthController implements TabController {
	
	@FXML
	PieChart NutritionPieChart;
	
	@FXML
	ListView<Nutrition> listview;
	
	@FXML
	Label PieChartDataLabel;
	
	@FXML
	TextArea caloriesText;	
		
	private Client client;
	
	
	public HealthController(Client client) {
		this.client = client;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	@FXML
	public void updateInfo() {
		fillListview();
		updateView(null);
		
	}
	
	public void fillListview() {
		ObservableList<Nutrition> nutritionItems = FXCollections.observableArrayList ();
		for(Nutrition nutrition : this.client.getNutritionList()) {
			nutritionItems.add(nutrition);
		}
		listview.setItems(nutritionItems);
	}
	
	public void setNavigationLogic() {
		// Adding logic for updating view when different nutrition-dates gets selected.
		// Mouseclick
		listview.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Getting the selected nutrition
				Nutrition selected = listview.getSelectionModel().getSelectedItem();
				// Setting data in the view thereafter
				updateView(selected);
			}
		});

		// Keyboard (up- and down-arrows)
		listview.setOnKeyReleased(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				// Getting the selected nutrition
				Nutrition selected = listview.getSelectionModel().getSelectedItem();
				// Setting data in the view thereafter
				updateView(selected);
			}
		});
	}
	
	public void updateView(Nutrition n) {
		if (n != null) {
			ObservableList<Data> pieChartData = FXCollections.observableArrayList(
					new Data("Fat", n.getFat()),
					new Data("Protein", n.getProtein()),
					new Data("Carbs" , n.getCarbs())
				);
			
			//Setting the title of the chart + placing the legend on the left side of the chart.
			NutritionPieChart.setTitle("Nutrition of " + n);
			NutritionPieChart.setData(pieChartData);
			
			
			// Changes the colors of the Pie chart so that they are the same every time
			applyCustomColorSequence(
				      pieChartData, 
				      "bisque", 
				      "coral", 
				      "crimson"
				    );
			NutritionPieChart.setLegendVisible(false);
			PieChartDataLabel.setText("Hover Pie Chart For Info");
			caloriesText.setText("Calories: " + n.getCalories());
			
			addEventHandlerPieChart();

			
		}

		else {
			NutritionPieChart.setData(FXCollections.observableArrayList());
		}

	}

	@Override
	public void startup() {
		setNavigationLogic();	
	}
	
	
	//Creates an event handler every time you change the date for that date's data
	public void addEventHandlerPieChart() {
		for(PieChart.Data data : NutritionPieChart.getData()) {
			data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					PieChartDataLabel.setText(data.getName() + " - " + data.getPieValue() + " grams");
				}
			});
		}
	}
	
	//Creating custom colors for all the pies
	private void applyCustomColorSequence(ObservableList<PieChart.Data> pieChartData, String... pieColors) {
	    int i = 0;
	    for (PieChart.Data data : pieChartData) {
	      data.getNode().setStyle("-fx-pie-color: " + pieColors[i % pieColors.length] + ";");
	      i++;
	    }
	  }
	
}
