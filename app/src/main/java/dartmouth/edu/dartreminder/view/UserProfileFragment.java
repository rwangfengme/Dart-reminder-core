package dartmouth.edu.dartreminder.view;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dartmouth.edu.dartreminder.R;

public class UserProfileFragment extends Fragment {

    private final RecentTimeListFragment mRecentTimeListFragment = new RecentTimeListFragment();

    Button mCancelButton;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mCancelButton = (Button) view.findViewById(R.id.Button_CancelProfile);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_page, mRecentTimeListFragment)
                        .commit();
            }
        });
        return view;
    }

}
