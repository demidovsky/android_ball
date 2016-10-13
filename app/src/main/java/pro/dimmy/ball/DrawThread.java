package pro.dimmy.ball;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.SurfaceHolder;

import static java.lang.Math.round;

/**
 * Created by ddemidovskiy on 11.10.2016.
 */

public class DrawThread extends Thread
{


    private static final int FPS = 60;
    private static final int FRAME_LENGTH  = round(1000/FPS);
    private final Bitmap ball;
    private final Matrix matrix;
    private boolean isRunning;
    private long prevTime;
    private SurfaceHolder surfaceHolder;
    private float x = 0;
    private float y = 0;



    // конструктор
    public DrawThread(SurfaceHolder surfaceHolder, Resources resources)
    {
        this.surfaceHolder = surfaceHolder;
        this.ball = BitmapFactory.decodeResource(resources, R.drawable.ball_small);
        this.matrix = new Matrix();
    }




    @Override
    public void run()
    {
        Canvas canvas;



        while (isRunning)
        {

            long now = System.currentTimeMillis();
            long elapsedTime = now - prevTime;



            if (elapsedTime > FRAME_LENGTH)
            {
                prevTime = now;
                matrix.setTranslate(x, y);
            }



            canvas = null;
            try
            {
                // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas(null);
                synchronized(surfaceHolder)
                {
                    canvas.drawColor(Color.BLACK);
                    canvas.drawBitmap(ball, matrix, null);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (canvas != null)
                {
                    // отрисовка выполнена. выводим результат на экран
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        }

    }



    public void setRunning(boolean isRunning)
    {
        this.isRunning = isRunning;
    }



    public void setBallPosition(float x, float y)
    {
        this.x = x;
        this.y = y;
    }



}
