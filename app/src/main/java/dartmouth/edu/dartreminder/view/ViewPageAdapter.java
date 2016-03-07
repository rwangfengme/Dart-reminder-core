package dartmouth.edu.dartreminder.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

import dartmouth.edu.dartreminder.utils.Globals;


public class ViewPageAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    public static final int START = 0;
    public static final int HISTORY = 1;
    public static final int SETTINGS = 2;

    public ViewPageAdapter(FragmentManager fm, ArrayList<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    public Fragment getItem(int pos){
        return fragments.get(pos);
    }

    public int getCount(){
        return fragments.size();
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case START:
                return Globals.UI_TAB_ALL;
            case HISTORY:
                return Globals.UI_TAB_MAP;
            case SETTINGS:
                return Globals.UI_TAB_ACTIVITY;
            default:
                break;
        }
        return null;
    }
}
