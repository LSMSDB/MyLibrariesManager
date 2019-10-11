package mylibrariesmanager;

import javafx.beans.property.*;

public class Borrowing {
    private final SimpleIntegerProperty id;
    private final Book borrowedBook;
    private final SimpleStringProperty borrowingDate;
    private final SimpleStringProperty returnDate;
    private final SimpleStringProperty expirationDate;
    
    public Borrowing(int id, Book borrowedBook, String borrowingDate, 
                     String returnDate, String expirationDate){
        
        this.id = new SimpleIntegerProperty(id);
        this.borrowedBook = borrowedBook;
        this.borrowingDate = new SimpleStringProperty(borrowingDate);
        this.returnDate = new SimpleStringProperty(returnDate);
        this.expirationDate = new SimpleStringProperty(expirationDate);
    }
    
    public int getId(){
        return id.get();
    }
    
    public Book getBook(){
        return borrowedBook;
    }
    
    public String getBorrowingDate(){
        return borrowingDate.get();
    }
    
    public String getReturnDate(){
        return returnDate.get();
    }
    
    public String getExpirationDate(){
        return expirationDate.get();
    }
   
}
