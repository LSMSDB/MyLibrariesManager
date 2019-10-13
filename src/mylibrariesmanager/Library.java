package mylibrariesmanager;

import java.util.*;
import javafx.beans.property.*;

public class Library {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty address;
    private final List<Book> catalog;
    
    public Library(int id, String name, String address, List<Book> catalog){
        
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.catalog = catalog;
    }
    
    public int getId(){
        return id.get();
    }
    
    public String getName(){
        return name.get();
    }
    
    public String getAddress(){
        return address.get();
    }
    
    public List<Book> getCatalog(){
        return catalog;
    }
    
    public String toString(){
        return name.get() + address.get();
    }
    
}
