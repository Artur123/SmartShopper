package at.jku.smartshopper.listitems;

import java.io.Serializable;

public class Articletest implements Serializable {


	private String name = "";
	private double value = 0;
	private int count;
	
	public Articletest(String name, double value) {
		this.setName(name);
		this.setValue(value);
		this.setCount(1);
	}
	
	public String getName() {
		return name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count){
		this.count = count;
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