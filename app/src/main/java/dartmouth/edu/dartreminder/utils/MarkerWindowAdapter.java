package dartmouth.edu.dartreminder.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.Schedule;

/**
 * Created by _ReacTor on 16/3/4.
 */
public class MarkerWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private ArrayList<Schedule> mSchedules;
    private Context mContext;

    public MarkerWindowAdapter(ArrayList<Schedule> schedules, Context context){
        this.mSchedules = schedules;
        this.mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        int index = Integer.parseInt(marker.getTitle());
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View v = inflater.inflate(R.layout.marker_window, null);

        // Getting reference to the TextView to set latitude
        TextView tvLocName = (TextView) v.findViewById(R.id.loc_name);

        // Getting reference to the TextView to set longitude
        TextView tvTitle = (TextView) v.findViewById(R.id.loc_act_title);
        TextView tvNotes = (TextView) v.findViewById(R.id.loc_act_note);

        tvLocName.setText(mSchedules.get(index).getLocationName());
        tvTitle.setText(mSchedules.get(index).getTitle());
        tvNotes.setText(mSchedules.get(index).getNotes());

        // Returning the view containing InfoWindow contents
        return v;
    }
}
