package dartmouth.edu.dartreminder.backend;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dartmouth.edu.dartreminder.backend.data.Datastore;
import dartmouth.edu.dartreminder.backend.data.Schedule;

public class ScheduleFetchByIdServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    //query elements in the data store
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        String id = req.getParameter("Id");
        Schedule schedule = Datastore.getScheduleById(id);

        JSONArray resultSet = new JSONArray();
        JSONObject rowObject = new JSONObject();
        try {
            rowObject.put(Schedule.FIELD_NAME_ID, schedule.getId());
            rowObject.put(Schedule.FIELD_NAME_TITLE, schedule.getTitle());
            rowObject.put(Schedule.FIELD_NAME_NOTES, schedule.getNotes());
            rowObject.put(Schedule.FIELD_NAME_USE_TIME, schedule.getUseTime());
            rowObject.put(Schedule.FIELD_NAME_TIME, schedule.getTime());
            rowObject.put(Schedule.FIELD_NAME_LOCATION_NAME, schedule.getLocationName());
            rowObject.put(Schedule.FIELD_NAME_LAT, schedule.getLat());
            rowObject.put(Schedule.FIELD_NAME_LNG, schedule.getLng());
            rowObject.put(Schedule.FIELD_NAME_ARRIVE, schedule.getArrive());
            rowObject.put(Schedule.FIELD_NAME_RADIUS, schedule.getRadius());
            rowObject.put(Schedule.FIELD_NAME_PRIORITY, schedule.getPriority());
            rowObject.put(Schedule.FIELD_NAME_REPEAT, schedule.getRepeat());
            rowObject.put(Schedule.FIELD_NAME_IS_COMPLETED, schedule.getCompleted());
            rowObject.put(Schedule.FIELD_NAME_USER_NAME, schedule.getUserName());
            rowObject.put(Schedule.FIELD_NAME_SENDER, schedule.getSender());
        } catch (Exception e) {

        }
        resultSet.put(rowObject);

        PrintWriter writer = resp.getWriter();
        writer.write(resultSet.toString());
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        doGet(req, resp);
    }
}
