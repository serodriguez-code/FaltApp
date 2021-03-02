package dad.javafx.faltapp.model;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

@XmlType
public class Falta {
	
	private IntegerProperty hora = new SimpleIntegerProperty();
	private BooleanProperty retraso = new SimpleBooleanProperty();
	private ObjectProperty<LocalDate> fecha = new SimpleObjectProperty<>();

	public Falta() {}
	
	public Falta(LocalDate fecha, int hora, boolean retraso) {
		this();
		setHora(hora);
		setRetraso(retraso);
		setFecha(fecha);
	}

	public final IntegerProperty horaProperty() {
		return this.hora;
	}

	@XmlAttribute
	public final int getHora() {
		return this.horaProperty().get();
	}

	public final void setHora(final int hora) {
		this.horaProperty().set(hora);
	}

	public final BooleanProperty retrasoProperty() {
		return this.retraso;
	}

	@XmlAttribute
	public final boolean isRetraso() {
		return this.retrasoProperty().get();
	}

	public final void setRetraso(final boolean retraso) {
		this.retrasoProperty().set(retraso);
	}

	public final ObjectProperty<LocalDate> fechaProperty() {
		return this.fecha;
	}

	@XmlAttribute
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public final LocalDate getFecha() {
		return this.fechaProperty().get();
	}

	public final void setFecha(final LocalDate fecha) {
		this.fechaProperty().set(fecha);
	}

	@Override
	public String toString() {
		return getFecha() + " a " + getHora() + "º hora " + (isRetraso() ? " (retraso)" : "");
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof Falta) {
			Falta f = (Falta) obj;
			return getFecha().equals(f.getFecha()) && getHora() == f.getHora();
		}
		return false;
	}
}