package com.gcit.lms.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoans;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.LibraryBranch;
import com.gcit.lms.entity.Publisher;
import com.gcit.lms.service.AdminService;
import com.gcit.lms.service.LibrarianService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet({ "/addAuthor", "/deleteAuthor", "/editAuthor","/editBook", "/pageAuthors", "/pagePublishers", "/editPublisher",
		"/deletePublisher", "/addPublisher", "/pageBorrowers", "/editBorrower", "/deleteBorrower", "/addBorrower",
		"/pageBranches", "/editBranch", "/deleteBranch", "/addBranch", "/pageBooks", "/deleteBook", "/addBook",
		"/overRide", "/overRide2", "/submitDue" })
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AdminService adminService = new AdminService();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String reqUrl = request.getRequestURI().substring(request.getContextPath().length(),
				request.getRequestURI().length());
		String redirectUrl = "/viewpublisher.jsp";

		switch (reqUrl) {
		case "/deleteAuthor":
			redirectUrl = deleteAuthor(request, response, redirectUrl);
			break;

		case "/deletePublisher":
			redirectUrl = deletePublisher(request, response, redirectUrl);
			break;

		case "/deleteBorrower":
			redirectUrl = deleteBorrower(request, response, redirectUrl);
			break;

		case "/deleteBranch":
			redirectUrl = deleteBranch(request, response, redirectUrl);
			break;

		case "/deleteBook":
			redirectUrl = deleteBook(request, response, redirectUrl);
			break;

		case "/pageAuthors":
			redirectUrl = pageAuthors(request, response, redirectUrl);
			break;

		case "/pageBorrowers":
			redirectUrl = pageBorrowers(request, response, redirectUrl);
			break;

		case "/pageBranches":
			redirectUrl = pageBranches(request, response, redirectUrl);
			break;

		case "/pageBooks":
			redirectUrl = pageBooks(request, response, redirectUrl);
			break;

		case "/pagePublishers":
			redirectUrl = pagePublishers(request, response, redirectUrl);
			break;

		
		default:
			break;
		}
		RequestDispatcher rd = request.getRequestDispatcher(redirectUrl);
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String reqUrl = request.getRequestURI().substring(request.getContextPath().length(),
				request.getRequestURI().length());
		String redirectUrl = "/viewpublisher.jsp";
		switch (reqUrl) {
		case "/addAuthor":
			redirectUrl = addAuthor(request, redirectUrl, false);
			break;
		case "/editAuthor":
			redirectUrl = addAuthor(request, redirectUrl, true);
			break;
		case "/editBook":
			redirectUrl = addBook(request, redirectUrl);
			break;

		case "/editPublisher":
			redirectUrl = editPublisher(request, response, redirectUrl);
			break;

		case "/addPublisher":
			redirectUrl = addPublisher(request, redirectUrl);
			break;

		case "/addBook":
			redirectUrl = addBook(request, redirectUrl);
			break;

		case "/addBorrower":
			redirectUrl = addBorrower(request, redirectUrl);
			break;

		case "/addBranch":
			redirectUrl = addBranch(request, redirectUrl);
			break;

		case "/editBorrower":
			redirectUrl = editBorrower(request, response, redirectUrl);
			break;

		case "/editBranch":
			redirectUrl = editBranch(request, response, redirectUrl);
			break;

		case "/overRide":
			redirectUrl = overRide(request, response, redirectUrl);
			break;

		case "/overRide2":
			redirectUrl = overRide2(request, response, redirectUrl);
			break;
			
		case "/submitDue":
			redirectUrl = submitDue(request, response, redirectUrl);
			break;	
		 
		default:
			break;
		}

		RequestDispatcher rd = request.getRequestDispatcher(redirectUrl);
		rd.forward(request, response);

	}

	private String addAuthor(HttpServletRequest request, String redirectUrl, Boolean editMode) {
		Author author = new Author();
		String message = "Author added Sucessfully";

		if (request.getParameter("authorName") != null && !request.getParameter("authorName").isEmpty()) {
			if (request.getParameter("authorName").length() > 45) {
				message = "Author Name cannot be more than 45 chars";
				redirectUrl = "/addauthor.jsp";
			} else {
				author.setAuthorName(request.getParameter("authorName"));
				// String[] bookIds = request.getParameterValues("bookIds");
				if (editMode) {
					author.setAuthorId(Integer.parseInt(request.getParameter("authorId")));
				}
				try {
					adminService.saveAuthor(author);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			message = "Author Name cannot be Empty";
			redirectUrl = "/addauthor.jsp";
		}
		request.setAttribute("statusMessage", message);
		return redirectUrl;
	}

	private String deleteAuthor(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {
		String message = "Author deleted Sucessfully";
		if (request.getParameter("authorId") != null) {
			Integer authorId = Integer.parseInt(request.getParameter("authorId"));
			Author author = new Author();
			author.setAuthorId(authorId);
			try {
				adminService.deleteAuthor(author);
			} catch (SQLException e) {
				e.printStackTrace();
				message = "Author deleted failed. Contact Admin";
			}
			request.setAttribute("statusMessage", message);
			return redirectUrl;
		}

		return null;
	}

	private String pageAuthors(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {
		if (request.getParameter("pageNo") != null) {
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			try {
				request.setAttribute("authors", adminService.readAuthors(null, pageNo));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return "viewauthors.jsp";
		}

		return null;
	}

	private String pagePublishers(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {
		if (request.getParameter("pageNo") != null) {
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			try {
				request.setAttribute("publishers", adminService.readPublishers(null, pageNo));
				System.out.println("arezoo 21");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return "viewpublisher.jsp";
		}

		return null;
	}

	private String pageBorrowers(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {
		if (request.getParameter("pageNo") != null) {
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			try {
				request.setAttribute("borrowers", adminService.readBorrowers(null, pageNo));
				System.out.println("arezoo 21");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return "viewborrowers.jsp";
		}

		return null;
	}

	private String editPublisher(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {

		redirectUrl = "editpublisher.jsp";
		String message = null;

		Publisher p = new Publisher();
		p.setPublisherId(Integer.parseInt(request.getParameter("publisherId")));
	

		if (request.getParameter("publisherName") != null && !request.getParameter("publisherName").isEmpty()) {
			p.setPublisherName(request.getParameter("publisherName"));
			
		}
		if (request.getParameter("publisherAddress") != null && !request.getParameter("publisherAddress").isEmpty()) {
			p.setPublisherAddress("publisherAddress");
			
		}

		// System.out.println(lb.getBranchName());
		// System.out.println(lb.getBranchAddress());

		if ((request.getParameter("publisheName") == null || request.getParameter("publisherName").isEmpty())
				&& (request.getParameter("publisherAddress") == null
						|| request.getParameter("publisherAddress").isEmpty())
				&& (request.getParameter("publisherPhone") == null
						|| request.getParameter("publisherPhone").isEmpty())) {

			redirectUrl = "editpublisher.jsp";
			message = "You have not entered anything";
			request.setAttribute("statusMessage", message);
			return redirectUrl;
		}
		try {

			adminService.updatePublisher(p);

			System.out.println("here");
			redirectUrl = "viewpublisher.jsp";
			message = "Successfully Updated!";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("statusMessage", message);
		return redirectUrl;
	}

	// next method here
	private String deletePublisher(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {

		String message = null;
		redirectUrl = null;

		if (request.getParameter("publisherId") != null) {
			Integer publisherId = Integer.parseInt(request.getParameter("publisherId"));
			Publisher p = new Publisher();
			p.setPublisherId(publisherId);
			try {
				adminService.deletePublisher(p);
				message = "Publisher deleted Sucessfully";
				redirectUrl = "viewpublisher.jsp";

			} catch (SQLException e) {
				e.printStackTrace();
				message = "Author deleted failed. Contact Admin";
			}
			request.setAttribute("statusMessage", message);
			return redirectUrl;
		}

		return null;
	}

	private String addPublisher(HttpServletRequest request, String redirectUrl) {
		Publisher p = new Publisher();
		String message = null;
		redirectUrl = null;
		int pubId = -1;
		String[] bookIds = null;

		if (request.getParameter("publisherName") != null && !request.getParameter("publisherName").isEmpty()) {
			if (request.getParameter("publisherName").length() > 45) {
				message = "Publisher Name cannot be more than 45 chars";
				redirectUrl = "/addpublisher.jsp";
			} else {
				try {
					System.out.println("publisherName:" + request.getParameter("publisherName"));
					p.setPublisherName(request.getParameter("publisherName"));
					// pubId = adminService.savePublisherId(p);
					p.setPublisherId(pubId);

					System.out.println("dp");
					if (request.getParameter("publisherAddress") != null
							&& !request.getParameter("publisherAddress").isEmpty())
						p.setPublisherAddress(request.getParameter("publisherAddress"));
					System.out.println("dp");
					if (request.getParameter("publisherPhone") != null
							&& !request.getParameter("publisherPhone").isEmpty())
						p.setPublisherPhone(request.getParameter("publisherPhone"));
					System.out.println("dp");

					if (request.getParameter("bookIds") != null && !request.getParameter("bookIds").isEmpty()) {
						bookIds = request.getParameterValues("bookIds");
						System.out.println("bookIds");

					}
					pubId = adminService.savePublisherId(p);
					System.out.println("pub id" + pubId);
					if (bookIds != null) {
						for (String bookId : bookIds) {
							Book book = new Book();
							book.setBookId(Integer.parseInt(bookId));
							book.setPubId(pubId);
							String title = (adminService.readBookByPK(Integer.parseInt(bookId))).getTitle();
							book.setTitle(title);
							System.out.println("dp3");
							adminService.updateBook1(book);
						}
						message = "Publisher Successfully Added";
						redirectUrl = "addpublisher.jsp";
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			message = "Publisher Name cannot be Empty";
			redirectUrl = "/addpublisher.jsp";
		}
		request.setAttribute("statusMessage", message);
		return redirectUrl;
	}

	private String editBranch(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {

		redirectUrl = "editbranch.jsp";
		String message = null;

		LibraryBranch b = new LibraryBranch();
		b.setBranchId(Integer.parseInt(request.getParameter("branchId")));
		// System.out.println(b.getBranchId());

		if (request.getParameter("name") != null && !request.getParameter("name").isEmpty()) {
			b.setBranchName(request.getParameter("name"));
			System.out.println(b.getBranchName());
		}
		if (request.getParameter("address") != null && !request.getParameter("address").isEmpty()) {
			b.setBranchAddress(request.getParameter("address"));
			System.out.println(b.getBranchAddress());
		}

		// System.out.println(lb.getBranchName());
		// System.out.println(lb.getBranchAddress());

		if ((request.getParameter("name") == null || request.getParameter("name").isEmpty())
				&& (request.getParameter("address") == null || request.getParameter("address").isEmpty())) {

			redirectUrl = "editbranch.jsp";
			message = "You have not entered anything";
			request.setAttribute("statusMessage", message);
			return redirectUrl;
		}
		try {

			adminService.updateBranch(b);

			
			redirectUrl = "viewbranch.jsp";
			message = "Successfully Updated!";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("statusMessage", message);
		return redirectUrl;
	}
	//new
	
	/*private String editBook(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {

		redirectUrl = "editbook.jsp";
		String message = null;

		LibraryBranch b = new LibraryBranch();
		b.setBranchId(Integer.parseInt(request.getParameter("bookId")));
		// System.out.println(b.getBranchId());

		if (request.getParameter("title") != null && !request.getParameter("title").isEmpty()) {
			b.setBranchName(request.getParameter("title"));
			System.out.println(b.getBranchName());
		}
		
		

		// System.out.println(lb.getBranchName());
		// System.out.println(lb.getBranchAddress());

		if ((request.getParameter("title") == null || request.getParameter("title").isEmpty())) {

			redirectUrl = "editbook.jsp";
			message = "You have not entered anything";
			request.setAttribute("statusMessage", message);
			return redirectUrl;
		}
		try {

			adminService.updateBranch(b);

			
			redirectUrl = "viewbooks.jsp";
			message = "Successfully Updated!";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("statusMessage", message);
		return redirectUrl;
	}*/

	private String deleteBorrower(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {

		String message = null;
		redirectUrl = null;

		if (request.getParameter("cardNo") != null) {
			Integer cardNo = Integer.parseInt(request.getParameter("cardNo"));
			Borrower b = new Borrower();
			b.setCardNo(cardNo);
			try {
				adminService.deleteBorrower(b);
				message = "Borrower deleted Sucessfully";
				redirectUrl = "viewborrowers.jsp";

			} catch (SQLException e) {
				e.printStackTrace();
				message = "Author deleted failed. Contact Admin";
			}
			request.setAttribute("statusMessage", message);
			return redirectUrl;
		}

		return null;
	}

	private String addBorrower(HttpServletRequest request, String redirectUrl) {
		Borrower b = new Borrower();
		String message = "Author added Sucessfully";

		if (request.getParameter("name") != null && !request.getParameter("name").isEmpty())
			b.setName(request.getParameter("name"));

		if (request.getParameter("address") != null && !request.getParameter("address").isEmpty())
			b.setAddress(request.getParameter("address"));

		if (request.getParameter("phone") != null && !request.getParameter("phone").isEmpty())
			b.setPhone(request.getParameter("phone"));
		if (request.getParameter("cardNo") != null && !request.getParameter("cardNo").isEmpty())
			b.setCardNo(Integer.parseInt(request.getParameter("cardNo")));
		System.out.println(b.getName());
		System.out.println(b.getAddress());
		System.out.println(b.getPhone());

		if ((request.getParameter("name") == null || request.getParameter("name").isEmpty())
				&& (request.getParameter("address") == null || request.getParameter("address").isEmpty())
				&& (request.getParameter("phone") == null || request.getParameter("phone").isEmpty())
				&& (request.getParameter("cardNo") == null || request.getParameter("cardNo").isEmpty())) {

			redirectUrl = "addborrower.jsp";
			message = "You have not entered anything";
			request.setAttribute("statusMessage", message);
			return redirectUrl;

		}
		try {
			adminService.saveBorrower(b);
			System.out.println("after save");
			redirectUrl = "/addborrower.jsp";

		} catch (SQLException e) {
			e.printStackTrace();

		}

		request.setAttribute("statusMessage", message);
		return redirectUrl;
	}

	// insert next method

	private String pageBranches(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {
		if (request.getParameter("pageNo") != null) {
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			try {
				request.setAttribute("branches", adminService.readBranches(null, pageNo));
				System.out.println("a1");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return "viewbranch.jsp";
		}

		return null;
	}

	private String editBorrower(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {

		redirectUrl = "editborrower.jsp";
		String message = null;

		Borrower b = new Borrower();
		b.setCardNo(Integer.parseInt(request.getParameter("cardNo")));
		// System.out.println(b.getBranchId());

		if (request.getParameter("name") != null && !request.getParameter("name").isEmpty()) {
			b.setName(request.getParameter("name"));
			// System.out.println(b.getBranchName());
		}
		if (request.getParameter("address") != null && !request.getParameter("address").isEmpty()) {
			b.setAddress(request.getParameter("address"));
			// System.out.println(b.getBranchAddress());
		}

		if (request.getParameter("phone") != null && !request.getParameter("phone").isEmpty()) {
			b.setPhone(request.getParameter("phone"));
			// System.out.println(b.getBranchAddress());
		}

		// System.out.println(lb.getBranchName());
		// System.out.println(lb.getBranchAddress());

		if ((request.getParameter("name") == null || request.getParameter("name").isEmpty())
				&& (request.getParameter("address") == null || request.getParameter("address").isEmpty())
				&& (request.getParameter("phone") == null || request.getParameter("phone").isEmpty())) {

			redirectUrl = "editborrower.jsp";
			message = "you have not entered anything!";
			request.setAttribute("statusMessage", message);
			return redirectUrl;
		}
		try {

			adminService.updateBorrower(b);

			// System.out.println("here");
			redirectUrl = "viewborrowers.jsp";
			message = "Successfully Updated!";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("statusMessage", message);
		return redirectUrl;
	}

	private String deleteBranch(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {

		String message = null;
		redirectUrl = null;

		if (request.getParameter("branchId") != null) {
			Integer branchId = Integer.parseInt(request.getParameter("branchId"));
			LibraryBranch lb = new LibraryBranch();
			lb.setBranchId(branchId);
			try {
				adminService.deleteBranch(lb);
				message = "LibraryBranch deleted Sucessfully";
				redirectUrl = "viewbranch.jsp";

			} catch (SQLException e) {
				e.printStackTrace();
				message = "Author deleted failed. Contact Admin";
			}
			request.setAttribute("statusMessage", message);
			return redirectUrl;
		}

		return null;
	}

	private String addBranch(HttpServletRequest request, String redirectUrl) {
		LibraryBranch lb = new LibraryBranch();
		String message = "Branch added Sucessfully";

		if (request.getParameter("name") != null && !request.getParameter("name").isEmpty())
			lb.setBranchName(request.getParameter("name"));

		if (request.getParameter("address") != null && !request.getParameter("address").isEmpty())
			lb.setBranchAddress(request.getParameter("address"));

		System.out.println(lb.getBranchName());
		System.out.println(lb.getBranchAddress());

		if ((request.getParameter("name") == null || request.getParameter("name").isEmpty())
				&& (request.getParameter("address") == null || request.getParameter("address").isEmpty())) {

			redirectUrl = "addbranch.jsp";
			message = "You have not entered anything";
			request.setAttribute("statusMessage", message);
			return redirectUrl;

		}
		try {
			adminService.saveBranch(lb);
			System.out.println("after save");
			redirectUrl = "/addbranch.jsp";

		} catch (SQLException e) {
			e.printStackTrace();

		}

		request.setAttribute("statusMessage", message);
		return redirectUrl;
	}

	private String pageBooks(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {
		if (request.getParameter("pageNo") != null) {
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			try {
				request.setAttribute("books", adminService.readBooks(null, pageNo));
				System.out.println("arezoo 21");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return "viewbooks.jsp";
		}

		return null;
	}

	private String deleteBook(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {

		String message = null;
		redirectUrl = null;

		if (request.getParameter("bookId") != null) {
			Integer bookId = Integer.parseInt(request.getParameter("bookId"));
			Book book = new Book();
			book.setBookId(bookId);
			try {
				adminService.deleteBook(book);
				message = "Book deleted Sucessfully";
				redirectUrl = "viewbooks.jsp";

			} catch (SQLException e) {
				e.printStackTrace();
				message = "Author deleted failed. Contact Admin";
			}
			request.setAttribute("statusMessage", message);
			return redirectUrl;
		}

		return null;
	}

	private String addBook(HttpServletRequest request, String redirectUrl) {
		Book book = new Book();
		String message = null;
		redirectUrl = null;
		Integer bookId = null;

		if (request.getParameter("title") != null && !request.getParameter("title").isEmpty()) {

			try {
				book.setTitle(request.getParameter("title"));
				bookId = adminService.saveBookId(book);
				book.setBookId(bookId);

				if (request.getParameter("publisherId") != null && !request.getParameter("publisherId").isEmpty()) {
					book.setPubId(Integer.parseInt(request.getParameter("publisherId")));
					System.out.println("pubId" + book.getPubId());
					adminService.updateBook2(book);
				}

				if (request.getParameter("authorId") != null && !request.getParameter("authorId").isEmpty()) {
					for (String authorId : request.getParameterValues("authorId"))
						adminService.saveBookAuthor(bookId, Integer.parseInt(authorId));

				}

				if (request.getParameter("genreId") != null && !request.getParameter("genreId").isEmpty()) {
					for (String genreId : request.getParameterValues("genreId"))
						adminService.saveBookGenre(bookId, Integer.parseInt(genreId));

				}
				message = "Book Inserted Successfully";
				redirectUrl = "addbook.jsp";
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
			message = "feild Title cannot be empty";
			redirectUrl = "addbook.jsp";
		}

		request.setAttribute("statusMessage", message);
		return redirectUrl;
	}

	private String overRide2(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {

		String message = null;
		// System.out.println(request.getParameter("branchIds") +" branchId");

		if (request.getParameter("bookIds") != null && !request.getParameter("bookIds").isEmpty()) {
			System.out.println("a1");

			redirectUrl = "override3.jsp";
			try {

				int bookId = Integer.parseInt(request.getParameter("bookIds"));
				int branchId = Integer.parseInt(request.getParameter("branchIds"));
				Book book = adminService.readBookByPK(bookId);
				LibraryBranch branch = adminService.readBranchByPK(branchId);
				List<BookLoans> bookloans = adminService.readBookLoansByBranchBook(bookId, branchId);

				System.out.println("a2");

				int cardNo = bookloans.get(0).getCardNo();
				System.out.println("a3");

				Borrower borrower = adminService.readBorrowerByPK(cardNo);
				AdminService.borrower = borrower;
				
				message = "You Want To Overide Duedate For Librarybranch: " + branch.getBranchName() + " And Book: "
						+ book.getTitle() + " And Borrower: " + borrower.getName();

				//System.out.println("arezoo1");

			} catch (NumberFormatException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			message = "you have to choose a book";
			redirectUrl = "override2.jsp";
		}
		request.setAttribute("statusMessage", message);
		return redirectUrl;
	}
	
	private String overRide(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {

		String message = null;
		// System.out.println(request.getParameter("branchIds") +" branchId");

		if (request.getParameter("branchIds") != null && !request.getParameter("branchIds").isEmpty()) {
			System.out.println("a1");

			redirectUrl = "override2.jsp";
			try {

				//int bookId = Integer.parseInt(request.getParameter("bookIds"));
				int branchId = Integer.parseInt(request.getParameter("branchIds"));
				List<BookLoans> bookloans = adminService.readBookLoansByBranch( branchId);

				System.out.println("a2");



				System.out.println("a3");
				for(BookLoans bl: bookloans) {

				Book book = adminService.readBookByPK(bl.getBookId());
				AdminService.books.add(book);
				
				}

			} catch (NumberFormatException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			message = "you have to choose a book";
			redirectUrl = "override2.jsp";
		}
		request.setAttribute("statusMessage", message);
		return redirectUrl;
	}
	
	///////////////////
	private String submitDue(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {

		String message = null;
		// System.out.println(request.getParameter("branchIds") +" branchId");

		if (request.getParameter("dueDate") != null && !request.getParameter("dueDate").isEmpty()) {
			System.out.println("a1");

			redirectUrl = "override.jsp";
			try {

				int bookId = Integer.parseInt(request.getParameter("bookIds"));
				int branchId = Integer.parseInt(request.getParameter("branchIds"));
				int cardNo = AdminService.borrower.getCardNo();
				String dueDate = request.getParameter("dueDate");
				BookLoans bl = new BookLoans();
				bl.setBookId(bookId);
				bl.setBranchId(branchId);
				bl.setCardNo(cardNo);
				bl.setDueDate(dueDate);
				
				adminService.updateBookLoans(bl);

				
				message = "DueDate Successfully overRiden";
				redirectUrl = "override.jsp";

			} catch (NumberFormatException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			message = "you have to enter a new dueDate";
			redirectUrl = "override3.jsp";
		}
		request.setAttribute("statusMessage", message);
		return redirectUrl;
	}
}
