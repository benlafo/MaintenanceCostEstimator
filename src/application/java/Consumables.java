package application.java;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;

/**
 * Created by Ben LaForge on 5/19/2017.
 *
 * For the use and distribution of Trackmobile, LLC.
 */

public class Consumables {

    private SimpleStringProperty name;
    private SimpleDoubleProperty price;
    private SimpleStringProperty unit = new SimpleStringProperty();

    private SimpleDoubleProperty swxqty;
    private SimpleDoubleProperty tmqty;

    private SimpleDoubleProperty swxMaintInterval;
    private SimpleDoubleProperty tmMaintInterval;

    private final double swxMaintIntervalOrig;
    private final double tmMaintIntervalOrig;
    
    final SimpleBooleanProperty isCompressor;
    private SimpleBooleanProperty tmBreakIn;
    private SimpleBooleanProperty swxBreakIn;


	Consumables(String partname, double price, String unit, double swxqty, double tmqty, double swxMaintInterval, 
			double tmMaintInterval, boolean isCompressor, boolean tmBreakIn, boolean swxBreakIn) {
        this.name = new SimpleStringProperty(partname);
        this.price = new SimpleDoubleProperty(price);
        this.unit = new SimpleStringProperty(unit);
        this.swxqty = new SimpleDoubleProperty(swxqty);
        this.tmqty = new SimpleDoubleProperty(tmqty);
        this.tmMaintInterval = new SimpleDoubleProperty(tmMaintInterval);
        this.swxMaintInterval = new SimpleDoubleProperty(swxMaintInterval);
        swxMaintIntervalOrig = swxMaintInterval;
        tmMaintIntervalOrig = tmMaintInterval;
        this.isCompressor = new SimpleBooleanProperty(isCompressor);
        this.tmBreakIn = new SimpleBooleanProperty(tmBreakIn);
        this.swxBreakIn = new SimpleBooleanProperty(swxBreakIn);
    }


    public String getName() {
        return name.get();
    }

    public void setName(String fName) {
        name.set(fName);
    }
    
    public StringProperty nameProperty() {
    	return name;
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public String getUnit() {
        return unit.get();
    }

    public void setUnit(String fName) {
        unit.set(fName);
    }

    public double getTMQty() {
        return tmqty.get();
    }

    public void setTMQty(double qty) {
        this.tmqty.set(qty);
    }

    public DoubleProperty tmqtyProperty() {
        return tmqty;
    }

    public double getSWXQty() {
        return swxqty.get();
    }

    public void setSWXQty(double qty) {
        this.swxqty.set(qty);
    }

    public DoubleProperty swxqtyProperty() {
        return swxqty;
    }

    public double getSwxMaintInterval() {
        return this.swxMaintInterval.getValue();
    }

    public void setSwxMaintInterval(double value) {
        this.swxMaintInterval.setValue(value);
    }

    public DoubleProperty swxMaintIntervalProperty() {
        return new SimpleDoubleProperty(this.swxMaintInterval.getValue());
    }

    public double getTMMaintInterval() {
        return this.tmMaintInterval.getValue();
    }

    public void setTMMaintInterval(double value) {
        this.tmMaintInterval.setValue(value);
    }

    public DoubleProperty tmMaintIntervalProperty() {
        return new SimpleDoubleProperty(this.tmMaintInterval.getValue());
    }

    public double getSwxMaintIntervalOrig() {
        return swxMaintIntervalOrig;
    }

    public double getTmMaintIntervalOrig() {
        return tmMaintIntervalOrig;
    }

    public void print() {
        System.out.println(name + ": " + price);
    }
    
    public SimpleBooleanProperty isCompressorProperty() {
		return isCompressor;
	}
    public boolean getIsCompressor() {
    	return isCompressor.get();
    }
    
    public SimpleBooleanProperty tmBreakInProperty() {
		return tmBreakIn;
	}

    public boolean gettmBreakIn() {
    	return tmBreakIn.get();
    }

	public void settmBreakIn(boolean tmBreakIn) {
		this.tmBreakIn = new SimpleBooleanProperty(tmBreakIn);
	}
	
	public SimpleBooleanProperty swxBreakInProperty() {
		return swxBreakIn;
	}

    public boolean getswxBreakIn() {
    	return swxBreakIn.get();
    }

	public void setswxBreakIn(boolean swxBreakIn) {
		this.swxBreakIn = new SimpleBooleanProperty(swxBreakIn);
	}
}
