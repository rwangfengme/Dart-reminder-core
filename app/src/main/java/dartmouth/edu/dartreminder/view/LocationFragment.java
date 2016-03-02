package dartmouth.edu.dartreminder.view;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.utils.LocationGridViewAdapter;

public class LocationFragment extends Fragment {

    public LocationFragment() {
        // Required empty public constructor
    }

    private GridView mLocationListGrid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_location, container, false);
        mLocationListGrid = (GridView) view.findViewById(R.id.GridView_Location_History);
        mLocationListGrid.setAdapter(new LocationGridViewAdapter(getActivity().getApplicationContext()));

        mLocationListGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

            }
        });

        return view;
    }

}
