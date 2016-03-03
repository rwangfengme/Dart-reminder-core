package dartmouth.edu.dartreminder.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import dartmouth.edu.dartreminder.R;

/**
 * Created by gejing on 3/1/16.
 */
public class LocationGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private int[] mThumbIds = Globals.LOCATION_LIST;

    public LocationGridViewAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
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
            textView.setText("Sudikoff");
            imageView.setImageResource(mThumbIds[position]);
        } else {
            grid = convertView;
        }

        return grid;
    }

}
