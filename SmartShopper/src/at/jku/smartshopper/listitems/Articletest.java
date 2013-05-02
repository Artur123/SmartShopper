package at.jku.smartshopper.listitems;

import java.io.Serializable;

public class Articletest implements Serializable {


	private String name = "";
	private double value = 0;
	private int quantity;
	
	public Articletest(String name, double value) {
		this.setName(name);
		this.setValue(value);
		this.setQuantity(2);
	}
	
	public String getName() {
		return name;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int count){
		this.quantity = count;
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