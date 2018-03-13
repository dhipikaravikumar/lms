package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.LibraryBranch;
import com.gcit.lms.entity.Publisher;

public class BookDAO extends BaseDAO<Book> {

	public BookDAO(Connection conn) {
		super(conn);
	}

	public void saveBook(Book book) throws SQLException {
		save("INSERT INTO tbl_book (title) VALUES (?)", new Object[] { book.getTitle() });
	}

	public void saveBookAuthor(Book book) throws SQLException {
		for (Author a : book.getAuthors()) {
			save("INSERT INTO tbl_book_authors VALUES (?, ?)", new Object[] { book.getBookId(), a.getAuthorId() });
		}
	}

	public void saveBookAuthor(Integer authorId, Integer bookId) throws SQLException {

		save("INSERT INTO tbl_book_authors VALUES (?, ?)", new Object[] { bookId, authorId });
	}
	
	public void saveBookGenre(Integer bookId, Integer genreId) throws SQLException {

		save("INSERT INTO tbl_book_genres VALUES (?, ?)", new Object[] { genreId, bookId });
	}

	public void saveBookGenre(Book book) throws SQLException {
		for (Genre g : book.getGenres()) {
			save("INSERT INTO tbl_book_genres VALUES (?, ?)", new Object[] { book.getBookId(), g.getGenreId() });
		}
	}

	public Integer saveBookID(Book book) throws SQLException {
		return saveWithID("INSERT INTO tbl_book (title) VALUES (?)", new Object[] { book.getTitle() });
	}

	public void updateBook(Book book) throws SQLException {
		save("UPDATE tbl_book SET title = ? WHERE bookId = ?", new Object[] { book.getTitle(), book.getBookId() });
	}

	public void updateBook1(Book book) throws SQLException {
		save("UPDATE tbl_book SET title = ?, pubId = ? WHERE bookId = ?",
				new Object[] { book.getTitle(), book.getPubId(), book.getBookId() });
	}
	
	public void updateBook2(Book book) throws SQLException {
		save("UPDATE tbl_book SET pubId = ? WHERE bookId = ?",
				new Object[] { book.getPubId(), book.getBookId() });
		 System.out.println("updateBook2");
	}

	public void updatePubId(Book book) throws SQLException {
		save("UPDATE tbl_book SET pubId = ? WHERE bookId = ?", new Object[] { book.getPubId(), book.getBookId() });
	}

	public void deleteBook(Book book) throws SQLException {
		save("DELETE FROM tbl_book WHERE bookId = ?", new Object[] { book.getBookId() });
	}

	public List<Book> readAllBooks() throws SQLException {
		return readAll("SELECT * FROM tbl_book", null);
	}

	public List<Book> readAllBooksByPK(int bookId) throws SQLException {
		return readAll("SELECT * FROM tbl_book WHERE bookId = ?", new Object[] { bookId });
	}
	//new
	//public List<Book> readAllBooksByPK(int bookId) throws SQLException {
		//return readAll("SELECT * FROM tbl_book WHERE bookId = ?", new Object[] { bookId });
	
	//}readBookByPK1
	/*public Author readBookByPK1(Integer bookId) throws SQLException {
		List<Book> books = readAll("SELECT * FROM tbl_author WHERE bookId = ?", new Object[]{bookId});
		if(books!=null){
			return books.get(0);
		}
		return null;*/

	public List<Book> readBooksByTitle(String bookTitle) throws SQLException {
		bookTitle = "%" + bookTitle + "%";
		return readAll("SELECT * FROM tbl_book WHERE title like ?", new Object[] { bookTitle });
	}

	public List<Book> readBooksBybranch(int branchId) throws SQLException {

		return readAll(
				"SELECT * FROM tbl_book WHERE bookId in (SELECT bookId FROM tbl_book_copies WHERE branchId = ? AND noOfCopies > 0)",
				new Object[] { branchId });
	}

	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException {
		AuthorDAO adao = new AuthorDAO(conn);
		GenreDAO gdao = new GenreDAO(conn);
		PublisherDAO pdao = new PublisherDAO(conn);
		List<Book> books = new ArrayList<>();
		while (rs.next()) {
			Book b = new Book();
			b.setBookId(rs.getInt("bookId"));
			b.setTitle(rs.getString("title"));
			b.setAuthors(adao.readAllFirstLevel(
					"SELECT * FROM tbl_author WHERE authorId IN (SELECT authorId FROM tbl_book_authors WHERE bookId = ?)",
					new Object[] { b.getBookId() }));
			b.setGenres(gdao.readAllFirstLevel(
					"SELECT * FROM tbl_genre WHERE genre_id IN (SELECT genre_id FROM tbl_book_genres WHERE bookId = ?)",
					new Object[] { b.getBookId() }));
			Publisher publisher = (Publisher) (pdao.readAllFirstLevel(
					"SELECT * FROM tbl_publisher WHERE publisherId IN (SELECT publisherId FROM tbl_book WHERE bookId = ?)",
					new Object[] { b.getBookId() })).get(0);
			b.setPublisher(publisher);
			// do the same for genres
			// do the same for One Publisher
			books.add(b);
		}
		return books;
	}

	@Override
	public List<Book> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<Book> books = new ArrayList<>();
		while (rs.next()) {
			Book b = new Book();
			b.setBookId(rs.getInt("bookId"));
			b.setTitle(rs.getString("title"));
			books.add(b);
		}
		return books;
	}

	public Integer getBookCount() throws SQLException {
		return getCount("SELECT count(*) as COUNT FROM tbl_book", null);
	}

	public List<Book> readBooks(String name, Integer pageNo) throws SQLException {
		setPageNo(pageNo);
		if (name != null && !name.isEmpty()) {
			name = "%" + name + "%";
			return readAll("SELECT * FROM tbl_book_branch WHERE title like ?", new Object[] { name });
		} else {
			return readAll("SELECT * FROM tbl_book", null);
		}

	}

	public Book readBookByPK(Integer bookId) throws SQLException {
		List<Book> books = readAll("SELECT * FROM tbl_book WHERE bookId = ?", new Object[] { bookId });
		if (books != null) {
			return books.get(0);
		}
		return null;
	}
}
