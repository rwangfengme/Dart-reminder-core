package dartmouth.edu.dartreminder.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.CustomLocation;

/**
 * Created by gejing on 3/1/16.
 */
public class LocationGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private int[] mThumbIds = Globals.LOCATION_LIST;
    private List<CustomLocation> customLocationList;

    public LocationGridViewAdapter(Context c, List<CustomLocation> customLocationList) {
        mContext = c;
        this.customLocationList = customLocationList;
    }

    public int getCount() {
        return customLocationList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = inflater.inflate(R.layout.location_history_list_item, null);
            TextView textView = (TextView) grid.findViewById(R.id.TextView_Grid_Location);
            ImageView imageView = (ImageView)grid.findViewById(R.id.ImageView_Grid_Location);
            if (customLocationList != null && customLocationList.size() > position){
                CustomLocation customLocation = customLocationList.get(position);
                if (customLocation != null){
                    textView.setText(customLocation.getTitle());
                    imageView.setImageResource(customLocation.getIcon());
                }
            }
        } else {
            grid = convertView;
        }

        return grid;
    }

    public void add(CustomLocation customLocation){
        this.customLocationList.add(customLocation);
        notifyDataSetChanged();
    }
}
