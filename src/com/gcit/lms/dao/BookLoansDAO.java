package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.entity.BookLoans;

public class BookLoansDAO extends BaseDAO<BookLoans> {

	public BookLoansDAO(Connection conn) {
		super(conn);

	}

	public void saveBookLoans(BookLoans bl) throws SQLException {

		if (bl.getBookId() != null && bl.getBranchId() != null && bl.getCardNo() != null) {

			if (bl.getDueDate() == null && bl.getDateIn() == null)
				save("INSERT INTO tbl_book_loans (bookId, branchId, cardNo, dateOut) VALUES (?,?,?,?)",
						new Object[] { bl.getBookId(), bl.getBranchId(), bl.getCardNo(), bl.getDateOut() });

			else if (bl.getDueDate() != null)
				save("INSERT INTO tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate) VALUES (?,?,?,?,?)",
						new Object[] { bl.getBookId(), bl.getBranchId(), bl.getCardNo(), bl.getDateOut(),
								bl.getDueDate() });

			else if (bl.getDateIn() != null)
				save("INSERT INTO tbl_book_loans (bookId, branchId, cardNo, dateOut, dateIn) VALUES (?,?,?,?,?)",
						new Object[] { bl.getBookId(), bl.getBranchId(), bl.getCardNo(), bl.getDateOut(),
								bl.getDateIn() });
			else
				save("INSERT INTO tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate, dateIn) VALUES (?,?,?,?,?,?)",
						new Object[] { bl.getBookId(), bl.getBranchId(), bl.getCardNo(), bl.getDateOut(),
								bl.getDueDate(), bl.getDateIn() });

		}
	}

	//this is actually return
	public void checkout(int cardNo, int branchId, int bookId) throws SQLException {
		save("UPDATE tbl_book_loans SET dateIn = CURDATE() WHERE bookId = ? AND branchId = ? AND cardNo = ?",
				new Object[] { bookId, branchId, cardNo });
	}
	
	public void checkout1(int cardNo, int branchId, int bookId) throws SQLException {
		save("INSERT INTO tbl_book_loans(bookId, branchId, cardNo, dateOut,dueDate,dateIn) VALUES (?,?,?,NOW(),NOW(),null)",
				new Object[] { bookId, branchId, cardNo });
	}
	
	

	public void updateBookLoans(BookLoans bl) throws SQLException {

		if (bl.getDueDate() != null && bl.getDateIn() == null) {
			save("UPDATE tbl_book_loans SET dueDate = ? WHERE bookId = ? AND branchId = ? AND cardNo = ? ",
					new Object[] { bl.getDueDate(), bl.getBookId(), bl.getBranchId(), bl.getCardNo() });

		}

		else if (bl.getDueDate() == null && bl.getDateIn() != null)
			save("UPDATE tbl_book_loans SET dateIn = ? WHERE bookId = ? AND branchId = ? AND cardNo = ? ",
					new Object[] { bl.getDateIn(), bl.getBookId(), bl.getBranchId(), bl.getCardNo() });

		else if (bl.getDueDate() != null && bl.getDateIn() != null)
			save("UPDATE tbl_book_loans SET dateIn = ?, dueDate = ? WHERE bookId = ? AND branchId = ? AND cardNo = ? ",
					new Object[] { bl.getDateIn(), bl.getDueDate(), bl.getBookId(), bl.getBranchId(), bl.getCardNo() });

	}

	public void deleteBookLoans(BookLoans bl) throws SQLException {
		save("DELETE FROM tbl_book_loans WHERE bookId = ? AND branchId = ? AND cardNo = ?",
				new Object[] { bl.getBookId(), bl.getBranchId(), bl.getCardNo() });
	}

	@Override
	public List<BookLoans> extractData(ResultSet rs) throws SQLException {

		List<BookLoans> bookLoans = new ArrayList<>();
		while (rs.next()) {
			BookLoans bl = new BookLoans();
			bl.setBookId(rs.getInt("bookId"));
			bl.setBranchId(rs.getInt("branchId"));
			bl.setCardNo(rs.getInt("cardNo"));
			bl.setDateIn(rs.getString("dateIn"));
			bl.setDateOut(rs.getString("dateOut"));
			bl.setDueDate(rs.getString("dueDate"));

			bookLoans.add(bl);
		}
		return bookLoans;
	}

	public List<BookLoans> readAllBookLoans() throws SQLException {
		return readAll("SELECT * FROM tbl_book_loans", null);

	}

	public List<BookLoans> readBookLoansByBranchBook(int bookId, int branchId) throws SQLException {
		return readAll("SELECT * FROM tbl_book_loans WHERE bookId = ? AND branchId = ?",
				new Object[] { bookId, branchId });

	}

	public List<BookLoans> readBookLoansByPK(int cardNo, int branchId, int bookId) throws SQLException {
		return readAll("SELECT * FROM tbl_book_loans WHERE cardNo = ? AND branchId = ? ",
				new Object[] { cardNo, branchId});

	}
	
	public List<BookLoans> readBookLoansByBranch(int branchId) throws SQLException {
		System.out.println("lowlevel");
		return readAll("SELECT * FROM tbl_book_loans WHERE branchId = ? ",
				new Object[] { branchId });

	}

	@Override
	public List<BookLoans> extractDataFirstLevel(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub

		List<BookLoans> bookLoans = new ArrayList<>();
		while (rs.next()) {
			BookLoans bl = new BookLoans();
			bl.setBookId(rs.getInt("bookId"));
			bl.setBranchId(rs.getInt("branchId"));
			bl.setCardNo(rs.getInt("cardNo"));
			bl.setDateIn(rs.getString("dateIn"));
			bl.setDateOut(rs.getString("dateOut"));
			bl.setDueDate(rs.getString("dueDate"));

			bookLoans.add(bl);
		}
		return bookLoans;
	}

}
