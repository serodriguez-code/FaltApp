package dad.javafx.faltapp;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.FormatStyle;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import dad.javafx.faltapp.model.Alumno;
import dad.javafx.faltapp.model.Falta;
import dad.javafx.faltapp.model.Grupo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.NumberStringConverter;

public class GrupoTabView implements Initializable{

	@FXML
    private GridPane view;

    @FXML
    private Button addButton,deleteButton;

    @FXML
    private TextField cicloTF, moduloTF;

    @FXML
    private Spinner<Integer> cursoSpinner;

    @FXML
    private TableView<Alumno> alumnoTable;

    @FXML
    private TableColumn<Alumno, String> cialColumn;

    @FXML
    private TableColumn<Alumno, String> nombreColumn;

    @FXML
    private TableColumn<Alumno, String> apellidosColumn;

    @FXML
    private TableColumn<Alumno, LocalDate> fechaNacimientoColumn;

    @FXML
	private TableView<Falta> faltaTable;

    @FXML
    private TableColumn<Falta, LocalDate> diaColumn;

    @FXML
    private TableColumn<Falta, Number> horaColumn;

    @FXML
    private TableColumn<Falta, Boolean> retrasoColumn;
    
    private ObjectProperty<Grupo> grupo=new SimpleObjectProperty<>();   
    private ObjectProperty<Alumno> selectedAlumno=new SimpleObjectProperty<>();

    public GrupoTabView()  throws IOException{
	
		FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/GrupoTabView.fxml"));
		loader.setController(this);
		loader.load();
	}
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		cursoSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,2));
				
		grupo.addListener((o,ov,nv)->{
			if(nv!=null) { 
				grupo.unbind();
				grupo.bindBidirectional(MainController.mainController.grupoProperty());
				alumnoTable.itemsProperty().unbind();
				alumnoTable.itemsProperty().bind(grupo.get().alumnosProperty());
				cursoSpinner.getValueFactory().valueProperty().bindBidirectional(grupo.get().cursoProperty().asObject());
				cicloTF.textProperty().bindBidirectional(grupo.get().cicloProperty());
				moduloTF.textProperty().bindBidirectional(grupo.get().moduloProperty());
			}
		});
		
	/*Tabla alumno*/ 	
		cialColumn.setCellValueFactory(v->v.getValue().cialProperty());
		nombreColumn.setCellValueFactory(v->v.getValue().nombreProperty());
		apellidosColumn.setCellValueFactory(v->v.getValue().apellidosProperty());
		fechaNacimientoColumn.setCellValueFactory(v->v.getValue().fechaNacimientoProperty());
		
		cialColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nombreColumn.setCellFactory(TextFieldTableCell.forTableColumn()); 
		apellidosColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		fechaNacimientoColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter(FormatStyle.SHORT)));
		
	/*Tabla falta*/
		faltaTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		diaColumn.setCellValueFactory(v->v.getValue().fechaProperty());
		horaColumn.setCellValueFactory(v->v.getValue().horaProperty());
		retrasoColumn.setCellValueFactory(v->v.getValue().retrasoProperty());
		
		diaColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
		horaColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		retrasoColumn.setCellFactory(CheckBoxTableCell.forTableColumn(retrasoColumn));
	
	/* Alumno seleccionado */
		selectedAlumno.bind(alumnoTable.getSelectionModel().selectedItemProperty());
		
		deleteButton.setDisable(true);
		
		selectedAlumno.addListener((o,ov,nv)->{
			if(nv!=null) {
				faltaTable.itemsProperty().unbind();
				faltaTable.itemsProperty().bind(nv.faltasProperty());
			}else {
				faltaTable.itemsProperty().unbind();
				faltaTable.getItems().clear();
			}
				deleteButton.setDisable(nv==null);
		});
		
	/* KeyEvent */
		faltaTable.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(KeyCode.DELETE))
					deleteFaltasAction();
			}			
		});
	}
	
    @FXML
    private void onAddButton(ActionEvent event) {
    	Alumno alumno=new Alumno();
    	alumno.setCial(UUID.randomUUID().toString().split("-")[0].toUpperCase());
    	alumno.setNombre("Nombre");
    	alumno.setApellidos("Apellidos");
    	alumno.setFechaNacimiento(LocalDate.now());
    	
    	grupo.get().alumnosProperty().add(alumno);
    	alumnoTable.requestFocus();
    	alumnoTable.getSelectionModel().select(grupo.get().alumnosProperty().size()-1);
    }

    @FXML
    private void onDeleteButton(ActionEvent event) {
    	
    	Stage stage=(Stage)view.getScene().getWindow();
    	Optional<ButtonType>result=MainController.mainController.alertConfirmation(stage, "Eliminar alumno", 
    			"Se va a eliminar el alumno '"+selectedAlumno.get().getNombreCompleto()+"'.", "¿Desea Continuar?").showAndWait();
    	
    	if(result.get()==ButtonType.OK) 
    		grupo.get().alumnosProperty().remove(selectedAlumno.get());
    }

    private void deleteFaltasAction() {
    	Stage stage=(Stage)view.getScene().getWindow();
    	Optional<ButtonType>result=MainController.mainController.alertWarningCancelB(stage, "Eliminar faltas", 
    			"Se van a eliminar las faltas seleccionadas","¿Desea continuar?").showAndWait();
    	
    	if(result.get()==ButtonType.OK) {
    		selectedAlumno.get().faltasProperty().removeAll(faltaTable.getSelectionModel().getSelectedItems());
    	}
    }

	public final ObjectProperty<Grupo> grupoProperty() {
		return this.grupo;
	}
	

	public final Grupo getGrupo() {
		return this.grupoProperty().get();
	}
	

	public final void setGrupo(final Grupo grupo) {
		this.grupoProperty().set(grupo);
	}
	
    public GridPane getView() {
    	return this.view;
    }
}
