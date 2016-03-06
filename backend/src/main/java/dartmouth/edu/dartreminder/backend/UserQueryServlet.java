package dartmouth.edu.dartreminder.backend;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dartmouth.edu.dartreminder.backend.data.Datastore;
import dartmouth.edu.dartreminder.backend.data.UserAccount;

public class UserQueryServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    //query elements in the data store
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		ArrayList<UserAccount> result = Datastore.queryAll();
		req.setAttribute("result", result);
		getServletContext().getRequestDispatcher("/user_query_result.jsp").forward(
				req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doGet(req, resp);
	}
}
