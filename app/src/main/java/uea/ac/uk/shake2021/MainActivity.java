package uea.ac.uk.shake2021;
//Load Associated Libraries
import android.os.Bundle;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener{
    private SensorManager sensorManager;
    private boolean isColor = false;
    private View view;
    private long lastUpdate;
    private TextView mXAccelerationTextView;
    private TextView mYAccelerationTextView;
    private TextView mZAccelerationTextView;
    private EditText mThresholdEditText;//https://developer.android.com/reference/android/widget/EditText; https://javawithumer.com/2019/07/get-value-edittext.html
    private float mThreshold;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//Layout loaded from activity_main.html
        view = findViewById(R.id.textView);
        view.setBackgroundColor(Color.CYAN);

//use the sensorManager 
//https://www.javatpoint.com/android-sensor-example
//https://guides.codepath.com/android/Listening-to-Sensors-using-SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mXAccelerationTextView = findViewById(R.id.acc_x);
        mYAccelerationTextView = findViewById(R.id.acc_y);
        mZAccelerationTextView = findViewById(R.id.acc_z);
        mThresholdEditText = findViewById(R.id.threshold_edittext);
        lastUpdate = System.currentTimeMillis();

    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    @Override
    ///Sensor data collection
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }

    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        
        float x = values[0];
        float y = values[1];
        float z = values[2];

        //Log.d("x:", Float.toString(x));
// Set the texview for displaying it to your screen/frontend
        mXAccelerationTextView.setText(Float.toString(x));
        mYAccelerationTextView.setText(Float.toString(y));
        mZAccelerationTextView.setText(Float.toString(z));

//The processing part of your sensor data is here: We are calculating the magnitude of the accelerometer.
        mThreshold=2; 
        //You need to tie your mThresholdEditText variable here to make this flexible/user inputtable...add the necessary code
        //https://www.studytonight.com/android/get-edittext-set-textview; checkout an example 
 
        float accelerationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);//

        long actualTime = System.currentTimeMillis();

 //take action by changing the colour of the screen based on the threshold
        if (accelerationSquareRoot >= mThreshold)
        {

            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
           
            if (isColor) {
                view.setBackgroundColor(Color.MAGENTA);

            } else {
                view.setBackgroundColor(Color.BLUE);
            }
            isColor = !isColor;
        }

    }
//Use sensorManager.registerListener and sensorManager.unregisterListener to turn on and off of the data capture
//Add a button in your layout file activity_main.xml the start and stop button to tie the events
//https://developer.android.com/reference/android/widget/Button
    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {

        super.onPause();
        sensorManager.unregisterListener(this);
        //Toast.cancel();
    }
}
