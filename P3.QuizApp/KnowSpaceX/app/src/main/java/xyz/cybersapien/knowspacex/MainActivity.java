package xyz.cybersapien.knowspacex;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity{

    private int score;
    private boolean isQ1done;
    private boolean isQ2done;
    private boolean isQ3done;
    private boolean isQ4done;
    private boolean isQ5done;
    private boolean isQ6done;
    private boolean isQ7done;
    private boolean isQ8done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.ScoreGetter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, String.valueOf(getScore()),Snackbar.LENGTH_SHORT).show();
                if (!isQ1done){
                    findViewById(R.id.q1).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else {
                    findViewById(R.id.q1).setBackgroundColor(Color.parseColor("#00FF00"));
                }
                if (!isQ2done){
                    findViewById(R.id.q2).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else {
                    findViewById(R.id.q2).setBackgroundColor(Color.parseColor("#00FF00"));
                }
                if (!isQ3done){
                    findViewById(R.id.q3).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else {
                    findViewById(R.id.q3).setBackgroundColor(Color.parseColor("#00FF00"));
                }
                if (!isQ4done){
                    findViewById(R.id.q4).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else {
                    findViewById(R.id.q4).setBackgroundColor(Color.parseColor("#00FF00"));
                }
                if (!isQ5done){
                    findViewById(R.id.q5).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else {
                    findViewById(R.id.q5).setBackgroundColor(Color.parseColor("#00FF00"));
                }
                if (!isQ6done){
                    findViewById(R.id.q6).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else {
                    findViewById(R.id.q6).setBackgroundColor(Color.parseColor("#00FF00"));
                }
                if (!isQ7done){
                    findViewById(R.id.q7).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else {
                    findViewById(R.id.q7).setBackgroundColor(Color.parseColor("#00FF00"));
                }
                if (!isQ8done){
                    findViewById(R.id.q8).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else {
                    findViewById(R.id.q8).setBackgroundColor(Color.parseColor("#00FF00"));
                }

            }
        });
        isQ1done = false;
        isQ2done = false;
        isQ3done = false;
        isQ4done = false;
        isQ5done = false;
        isQ6done = false;
        isQ7done = false;
        isQ8done = false;

        /**
         * Text Change Listener on Question 2
         * The Answer here is Dragon capsules, which are used for transporting cargo, and by 2017, transporting Astronauts
         */
        EditText question2Answer = (EditText) findViewById(R.id.q2Answer);
        question2Answer.addTextChangedListener(new validateText(question2Answer) {
            @Override
            public void validate(EditText editText, String text) {
                String Answer2 = editText.getText().toString();
                if(Answer2.equalsIgnoreCase("Dragon")){
                    isQ2done = true;
                    addToScore(editText, 10);
                } else if (isQ2done){
                    addToScore(editText, -10);
                    isQ2done = false;
                }
            }

        });

        /**
         * Text Change Listener on Question 7
         * The Answer is "Millennium Falcon", the ship from Star Wars, piloted by Han Solo and Chewbacca
         */
        EditText question7Answer = (EditText) findViewById(R.id.q7Answer);
        question7Answer.addTextChangedListener(new validateText(question7Answer) {
            @Override
            public void validate(EditText editText, String text) {
                String Answer7 = editText.getText().toString();
                if(Answer7.equalsIgnoreCase("Millennium Falcon")){
                    isQ7done = true;
                    addToScore(editText, 10);
                } else if (isQ7done){
                    addToScore(editText, -10);
                    isQ7done = false;
                }
            }

        });


    }

    /**
     * gets the current Score for the Snackbar
     * @return String with Current Score
     */
    private String getScore(){
        return getString(R.string.currentScore, score);
    }

    /**
     * Adds the points to the current score
     * @param additionalPoints adds this to the score
     */
    private void addToScore(View v,int additionalPoints){
        score += additionalPoints;
        if (additionalPoints>0)
        Snackbar.make(v, getString(R.string.addedPoints, additionalPoints), Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Checks Question 1 for Answer
     * The Company is Leaded by Tech Entrepreneur and a personal role model, Elon Musk
     * @param view This view's ID is checked to determine right or wrong answer
     */
    public void onQuestion1(View view){
        if (!isQ1done && view == findViewById(R.id.q1Ans1)){
            addToScore(view,10);
            isQ1done = true;
        } else if (isQ1done && view != findViewById(R.id.q1Ans1)){
            addToScore(view, -10);
            isQ1done = false;
        }
    }

    /**
     * Checks Question 3 for Answer
     * The Rockets are named Falcon, after the Millennium Falcon from Star Wars.
     * Currently, Falcon 9 is operational, and the Falcon Heavy is going to fly soon
     * @param view This view's ID is checked to determine right or wrong answer
     */
    public void onQuestion3(View view){
        CheckBox box1 = (CheckBox) findViewById(R.id.q3check1);
        CheckBox box2 = (CheckBox) findViewById(R.id.q3check2);
        CheckBox box3 = (CheckBox) findViewById(R.id.q3check3);
        CheckBox box4 = (CheckBox) findViewById(R.id.q3check4);

        if ((!isQ3done) && box2.isChecked() && !(box1.isChecked() || box4.isChecked()) && box3.isChecked()){
            isQ3done = true;
            addToScore(view, 10);
        } else if (isQ3done){
            isQ3done = false;
            addToScore(view, -10);
        }
    }

    /**
     * Checks Question 4 for Answer
     * The Falcon Heavy features 3 clusters of engines, each cluster having 9 engines. Thus, three times the power of the Falcon 9
     * So, correct answer here is 27
     * @param view This view's ID is checked to determine right or wrong answer
     */
    public void onQuestion4(View view){
        if (!isQ4done && view == findViewById(R.id.q4Ans2)){
            addToScore(view,10);
            isQ4done = true;
        } else if (isQ4done && view != findViewById(R.id.q4Ans2)){
            addToScore(view, -10);
            isQ4done = false;
        }
    }

    /**
     * Checks Question 5 for Answer
     * SpaceX's Mission Control is located right next to their factory floor and employee cubicles at Hawthorne, California
     * @param view This view's ID is checked to determine right or wrong answer
     */
    public void onQuestion5(View view){
        if (!isQ5done && view == findViewById(R.id.q5Ans1)){
            addToScore(view,10);
            isQ5done = true;
        } else if (isQ5done && view != findViewById(R.id.q5Ans1)){
            addToScore(view, -10);
            isQ5done = false;
        }
    }

    /**
     * Checks Question 6 for Answer
     * Elon Musk is the CEO of Solar City, SpaceX and Tesla Motors
     * @param view This view's ID is checked to determine right or wrong answer
     */
    public void onQuestion6(View view){
        CheckBox box1 = (CheckBox) findViewById(R.id.q6check1);
        CheckBox box2 = (CheckBox) findViewById(R.id.q6check2);
        CheckBox box3 = (CheckBox) findViewById(R.id.q6check3);
        CheckBox box4 = (CheckBox) findViewById(R.id.q6check4);

        if ((!isQ6done) && box1.isChecked() && !box2.isChecked() && box3.isChecked() && box4.isChecked()){
            isQ6done = true;
            addToScore(view, 10);
        } else if (isQ6done){
            isQ6done = false;
            addToScore(view, -10);
        }
    }

    /**
     * Checks Question 8 for Answer
     * Scenes from the Marvel movie, Iron Man 2, were shot at the SpaceX facility in Hawthorne.
     * @param view This view's ID is checked to determine right or wrong answer
     */
    public void onQuestion8(View view){
        if (!isQ8done && view == findViewById(R.id.q8Ans1)){
            addToScore(view,10);
            isQ8done = true;
        } else if (isQ8done && view != findViewById(R.id.q8Ans1)){
            addToScore(view, -10);
            isQ8done = false;
        }
    }

    /**
     * Abstract class validateText, hints taken from stackExchange Answer by Christopher Perry from StackExchange.
     * URL: http://goo.gl/lfFhlo
     */
    private abstract class validateText implements TextWatcher {

        private final EditText editText;

        public validateText(EditText textView) {
            editText = textView;
        }

        public abstract void validate(EditText textView, String text);

        @Override
        final public void afterTextChanged(Editable s) {
            String text = editText.getText().toString();
            validate(editText, text);
        }

        @Override
        final public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            /* Unneeded, just here to remove the Error */
        }

        @Override
        final public void onTextChanged(CharSequence s, int start, int before, int count) {
            /* Unneeded, just to remove Error */

        }
    }
}


