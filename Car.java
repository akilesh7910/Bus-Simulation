
import java.io.Serializable;


public class Car implements Serializable{
    
    String registration;
    String make;
    int price;
    double mileage;
    boolean forSale;
    
    public Car(){}

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public boolean isForSale() {
        return forSale;
    }

    public void setForSale(boolean forSale) {
        this.forSale = forSale;
    }
    
    public void showDetails(){
        System.out.println("Registration:"+this.getRegistration()
                +"\tMake:"+this.getMake()
                +"\tPrice:"+this.getPrice()
                +"\tMileage:"+this.getMileage()
                +"\tForSale"+this.isForSale());
    }    
}
