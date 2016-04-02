package com.codepath.apps.beetwitterultimate.Tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.beetwitterultimate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MentionTabFragment extends Fragment {

    public static final String PARAM_CURRENT_USER_ID = "userID";
    public MentionTabFragment() {
        // Required empty public constructor
    }

    private static MentionTabFragment mentionTabFragment;

    public static MentionTabFragment getInstance(String id)
    {
        if(mentionTabFragment==null)
        {
            mentionTabFragment = new MentionTabFragment();
            Bundle b = new Bundle();
            b.putString(PARAM_CURRENT_USER_ID,id);
            mentionTabFragment.setArguments(b);
        }
        return mentionTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mention_tab, container, false);
    }

}
