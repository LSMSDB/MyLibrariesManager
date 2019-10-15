package mylibrariesmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LibrariesArchive {
    private static Connection archiveConnection;
    
    public static boolean initializeConnection(String addressDBMS, String portDBMS) {
        String url = "jdbc:mysql://" + addressDBMS + ":" + portDBMS + "/mylibrariesmanager?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Paris";
        String username = "root";
        String password = "root";
                
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            }catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            archiveConnection = DriverManager.getConnection(url, username, password);
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public static boolean addUser(User u) {
        PreparedStatement preparedStatement = null;
        
        try {
            String selectSQL = "INSERT INTO user (name, surname, address, email, phone) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = archiveConnection.prepareStatement(selectSQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, u.getName());
            preparedStatement.setString(2, u.getSurname());
            preparedStatement.setString(3, u.getAddress());
            preparedStatement.setString(4, u.getEmail());
            preparedStatement.setString(5, u.getPhone());
            
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }
            else {
                return true;
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public static List<User> retrieveUsers() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<User>();
        
        try {
            preparedStatement = archiveConnection.prepareStatement("select * from user");
            resultSet = preparedStatement.executeQuery();
 
            while(resultSet.next()) { 
                
                int idUser = resultSet.getInt("id");
                
                String nameUser = resultSet.getString("name");
                String surnameUser = resultSet.getString("surname");
                String addressUser = resultSet.getString("address");
                String emailUser = resultSet.getString("email");
                String phoneUser = resultSet.getString("phone");
                List<Borrowing> borrowings = retrieveBorrowings(idUser);
                
                User u = new User(idUser, nameUser, surnameUser, addressUser, emailUser, phoneUser, borrowings);
                users.add(u);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public static boolean deleteUser(User u) {
        PreparedStatement preparedStatement = null;
        
        try {
            preparedStatement = archiveConnection.prepareStatement("delete from user WHERE id = ?");
            preparedStatement.setInt(1, u.getId());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }
            else {
                return true;
            }           
            
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
      
    public static List<Library> retrieveLibraries() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        List<Library> libraries = new ArrayList<Library>();
        
        try {
            preparedStatement = archiveConnection.prepareStatement("select * from library");
            resultSet = preparedStatement.executeQuery();
 
            while(resultSet.next()) { 
                
                int idLibrary = resultSet.getInt("id");
                
                String nameLibrary = resultSet.getString("name");
                String addressLibrary = resultSet.getString("address");                 

                List<Book> book = retrieveBooks(idLibrary);
                
                Library lib = new Library(idLibrary, nameLibrary, addressLibrary, book);
                libraries.add(lib);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return libraries;
    }
    
    public static boolean addBorrowing(User user, Borrowing borrowing) {
        PreparedStatement preparedStatement = null;
        try {
            archiveConnection.setAutoCommit(false); // Queries will be executed in a single atomic transaction
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date sdf_borrowingDate = new java.sql.Date(sdf.parse(borrowing.getBorrowingDate()).getTime());
            Date sdf_returnDate = new java.sql.Date(sdf.parse(borrowing.getReturnDate()).getTime());
            Date sdf_expirationDate = new java.sql.Date(sdf.parse(borrowing.getExpirationDate()).getTime());
            
            String selectSQL = "INSERT INTO borrowing (id_user, id_book, borrowing_date, return_date, expiration_date) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = archiveConnection.prepareStatement(selectSQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, borrowing.getBook().getId());
            preparedStatement.setDate(3, (java.sql.Date) sdf_borrowingDate);
            preparedStatement.setDate(4, (java.sql.Date) sdf_returnDate);
            preparedStatement.setDate(5, (java.sql.Date) sdf_expirationDate);
            
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }
            else {
                changeAvailableBook(borrowing.getBook());
                archiveConnection.commit();  // Queries executed in a single atomic transaction
                archiveConnection.setAutoCommit(true); // Return to the normal commit mode
                return true;
            } 
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }
    
    public static boolean endBorrowing(Borrowing borrowing) {
        PreparedStatement preparedStatement = null;
        
        try {
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date sdf_returnDate = new java.sql.Date(sdf.parse(borrowing.getReturnDate()).getTime());
            
            String selectSQL = "UPDATE borrowing SET return_date = ? WHERE id = ?";
            preparedStatement = archiveConnection.prepareStatement(selectSQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDate(1, (java.sql.Date) sdf_returnDate);
            preparedStatement.setInt(2, borrowing.getId());
            
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }
            else {
                changeAvailableBook(borrowing.getBook());
                return true;
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean renewBorrowing(Borrowing borrowing) {
        PreparedStatement preparedStatement = null;
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date sdf_expirationDate = new java.sql.Date(sdf.parse(borrowing.getExpirationDate()).getTime());
            
            String selectSQL = "UPDATE borrowing SET expiration_date = ? WHERE id = ?";
            preparedStatement = archiveConnection.prepareStatement(selectSQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDate(1, (java.sql.Date) sdf_expirationDate);
            preparedStatement.setInt(2, borrowing.getId());
            
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }
            else {
                return true;
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    
    public static  List<Genre> retrieveGenres(Library library) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        List<Genre> genres = new ArrayList<Genre>();
        
        try {
            preparedStatement = archiveConnection.prepareStatement("select * from book where id_library = ?");
            preparedStatement.setInt(1, library.getId());
            resultSet = preparedStatement.executeQuery();
 
            while(resultSet.next()) { 
                int idGenre = resultSet.getInt("id_genre");               
                Genre genre = retrieveGenre(idGenre);
                
                boolean checkIfInList = false;
                
                for (Genre addedGenre : genres)
                    if (addedGenre.getId() == genre.getId())
                        checkIfInList = true;
                
                if (!checkIfInList)
                    genres.add(genre);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return genres;
    }
    
    public static List<BookStatistic> retrieveMostBorrowedBooks(Library library, Genre genre) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        List<BookStatistic> bookStatistics = new ArrayList<BookStatistic>();
        List<List<Integer>> mostBorrowedBooks = new ArrayList<List<Integer>>();
        
        for (int i = 0; i < 10; i++) {
            List<Integer> mostBorrowedBook = new ArrayList<Integer>();
            mostBorrowedBook.add(0);
            mostBorrowedBook.add(0);
            mostBorrowedBooks.add(mostBorrowedBook);
        }
        
        try {
            preparedStatement = archiveConnection.prepareStatement("select * from book where id_library = ? and id_genre = ?");
            preparedStatement.setInt(1, library.getId());
            preparedStatement.setInt(2, genre.getId());
            resultSet = preparedStatement.executeQuery();
 
            while(resultSet.next()) { 
                
                int idBook = resultSet.getInt("id");            
                int numberBorrowing = countBorrowing(idBook);
                boolean mostPopular = false;
                
                if (numberBorrowing > mostBorrowedBooks.get(9).get(1)) {
                    int k = 0;
                    while (!mostPopular) {
                        if (numberBorrowing > mostBorrowedBooks.get(k).get(1)) {
                            List<Integer> bookPopular = new ArrayList<Integer>();
                            bookPopular.add(idBook);
                            bookPopular.add(numberBorrowing);
                            mostPopular = true;
                            
                            mostBorrowedBooks.add(k, bookPopular);
                        }
                        k++;
                    }
                }
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        
        for (int j = 0; j < 10; j++) {
            if (mostBorrowedBooks.get(j).get(0) != 0) {
                Book b = retrieveBook(mostBorrowedBooks.get(j).get(0));
                BookStatistic bookStatistic = new BookStatistic(b.getTitle(), mostBorrowedBooks.get(j).get(1));
                bookStatistics.add(bookStatistic);
            }
        }
        return bookStatistics;
    }
    
    public static void closeConnection(){
        try{
            archiveConnection.close();
            
        }catch(SQLException exception) {
            exception.printStackTrace();
        }
    }
    
    
    public static List<Borrowing> retrieveBorrowings(int idUser) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        List<Borrowing> borrowings = new ArrayList<Borrowing>();
        
        try {
            preparedStatement = archiveConnection.prepareStatement("select * from borrowing where id_user = ?");
            preparedStatement.setInt(1, idUser);
            resultSet = preparedStatement.executeQuery();
 
            while(resultSet.next()) { 
                
                int idBorrowing = resultSet.getInt("id");               
                int idBook = resultSet.getInt("id_book");               
                Date borrowingDate = resultSet.getDate("borrowing_date");
                Date returnDate = resultSet.getDate("return_date");
                Date expirationDate = resultSet.getDate("expiration_date");
                
                Book book = retrieveBook(idBook);
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String sdf_borrowingDate = sdf.format(borrowingDate);
                String sdf_returnDate = sdf.format(returnDate);
                String sdf_expirationDate = sdf.format(expirationDate);
                
                
                if (sdf_returnDate.equals("30-11-0002"))
                    sdf_returnDate = "00-00-0000";
                
                Borrowing borrowing = new Borrowing(idBorrowing, book, sdf_borrowingDate, sdf_returnDate, sdf_expirationDate);
                borrowings.add(borrowing);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return borrowings;
    }
    
    public static List<Book> retrieveBooks(int idLibrary) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        List<Book> books = new ArrayList<Book>();
        
        try {
            preparedStatement = archiveConnection.prepareStatement("select * from book where id_library = ?");
            preparedStatement.setInt(1, idLibrary);
            resultSet = preparedStatement.executeQuery();
 
            while(resultSet.next()) { 
                
                int idBook = resultSet.getInt("id");
                String titleBook = resultSet.getString("title");
                String authorBook = resultSet.getString("author");
                String editionBook = resultSet.getString("edition");
                int genreBook = resultSet.getInt("id_genre");
                boolean availableBook = resultSet.getBoolean("available");
                
                Genre genre = retrieveGenre(genreBook);
                
                Book book = new Book(idBook, titleBook, authorBook, editionBook, availableBook, genre);
                books.add(book);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    
    private static Book retrieveBook(int idBook) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        Book book = null;
        
        try {
            preparedStatement = archiveConnection.prepareStatement("select * from book where id = ?");
            preparedStatement.setInt(1, idBook);
            resultSet = preparedStatement.executeQuery();
 
            while(resultSet.next()) { 
                
                String titleBook = resultSet.getString("title");
                String authorBook = resultSet.getString("author");
                String editionBook = resultSet.getString("edition");
                int genreBook = resultSet.getInt("id_genre");
                boolean availableBook = resultSet.getBoolean("available");
                
                Genre genre = retrieveGenre(genreBook);
                
                book = new Book(idBook, titleBook, authorBook, editionBook, availableBook, genre);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return book;
    }
    
    private static Genre retrieveGenre(int idGenre) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        Genre genre = null;
        
        try {
            preparedStatement = archiveConnection.prepareStatement("select * from genre where id = ?");
            preparedStatement.setInt(1, idGenre);
            resultSet = preparedStatement.executeQuery();
 
            while(resultSet.next()) { 
                
                String nameGenre = resultSet.getString("name");                 
                genre = new Genre(idGenre, nameGenre);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return genre;
    }
    
    private static int countBorrowing(int idBook) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int affectedRows = 0;
        
        try {
            preparedStatement = archiveConnection.prepareStatement("select * from borrowing where id_book = ?");
            preparedStatement.setInt(1, idBook); 
            rs = preparedStatement.executeQuery();
            
            if (rs != null) {
                rs.last();
                affectedRows = rs.getRow();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return affectedRows;        
    }
    
    private static boolean changeAvailableBook(Book book) {
        PreparedStatement preparedStatement = null;
        
        try {           
            String selectSQL = "UPDATE book SET available = ? WHERE id = ?";
            preparedStatement = archiveConnection.prepareStatement(selectSQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setBoolean(1, !book.getAvailable());
            preparedStatement.setInt(2, book.getId());
            
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }
            else {
                return true;
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
}
