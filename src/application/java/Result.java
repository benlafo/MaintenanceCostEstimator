package application.java;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Ben LaForge on 5/24/2017.
 *
 * For the use and distribution of Trackmobile, LLC.
 */
public class Result {
    private SimpleDoubleProperty t4;

    private SimpleDoubleProperty timePeriod;
    private SimpleDoubleProperty timePeriodStart;
    private SimpleDoubleProperty laborCost;

    private ObservableList<PartResult> parts = FXCollections.observableArrayList();
    private Map laborHours;
    private double tireHours;

    Result(double timePeriodStart, double timePeriod, Map laborHours, double laborCost, double tireHours) {
        setTimePeriod(timePeriodStart, timePeriod);
        this.laborHours = laborHours;
        this.laborCost = new SimpleDoubleProperty(laborCost);
        this.tireHours = tireHours;
    }

    public void setTimePeriod(double timePeriodStart, double timePeriod) {
        this.timePeriod = new SimpleDoubleProperty(timePeriod);
        this.timePeriodStart = new SimpleDoubleProperty(timePeriodStart);
        t4 = new SimpleDoubleProperty(0);
    }

    public void add(String name, double price, double qty, double interval, boolean breakIn) {
        parts.add(new PartResult(name, price, qty, interval, breakIn));
    }

    public void calculate(double start, double end) {
        t4.set(getValue(end) - getValue(start));
        for (PartResult part : parts) {
        	if (part.getName() != "Labor Cost") {
        		part.setT4(part.getCumValueAtTime(end) - part.getCumValueAtTime(start));
        	}
        }
    }

    public double getValue(double time) {
        double value = 0;
        for (PartResult part : parts) {
            if (part.getQty() != 0) {
                double num = part.getCumValueAtTime(time);
                value += num;
            }
        }
        Iterator it = laborHours.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Double,Integer> pair = (Map.Entry)it.next();
            if (pair.getKey() <= time) {
            	get("Labor Cost").setT4(get("Labor Cost").getT4() + pair.getValue()*laborCost.get());
                value += pair.getValue()*laborCost.get();
                if (get("Road Tires (Estimated)") != null) { //not tm
    	            double tireinterval = get("Road Tires (Estimated)").getInterval();
    	            if ((int) (pair.getKey() % tireinterval) == 0) {
    	            	get("Labor Cost").setT4(get("Labor Cost").getT4() + tireHours*laborCost.get());
    	            	value += tireHours*laborCost.get();
    	            }
                }
            }
        }
        return value;
    }

    public ObservableList<PartResult> getParts() {
        return parts;
    }

    public PartResult get(String item) {
    	for (PartResult part : parts) {
    		if (part.getName().equals(item)) {
    			return part;
    		} 
    	}
    	return null;
    }
    
    public void print() {
        for (PartResult part : parts) {
            part.print();
        }
    }

    public double getT4() {
        return t4.get();
    }

    public SimpleDoubleProperty t4Property() {
        return t4;
    }

    public double getTimePeriod() {
        return timePeriod.get();
    }

    public SimpleDoubleProperty timePeriodProperty() {
        return timePeriod;
    }

    public double getTimePeriodStart() {
        return timePeriodStart.get();
    }

    public SimpleDoubleProperty timePeriodStartProperty() {
        return timePeriodStart;
    }

    public double getColumnTotal() {
        double total = 0;
        for (PartResult part : parts) {
            total += part.getT4();
        }
        return total;
    }

}

