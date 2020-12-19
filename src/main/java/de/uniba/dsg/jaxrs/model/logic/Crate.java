package de.uniba.dsg.jaxrs.model.logic;


/*
 * Normal Crate Object
 */
public class Crate implements Beverage{
	
	// DISCRIMINATOR FIELD
    private final String className = getClass().getSimpleName();
    
    private int id;
    private Bottle bottle;
    private int noOfBottles;
    private double price;
    private int inStock;
    
    

    public Crate() {

    }

	public Crate(int id, Bottle bottle, int noOfBottles, double price, int inStock) {
        this.id = id;
        this.bottle = bottle;
        this.noOfBottles = noOfBottles;
        this.price = price;
        this.inStock = inStock;
    }

    public int getId() {
        return id;
    }

    public Bottle getBottle() {
        return bottle;
    }

    public void setBottle(Bottle bottle) {
        this.bottle = bottle;
    }

    public int getNoOfBottles() {
        return noOfBottles;
    }

    public void setNoOfBottles(int noOfBottles) {
        this.noOfBottles = noOfBottles;
    }

    @Override
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

    @Override
    public String toString() {
        return "Crate{" +
                "id=" + id +
                ", bottle=" + bottle +
                ", noOfBottles=" + noOfBottles +
                ", price=" + price +
                ", inStock=" + inStock +
                '}';
    }

	@Override
	public boolean isAlcoholic() {
		return this.bottle.isAlcoholic();
	}

	@Override
	public double getVolumePercent() {
		return this.bottle.getVolumePercent();
	}

	@Override
	public String getClassName() {
		return className;
	}


}
