package dad.javafx.faltapp.model;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
	
	public LocalDate unmarshal(String value) throws Exception {
		return LocalDate.parse(value);
	}

	public String marshal(LocalDate value) throws Exception {
		return value.toString();
	}
	
}