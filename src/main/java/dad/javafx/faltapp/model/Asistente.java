package dad.javafx.faltapp.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;

public class Asistente {
	
	private ReadOnlyObjectWrapper<Alumno> alumno = new ReadOnlyObjectWrapper<>();
	private BooleanProperty falta = new SimpleBooleanProperty(false);
	private BooleanProperty retraso = new SimpleBooleanProperty(false);

	public Asistente(Alumno alumno) {
		this.alumno.set(alumno);
		this.falta.addListener((observable, oldValue, newValue) -> {
			if (newValue) this.retraso.set(false);
		});
		this.retraso.addListener((observable, oldValue, newValue) -> {
			if (newValue) this.falta.set(false);
		});
	}	

	public final ReadOnlyObjectProperty<Alumno> alumnoProperty() {
		return this.alumno.getReadOnlyProperty();
	}

	public final Alumno getAlumno() {
		return this.alumnoProperty().get();
	}

	public final BooleanProperty faltaProperty() {
		return this.falta;
	}

	public final boolean isFalta() {
		return this.faltaProperty().get();
	}

	public final void setFalta(final boolean falta) {
		this.faltaProperty().set(falta);
	}

	public final BooleanProperty retrasoProperty() {
		return this.retraso;
	}

	public final boolean isRetraso() {
		return this.retrasoProperty().get();
	}

	public final void setRetraso(final boolean retraso) {
		this.retrasoProperty().set(retraso);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof Asistente) {
			Asistente a = (Asistente) obj;
			return getAlumno().equals(a.getAlumno());
		}
		if (obj instanceof Alumno) {
			Alumno a = (Alumno) obj;
			return getAlumno().equals(a);			
		}
		return super.equals(obj);
	}

}
