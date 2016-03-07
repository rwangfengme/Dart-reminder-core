package dartmouth.edu.dartreminder.backend;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dartmouth.edu.dartreminder.backend.data.Datastore;

public class UserLoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    //query elements in the data store
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String userName = req.getParameter("userName");
        String pwd = req.getParameter("pwd");
		int result = Datastore.userLogin(userName, pwd);

        PrintWriter writer = resp.getWriter();
        writer.write(result + "");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doGet(req, resp);
	}
}
