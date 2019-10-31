package mylibrariesmanager;

import java.util.*;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisException;

public class LibrariesArchive {
    private static String archiveAddress;
    private static int archivePort;
    private static String namespace;
    private static int maxTransactionAttempts;
    
    public static void initializeConnectionParameters(String addressDB, String portDB) {
        archiveAddress = addressDB;
        archivePort = Integer.valueOf(portDB);
        namespace = "mylibmanag:";
        maxTransactionAttempts = 10;
    }
       
    public static boolean addUser(User user) {
      int transactionAttemptsCounter = 0;
      boolean checkResult = false;
      
      try(Jedis archiveConnection = new Jedis(archiveAddress, archivePort)){
        int newUserId = -1;
        
        // Get the unique id for the new user
        while(transactionAttemptsCounter < maxTransactionAttempts){ 
            archiveConnection.watch(namespace + "user:id");
            newUserId = Integer.valueOf(archiveConnection.get(namespace + "user:id"));
            
            System.out.println("NEW_USER" + newUserId);
            
            Transaction getIdTransaction = archiveConnection.multi();
            getIdTransaction.incr(namespace + "user:id");
            List<Object> result = getIdTransaction.exec();
            
            if (!result.isEmpty())  // Transaction succeeds
                break;
            
            transactionAttemptsCounter++;
        }
        
        if (transactionAttemptsCounter == maxTransactionAttempts)
            return checkResult;
        
        String userKey = namespace + "user:" + newUserId + ":";
      
        
        Transaction newUserTransaction = archiveConnection.multi();
        newUserTransaction.set(userKey + "id", String.valueOf(newUserId));
        newUserTransaction.set(userKey + "name", user.getName());
        newUserTransaction.set(userKey + "surname", user.getSurname());
        newUserTransaction.set(userKey + "address", user.getAddress());
        newUserTransaction.set(userKey + "email", user.getEmail());
        newUserTransaction.set(userKey + "phone", user.getPhone());
        
        List<Object> result = newUserTransaction.exec();
        
        if (result.isEmpty())   // Transaction fails
            return checkResult;
        
        checkResult = true;
        
      }catch (JedisException exception) {
          exception.printStackTrace();
      }
      
      System.out.println("CHECK_RESULT::" + checkResult);
    
      return checkResult;
      
    }
    
    public static boolean deleteUser(User user){
        return true;
    }
    
    public static List<User> retrieveUsers(){
      List<User> userList = new ArrayList<>();
      List<Borrowing> borrowingList = new ArrayList<>();
      
      try(Jedis archiveConnection = new Jedis(archiveAddress, archivePort)){
        int numOfUsers = Integer.valueOf(archiveConnection.get(namespace + "user:id"));
        
        for(int i = 0; i < numOfUsers; i++) {
          String userKey = namespace + "user:" + i;
          String userId = userKey + ":id";
          
          if (archiveConnection.get(userId) != null) {
            String name = archiveConnection.get(userKey + ":name");
            String surname = archiveConnection.get(userKey + ":surname");
            String address = archiveConnection.get(userKey + ":address");
            String email = archiveConnection.get(userKey + ":email");
            String phone = archiveConnection.get(userKey + ":phone");
            

            userList.add(new User(i, name, surname, address, email, phone, borrowingList));
          }
        }
       
      } catch (JedisException exception) {
        exception.printStackTrace();
      }
      
      
    	return userList;

    }
    
    public static List<Borrowing> retrieveBorrowings(User user) {
    	return new ArrayList<>();
    }
    
    public static List<Library> retrieveLibraries(){
      List<Library> libraryList = new ArrayList<>();
      
      try(Jedis archiveConnection = new Jedis(archiveAddress, archivePort)){
        int numOfLibraries = Integer.valueOf(archiveConnection.get(namespace + "library:id"));
        
        for(int i = 0; i < numOfLibraries; i++) {
          String libraryKey = namespace + "library:" + i;
          String libraryId = libraryKey + ":id";
          
          if (archiveConnection.get(libraryId) != null) {
            String name = archiveConnection.get(libraryKey + ":name");
            String address = archiveConnection.get(libraryKey + ":address");
            List<Book> bookList = retrieveBooks(i);
            
            libraryList.add(new Library(i, name, address, bookList));
          }
        }
       
      }catch (JedisException exception) {
        exception.printStackTrace();
      }
      
      
    	return libraryList;
    }
        
    public static List<Book> retrieveBooks(int libraryId){
    	List<Book> bookList = new ArrayList<>();
    	
    	try(Jedis archiveConnection = new Jedis(archiveAddress, archivePort)){
    		String libraryCatalogKey = namespace + "library:" + libraryId + ":catalog";
    		List<String> bookIdList = archiveConnection.lrange(libraryCatalogKey, 0, -1);
    		
    		for(String id : bookIdList) {
    			String bookKey = namespace + "book:" + id + ":";
    			
    			int genreId = Integer.valueOf(archiveConnection.get(bookKey + "genre"));
    			String genreKey = namespace + "genre:" + genreId + ":";
    			
    			boolean bookAvailable = false;
    			if (archiveConnection.get(bookKey + "available").equals("1"))
    				bookAvailable = true;


                bookList.add(new Book(Integer.valueOf(id),
                                      archiveConnection.get(bookKey + "title"),
                                      archiveConnection.get(bookKey + "author"),
                                      archiveConnection.get(bookKey + "edition"),
                                      bookAvailable,
                                      new Genre(genreId, archiveConnection.get(genreKey + "name")),
                                      Integer.valueOf(archiveConnection.get(bookKey + "numberOfBorrowings")))
                             ); 
            }
        }catch (JedisException exception) {
            exception.printStackTrace();
        }
        
        return bookList;
    }
    
