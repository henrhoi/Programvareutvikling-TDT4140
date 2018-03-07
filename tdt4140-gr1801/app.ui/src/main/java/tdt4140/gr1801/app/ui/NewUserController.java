package tdt4140.gr1801.app.ui;


import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tdt4140.gr1801.app.core.PersonalTrainer;





public class NewUserController implements Controller{
	
	@FXML 
	TextField usernameField, firstNameField, lastNameField, phoneNumberField, emailField;
	
	@FXML 
	PasswordField passwordField, confirmPasswordField;
	
	@FXML
	Label infoLabel;
	
	@FXML
	DatePicker birthdayField;
	
	public NewUserController() {
		
	}
	
	//Action on createUserButton
		@FXML
		public void createUser() {
			
			//Check fields
			ArrayList<TextField> fields = new ArrayList<TextField>(Arrays.asList(usernameField,firstNameField, lastNameField,passwordField, phoneNumberField, emailField));
			fields.stream().forEach(f -> check(f));
			checkBirthday();
			
			Pane pane = (Pane)usernameField.getParent();
			List<Node> errors = pane.getChildren().stream().filter(f -> f.getStyleClass().contains("error")).collect(Collectors.toList());
			
			
			if(errors.size() == 0) {
				//TODO add new user to database and check that usernames not is used in database
				Stage stage = (Stage)usernameField.getScene().getWindow();
				LoginController controller = new LoginController();
	    			URL path = getClass().getResource("FxLogin.fxml");
	    			SceneLoader.setScene(stage, path, controller);
	    			System.out.println("New User added");
			}
			
		}
	
	
	///////////////////////////////////////////////////////Private methods////////////////////////////////////////////////////////////////////////
		
	private void check(TextField field) {
		boolean bool;
		ObservableList<String> style = field.getStyleClass();
		switch (field.getId()) {
		case "usernameField": bool = PersonalTrainer.checkUsername(field.getText());break;
		case "firstNameField": bool = PersonalTrainer.checkFirstName(field.getText());break;
		case "lastNameField": bool = PersonalTrainer.checkLastName(field.getText());break;
		case "emailField": bool = PersonalTrainer.checkEmail(field.getText());break;
		case "phoneNumberField": bool = PersonalTrainer.checkPhoneNumber(field.getText());break;
		case "passwordField": bool = field.getText().equals(confirmPasswordField.getText()) && field.getText().length() >= 4;break;
		default: bool = true;break;
		}
		if (!bool) {
            style.add("error");
        }
        else if (style.contains("error") && bool){
        		style.remove("error");
        }
	}
	
	private void checkBirthday() {
		boolean bool = birthdayField.getValue() == null ? false:birthdayField.getValue().isBefore(LocalDate.now());
		ObservableList<String> style = birthdayField.getStyleClass();
		if (!bool) {
            style.add("error");
        }
        else if (style.contains("error") ){
        		style.remove("error");
        }
	}
	
	
	
	
}
