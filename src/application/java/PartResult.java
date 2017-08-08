package application.java;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.RadioButton;

/**
 * Created by benja
 * minl on 5/26/2017.
 */
public class PartResult {
    private SimpleStringProperty name;

    private SimpleDoubleProperty t1 = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty t2 = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty t3 = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty t4 = new SimpleDoubleProperty(0);
    private double price;
    private double qty;
    private double interval;
    private boolean breakIn;

    PartResult(String name, double price, double qty, double interval, boolean breakIn) {
        this.name = new SimpleStringProperty(name);
        this.price = price;
        this.qty = qty;
        this.interval = interval;
        this.breakIn = breakIn;
    }
    
    public double getCumValueAtTime(double time) {
    	double init = 0;
    	if (breakIn && time != 0) {
    		init = price * qty;
    	}
        return ((price * qty)*(Math.floor((time / interval))) + init);
    }

    public double getQty() {
        return qty;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public double getT1() {
        return t1.get();
    }

    public void setT1(double t1) {
        this.t1.set(t1);
    }

    public double getPrice() {
        return price;
    }

    public double getInterval() {
        return interval;
    }

    public void print() {
        System.out.println(name.get() + ": " + t1.get() + " = " + price + " * " + qty + " * timeperiod/" + interval);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty t1Property() {
        return new SimpleStringProperty("$" + String.format("%.2f",t1.get()));
    }

    public double getT2() {
        return t2.get();
    }

    public SimpleStringProperty t2Property() {
        return new SimpleStringProperty("$" + String.format("%.2f",t2.get()));
    }

    public void setT2(double t2) {
        this.t2.set(t2);
    }

    public double getT3() {
        return t3.get();
    }

    public SimpleStringProperty t3Property() {
        return new SimpleStringProperty("$" + String.format("%.2f",t3.get()));
    }

    public void setT3(double t3) {
        this.t3.set(t3);
    }

    public double getT4() {
        return t4.get();
    }

    public SimpleStringProperty t4Property() {
        return new SimpleStringProperty("$" + String.format("%.2f",t4.get()));
    }

    public void setT4(double t4) {
        this.t4.set(t4);
    }
}

