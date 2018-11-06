package jp.co.nttdata.tools.loadstarter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import jp.co.nttdata.tools.loadstarter.LoadStarterController.Config.Scope;

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
    private CheckBox confCheckBox;

    @FXML
    private TableView<Config> table;
    @FXML

    private TableColumn<Config, String> nameColumn;

    @FXML
    private TableColumn<Config, String> valueColumn;

    @FXML
    private TableColumn<Config, Scope> scopeColumn;

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

		this.initTable();

	}


	private void initTable() {
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<Config, String>("name"));
		this.nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.nameColumn.setOnEditCommit(edit -> {
			edit.getRowValue().setName(edit.getNewValue());
		});

		this.valueColumn.setCellValueFactory(new PropertyValueFactory<Config, String>("value"));
		this.valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.valueColumn.setOnEditCommit(edit -> {
			edit.getRowValue().setValue(edit.getNewValue());
		});

		this.scopeColumn.setCellValueFactory(new PropertyValueFactory<Config,Scope>("scope"));
		this.scopeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(Scope.values()));

		this.scopeColumn.setOnEditCommit(edit -> {
			edit.getRowValue().setScope(edit.getNewValue());
		});

		this.table.setEditable(true);
		this.nameColumn.setEditable(true);
		this.valueColumn.setEditable(true);
		this.scopeColumn.setEditable(true);
		this.table.getItems().add(new Config("","",Scope.global));

	}


	public static class Config {
		private final SimpleStringProperty  name;
		private final SimpleStringProperty  value;
		private final SimpleStringProperty  scope;
		public enum Scope{
			global,
			local
		}

		private Config(String name,String value,Scope scope) {
			this.name = new SimpleStringProperty(name);
			this.value = new SimpleStringProperty(value);
			this.scope = new SimpleStringProperty(scope.name());
		}

		public String getName() {
			return name.get();
		}
		public void setName(String name) {
			this.name.set(name);
		}
		public String getValue() {
			return value.get();
		}
		public void setValue(String value) {
			this.value.set(value);
		}
		public void setScope(Scope scope) {
			this.scope.set(scope.name());
		}
		public String getScope() {
			return scope.get();
		}

		@SuppressWarnings("exports")
		public StringProperty nameProperty() {
			return name;
		}

		@SuppressWarnings("exports")
		public StringProperty valueProperty() {
			return value;
		}

		@SuppressWarnings("exports")
		public StringProperty scopeProperty() {
			return scope;
		}


	}

}


