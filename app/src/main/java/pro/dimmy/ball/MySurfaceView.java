package pro.dimmy.ball;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    private DrawThread drawThread;

    private float x = 0;
    private float y = 0;

    private int w;
    private int h;

    public MySurfaceView(Context context)
    {
        super(context);
        getHolder().addCallback(this); // получаем SurfaceHolder
    }




    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        drawThread = new DrawThread(getHolder(), getResources());
        drawThread.setRunning(true);
        drawThread.setBallPosition(x, y);
        drawThread.start();

    }





    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        w = width;
        h = height;
    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        Log.d("xxx", "surfaceDestroyed");
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry)
        {
            try
            {
                drawThread.join();
                retry = false;
            }
            catch (InterruptedException e) { }
        }

    }


    public void setBallPosition(float x, float y)
    {
        if (drawThread == null) return;

        this.x = x;
        this.y = y;
        drawThread.setBallPosition(w*x/100, h*y/100);
    }
}
