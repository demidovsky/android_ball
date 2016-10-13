package pro.dimmy.ball;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

/**
 * Created by ddemidovskiy on 12.10.2016.
 */

public class Accelerometer implements SensorEventListener
{
    private static final int SENSOR_PERIOD_MICROSEC = 500;
    private final WindowManager windowManager;
    private final SensorManager sensorManager;
    private final Sensor rotationSensor;
    private Callbacks extListener;




    public interface Callbacks
    {
        void onRoll(float x, float y/*, float z*/);
    }



    // конструктор
    public Accelerometer(Activity activity)
    {
        windowManager = (WindowManager) activity.getWindowManager();
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }



    // запуск сенсора
    public void startListening(Callbacks extListener)
    {
        if (this.extListener != null && this.extListener == extListener) return;
        else this.extListener = extListener;

        if (rotationSensor == null)
        {
            Log.w("xxx", "Rotation vector sensor not available; will not provide orientation data.");
            return;
        }
        sensorManager.registerListener(this, rotationSensor, SENSOR_PERIOD_MICROSEC);
    }



    // остановка сенсора
    public void stopListening()
    {
        sensorManager.unregisterListener(this);
        this.extListener = null;
    }




    // получение данных с сенсора
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (extListener == null) return;
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;

        float sx = event.values[0];
        float sy = event.values[1];
        float x;
        float y;

        // float z = event.values[2];


        switch (windowManager.getDefaultDisplay().getRotation()) {
            default:
            case Surface.ROTATION_0:
                Log.d("ROTATION", "ROTATION_0");
                x = -sx;
                y = sy;
                break;
            case Surface.ROTATION_90:
                Log.d("ROTATION", "ROTATION_90");
                x = sy;
                y = sx;
                break;
            case Surface.ROTATION_180:
                Log.d("ROTATION", "ROTATION_180");
                x = -sx;
                y = -sy;
                break;
            case Surface.ROTATION_270:
                Log.d("ROTATION", "ROTATION_270");
                x = -sy;
                y = -sx;
                break;

        }

        extListener.onRoll(x, y/*, z*/);


        /*if (event.sensor == rotationSensor)
        {
            updateOrientation(event.values);
        }*/

    }



/*

    // разбор данных сенсора
    // @SuppressWarnings("SuspiciousNameCombination")
    private void updateOrientation(float[] values) {

        // float[] rotationMatrix = new float[9];
        // SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector);

        final int worldAxisForDeviceAxisX;
        final int worldAxisForDeviceAxisY;

        // Remap the axes as if the device screen was the instrument panel,
        // and adjust the rotation matrix for the device orientation.
        switch (windowManager.getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
            default:
                worldAxisForDeviceAxisX = SensorManager.AXIS_X;
                worldAxisForDeviceAxisY = SensorManager.AXIS_Z;
                break;
            case Surface.ROTATION_90:
                worldAxisForDeviceAxisX = SensorManager.AXIS_Z;
                worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_X;
                break;
            case Surface.ROTATION_180:
                worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_X;
                worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_Z;
                break;
            case Surface.ROTATION_270:
                worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_Z;
                worldAxisForDeviceAxisY = SensorManager.AXIS_X;
                break;
        }

        // float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisForDeviceAxisX, worldAxisForDeviceAxisY, adjustedRotationMatrix);




            // Transform rotation matrix into azimuth/pitch/roll
            float[] orientation = new float[3];
            SensorManager.getOrientation(adjustedRotationMatrix, orientation);

            // Convert radians to degrees
            float pitch = orientation[1] * -57;
            float roll = orientation[2] * -57;




        // extListener.onRoll(x, y, z);
    }
*/




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
}
