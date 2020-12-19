package de.uniba.dsg.jaxrs.model.logic;

public interface Beverage {
	
	String className ="";
	
	String getClassName();
	
	double getPrice();
	
	double getVolumePercent();
	
	boolean isAlcoholic();
	
}
