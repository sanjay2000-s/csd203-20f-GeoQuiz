package com.bignerdranch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.DayOfWeek;

import mypackage.Question;
import mypackage.QuizViewModel;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private final String KEY_INDEX = "index";
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private final int REQUEST_CODE_CHEAT = 0;


    private Question[] mQuestionBank = {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };
    private int mCurrentIndex = 0;

    private QuizViewModel mQuizViewModel;

    private Button mCheatButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");

        setContentView(R.layout.activity_main);

        ViewModelProvider provider = ViewModelProviders.of(this);
        mQuizViewModel = provider.get(QuizViewModel.class);
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel");

        if (savedInstanceState != null) {
            mQuizViewModel.mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        } else {
            mQuizViewModel.mCurrentIndex = 0;
        }

        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mNextButton = findViewById(R.id.next);
        mQuestionTextView = findViewById(R.id.question_text_view);

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuizViewModel.moveToNext();
                updateQuestion();
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuizViewModel.currentQuestionAnswer();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mQuizViewModel.mCurrentIndex);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart called.");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called.");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called.");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data != null) {
                mQuizViewModel.setIsCheater(
                        data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false));
            }
        }
    }

    private void updateQuestion() {
        int questionTextResId = mQuizViewModel.currentQuestionText();
        mQuestionTextView.setText(questionTextResId);
    }
    private void checkAnswer(boolean answer) {
        boolean correctAnswer = mQuestionBank[mCurrentIndex].isAnswer();
        if (mQuizViewModel.isCheater()) {
            Toast.makeText(this, R.string.judgment_toast, Toast.LENGTH_SHORT).show();
        } else if (answer == correctAnswer) {
            Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
            }
    }
}