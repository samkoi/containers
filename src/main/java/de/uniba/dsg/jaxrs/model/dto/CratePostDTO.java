package de.uniba.dsg.jaxrs.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.uniba.dsg.jaxrs.model.logic.Beverage;
import de.uniba.dsg.jaxrs.model.logic.Bottle;
import de.uniba.dsg.jaxrs.model.logic.Crate;

/*
 * this class is used to get values from swagger which are used to update/ Create a crate
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "crate")
@XmlType(propOrder = { "id", "price", "inStock", "bottleId", "noOfBottles" })
public class CratePostDTO {

	@XmlElement(required = true)
	private int id;
	@XmlElement(required = true)
	private double price;
	@XmlElement(required = true)
	private int inStock;
	// Crate Attribute
	@XmlElement(required = true)
	private Integer bottleId;
	@XmlElement
	private int noOfBottles;

	public CratePostDTO() {

	}



	public CratePostDTO(int id, double price, int inStock, Integer bottleId, int noOfBottles) {
		super();
		this.id = id;
		this.price = price;
		this.inStock = inStock;
		this.bottleId = bottleId;
		this.noOfBottles = noOfBottles;
	}



	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getInStock() {
		return inStock;
	}

	public void setInStock(int inStock) {
		this.inStock = inStock;
	}

	public int getNoOfBottles() {
		return noOfBottles;
	}

	public void setNoOfBottles(int noOfBottles) {
		this.noOfBottles = noOfBottles;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public Integer getBottleId() {
		return bottleId;
	}



	public void setBottleId(Integer bottleId) {
		this.bottleId = bottleId;
	}
	
	public Crate unmarshall(Bottle bottle) {
		return new Crate(this.id, bottle, this.noOfBottles, this.price, this.inStock);
	}



	@Override
	public String toString() {
		return "CratePostDTO [id=" + id + ", price=" + price + ", inStock=" + inStock + ", bottleId=" + bottleId
				+ ", noOfBottles=" + noOfBottles + "]";
	}
	
	


	
	


}
