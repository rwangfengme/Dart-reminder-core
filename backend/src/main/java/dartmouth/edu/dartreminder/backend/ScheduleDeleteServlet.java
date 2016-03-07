package dartmouth.edu.dartreminder.backend;

/**
 * Created by LeoZhu on 3/6/16.
 */
    import java.io.IOException;
    import javax.servlet.ServletException;
    import javax.servlet.http.HttpServlet;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;

    import dartmouth.edu.dartreminder.backend.data.Datastore;


public class ScheduleDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        String id = req.getParameter("id");
        Datastore.deleteSchedule(id);
        resp.sendRedirect("/schedule_query.do");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        doGet(req, resp);
    }
}
