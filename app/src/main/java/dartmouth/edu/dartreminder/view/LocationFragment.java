package dartmouth.edu.dartreminder.view;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.CustomLocation;
import dartmouth.edu.dartreminder.utils.CustomLocationLoader;
import dartmouth.edu.dartreminder.utils.Globals;
import dartmouth.edu.dartreminder.utils.LocationGridViewAdapter;

public class LocationFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<CustomLocation>> {
    public static LoaderManager loaderManager;

    private GridView mLocationListGrid;
    private FloatingActionButton mAddLocationButton;
    private List<CustomLocation> mCustomLocationList;
    public static LocationGridViewAdapter gridViewAdapter;

    public LocationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_location, container, false);
        mLocationListGrid = (GridView) view.findViewById(R.id.GridView_Location_History);

        //initiate the AsyncTaskLoader
        loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, this).forceLoad();

        mLocationListGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent i = new Intent(getActivity(), LocationDetailActivity.class);
                i.putExtra(Globals.ADD_LOCATION, false);
                if (position < mCustomLocationList.size()){
                    CustomLocation customLocation = mCustomLocationList.get(position);
                    if (customLocation != null){
                        i.putExtra(Globals.LOCATION_TITLE, customLocation.getTitle());
                        i.putExtra(Globals.LOCATION_DETAIL, customLocation.getDetail());
                        i.putExtra(Globals.LOCATION_LAT, customLocation.getLatitude());
                        i.putExtra(Globals.LOCATION_LNG, customLocation.getLongitude());
                    }
                    startActivity(i);
                }
            }
        });

        mAddLocationButton = (FloatingActionButton) view.findViewById(R.id.add_location_button);
        mAddLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LocationDetailActivity.class);
                i.putExtra(Globals.ADD_LOCATION, true);
                startActivity(i);

            }
        });

        return view;
    }

    @Override
    public Loader<ArrayList<CustomLocation>> onCreateLoader(int id, Bundle args) {
        return new CustomLocationLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<CustomLocation>> loader, ArrayList<CustomLocation> data) {
        mCustomLocationList = data;
        gridViewAdapter = new LocationGridViewAdapter(getActivity(), mCustomLocationList);
        mLocationListGrid.setAdapter(gridViewAdapter);
        Log.d("TAGG", "Load Finished");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<CustomLocation>> loader) {
        gridViewAdapter = new LocationGridViewAdapter(getActivity(), mCustomLocationList);
        mLocationListGrid.setAdapter(gridViewAdapter);
    }
}
