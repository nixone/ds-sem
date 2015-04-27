package sk.nixone.ds.agent.sem3.model;

import java.util.HashMap;
import java.util.Iterator;

public class VehicleTypes extends HashMap<String, VehicleType> implements Iterable<VehicleType> {
	
	public final VehicleType BUS_1;
	public final VehicleType BUS_2;
	public final VehicleType MICROBUS;
	
	public VehicleTypes(VehicleType bus1, VehicleType bus2, VehicleType microBus) {
		BUS_1 = bus1;
		BUS_2 = bus2;
		MICROBUS = microBus;
		
		add(bus1);
		add(bus2);
		add(microBus);
	}
	
	public void add(VehicleType vehicleType) {
		put(vehicleType.getName(), vehicleType);
	}

	@Override
	public Iterator<VehicleType> iterator() {
		return values().iterator();
	}
}
