package mylibrariesmanager;

import javafx.beans.property.*;

public class Book {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty title;
    private final SimpleStringProperty author;
    private final SimpleStringProperty edition;
    private final SimpleIntegerProperty available;
    private final Genre genre;
  
    public Book(int id, String title, String author, String edition,
                int available, Genre genre){
        
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.edition = new SimpleStringProperty(edition);
        this.available = new SimpleIntegerProperty(available);
        this.genre = genre;
    }
    
    public int getId(){
        return id.get();
    }
    
    public String getTitle(){
        return title.get();
    }
    
    public String getAuthor(){
        return author.get();
    }
    
    public String getEdition(){
        return edition.get();
    }
    
    public int getAvailable(){
        return available.get();
    }
    
    public Genre getGenre(){
        return genre;
    }
    
}
