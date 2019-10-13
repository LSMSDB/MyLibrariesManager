package mylibrariesmanager;

import javafx.beans.property.*;

public class Book {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty title;
    private final SimpleStringProperty author;
    private final SimpleStringProperty edition;
    private final SimpleBooleanProperty available;
    private final Genre genre;
  
    public Book(int id, String title, String author, String edition,
                boolean available, Genre genre){
        
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.edition = new SimpleStringProperty(edition);
        this.available = new SimpleBooleanProperty(available);
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
    
    public boolean getAvailable(){
        return available.get();
    }
    
    public Genre getGenre(){
        return genre;
    }
    
    public StringProperty nameProperty(){
      return title;
    }
    
    public StringProperty authorProperty(){
      return author;
    }
}
