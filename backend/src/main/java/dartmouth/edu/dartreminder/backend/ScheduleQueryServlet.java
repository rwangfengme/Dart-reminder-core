package dartmouth.edu.dartreminder.backend;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dartmouth.edu.dartreminder.backend.data.Datastore;
import dartmouth.edu.dartreminder.backend.data.Schedule;

public class ScheduleQueryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    //query elements in the data store
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        String userName = req.getParameter("userName");
        ArrayList<Schedule> result;
        if (userName == null || userName.length() == 0){
            result = Datastore.queryAllSchedule();
        }else{
            result = Datastore.fetchScheduleByUserName(userName);
        }

        req.setAttribute("result", result);
        getServletContext().getRequestDispatcher("/schedule_query_result.jsp").forward(
                req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        doGet(req, resp);
    }
}
