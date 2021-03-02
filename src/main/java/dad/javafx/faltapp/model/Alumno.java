package dad.javafx.faltapp.model;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@XmlType
public class Alumno {
	private StringProperty cial = new SimpleStringProperty();
	private StringProperty nombre = new SimpleStringProperty();
	private StringProperty apellidos = new SimpleStringProperty();
	private ReadOnlyStringWrapper nombreCompleto = new ReadOnlyStringWrapper();
	private ObjectProperty<LocalDate> fechaNacimiento = new SimpleObjectProperty<>();
	private ListProperty<Falta> faltas = new SimpleListProperty<>(FXCollections.observableArrayList());

	public Alumno() {
		nombreCompleto.bind(apellidos.concat(", ").concat(nombre));
	}

	public Alumno(String cial, String nombre, String apellidos, LocalDate fechaNacimiento) {
		this();
		setCial(cial);
		setNombre(nombre);
		setApellidos(apellidos);
		setFechaNacimiento(fechaNacimiento);
	}

	public final StringProperty cialProperty() {
		return this.cial;
	}

	@XmlAttribute
	public final String getCial() {
		return this.cialProperty().get();
	}

	public final void setCial(final String cial) {
		this.cialProperty().set(cial);
	}

	public final StringProperty nombreProperty() {
		return this.nombre;
	}

	@XmlAttribute
	public final String getNombre() {
		return this.nombreProperty().get();
	}

	public final void setNombre(final String nombre) {
		this.nombreProperty().set(nombre);
	}

	public final StringProperty apellidosProperty() {
		return this.apellidos;
	}

	@XmlAttribute
	public final String getApellidos() {
		return this.apellidosProperty().get();
	}

	public final void setApellidos(final String apellidos) {
		this.apellidosProperty().set(apellidos);
	}
	
	public final ObjectProperty<LocalDate> fechaNacimientoProperty() {
		return this.fechaNacimiento;
	}

	@XmlAttribute
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public final LocalDate getFechaNacimiento() {
		return this.fechaNacimientoProperty().get();
	}

	public final void setFechaNacimiento(final LocalDate fechaNacimiento) {
		this.fechaNacimientoProperty().set(fechaNacimiento);
	}

	public final ListProperty<Falta> faltasProperty() {
		return faltas;
	}

	@XmlElement(name = "falta")
	@XmlElementWrapper(name = "faltas")
	public final ObservableList<Falta> getFaltas() {
		return this.faltasProperty().get();
	}

	public final void setFaltas(final ObservableList<Falta> faltas) {
		this.faltasProperty().set(faltas);
	}

	public final ReadOnlyStringProperty nombreCompletoProperty() {
		return this.nombreCompleto.getReadOnlyProperty();
	}

	@XmlTransient
	public final String getNombreCompleto() {
		return this.nombreCompletoProperty().get();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof Alumno) {
			Alumno a = (Alumno) obj;
			return getCial().equalsIgnoreCase(a.getCial());
		}
		return false;
	}

	@Override
	public String toString() {
		return getNombreCompleto();
	}
	
}
