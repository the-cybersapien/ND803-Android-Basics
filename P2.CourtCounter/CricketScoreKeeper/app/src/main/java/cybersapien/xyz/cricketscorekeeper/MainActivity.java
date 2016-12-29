package cybersapien.xyz.cricketscorekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int scoreA;
    private int wicketsA;
    private int oversA;
    private int ballsA;
    private int scoreB;
    private int wicketsB;
    private int oversB;
    private int ballsB;
    private int resetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scoreA = 0;
        wicketsA = 0;
        oversA = 0;
        ballsA = 0;
        scoreB = 0;
        wicketsB = 0;
        oversB = 0;
        ballsB = 0;
        resetter = 1;
        refreshA();
        resetter = 1;
        refreshB();
    }

    /**
     * Change the score, wickets and the overs of A on the screen
     */
    private void refreshA(){
        TextView scoreAView = (TextView) findViewById(R.id.TeamAScore);
        TextView wicketsAView = (TextView) findViewById(R.id.TeamAWickets);
        TextView oversAView = (TextView) findViewById(R.id.oversA);
        if (resetter == 0){
            changeOversA();
        }
        scoreAView.setText(String.valueOf(scoreA));
        wicketsAView.setText(String.valueOf(wicketsA));
        oversAView.setText(getString(R.string.overs, oversA,ballsA));
        resetter = 0;
    }

    /**
     * Adds one to the Score of Team A
     */
    public void add1ToA(View view){
        scoreA++;
        refreshA();
    }

    /**
     * Adds two to the score of Team B
     */
    public void add2ToA(View view){
        scoreA+=2;
        refreshA();
    }

    /**
     * Adds three to the score of Team B
     */
    public void add3ToA(View view){
        scoreA+=3;
        refreshA();
    }

    /**
     * Adds four to the score of Team B
     */
    public void add4ToA(View view){
        scoreA+=4;
        refreshA();
    }

    /**
     * Adds six to the score of Team B
     */
    public void add6ToA(View view){
        scoreA+=6;
        refreshA();
    }

    /**
     * Increments the wickets for team A
     */
    public void fallWicketsA(View view){
        wicketsA++;
        refreshA();
    }

    /**
     * Changes the overs/balls count for A
     * Each over has 6 balls and thus, we keep incrementing the number of balls, and when the number of balls reach 6, we reset balls to 0 and increment the number of overs
     */
    private void changeOversA(){
        if (ballsA==5){
            ballsA = 0;
            oversA++;
        } else {
            ballsA++;
        }
    }

    /**
     * Displays the score,wickets and the Overs on B on the Screen
     */
    private void refreshB(){
        TextView scoreBView = (TextView) findViewById(R.id.TeamBScore);
        TextView wicketsBView = (TextView) findViewById(R.id.TeamBWickets);
        TextView oversBView = (TextView) findViewById(R.id.oversB);
        if (resetter==0){
            changeOversB();
        }
        scoreBView.setText(String.valueOf(scoreB));
        wicketsBView.setText(String.valueOf(wicketsB));
        oversBView.setText(getString(R.string.overs, oversB,ballsB));
        resetter = 0;
    }

    /**
     * Adds one to the score of Team B
     */
    public void add1ToB(View view){
        scoreB++;
        refreshB();
    }

    /**
     * Adds two to the score of Team B
     */
    public void add2ToB(View view){
        scoreB+=2;
        refreshB();
    }

    /**
     * Adds three to the score of Team B
     */
    public void add3ToB(View view){
        scoreB+=3;
        refreshB();
    }

    /**
     * Adds four to the score of Team B
     */
    public void add4ToB(View view){
        scoreB+=4;
        refreshB();
    }

    /**
     * Adds six to the score of Team B
     */
    public void add6ToB(View view){
        scoreB+=6;
        refreshB();
    }

    /**
     * Increments the wickets for team B
     */
    public void fallWicketB(View view){
        wicketsB++;
        refreshB();
    }

    /**
     * Changes the overs/balls count for B
     * Each over has 6 balls and thus, we keep incrementing the number of balls, and when the number of balls reach 6, we reset balls to 0 and increment the number of overs
     */
    private void changeOversB(){
        if (ballsB==5){
            ballsB = 0;
            oversB++;
        } else {
            ballsB++;
        }
    }

    /**
     * Resets the score for both teams
     */
    public void resetScore(View view){
        scoreA = 0;
        scoreB = 0;
        wicketsA = 0;
        wicketsB = 0;
        oversA = 0;
        ballsA = 0;
        oversB = 0;
        ballsB = 0;
        resetter = 1;
        refreshA();
        resetter = 1; //Changed resetter to 1 again otherwise it creates a problem while updating overs in B
        refreshB();
    }
}
