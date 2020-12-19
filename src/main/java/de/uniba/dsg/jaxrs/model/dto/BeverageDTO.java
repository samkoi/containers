package de.uniba.dsg.jaxrs.model.dto;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.uniba.dsg.jaxrs.model.logic.Beverage;
import de.uniba.dsg.jaxrs.model.logic.Bottle;
import de.uniba.dsg.jaxrs.model.logic.Crate;

/*
 * this class is used to send back Beverage Objects to client
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "bottle")
@XmlType(propOrder = { "id", "price", "inStock", "name", "volume", "isAlcoholic", "volumePercent", "supplier", "bottleId", "noOfBottles", "href"})
public class BeverageDTO {

	// Common Attributes --> Required
	@XmlElement
	private int id;
	@XmlElement
	private double price;
	@XmlElement
	private int inStock;
	
	// Bottle Attributes
	@XmlElement
	private String name;
	@XmlElement
	private Double volume;
	@XmlElement
	private Boolean isAlcoholic;
	@XmlElement
	private Double volumePercent;
	@XmlElement
	private String supplier;
	
	//crate Attributes
	@XmlElement(required = true)
    private Integer bottleId;
	@XmlElement
    private int noOfBottles;

	@XmlElement
	private URI href;
	
	public BeverageDTO() {
		
	}
	
	

	// Bottle
	public BeverageDTO(int id, double price, int inStock, String name, Double volume, Boolean isAlcoholic,
			Double volumePercent, String supplier,URI href) {
		this.id = id;
		this.price = price;
		this.inStock = inStock;
		this.name = name;
		this.volume = volume;
		this.isAlcoholic = isAlcoholic;
		this.volumePercent = volumePercent;
		this.supplier = supplier;
		this.href = href;
	}
	
	// Crate
	public BeverageDTO(int id, double price, int inStock, String name, Double volume, Boolean isAlcoholic,
			Double volumePercent, String supplier, Integer bottleId, int noOfBottles, URI href) {
		this.id = id;
		this.price = price;
		this.inStock = inStock;
		this.name = name;
		this.volume = volume;
		this.isAlcoholic = isAlcoholic;
		this.volumePercent = volumePercent;
		this.supplier = supplier;
		this.bottleId = bottleId;
		this.noOfBottles = noOfBottles;
		this.href = href;
	}





	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
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



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public Double getVolume() {
		return volume;
	}



	public void setVolume(Double volume) {
		this.volume = volume;
	}



	public Boolean getIsAlcoholic() {
		return isAlcoholic;
	}



	public void setIsAlcoholic(Boolean isAlcoholic) {
		this.isAlcoholic = isAlcoholic;
	}



	public Double getVolumePercent() {
		return volumePercent;
	}



	public void setVolumePercent(Double volumePercent) {
		this.volumePercent = volumePercent;
	}



	public String getSupplier() {
		return supplier;
	}



	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}





	public Integer getBottleId() {
		return this.bottleId;
	}



	public void setBottleId(Integer bottleId) {
		this.bottleId = bottleId.intValue();
	}



	public int getNoOfBottles() {
		return noOfBottles;
	}



	public void setNoOfBottles(int noOfBottles) {
		this.noOfBottles = noOfBottles;
	}



	public URI getHref() {
		return href;
	}



	public void setHref(URI href) {
		this.href = href;
	}



	public static List<BeverageDTO> marshall(final List<Beverage> beverageList, final URI baseUri) {
		final ArrayList<BeverageDTO> beverages = new ArrayList<>();
		for (final Beverage beverage : beverageList) {
			if (beverage.getClass().getName().equals(Bottle.class.getName())) {
				Bottle bottle = (Bottle) beverage;
				beverages.add(new BeverageDTO(bottle.getId(), bottle.getPrice(), bottle.getInStock(), bottle.getName(), bottle.getVolume(), bottle.isAlcoholic(), bottle.getVolumePercent(), bottle.getSupplier(), baseUri));
			}else {
				Crate crate = (Crate) beverage;
				beverages.add(new BeverageDTO(crate.getId(),//
						crate.getPrice(),//
						crate.getInStock(),//
						crate.getBottle().getName(),//
						crate.getBottle().getVolume(),//
						crate.isAlcoholic(),//
						crate.getVolumePercent(),//
						crate.getBottle().getSupplier(),//
						crate.getBottle().getId(),//
						crate.getNoOfBottles(),//
						baseUri));
			}
			
		}
		return beverages;
	}



	@Override
	public String toString() {
		return "BeverageDTO [id=" + id + ", price=" + price + ", inStock=" + inStock + ", name=" + name + ", volume="
				+ volume + ", isAlcoholic=" + isAlcoholic + ", volumePercent=" + volumePercent + ", supplier="
				+ supplier + ", bottleId=" + bottleId + ", noOfBottles=" + noOfBottles + ", href=" + href + "]";
	}
	

	
	

	
	

	
	

}
