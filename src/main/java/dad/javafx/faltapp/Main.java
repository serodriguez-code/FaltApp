package dad.javafx.faltapp;

import java.io.File;
import java.time.LocalDate;

import javax.xml.bind.JAXBException;

import dad.javafx.faltapp.model.Alumno;
import dad.javafx.faltapp.model.Falta;
import dad.javafx.faltapp.model.Grupo;

public class Main {

	public static void main(String[] args) throws JAXBException {
		
		Alumno a1 = new Alumno();
		a1.setNombre("Chuck");
		a1.setApellidos("Norris");
		a1.setCial("123456789A");
		a1.setFechaNacimiento(LocalDate.of(1940, 3, 10));
		a1.getFaltas().add(new Falta(LocalDate.now(), 2, false));
		a1.getFaltas().add(new Falta(LocalDate.now().minusDays(4), 5, true));

		Alumno a2 = new Alumno();
		a2.setNombre("David");
		a2.setApellidos("Carradine");
		a2.setCial("987654321Z");
		a2.setFechaNacimiento(LocalDate.of(1936, 12, 8));
		a2.getFaltas().add(new Falta(LocalDate.now().minusMonths(2), 4, true));
		a2.getFaltas().add(new Falta(LocalDate.now().minusDays(4), 4, false));

		Grupo dam2 = new Grupo(); 
		dam2.setCiclo("Desarrollo de Aplicaciones Multiplataforma");
		dam2.setCurso(2);
		dam2.setModulo("Desarrollo de Interfaces");
		dam2.getAlumnos().addAll(a1, a2);
		
		dam2.guardar(new File("dam2.faltas"));
		
		App.main(args); 
		
	}

}
