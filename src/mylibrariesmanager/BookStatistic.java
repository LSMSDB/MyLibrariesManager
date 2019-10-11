package mylibrariesmanager;

import javafx.beans.property.*;

public class BookStatistic {
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty numberOfBorrowings;
    
    public BookStatistic(String name, int numberOfBorrowings){
        this.name = new SimpleStringProperty(name);
        this.numberOfBorrowings = new SimpleIntegerProperty(numberOfBorrowings);
    }
    
    public String getName(){
        return name.get();
    }
    
    public int getNumberOfBorrowings(){
        return numberOfBorrowings.get();
    }
}
