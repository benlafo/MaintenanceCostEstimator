package application.java;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Slider;

/**
 * Created by Ben LaForge on 5/19/2017.
 *
 * For the use and distribution of Trackmobile, LLC.
 */

public class Part {

    private SimpleStringProperty name;
    private SimpleDoubleProperty swxprice;
    private SimpleDoubleProperty tmprice;
    private SimpleStringProperty id = new SimpleStringProperty();

    private SimpleDoubleProperty swxqty;
    private SimpleDoubleProperty tmqty;

    private SimpleDoubleProperty swxMaintInterval;
    private SimpleDoubleProperty tmMaintInterval;

    final double swxMaintIntervalOrig;
    final double tmMaintIntervalOrig;
    
    final SimpleBooleanProperty isCompressor;
    private SimpleBooleanProperty tmBreakIn;
    private SimpleBooleanProperty swxBreakIn;

    Part(String partname, double swxprice, double tmprice, String id, double swxqty, double tmqty, 
    		double swxMaintInterval, double tmMaintInterval, boolean isCompressor, boolean tmBreakIn, boolean swxBreakIn) {
        this.name = new SimpleStringProperty(partname);
        this.swxprice = new SimpleDoubleProperty(swxprice);
        this.tmprice = new SimpleDoubleProperty(tmprice);
        this.id = new SimpleStringProperty(id);
        this.swxqty = new SimpleDoubleProperty(swxqty);
        this.tmqty = new SimpleDoubleProperty(tmqty);
        this.swxMaintInterval = new SimpleDoubleProperty(swxMaintInterval);
        this.tmMaintInterval = new SimpleDoubleProperty(tmMaintInterval);
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

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public double getSWXPrice() {
        return swxprice.get();
    }

    public void setSWXPrice(double price) {
        this.swxprice.set(price);
    }

    public DoubleProperty swxpriceProperty() {
        return swxprice;
    }

    public double getTMPrice() {
        return tmprice.get();
    }

    public void setTMPrice(double price) {
        this.tmprice.set(price);
    }

    public DoubleProperty tmpriceProperty() {
        return tmprice;
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

	public void print() {
        System.out.println(name + " TM: " + tmprice);
        System.out.println(name + " SWX: " + swxprice);
    }
}
