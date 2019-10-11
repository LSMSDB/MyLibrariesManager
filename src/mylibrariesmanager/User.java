package mylibrariesmanager;

import java.util.*;
import javafx.beans.property.*;


public class User {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty surname;
    private final SimpleStringProperty address;
    private final SimpleStringProperty email;
    private final SimpleStringProperty phone;
    private final List<Borrowing> borrowings;
    
    public User(int id, String name, String surname, String address, String email, 
                String phone, List<Borrowing> borrowings){
        
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.address = new SimpleStringProperty(address);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        this.borrowings = borrowings;   
    }
    
    public int getId(){
        return id.get();
    }
    
    public String getName(){
        return name.get();
    }
    
    public String getSurname(){
        return surname.get();
    }
    
    public String getAddress(){
        return address.get();
    }
    
    public String getEmail(){
        return email.get();
    }
    
    public String getPhone(){
        return phone.get();
    }
    
    public List<Borrowing> getBorrowings(){
        return borrowings;
    }
    
}
