package de.uniba.dsg.jaxrs.db;

import de.uniba.dsg.jaxrs.controller.BeverageService;
import de.uniba.dsg.jaxrs.model.logic.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/*
 * Handles data storing and operations for the beverage Service (Insert/Delete/Update)
 */
public class DB {

	Object lock = new Object();

	private static final Logger logger = Logger.getLogger("DB.class");
	private final List<Bottle> bottles;
	private final List<Crate> crates;

	Gson gson;

	private final AtomicInteger beverageID = new AtomicInteger(1);

	public DB() {
		initGson();
		initShutdownHook();
        initDaemon();
		if(!Files.exists(Paths.get("../root/dev/BeverageService/data.json"))) {
			logger.info("new Json data File created");
			this.bottles = initBottles();
	        this.crates = initCases();
	        saveToJson();
		}else {
			logger.info("loading Json data file");
			bottles= retrieveBottles();
			crates= retrieveCrates();
		}
	}
	
    private List<Bottle> initBottles() {
        return new ArrayList<>(Arrays.asList(
                new Bottle(beverageID.getAndIncrement(), "Pils", 0.5, true, 4.8, 0.79, "Keesmann", 34),
                new Bottle(beverageID.getAndIncrement(), "Helles", 0.5, true, 4.9, 0.89, "Mahr", 17),
                new Bottle(beverageID.getAndIncrement(), "Boxbeutel", 0.75, true, 12.5, 5.79, "Divino", 11),
                new Bottle(beverageID.getAndIncrement(), "Tequila", 0.7, true, 40.0, 13.79, "Tequila Inc.", 5),
                new Bottle(beverageID.getAndIncrement(), "Gin", 0.5, true, 42.00, 11.79, "Hopfengarten", 3),
                new Bottle(beverageID.getAndIncrement(), "Export Edel", 0.5, true, 4.8, 0.59, "Oettinger", 66),
                new Bottle(beverageID.getAndIncrement(), "Premium Tafelwasser", 0.7, false, 0.0, 4.29, "Franken Brunnen", 12),
                new Bottle(beverageID.getAndIncrement(), "Wasser", 0.5, false, 0.0, 0.29, "Franken Brunnen", 57),
                new Bottle(beverageID.getAndIncrement(), "Spezi", 0.7, false, 0.0, 0.69, "Franken Brunnen", 42),
                new Bottle(beverageID.getAndIncrement(), "Grape Mix", 0.5, false, 0.0, 0.59, "Franken Brunnen", 12),
                new Bottle(beverageID.getAndIncrement(), "Still", 1.0, false, 0.0, 0.66, "Franken Brunnen", 34),
                new Bottle(beverageID.getAndIncrement(), "Cola", 1.5, false, 0.0, 1.79, "CCC", 69),
                new Bottle(beverageID.getAndIncrement(), "Cola Zero", 2.0, false, 0.0, 2.19, "CCC", 12),
                new Bottle(beverageID.getAndIncrement(), "Apple", 0.5, false, 0.0, 1.99, "Juice Factory", 25),
                new Bottle(beverageID.getAndIncrement(), "Orange", 0.5, false, 0.0, 1.99, "Juice Factory", 55),
                new Bottle(beverageID.getAndIncrement(), "Lime", 0.5, false, 0.0, 2.99, "Juice Factory", 8)
        ));
    }

    private List<Crate> initCases() {
        return new ArrayList<>(Arrays.asList(
                new Crate(beverageID.getAndIncrement(), this.bottles.get(0), 20, 14.99, 3),
                new Crate(beverageID.getAndIncrement(), this.bottles.get(1), 20, 15.99, 5),
                new Crate(beverageID.getAndIncrement(), this.bottles.get(2), 6, 30.00, 7),
                new Crate(beverageID.getAndIncrement(), this.bottles.get(7), 12, 1.99, 11),
                new Crate(beverageID.getAndIncrement(), this.bottles.get(8), 20, 11.99, 13),
                new Crate(beverageID.getAndIncrement(), this.bottles.get(11), 6, 10.99, 4),
                new Crate(beverageID.getAndIncrement(), this.bottles.get(12), 6, 11.99, 5),
                new Crate(beverageID.getAndIncrement(), this.bottles.get(13), 20, 35.00, 7),
                new Crate(beverageID.getAndIncrement(), this.bottles.get(14), 12, 20.00, 9)
        ));
    }



	private List<Beverage> getAllBeverages() {
		List<Beverage> beverages = new ArrayList<Beverage>();
		beverages.addAll(bottles);
		beverages.addAll(crates);
		return beverages;
	}

	public Bottle getBottleById(int id) {
		return bottles.stream().filter(bottle -> bottle.getId() == id).findFirst().orElse(null);
	}

