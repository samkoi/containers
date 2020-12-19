package de.uniba.dsg.jaxrs.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.uniba.dsg.jaxrs.db.DB;
import de.uniba.dsg.jaxrs.model.logic.Beverage;
import de.uniba.dsg.jaxrs.model.logic.Bottle;
import de.uniba.dsg.jaxrs.model.logic.Crate;

import de.uniba.dsg.jaxrs.resources.FilterFunction;

/**
 * delegates requests to the database 
 * Database acces is synchronized --> not the most performant solution but sufficient for this simple task
 */
public class BeverageService {
	
	

	public static final BeverageService instance = new BeverageService();

	private final DB database;

	public BeverageService() {
		this.database = new DB();
	}

	/*
	 * Get
	 */

	public Bottle getBottleById(final int bottleId) {
		synchronized(this) {
			return database.getBottleById(bottleId);
		}
	}

	public Crate getCrateById(final int crateId) {
		synchronized(this) {
		return database.getCrateById(crateId);
		}
	}



	public List<Beverage> getBeverages(String searchTerm, FilterFunction filterFunction) {
		List<Beverage> beverages;
		synchronized(this) {
		beverages = database.getBeverages();
		}
		if (searchTerm != null && !searchTerm.isEmpty()) {
			beverages = beverages.stream()
					.filter(beverage -> beverage.toString().toUpperCase().contains(searchTerm.toUpperCase()))
					.collect(Collectors.toList());
		}
		if (filterFunction == null) {
			return beverages;
		}

		switch (filterFunction) {
		case Alcoholic:
			return beverageFilterAlcoholic(beverages);
		case MaxPrice:
			return beverageFilterPrice(beverages, true);
		case MinPrice:
			return beverageFilterPrice(beverages, false);
		case VolumePercent:
			return beverageFilterVolume(beverages);
		default:
			return beverages;
		}

	}

	public void addBottle(Bottle bottle) {
		synchronized(this) {
		database.addBottle(bottle);
		}
	}

	public void addCrate(Crate crate) {
		synchronized(this) {
		database.addCrate(crate);
		}

	}

	public Bottle updateBottle(int id, Beverage bottle) {
		synchronized(this) {
		return database.updateBottle(id, (Bottle) bottle);
		}
	}

	public Crate updateCrate(int id, Beverage crate) {
		synchronized(this) {
		return database.updateCrate(id, (Crate) crate);
		}
	}

	public boolean deleteBottle(int id) {
		synchronized(this) {
		return database.deleteBottle(id);
		}
	}

	public boolean deleteCrate(int id) {
		synchronized(this) {
		return database.deleteCrate(id);
		}
	}


	public List<String> getbevTypes() {
		List<String> list = new ArrayList<String>();
		list.add("Bottle");
		list.add("Crate");
		return list;
	}

	public int getAndIncrementBeverageID() {
		synchronized(this) {
		return database.getAndIncrementBeverageID();
		}
	}





	private List<Beverage> beverageFilterVolume(List<Beverage> beverages) {
		beverages.sort(Comparator.comparingDouble(Beverage::getVolumePercent).reversed());
		return beverages;
	}

	private List<Beverage> beverageFilterPrice(List<Beverage> beverages, boolean max) {
		if (max) {
			beverages.sort(Comparator.comparingDouble(Beverage::getPrice).reversed());
			return beverages;
		} else {
			beverages.sort(Comparator.comparingDouble(Beverage::getPrice));
			return beverages;
		}
	}

	private List<Beverage> beverageFilterAlcoholic(List<Beverage> beverages) {
		beverages = beverages.stream().filter(Beverage::isAlcoholic).collect(Collectors.toList());
		return beverages;
	}

    public List<Beverage> getAllBottles() {
    	synchronized(this) {
		return database.getBottles();
    	}
    }

	public List<Beverage> getAllCrates() {
		synchronized(this) {
		return database.getCrates();
		}
	}

	public void persistData() {
		synchronized(this) {
		database.saveToJson();
		}
	}
}
