package dartmouth.edu.dartreminder.view;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.appengine.labs.repackaged.org.json.JSONArray;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.DartReminderDBHelper;
import dartmouth.edu.dartreminder.data.Schedule;
import dartmouth.edu.dartreminder.server.ServerUtilities;
import dartmouth.edu.dartreminder.service.TimeReceiver;
import dartmouth.edu.dartreminder.utils.Globals;
import dartmouth.edu.dartreminder.utils.Utils;

public class UserProfileFragment extends Fragment {

    //private final RecentListFragment mRecentListFragment = new RecentListFragment();

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
                /*getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_page, mRecentListFragment)
                        .commit();*/

                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                //getActivity().getFragmentManager().beginTransaction().remove(UserProfileFragment.this).commit();

                //MainActivity.slidingTabLayout.setVisibility(View.VISIBLE);
                //MainActivity.viewPager.setVisibility(View.VISIBLE);
            }
        });

        mCancelButton = (Button) view.findViewById(R.id.Button_CancelProfile);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_page, mRecentListFragment)
                        .commit();*/
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
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
