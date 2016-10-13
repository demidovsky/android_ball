package pro.dimmy.ball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements Accelerometer.Callbacks, Physics.Callbacks
{

    private static final String GAME_STATE = "GAME_STATE";
    private MySurfaceView surfaceView;
    private Accelerometer accelerometer;
    private Physics physics;


    private float ballX;
    private float ballY;

    private float gravX;
    private float gravY;




    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // графика
        surfaceView = new MySurfaceView(this);
        setContentView(surfaceView);

        // сенсор
        accelerometer = new Accelerometer(this);
        accelerometer.startListening(this);

        // физика
        physics = new Physics(this);
        physics.start();


        switch (getWindowManager().getDefaultDisplay().getRotation()) {
            default:
            case Surface.ROTATION_0:
                Toast.makeText(this, "ROTATION_0", Toast.LENGTH_SHORT).show();
                break;
            case Surface.ROTATION_90:
                Toast.makeText(this, "ROTATION_90", Toast.LENGTH_SHORT).show();
                break;
            case Surface.ROTATION_180:
                Toast.makeText(this, "ROTATION_180", Toast.LENGTH_SHORT).show();
                break;
            case Surface.ROTATION_270:
                Toast.makeText(this, "ROTATION_270", Toast.LENGTH_SHORT).show();
                break;

        }


    }



    @Override
    public void onRoll(float x, float y/*, float z*/)
    {
        // Log.d("onRoll", x + " " + y + " " + z);

        gravX = x;
        gravY = y;
        physics.setGravity(gravX, gravY);

    }


    @Override
    public void onBallPositionChange(float x, float y)
    {
        // Log.d("onBallPositionChange", x + " " + y);
        ballX = x;
        ballY = y;
        surfaceView.setBallPosition(ballX, ballY);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        Log.d("xxx", "onSaveInstanceState");

        float[] gameState = { ballX, ballY, gravX, gravY };
        outState.putFloatArray(GAME_STATE, gameState);

        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("xxx", "onRestoreInstanceState");

        float[] gameState = savedInstanceState.getFloatArray(GAME_STATE);
        ballX = gameState[0];
        ballY = gameState[1];
        gravX = gameState[2];
        gravY = gameState[3];

        /*Set<String> keyset = savedInstanceState.keySet();
        for(String s: keyset)
        {
            Log.d("happy", "key: "+ s);
        }


        if(savedInstanceState != null)
        {
            if(savedInstanceState.containsKey(COUNTER_KEY))
            {
                counter = savedInstanceState.getInt(COUNTER_KEY);
                text.setText(""+counter);
            }
        }*/
    }







}
