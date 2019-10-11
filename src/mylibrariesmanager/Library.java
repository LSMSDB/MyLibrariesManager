package mylibrariesmanager;

import java.util.*;
import javafx.beans.property.*;

public class Library {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty address;
    private final List<Book> catalog;
    private final List<BookStatistic> bookStatistics;
    
    public Library(int id, String name, String address, List<Book> catalog, 
                   List<BookStatistic> bookStatistics){
        
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.catalog = catalog;
        this.bookStatistics = bookStatistics;
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
    
    public List<BookStatistic> getBookStatistics(){
        return bookStatistics;
    }
    
}
