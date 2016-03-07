package dartmouth.edu.dartreminder.backend;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dartmouth.edu.dartreminder.backend.data.Datastore;
import dartmouth.edu.dartreminder.backend.data.UserAccount;

public class AddUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
        String userName = req.getParameter("userName");
        String pwd = req.getParameter("pwd");

        if (userName == null || userName.equals("")) {
            req.setAttribute("_retStr", "invalid input");
            getServletContext().getRequestDispatcher("/user_query_result.jsp")
                    .forward(req, resp);
            return;
        }

        UserAccount userAccount = new UserAccount(userName, pwd);
        int success = Datastore.addUser(userAccount);

        if (success == 0) {
            req.setAttribute("_retStr", "Add contact " + userName + " success");
            MessagingEndpoint msg = new MessagingEndpoint();
            msg.sendMessage("SignedIn");
        }

        PrintWriter writer = resp.getWriter();
        writer.write(success + "");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doGet(req, resp);
	}

}
