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
    
}
