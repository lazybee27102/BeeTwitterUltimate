package com.codepath.apps.beetwitterultimate.Comment_Retweet_Like_Share_Activty;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.beetwitterultimate.Other_useful_class.GlobalVariable;
import com.codepath.apps.beetwitterultimate.R;
import com.codepath.apps.beetwitterultimate.models.Tweet;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends DialogFragment {


    private ImageView profile;
    private TextView name, others, status;
    private Button button_tweet;
    private getResponseFromComment delegate;
    private EditText text;


    public CommentFragment() {
    }

    public static CommentFragment newInstance(Tweet tweet, getResponseFromComment delegate) {

        CommentFragment frag = new CommentFragment();

        frag.setDelegate(delegate);

        Bundle args = new Bundle();

        args.putParcelable(GlobalVariable.COMMENT_TWEET_TRANSFER, tweet);

        frag.setArguments(args);

        return frag;

    }

    public getResponseFromComment getDelegate() {
        return delegate;
    }

    public void setDelegate(getResponseFromComment delegate) {
        this.delegate = delegate;
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_comment, container);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Tweet tweet = getArguments().getParcelable(GlobalVariable.COMMENT_TWEET_TRANSFER);
        profile = (ImageView) view.findViewById(R.id.imageView_mention_profile_image);
        name = (TextView) view.findViewById(R.id.textView_mention_profile_name);
        others = (TextView) view.findViewById(R.id.textView_mention_create_at);
        status = (TextView) view.findViewById(R.id.textView_comment_status);
        button_tweet = (Button) view.findViewById(R.id.button_comment_tweet);

        Glide.with(getContext()).load(tweet.getUser().getProfileImageURL()).placeholder(R.drawable.placeholder).into(profile);
        name.setText(tweet.getUser().getName());
        others.setText("@"+tweet.getUser().getScreenName() + " - "+tweet.getCreateAt());
        status.setText(tweet.getBody());

        button_tweet.setEnabled(false);

        text = (EditText)view.findViewById(R.id.editText_comment_text);
        if (tweet.getTags().size()!=0)
        {
            for (String s : tweet.getTags())
            {
                text.append(s);
            }
        }

        text.append("@"+ tweet.getUser().getScreenName()+" ");
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (text.getText().toString().trim().length() > 0)
                    button_tweet.setEnabled(true);
                else
                    button_tweet.setEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (text.getText().toString().trim().length() > 0)
                    button_tweet.setEnabled(true);
                else
                    button_tweet.setEnabled(false);
            }
        });






        this.button_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.accessResponse(text.getText().toString());
                dismiss();
            }
        });


    }


    public interface getResponseFromComment {
        void accessResponse(String s);
    }

}
