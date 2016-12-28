package com.only.dawns.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private TextView mAnswerTextView;
    private TextView mApiLevelTextView;

    private static final String EXTRA_ANSWER_IS_TRUE = "com.only.dawns.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.only.dawns.geoquiz.answer_shown";
    private boolean mAnswerIsTrue;

    private static final String TAG = "CheatActivity";
    private static final String IS_ANSWER_SHOWN = "isAnswerShown";
    private static boolean mIsAnswerShown;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putBoolean(IS_ANSWER_SHOWN, mIsAnswerShown);
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        CheatActivity.mIsAnswerShown = isAnswerShown;
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        if (savedInstanceState != null) {
            mIsAnswerShown = savedInstanceState.getBoolean(IS_ANSWER_SHOWN, false);
            if (mIsAnswerShown) {
                setAnswerShownResult(true);
                Log.i(TAG, "mIsAnswerShown:" + mIsAnswerShown);
            }

        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mApiLevelTextView = (TextView) findViewById(R.id.api_text_view);
        mApiLevelTextView.setText("API Level " + Build.VERSION.SDK_INT);
        final Button showAnswer = (Button) findViewById(R.id.showAnswerButton);
        showAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue)
                    mAnswerTextView.setText(R.string.true_button);
                else
                    mAnswerTextView.setText(R.string.false_button);
                setAnswerShownResult(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //在API21级以上才会显示动画效果
                    int cx = showAnswer.getWidth() / 2;
                    int cy = showAnswer.getHeight() / 2;
                    float radius = showAnswer.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(showAnswer, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            showAnswer.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else
                    showAnswer.setVisibility(View.INVISIBLE);
            }
        });
    }
}
