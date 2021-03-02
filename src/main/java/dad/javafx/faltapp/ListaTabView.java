package dad.javafx.faltapp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.javafx.faltapp.model.Alumno;
import dad.javafx.faltapp.model.Asistente;
import dad.javafx.faltapp.model.Falta;
import dad.javafx.faltapp.model.Grupo;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;

public class ListaTabView implements Initializable{
	
	@FXML
    private BorderPane view;

    @FXML
    private DatePicker datePicker;

    @FXML
    private CheckBox todasCH, primeraCH, segundaCH, terceraCH, cuartaCH, quintaCH, sextaCH;
    
    @FXML
    private Button faltasButton;

    @FXML
    private TableView<Asistente> asistenciaTable;

    @FXML
    private TableColumn<Asistente, String> nombreColumn;

    @FXML
    private TableColumn<Asistente, Boolean> faltaColumn;

    @FXML
    private TableColumn<Asistente, Boolean> retrasoColumn;

    private ObjectProperty<Grupo> grupo=new SimpleObjectProperty<>();
    private ListProperty<Asistente> listAsistentes=new SimpleListProperty<>(FXCollections.observableArrayList());
    
    public ListaTabView() throws IOException{
		FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/PasarListaTabView.fxml"));
		loader.setController(this);
		loader.load();
	}
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		asistenciaTable.itemsProperty().bind(listAsistentes);	

		nombreColumn.setCellValueFactory(v->v.getValue().alumnoProperty().get().nombreCompletoProperty());
		faltaColumn.setCellValueFactory(v->v.getValue().faltaProperty());
		retrasoColumn.setCellValueFactory(v->v.getValue().retrasoProperty());
		
		faltaColumn.setCellFactory(CheckBoxTableCell.forTableColumn(faltaColumn));
		retrasoColumn.setCellFactory(CheckBoxTableCell.forTableColumn(retrasoColumn));
		
		grupo.addListener((o,ov,nv)->{
			if(nv!=null) {
				listAsistentes.clear();
				for(Alumno alumno:nv.getAlumnos()) {
					Asistente asistente=new Asistente(alumno);
					listAsistentes.add(asistente);
				}
			} 
		});
		
		todasCH.selectedProperty().addListener((o,ov,nv)->{
			primeraCH.setDisable(todasCH.selectedProperty().get());
			segundaCH.setDisable(todasCH.selectedProperty().get());
			terceraCH.setDisable(todasCH.selectedProperty().get());
			cuartaCH.setDisable(todasCH.selectedProperty().get());
			quintaCH.setDisable(todasCH.selectedProperty().get());
			sextaCH.setDisable(todasCH.selectedProperty().get());
			
			primeraCH.selectedProperty().set(todasCH.selectedProperty().get());
			segundaCH.selectedProperty().set(todasCH.selectedProperty().get());
			terceraCH.selectedProperty().set(todasCH.selectedProperty().get());
			cuartaCH.selectedProperty().set(todasCH.selectedProperty().get());
			quintaCH.selectedProperty().set(todasCH.selectedProperty().get());
			sextaCH.selectedProperty().set(todasCH.selectedProperty().get());	
		});
		
		faltasButton.disableProperty().bind(Bindings.isNull(datePicker.valueProperty()).or(todasCH.selectedProperty().not()
						.and((primeraCH.selectedProperty().not().and(segundaCH.selectedProperty().not()).and(terceraCH.selectedProperty().not())
						.and(cuartaCH.selectedProperty().not()).and(quintaCH.selectedProperty().not()).and(sextaCH.selectedProperty().not())))));	
	}
	 
    @FXML
    private void onPonerFaltasButton(ActionEvent event) {
    	for(Asistente asistente:listAsistentes) {
    		
    		if(todasCH.selectedProperty().get()) {
    			for(int i=1;i<=6;i++) 
    				asistente.getAlumno().getFaltas().add(new Falta(datePicker.getValue(), i, !asistente.isFalta()));
    		}else {
    			if(primeraCH.selectedProperty().get())
    				asistente.getAlumno().getFaltas().add(new Falta(datePicker.getValue(), 1, !asistente.isFalta()));
    			if(segundaCH.selectedProperty().get())
    				asistente.getAlumno().getFaltas().add(new Falta(datePicker.getValue(), 2, !asistente.isFalta()));
    			if(terceraCH.selectedProperty().get())
    				asistente.getAlumno().getFaltas().add(new Falta(datePicker.getValue(), 3, !asistente.isFalta()));
    			if(cuartaCH.selectedProperty().get())
    				asistente.getAlumno().getFaltas().add(new Falta(datePicker.getValue(), 4, !asistente.isFalta()));
    			if(quintaCH.selectedProperty().get())
    				asistente.getAlumno().getFaltas().add(new Falta(datePicker.getValue(), 5, !asistente.isFalta()));
    			if(sextaCH.selectedProperty().get())
    				asistente.getAlumno().getFaltas().add(new Falta(datePicker.getValue(), 6, !asistente.isFalta()));
    		}
    		asistente.setFalta(false);
    		asistente.setRetraso(false);
    	}
    }
    
    public BorderPane getView() {
    	return this.view;
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
}