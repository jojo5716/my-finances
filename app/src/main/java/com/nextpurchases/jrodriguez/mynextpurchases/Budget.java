package com.nextpurchases.jrodriguez.mynextpurchases;


/**
 * Created by root on 2/01/16.
 */
public class Budget {
    private int id;
    private String name;
    private double max_amount;
    private double spent;

    public Budget(){
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMax_amount() {
        return max_amount;
    }

    public void setMax_amount(double max_amount) {
        this.max_amount = max_amount;
    }

    public double getSpent() {
        return spent;
    }

    public void setSpent(double spent) {
        this.spent = spent;
    }

    public Budget(int id, String name, double max_amount, double spent){
        super();
        this.id = id;
        this.name = name;
        this.max_amount = max_amount;
        this.spent = spent;
    }

    @Override
    public String toString(){
        if(this.id != 0){
            return this.id + ".- " + this.name + " (" + this.spent + "€ of " + this.max_amount + "€) ";
        }

        return "You do not hace any budget yet.";
    }
}
