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
import dartmouth.edu.dartreminder.backend.data.Schedule;

public class ScheduleAddByNameServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String json = req.getParameter("ScheduleKey");
        ArrayList<Schedule> result = new ArrayList<>();
        int ret = 0;
        try{
            JSONArray resultSet = new JSONArray(json);
            //delete all the elements in the data store
            int length = resultSet.length();
            for (int i = 0; i < length; i++){
                //obtain attribute value from json object
                JSONObject scheduleElement = resultSet.getJSONObject(i);

                String id = scheduleElement.getString("Id");
                String title = scheduleElement.getString("Title");
                String notes = scheduleElement.getString("Notes");
                boolean useTime = Boolean.parseBoolean(scheduleElement.getString("UseTime"));
                String time = scheduleElement.getString("Time");
                String locationName = scheduleElement.getString("LocationName");
                double lat = Double.parseDouble(scheduleElement.getString("Lat"));
                double lng = Double.parseDouble(scheduleElement.getString("Lng"));
                boolean arrive = Boolean.parseBoolean(scheduleElement.getString("Arrive"));
                double radius = Double.parseDouble(scheduleElement.getString("Radius"));
                String priority = scheduleElement.getString("Priority");
                String repeat = scheduleElement.getString("Repeat");
                boolean isCompleted = Boolean.parseBoolean(scheduleElement.getString("isCompleted"));
                String userName = scheduleElement.getString("userName");
                String sender = scheduleElement.getString("sender");

                //store the exercise entry into data store if id is not null
                if (id != null && !id.isEmpty()){
                    Schedule schedule = new Schedule(id, title, notes, useTime, time, locationName,
                            lat, lng, arrive, radius, priority, repeat, isCompleted, userName, sender);
                    ret = Datastore.addSchedule(schedule);
                    if (ret == 1) {
                        req.setAttribute("_retStr", "Add schedule " + id + " success");
                        result.add(schedule);
                        req.setAttribute("result", result);
                    }else{
                        req.setAttribute("_retStr", id + " exists");
                    }
                }
            }
        }catch (JSONException e){

        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        doGet(req, resp);
    }

}
