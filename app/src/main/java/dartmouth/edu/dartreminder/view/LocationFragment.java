package dartmouth.edu.dartreminder.view;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dartmouth.edu.dartreminder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {


    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        

        return view;
    }

}
