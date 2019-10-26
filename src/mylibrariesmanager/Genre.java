package mylibrariesmanager;

import javafx.beans.property.*;

public class Genre {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    
    public Genre(int id, String name){
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
    }
    
    public int getId(){
        return id.get();
    }
    
    public String getName(){
        return name.get();
    }
    
    public String toString(){
        return name.get();
    }
    
    public boolean equals(Object otherGenre){  
        if (otherGenre == this)
            return true; 
 
        if (!(otherGenre instanceof Genre))
            return false; 
 
    	return id.get() == ((Genre)otherGenre).getId();
    }
}
