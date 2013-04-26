package at.jku.smartshopper.listitems;

import java.io.Serializable;

public class Articletest implements Serializable {


	private String name = "";
	private double value = 0;

	public Articletest(String name, double value) {
		this.setName(name);
		this.setValue(value);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}