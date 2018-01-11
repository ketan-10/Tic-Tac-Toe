package audioapk.com.example.android.tic_tac_toe;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gaming extends AppCompatActivity {

    private boolean singlePlayer = FirstPageGameStatus.players;
    private GridLayout gridLayout;
    private int activePlayer = 1; // 1 for red 0 for yellow
    private int[] position = {2,2,2,2,2,2,2,2,2};
    private int[][] winState = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    private String winner;
    private boolean naughts;
    private int aiChance;
    private int humanChance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamming);
        gridLayout = (GridLayout) findViewById(R.id.grid);
        naughts =  FirstPageGameStatus.naughts;
        aiChance = (naughts) ? 0 : 1;
        humanChance = (naughts) ? 1 : 0;
        if (!naughts){aiCalls();}

    }
    @Nullable
    @Override
    public ActionBar getActionBar() {
        return super.getActionBar();
    }




    /**
     * Called when some one clicks on the grid
     */
    public void dropIn(View view) {

        ImageView counter = (ImageView) view;
        int tag = Integer.parseInt(counter.getTag().toString());
        if(position[tag] == 2 && checkWin() == 2) {

            position[tag] = activePlayer;

            counter.setTranslationY(-1000f);
            if (activePlayer == 0) {
                counter.setImageResource(R.drawable.yellow);
                activePlayer = 1;
            } else {
                counter.setImageResource(R.drawable.red);
                activePlayer = 0;
            }
            counter.animate().translationYBy(1000f).setDuration(500);

            //AI will have a chance if he is there
            boolean end = winner();
            if (singlePlayer && !end) {
                aiCalls();
            }

        }
    }
    private void aiCalls(){

        clickable(false);


//        for (int po:position) {
//            Log.i("Positions", String.valueOf(po));
//        }
//        Log.i("Positions", "--------------------------------------------------------------");
//        Log.i("AiChance",String.valueOf(aiChance));


        int bestMove = playBestMove(aiChance).position;
        position[bestMove] = aiChance;

        ImageView aiPlay = (ImageView) findViewWithTagRecursively(gridLayout,String.valueOf(bestMove)).get(0);
        if (activePlayer == 0) {
            aiPlay.setImageResource(R.drawable.yellow);
            activePlayer = 1;
        } else {
            aiPlay.setImageResource(R.drawable.red);
            activePlayer = 0;
        }

        aiPlay.setTranslationY(-2000f);
        aiPlay.animate().translationYBy(2000f).setDuration(1000);
        winner();

        clickable(true);
    }






    private void clickable(boolean enable){
        for(int i=0;i<gridLayout.getChildCount();i++){
            (gridLayout.getChildAt(i)).setEnabled(enable);
        }
    }

    /**
     * Checking if someone Has won or not
     * '1' for game ended with a win
     * '0' game ended with a draw
     * '2' game is not ended yet
     */
    public int checkWin(){
        winner = " Naughts has won 'O'";
        boolean draw = true;

        for(int[] winState : this.winState){
            if((position[winState[0]] == position[winState[1]]) &&
                    (position[winState[1]] == position[winState[2]]) &&
                    (position[winState[0]] != 2)) {
                if(position[winState[0]] == 0) {
                    winner = " Crosses has won 'X'";
                }
                return 1; // someone won
            }
        }
        for (int i:position)
            if (i == 2)
                draw = false;

        if(draw) {
            winner = " Draw";
            return 0;
        }
        winner = "";
        return 2; // no one won
    }


    /**
     * @return win State
     * True -> game ended Print result
     * False -> game is NOT ended
     */
    private boolean winner(){
        TextView textView = (TextView) findViewById(R.id.textView);
        if (checkWin() != 2 ) {
            textView.setText(winner);
            if(winner.equals(" Naughts has won 'O'"))
                textView.setTextColor(Color.YELLOW);
            else if(winner.equals(" Draw"))
                textView.setTextColor(Color.BLACK);
            else
                textView.setTextColor(Color.RED);
            textView.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }


    /**
     * MiniMax algorithm for single player
     */
    private class NodeScore{
        int position;
        int score;

        NodeScore(int score) {
            this.score = score;
        }
        NodeScore() {}
    }

    private NodeScore max( ArrayList<NodeScore> treeLevel){

        int score = -100 ;
        int index;
        int k = 0;
        int[] winIndex = new int[10];

        for(int i =0;i<treeLevel.size();i++){
            if(score < treeLevel.get(i).score){
                score = treeLevel.get(i).score;
            }
        }

        for(int i =0;i<treeLevel.size();i++){
            if(score == treeLevel.get(i).score){
                winIndex[k] = i;
                k++;
            }
        }
        Random r = new Random();
        index = winIndex[r.nextInt(k)];

        return treeLevel.get(index);
    }

    private NodeScore min( ArrayList<NodeScore> treeLevel){

        int score = 100 ;
        int index;
        int k = 0;
        int[] winIndex = new int[10];

        for(int i =0;i<treeLevel.size();i++){
            if(score > treeLevel.get(i).score){
                score = treeLevel.get(i).score;
            }
        }
        for(int i =0;i<treeLevel.size();i++){
            if(score == treeLevel.get(i).score){
                winIndex[k] = i;
                k++;
            }
        }
        Random r = new Random();
        index = winIndex[r.nextInt(k)];

        return treeLevel.get(index);
    }

    private NodeScore playBestMove(int chance){

        //BASE CASE
        int WinState = checkWin();
        if(WinState == 1 && winner.equals(" Naughts has won 'O'")){
            if (aiChance == 1)
                return new NodeScore(10);
            return new NodeScore(-10);}
        if (WinState == 1 && winner.equals(" Crosses has won 'X'")){
            if (aiChance == 0)
                return new NodeScore(10);
            return new NodeScore(-10);}
        if (WinState == 0)
            return new NodeScore(0);

        ArrayList<NodeScore> treeLevel = new ArrayList<>();

        for(int i=0;i<position.length;i++)
            if(position[i] == 2) {
                position[i] = chance;
                NodeScore node = new NodeScore();
                node.position = i;
                if (chance == aiChance)
                    node.score = playBestMove(humanChance).score;
                else
                    node.score = playBestMove(aiChance).score;
                treeLevel.add(node);
                position[i] = 2;

            }
        if(chance == aiChance)
            return max(treeLevel);
        return min(treeLevel);
    }



    /**
     * Called when Play Again button is pressed
     * Resets every thing to start
     */
    public void playAgain(View view){

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setVisibility(View.INVISIBLE);
        activePlayer = 1;
        for(int i=0;i<position.length;i++){
            position[i]=2;
        }
        for(int i=0;i<gridLayout.getChildCount();i++){
            ((ImageView)gridLayout.getChildAt(i)).setImageResource(0);
        }
        if (!naughts){aiCalls();}
    }




    /**Gets every view from the GRID*/
    public static List<View> findViewWithTagRecursively(ViewGroup root, Object tag){
        List<View> allViews = new ArrayList<>();

        final int childCount = root.getChildCount();
        for(int i=0; i<childCount; i++){
            final View childView = root.getChildAt(i);

            if(childView instanceof ViewGroup){
                allViews.addAll(findViewWithTagRecursively((ViewGroup)childView, tag));
            }
            else{
                final Object tagView = childView.getTag();
                if(tagView != null && tagView.equals(tag))
                    allViews.add(childView);
            }
        }

        return allViews;
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(),FirstPageGameStatus.class);
        startActivity(intent);
        finish();
    }


}


