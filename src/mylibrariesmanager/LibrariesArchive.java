package mylibrariesmanager;

import java.util.*;

public class LibrariesArchive {
	private static String archiveAddress;
	private static String archivePort;
    
    public static void initializeConnectionParameters(String addressDB, String portDB) {
        archiveAddress = addressDB;
        archivePort = portDB;
    }
       
    public static boolean addUser(User user){
    	return true;
    }
    
    public static boolean deleteUser(User user){
    	return true;
    }
    
    public static List<User> retrieveUsers(){
    	return new ArrayList<>();
    }
    
    public static List<Borrowing> retrieveBorrowings(User user){
    	return new ArrayList<>();
    }
    
    public static List<Library> retrieveLibraries(){
    	return new ArrayList<>();
    }
    
    public static List<Book> retrieveBooks(Library library){
    	return new ArrayList<>();
    }
    
    public static List<Genre> retrieveGenres(Library library){
    	return new ArrayList<>();
    }
    
    public static boolean addBorrowing(User user, Borrowing borrowing){
    	return true;
    }
    
    public static boolean endBorrowing(Borrowing borrowing){
    	return true;
    }
    
    public static boolean renewBorrowing(Borrowing borrowing){
    	return true;
    }
    
    public static List<Book> retrieveMostBorrowedBooks(Library library, Genre genre){
    	return new ArrayList<>(); 
    }
    
}    
    
