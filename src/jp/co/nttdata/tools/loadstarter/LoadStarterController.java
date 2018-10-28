package jp.co.nttdata.tools.loadstarter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class LoadStarterController implements Initializable {

	@FXML
	private TextField scenarioFild;

	@FXML
	private Button scenarioBtn;

	@FXML
	private TextField logHomeField;

	@FXML
	private Button logHomeBtn;

	@FXML
	private TextField testNameFiled;

	@FXML
	private TextField durationField;

	@FXML
	private TextField rumpupField;

	@FXML
	private Button startBtn;

	@FXML
	void onLogDirBtnAction(ActionEvent event) {
		DirectoryChooser  dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Open Log Directory");
		File selectedDir = dirChooser.showDialog(null);
		if (selectedDir != null) {
			try {
				this.logHomeField.setText(selectedDir.getCanonicalFile().getPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@FXML
	void onScenarioBtnAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Jmeter scenario", "*.jmx"),
				new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			try {
				this.scenarioFild.setText(selectedFile.getCanonicalFile().getPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void onStartAction(ActionEvent event) {
		System.out.println("Start Bottun");
		if(this.testNameFiled.getText().isBlank()) {
			Alert alert = new Alert(AlertType.ERROR,"", ButtonType.OK);
			alert.setTitle( "Error" );
			alert.getDialogPane().setContentText( "Test Name is not set." );
			alert.showAndWait();
			return;
		}
	}

	@FXML
	void onDigitCheck(KeyEvent event) {
		if(!event.getText().isEmpty()&&!event.getCode().isDigitKey()) {
			Alert alert = new Alert(AlertType.ERROR,"", ButtonType.OK);
			alert.setTitle( "Error" );
			alert.getDialogPane().setContentText( "This field is only input integers." );
			alert.showAndWait();
		}
		
		
		
		
	}

	@Override
	public void initialize(URL location, ResourceBundle rb) {
		String temp = "";
		try {
			temp = (String) rb.getObject("LOG_HOME");
			this.logHomeField.setText(temp);
		} catch (MissingResourceException e) {
			System.err.println("LOG_HOME is not found in properties.");
		}

		try {
			temp = (String) rb.getObject("SCENARIO_FILE");
			this.scenarioFild.setText(temp);
		} catch (MissingResourceException e) {
			System.err.println("SCENARIO_FILE is not found in properties.");
		}

		try {
			temp = (String) rb.getObject("DURATION");
			this.durationField.setText(temp);
		} catch (MissingResourceException e) {
			this.durationField.setText("1800");
			System.err.println("DURATION is not found in properties.");
		}

		try {
			temp = (String) rb.getObject("RUMPUP");
			this.rumpupField.setText(temp);
		} catch (MissingResourceException e) {
			this.rumpupField.setText("30");
			System.err.println("RUMPUP is not found in properties.");
		}

		try {
			temp = (String) rb.getObject("TOOL_PATH");
			//this.durationField.setText(temp);
		} catch (MissingResourceException e) {
			//this.rumpupField.setText("30");
			System.err.println("TOOL_PATH is not found in properties.");
		}

	}
}