	public Crate getCrateById(int id) {
		return crates.stream().filter(crates -> crates.getId() == id).findFirst().orElse(null);
	}

	/*
	 * returns sorted list of Beverages. First Bottle, then Crates
	 */
	public List<Beverage> getBeverages() {
		List<Beverage> beverages = getAllBeverages();
		beverages.sort(new Comparator<Beverage>() {

			@Override
			public int compare(Beverage o1, Beverage o2) {
				if (o1.getClass() == o2.getClass()) {
					return 0;
				} else if (o1 instanceof Bottle) {
					return 1;
				} else {
					return -1;
				}
			}

		});

		return beverages;
	}

	public void addBottle(Bottle bottle) {
		bottles.add(bottle);

	}

	public void addCrate(Crate crate) {
		crates.add(crate);
	}

	public Bottle updateBottle(int id, Bottle bottle) {
		Bottle bottleToReplace = bottles.stream().filter(b -> b.getId() == id).findFirst().get();

		bottles.set(bottles.indexOf(bottleToReplace), bottle);
		return bottle;
	}

	public Crate updateCrate(int id, Crate crate) {
		Crate crateToReplace = crates.stream().filter(c -> c.getId() == id).findFirst().get();
		crates.set(crates.indexOf(crateToReplace), crate);
		return crate;
	}

	public boolean deleteBottle(int id) {
		Optional<Bottle> bottleToDelete = bottles.stream().filter(bottle -> bottle.getId() == id).findFirst();
		if (bottleToDelete.isPresent()) {
			return bottles.remove(bottleToDelete.get());
		} else {
			return false;
		}
	}

	public boolean deleteCrate(int id) {
		Optional<Crate> crateToDelete = crates.stream().filter(crate -> crate.getId() == id).findFirst();
		if (crateToDelete.isPresent()) {
			return crates.remove(crateToDelete.get());
		} else {
			return false;
		}
	}

	public int getAndIncrementBeverageID() {
		return beverageID.getAndIncrement();
	}



	public List<Beverage> getBottles() {
		return new ArrayList<>(bottles);
	}

	public List<Beverage> getCrates() {
		return new ArrayList<>(crates);
	}

	public void saveToJson() {
		synchronized (lock) {
			try (Writer writer = new FileWriter("../root/dev/BeverageService/data.json")) {
				gson.toJson(getAllBeverages(), writer);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private List<Bottle> retrieveBottles() {
		return retrieveBeverages().parallelStream().//
				filter(b -> b.getClass().getSimpleName().equals(Bottle.class.getSimpleName()))//
				.map(b -> {
					return (Bottle) b;
				})//
				.collect(Collectors.toList());
	}

	private List<Crate> retrieveCrates() {
		return retrieveBeverages().parallelStream()//
				.filter(b -> b.getClass().getSimpleName().equals(Crate.class.getSimpleName()))//
				.map(b -> {
					return (Crate) b;
				})//
				.collect(Collectors.toList());

	}

	/*
	 * loads json from given Path an transforms it with Discriminator Field in beverage List
	 */
	private List<Beverage> retrieveBeverages() {
		TypeToken<List<Beverage>> token = new TypeToken<List<Beverage>>() {
		};
		String data = "";
		try {
			data = new String(Files.readAllBytes(Paths.get("../root/dev/BeverageService/data.json")));
		} catch (IOException e) {
			logger.warning("Loading JSON Data file failed");
		}
		List<Beverage> beverages = gson.fromJson(data, token.getType());
		return beverages;
	}
	
	private void initGson() {

		RuntimeTypeAdapterFactory<Beverage> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
				.of(Beverage.class, "className").registerSubtype(Bottle.class, Bottle.class.getSimpleName())
				.registerSubtype(Crate.class, Crate.class.getSimpleName());
		gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();

	}

	private void initDaemon() {
		Timer timer = new Timer();
		timer.schedule(new PersistData(), 0, 30000);
		;
	}

	/*
	 * I think retrieving JSON-File for every request needs to many resources JSON
	 * is loaded once when first request comes --> every new request will be cached
	 * if docker stop (SIGTERM) --> Arraylists are going to be persisted in JSON
	 */
	private static void initShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				logger.info("Shutdown Requested - Persist Data");
				BeverageService.instance.persistData();
				logger.info("Data Persisted Succesfully");
			}
		});
	}

	/*
	 * Persists Data every n Seconds to avoid a total lost of cached data, when a
	 * SIGKILL is sent
	 */
	class PersistData extends TimerTask {
		public void run() {
			logger.info("Persist Data");
			BeverageService.instance.persistData();
		}
	}
}
