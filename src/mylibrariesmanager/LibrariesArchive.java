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
	
	public static Connection initializeConnection() {
		Connection con = null;
		String url = "jdbc:mysql://localhost:3306/mylibmanager?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Paris";
		String username = "root";
		String password = "root";

		try {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			con = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public void addUser(User u) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
 			connection = initializeConnection();
 			String selectSQL = "INSERT INTO user (name, surname, address, email, phone) VALUES (?, ?, ?, ?, ?)";
 			preparedStatement = connection.prepareStatement(selectSQL, Statement.RETURN_GENERATED_KEYS);
 			preparedStatement.setString(1, u.getName());
 			preparedStatement.setString(2, u.getSurname());
 			preparedStatement.setString(3, u.getAddress());
 			preparedStatement.setString(4, u.getEmail());
 			preparedStatement.setString(5, u.getPhone());
 			
 			int affectedRows = preparedStatement.executeUpdate();

 	        if (affectedRows == 0) {
 	            throw new SQLException("Creating user failed, no rows affected.");
 	        }
 		}
 		catch(SQLException ex) {
 			ex.printStackTrace();
 		}
	}
	
	public List<User> retrieveUsers() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
 		ResultSet resultSet = null;
		List<User> users = new ArrayList<User>();
		
		try {
			connection = initializeConnection();
			preparedStatement = connection.prepareStatement("select * from user");
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
	
	public void deleteUser(User u) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = initializeConnection();
 			preparedStatement = connection.prepareStatement("delete from user WHERE id = ?");
 			preparedStatement.setInt(1, u.getId());
 			preparedStatement.executeUpdate();
			
 		}
 		catch(SQLException e) {
 			e.printStackTrace();
 		}
	}
	
	
	
	public List<Library> retrieveLibraries() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
 		ResultSet resultSet = null;
 		
		List<Library> libraries = new ArrayList<Library>();
		
		try {
			connection = initializeConnection();
			preparedStatement = connection.prepareStatement("select * from library");
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
	
	
	
	public void addBorrowing(User user, Borrowing borrowing) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
 			connection = initializeConnection();
 			
 			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
 			Date sdf_borrowingDate = new java.sql.Date(sdf.parse(borrowing.getBorrowingDate()).getTime());
 			Date sdf_returnDate = new java.sql.Date(sdf.parse(borrowing.getReturnDate()).getTime());
 			Date sdf_expirationDate = new java.sql.Date(sdf.parse(borrowing.getExpirationDate()).getTime());
			
 			String selectSQL = "INSERT INTO borrowing (id_user, id_book, borrowing_date, return_date, expiration_date) VALUES (?, ?, ?, ?, ?)";
 			preparedStatement = connection.prepareStatement(selectSQL, Statement.RETURN_GENERATED_KEYS);
 			preparedStatement.setInt(1, user.getId());
 			preparedStatement.setInt(2, borrowing.getBook().getId());
 			preparedStatement.setDate(3, (java.sql.Date) sdf_borrowingDate);
 			preparedStatement.setDate(4, (java.sql.Date) sdf_returnDate);
 			preparedStatement.setDate(5, (java.sql.Date) sdf_expirationDate);
 			
 			int affectedRows = preparedStatement.executeUpdate();

 	        if (affectedRows == 0) {
 	            throw new SQLException("Creating user failed, no rows affected.");
 	        }
 		}
 		catch(SQLException ex) {
 			ex.printStackTrace();
 		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void endBorrowing(Borrowing borrowing) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
 			connection = initializeConnection();
 			
 			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
 			Date sdf_returnDate = new java.sql.Date(sdf.parse(borrowing.getReturnDate()).getTime());
 			
 			String selectSQL = "UPDATE borrowing SET return_date = ? WHERE id = ?";
 			preparedStatement = connection.prepareStatement(selectSQL, Statement.RETURN_GENERATED_KEYS);
 			preparedStatement.setDate(1, (java.sql.Date) sdf_returnDate);
 			preparedStatement.setInt(2, borrowing.getId());
 			
 			int affectedRows = preparedStatement.executeUpdate();

 	        if (affectedRows == 0) {
 	            throw new SQLException("Creating user failed, no rows affected.");
 	        }
 		}
 		catch(SQLException ex) {
 			ex.printStackTrace();
 		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void renewBorrowing(Borrowing borrowing) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
 			connection = initializeConnection();
 			
 			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
 			Date sdf_expirationDate = new java.sql.Date(sdf.parse(borrowing.getExpirationDate()).getTime());
 			
 			String selectSQL = "UPDATE borrowing SET expiration_date = ? WHERE id = ?";
 			preparedStatement = connection.prepareStatement(selectSQL, Statement.RETURN_GENERATED_KEYS);
 			preparedStatement.setDate(1, (java.sql.Date) sdf_expirationDate);
 			preparedStatement.setInt(2, borrowing.getId());
 			
 			preparedStatement.executeUpdate();
 		}
 		catch(SQLException ex) {
 			ex.printStackTrace();
 		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public List<Genre> retrieveGenres() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
 		ResultSet resultSet = null;
 		
		List<Genre> genres = new ArrayList<Genre>();
		
		try {
			connection = initializeConnection();
			preparedStatement = connection.prepareStatement("select * from genre");
			resultSet = preparedStatement.executeQuery();
 
 			while(resultSet.next()) { 
 				
 				int idGenre = resultSet.getInt("id");
 				String nameGenre = resultSet.getString("name"); 				
 				Genre genre = new Genre(idGenre, nameGenre);
 				genres.add(genre);
 			}
 		}
 		catch(SQLException e) {
 			e.printStackTrace();
 		}
		return genres;
	}
	
	
	
	private List<Borrowing> retrieveBorrowings(int idUser) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
 		ResultSet resultSet = null;
 		
		List<Borrowing> borrowings = new ArrayList<Borrowing>();
		
		try {
			connection = initializeConnection();
			preparedStatement = connection.prepareStatement("select * from borrowing where id_user = ?");
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
 				
 				Borrowing borrowing = new Borrowing(idBorrowing, book, sdf_borrowingDate, sdf_returnDate, sdf_expirationDate);
 				borrowings.add(borrowing);
 			}
 		}
 		catch(SQLException e) {
 			e.printStackTrace();
 		}
		return borrowings;
	}
	
	private List<Book> retrieveBooks(int idLibrary) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
 		ResultSet resultSet = null;
 		
 		List<Book> books = new ArrayList<Book>();
		
		try {
			connection = initializeConnection();
			preparedStatement = connection.prepareStatement("select * from book where id_library = ?");
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
	
	private Book retrieveBook(int idBook) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
 		ResultSet resultSet = null;
 		
		Book book = null;
		
		try {
			connection = initializeConnection();
			preparedStatement = connection.prepareStatement("select * from book where id = ?");
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
	
	private Genre retrieveGenre(int idGenre) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
 		ResultSet resultSet = null;
 		
		Genre genre = null;
		
		try {
			connection = initializeConnection();
			preparedStatement = connection.prepareStatement("select * from genre where id = ?");
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
	
	
}