    public static List<Genre> retrieveGenres(Library library){
        List<Genre> genreList = new ArrayList<>();
        List<Book> bookList = retrieveBooks(library.getId());
            
        for(Book book : bookList){
            if (!genreList.contains(book.getGenre()))
                genreList.add(book.getGenre());
        }
                    
        return genreList;
    }
    
    public static boolean addBorrowing(User user, Borrowing borrowing){
        int transactionAttemptsCounter = 0;
        boolean checkResult = false;
        
        try(Jedis archiveConnection = new Jedis(archiveAddress, archivePort)){
            int newBorrowingId = -1;
            
            // Get the unique id for the new borrowing
            while(transactionAttemptsCounter < maxTransactionAttempts){ 
                archiveConnection.watch(namespace + "borrowing:id");
                newBorrowingId = Integer.valueOf(archiveConnection.get(namespace + "borrowing:id"));
                Transaction getIdTransaction = archiveConnection.multi();
                getIdTransaction.incr(namespace + "borrowing:id");
                List<Object> result = getIdTransaction.exec();
                
                if (!result.isEmpty())  // Transaction succeeds
                    break;
                
                transactionAttemptsCounter++;
            }
            
            if (transactionAttemptsCounter == maxTransactionAttempts)
                return checkResult;
            
            String borrowingKey = namespace + "borrowing:" + newBorrowingId + ":";
            String userKey = namespace + "user:" + user.getId() + ":borrowings";
            String bookKey = namespace + "book:" + borrowing.getBook().getId() + ":";

            archiveConnection.watch(bookKey + "available");
            
            // Need to check the value of available: watch detects changes, not if available is true
            if (archiveConnection.get(bookKey + "available").equals("0")){
                archiveConnection.unwatch();
                return checkResult;
            }
            
            Transaction newBorrowingTransaction = archiveConnection.multi();
            newBorrowingTransaction.set(borrowingKey + "id", String.valueOf(newBorrowingId));
            newBorrowingTransaction.set(borrowingKey + "borrowedBook", String.valueOf(borrowing.getBook().getId()));
            newBorrowingTransaction.set(borrowingKey + "borrowingDate", borrowing.getBorrowingDate());
            newBorrowingTransaction.set(borrowingKey + "returnDate", borrowing.getReturnDate());
            newBorrowingTransaction.set(borrowingKey + "expirationDate", borrowing.getExpirationDate());
            newBorrowingTransaction.incr(bookKey + "numberOfBorrowings");
            newBorrowingTransaction.set(bookKey + "available", "0");
            newBorrowingTransaction.lpush(userKey, String.valueOf(newBorrowingId));
            List<Object> result = newBorrowingTransaction.exec();
            
            if (result.isEmpty())   // Transaction fails
                return checkResult;
            
            checkResult = true;
            
        }catch (JedisException exception) {
            exception.printStackTrace();
        }
        
        return checkResult;
    }
    
    public static boolean endBorrowing(Borrowing borrowing){
        boolean checkResult = false;
        
        try(Jedis archiveConnection = new Jedis(archiveAddress, archivePort)){
            String borrowingKey = namespace + "borrowing:" + borrowing.getId() + ":returnDate";
            String bookKey = namespace + "book:" + borrowing.getBook().getId() + ":available";
            
            Transaction endBorrowingTransaction = archiveConnection.multi();
            endBorrowingTransaction.set(borrowingKey, borrowing.getReturnDate());
            endBorrowingTransaction.set(bookKey, "1");
            List<Object> result = endBorrowingTransaction.exec();
            
            if (result.isEmpty())   // Transaction fails
                return checkResult;
            
            checkResult = true;
                
        }catch (JedisException exception) {
            exception.printStackTrace();
        }
        
        return checkResult;
    }
    
    public static boolean renewBorrowing(Borrowing borrowing){
        boolean checkResult = false;
        
        try(Jedis archiveConnection = new Jedis(archiveAddress, archivePort)){
            String borrowingKey = namespace + "borrowing:" + borrowing.getId() + ":expirationDate";
            
            archiveConnection.set(borrowingKey, borrowing.getExpirationDate());
            checkResult = true;
        
        }catch (JedisException exception) {
            exception.printStackTrace();
        }
        
        return checkResult;
    }
    
    public static List<Book> retrieveMostBorrowedBooks(Library library, Genre genre){
    	List<Book> libraryBooks = retrieveBooks(library.getId());
    	List<Book> booksOfSelectedGenre = new ArrayList<>(); 
    	
    	for (Book book : libraryBooks){
    		if (book.getGenre().equals(genre))
    			booksOfSelectedGenre.add(book);
    	}
    	
    	Collections.sort(booksOfSelectedGenre, new Comparator<Book>(){
    	    public int compare(Book b1, Book b2) {
    	        return Integer.compare(b1.getNumberOfBorrowings(), b2.getNumberOfBorrowings());
    	    }
    	});
    	
    	Collections.reverse(booksOfSelectedGenre);  // Sorted by decreasing value
    	
    	List<Book> mostBorrowedBooks = new ArrayList<Book>(booksOfSelectedGenre.subList(0, 10));
    	return mostBorrowedBooks;

    }
    
}    
