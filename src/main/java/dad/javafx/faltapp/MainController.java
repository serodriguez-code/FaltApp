package dad.javafx.faltapp;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import dad.javafx.faltapp.model.Grupo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable {

    @FXML
    private BorderPane view;

    @FXML
    private Tab groupTab,listTab;
    
    private GrupoTabView grupoTabView=new GrupoTabView();
    private ListaTabView listaTabView=new ListaTabView();
    public static MainController mainController;
    
    private File currentGrupoFile;
    private ObjectProperty<Grupo> grupo=new SimpleObjectProperty<>();
    private Stage stage;
    
	public MainController() throws IOException{
        MainController.mainController = this;
        
		FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
		loader.setController(this);
		loader.load();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		groupTab.setContent(grupoTabView.getView());
		listTab.setContent(listaTabView.getView());
		
		grupo.addListener((o,ov,nv)->{
			grupoTabView.setGrupo(grupo.get());
			listaTabView.setGrupo(grupo.get());
		});	
		grupo.set(new Grupo());
	} 
    
    @FXML 
    private void onNewButton(ActionEvent event) {
    	newAction();
    }

    @FXML
    private void onNewMenuBar(ActionEvent event) {
    	newAction();
    }
    
    private void newAction(){
    	
    	String alertTitle="Nuevo grupo";
    	String alertHeader="Se va a crear un nuevo grupo. \nSi hay datos sin guardar se perderán.";
    	String alertContent="¿Desea continuar?";
    	
    	Optional<ButtonType>result=alertWarningCancelB(stage,alertTitle,alertHeader,alertContent).showAndWait();

    	if(result.get()==ButtonType.OK) {
    		grupo.set(new Grupo());
    	}	
    }

    @FXML
    private void onOpenButton(ActionEvent event) {
    	openAction();
    }

    @FXML
    private void onOpenMenuBar(ActionEvent event) {
    	openAction();
    }
    
    private void openAction() {
    	
    	stage=(Stage)view.getScene().getWindow();
    	
    	String alertTitle="Abrir grupo";
    	String alertHeader="Se va a abrir un grupo desde fichero. \nSi hay datos sin guardar se perderán.";
    	String alertContent="¿Desea continuar?";
    	
    	Optional<ButtonType>result=alertWarningCancelB(stage,alertTitle,alertHeader,alertContent).showAndWait();
    	
    	if(result.get()==ButtonType.OK) {
	    	currentGrupoFile=null;
	    	FileChooser chooser=new FileChooser();
	    	chooser.setTitle("Abrir un curriculum");
	    	chooser.getExtensionFilters().add(new ExtensionFilter("Faltas (*.faltas)","*.faltas"));
	    	chooser.getExtensionFilters().add(new ExtensionFilter("Todos los archivos","*.*"));
	    	currentGrupoFile=chooser.showOpenDialog(stage);
	    	
	    	if(currentGrupoFile!=null) {
		    	try { 
					grupo.set(Grupo.leer(currentGrupoFile));
					String alertInformationContent="Se ha cargado el grupo '"+grupo.get().getCiclo()+"' con éxito";
					alertInformation(stage, "Abrir grupo" ,"Grupo cargado", alertInformationContent).showAndWait();
				} catch (JAXBException e) {
					alertError(stage,"error","Error al cargar el grupo",e.getMessage()).showAndWait();
				}
		    }
    	}
    }
    
    @FXML
    private void onSaveButton(ActionEvent event) {
    	saveAction();
    }

    @FXML
    private void onSaveMenuBar(ActionEvent event) {
    	saveAction();
    }
    
    private void saveAction(){
    	
    	currentGrupoFile=null;
    	
    	Stage stage=(Stage)view.getScene().getWindow();
    	FileChooser chooser=new FileChooser();
    	chooser.setTitle("Guardar como");
    	chooser.getExtensionFilters().add(new ExtensionFilter("Faltas (*.faltas)", "*.faltas"));
    	chooser.getExtensionFilters().add(new ExtensionFilter("Todos los archivos","*.*"));
    	currentGrupoFile=chooser.showSaveDialog(stage);
    	
    	if(currentGrupoFile!=null) {
    		try {
				grupo.get().guardar(currentGrupoFile);
				String alertInformationContent="Se ha guardado el grupo '"+grupo.get().getCiclo()+"' con éxito";
				alertInformation(stage, "Guardar grupo", "Grupo guardado", alertInformationContent).showAndWait();
			} catch (JAXBException e) {
				alertError(stage, "Error", "Error al guardar el grupo", 
						"Se ha producido el siguiente error al guardar el grupo:\n"+e.getMessage()).showAndWait();
			}
    	}

    }
    
    @FXML
    private void onExitButton(ActionEvent event) {
    	exitAction(event);
    }

    @FXML
    private void onExitMenuBar(ActionEvent event) {
    	exitAction(event);
    }

    public void exitAction(Event event) {
    	Stage stage=(Stage)view.getScene().getWindow();
    	Optional<ButtonType>result=alertConfirmation(stage,"Salir", "Va a salir de la aplicación y puede haber cambios sin guardar.",
    								"¿Seguro que desea salir?").showAndWait();
    	
    	if(result.get()==ButtonType.OK)
    		stage.close();
    	else
    		event.consume();
    }
    
    /**
     * Returns an alert of AlertType.Confirmation 
     * @param stage Stage
     * @param title String
     * @param headerText String
     * @param contentText String
     * @return Alert alert
     */
    public Alert alertConfirmation(Stage stage,String title, String headerText,String contentText) {
    	Alert alert=new Alert(AlertType.CONFIRMATION);
    	alert.setTitle(title);
    	alert.setHeaderText(headerText);
    	alert.setContentText(contentText);
    	alert.initModality(Modality.APPLICATION_MODAL);
    	alert.initOwner(stage);
    	return alert;
    }
    
    /**
     * Returns an alert of AlertType.Warning with ButtonType.Cancel
     * @param stage Stage
     * @param title String
     * @param headerText String
     * @param contentText String
     * @return Alert alert
     */
    public Alert alertWarningCancelB(Stage stage,String title, String headerText,String contentText) {
    	
    	Alert alert=new Alert(AlertType.WARNING);
    	alert.setTitle(title);
    	alert.setHeaderText(headerText);
    	alert.setContentText(contentText);
    	alert.initModality(Modality.APPLICATION_MODAL);
    	alert.initOwner(stage);
    	alert.getButtonTypes().add(ButtonType.CANCEL);
    	return alert;
    }

    /**
     * Returns an alert of AlertType information
     * @param stage Stage
     * @param title String
     * @param headerText String
     * @param contentText String
     * @return Alert alert
     */
    public Alert alertInformation(Stage stage, String title, String headerText, String contentText) {
    	
    	Alert alert=new Alert(AlertType.INFORMATION);
    	alert.setTitle(title);
    	alert.setHeaderText(headerText);
    	alert.setContentText(contentText);
    	alert.initModality(Modality.APPLICATION_MODAL);
    	alert.initOwner(stage);
    	return alert;
    }
    
    /**
     * Returns an alert of AlertType.Error
     * @param stage Stage
     * @param title String
     * @param headerText String
     * @param error String
     * @return Alert alert;
     */
    public Alert alertError(Stage stage, String title, String headerText, String error) {
    	
    	Alert alert=new Alert(AlertType.ERROR);
    	alert.setTitle(title);
    	alert.setHeaderText(headerText);
    	alert.setContentText(error);
    	alert.initModality(Modality.APPLICATION_MODAL);
    	alert.initOwner(stage);
    	return alert;
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
