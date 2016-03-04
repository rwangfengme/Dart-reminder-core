package dartmouth.edu.dartreminder.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import dartmouth.edu.dartreminder.R;

public class UserProfileFragment extends Fragment {

    private final RecentTimeListFragment mRecentListFragment = new RecentTimeListFragment();

    private Button mSaveButton;
    private Button mCancelButton;

    private EditText mUserName;
    private EditText mUserEmail;
    private EditText mUserPhone;

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

        mUserName = (EditText) view.findViewById(R.id.EditText_User_Name);
        mUserEmail = (EditText) view.findViewById(R.id.EditText_User_Email);
        mUserPhone = (EditText) view.findViewById(R.id.EditText_User_Phone);

        LoadProfile();

        mSaveButton = (Button) view.findViewById(R.id.Button_SaveProfile);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_page, mRecentListFragment)
                        .commit();
            }
        });

        mCancelButton = (Button) view.findViewById(R.id.Button_CancelProfile);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_page, mRecentListFragment)
                        .commit();
            }
        });
        return view;
    }

    private void LoadProfile() {
        SharedPreferences userProfile = getActivity().getSharedPreferences("userProfile", getActivity().MODE_PRIVATE);
        String name = userProfile.getString("USER", null);
        String email = userProfile.getString("USERNAME", null);
        String phone = userProfile.getString("USERPHONE", null);
        mUserName.setText(name);
        mUserEmail.setText(email);
        mUserPhone.setText(phone);
    }

    private void saveProfile() {
        SharedPreferences prefs = getActivity().getSharedPreferences("userProfile", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("USER", mUserName.getText().toString());
        editor.putString("USERPHONE", mUserPhone.getText().toString());
        editor.apply();
    }

}
