package dad.javafx.faltapp.model;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@XmlType
@XmlRootElement
public class Grupo {
	private StringProperty modulo = new SimpleStringProperty();
	private IntegerProperty curso = new SimpleIntegerProperty(1);
	private StringProperty ciclo = new SimpleStringProperty();
	private ListProperty<Alumno> alumnos = new SimpleListProperty<>(FXCollections.observableArrayList());

	public final StringProperty moduloProperty() {
		return this.modulo;
	}

	@XmlAttribute
	public final String getModulo() {
		return this.moduloProperty().get();
	}

	public final void setModulo(final String modulo) {
		this.moduloProperty().set(modulo);
	}

	public final IntegerProperty cursoProperty() {
		return this.curso;
	}

	@XmlAttribute
	public final int getCurso() {
		return this.cursoProperty().get();
	}

	public final void setCurso(final int curso) {
		this.cursoProperty().set(curso);
	}

	public final StringProperty cicloProperty() {
		return this.ciclo;
	}

	@XmlAttribute
	public final String getCiclo() {
		return this.cicloProperty().get();
	}

	public final void setCiclo(final String ciclo) {
		this.cicloProperty().set(ciclo);
	}

	public final ListProperty<Alumno> alumnosProperty() {
		return alumnos;
	}

	@XmlElement(name = "alumno")
	@XmlElementWrapper(name = "alumnos")
	public final ObservableList<Alumno> getAlumnos() {
		return this.alumnosProperty().get();
	}

	public final void setAlumnos(final ObservableList<Alumno> alumnos) {
		this.alumnosProperty().set(alumnos);
	}
	 
	@Override
	public String toString() {
		return getCurso() + "º de " + getCiclo();
	}

	public void guardar(File fichero) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Grupo.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(this, fichero);
	}

	public static Grupo leer(File fichero) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Grupo.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (Grupo) unmarshaller.unmarshal(fichero);
	}
}
