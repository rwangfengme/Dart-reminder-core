package dartmouth.edu.dartreminder.backend;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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
            getServletContext().getRequestDispatcher("/query_result.jsp")
                    .forward(req, resp);
            return;
        }

        UserAccount userAccount = new UserAccount(userName, pwd);
        boolean success = Datastore.addUser(userAccount);

        if (success) {
            req.setAttribute("_retStr", "Add contact " + userName + " success");
            MessagingEndpoint msg = new MessagingEndpoint();
            msg.sendMessage("SignedIn");
        } else {
            req.setAttribute("_retStr", userName + " exists");
        }

		getServletContext().getRequestDispatcher("/user_query_result.jsp").forward(
				req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doGet(req, resp);
	}

}
