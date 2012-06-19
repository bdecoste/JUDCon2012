package org.switchyard.quickstarts.bpm.service;

import java.util.HashMap;
import java.util.Map;

import org.switchyard.component.bean.Service;
import org.switchyard.quickstarts.bpm.service.data.Order;

@Service(Inventory.class) 
public class InventoryBean implements org.switchyard.quickstarts.bpm.service.Inventory {

    private final Map<String, Integer> inventory = new HashMap<String, Integer>();
    
    public InventoryBean() {
        inventory.put("cowbell", 100);
    }
    
    @Override
    public boolean checkAvailability(Order order) {
    	System.out.println("InventoryBean checking availability for: " + order);
        boolean available = inventory.containsKey(order.getItemId().toLowerCase()) &&
               inventory.get(order.getItemId().toLowerCase()) >= order.getQuantity();
               
        System.out.println("InventoryBean availability is " + available);
               
        return available;
    }

}
