package audioapk.com.example.android.tic_tac_toe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class FirstPageGameStatus extends AppCompatActivity {

    private boolean index = true; // true -> first , false-> second
    public static boolean players = true; // true -> single , false-> multi
    public static boolean naughts = true; // true -> naughts , false-> crosses

    private Button topButton;
    private Button bottomButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page_game_status);
        topButton = (Button)findViewById(R.id.button);
        bottomButton = (Button)findViewById(R.id.button2);
    }

    public void topButton(View view){
        if(index) {
            players = true;
            changeName();
            index = false;
        }else{
            naughts = true;
            playGame();
        }
    }
    public void bottomButton(View view){
        if(index) {
            players = false;
            playGame();
        }else{
            naughts = false;
            playGame();
        }
    }

    private void changeName(){
        topButton.setText("Naughts (first chance)");
        bottomButton.setText("Crosses");
    }

    private void playGame(){
        index = true;
        Intent intent = new Intent(getApplicationContext(),Gaming.class);
        startActivity(intent);
        finish();
    }
}