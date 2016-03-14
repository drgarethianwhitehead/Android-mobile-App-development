package com.example.admin.ece1778_assignment_3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CameraActivity extends Activity implements
        Camera.PictureCallback,
        SurfaceHolder.Callback,
        AccelerometerListener {

    private Camera mCamera;
//    private CameraPreview mPreview;

    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;
    Camera.Parameters params;
    Camera.Size cameraPreviewSize, cameraPictueSize;


    Button loadImage;
    Button back;
    Camera.PictureCallback getpicture;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camerapreview);
        mCamera = getCameraInstance();
//        mPreview = new CameraPreview(this, mCamera);
//        FrameLayout preview = (FrameLayout) findViewById(R.id.camerareview);
//        preview.addView(mPreview);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(mSurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        loadImage = (Button) findViewById(R.id.button3);
        loadImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCamera.release();
                        startActivity(new Intent(CameraActivity.this,ImageDisplay.class));
                    }
                }
        );

        back = (Button) findViewById(R.id.button_capture2);
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCamera.release();
                        finish();

                    }
                }

        );
        getpicture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    outStream = new FileOutputStream(Helper.getFilePath());

                    outStream.write(data);
                    outStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }

                Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_SHORT).show();
                refreshCamera();
            }
        };
    }

    public void refreshCamera() {
        Log.d("MYDEBUG", "refreshCamera");
        if (mSurfaceHolder.getSurface() == null) {
            return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e) {
        }

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
        }
    }


    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {

        }
        return c;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

   // public void loading() {
    //    Helper.hideNavBar(this);
    //}

    public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("MYDEBUG", "surfaceCreated");
        try {
            mCamera = Camera.open();

            params = mCamera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

            cameraPreviewSize = getBestPreviewSize(Helper.getFullScreenSizeWidth(this),
                    Helper.getFullScreenSizeHeight(this), params);
            cameraPictueSize = getBestPictureSize(Helper.getFullScreenSizeWidth(this),
                    Helper.getFullScreenSizeHeight(this), params);

            params.setPreviewSize(cameraPreviewSize.width, cameraPreviewSize.height);
            params.setPictureSize(1920, 1080);
            Log.d("MYDEBUG", Integer.toString(cameraPictueSize.width));
            Log.d("MYDEBUG", Integer.toString(cameraPictueSize.height));
            mCamera.setParameters(params);

        } catch (RuntimeException e) {
            System.err.println(e);
            return;
        }

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.release();
    }

    public Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size bestSize = null;
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();

        bestSize = sizeList.get(sizeList.size() - 1);

        for (int i = 1; i < sizeList.size(); i++) {
            if ((sizeList.get(i).width * sizeList.get(i).height) >
                    (bestSize.width * bestSize.height)) {
                if (isAspectRatioMatch((double) width / height,
                        (double) (sizeList.get(i).width) / (sizeList.get(i).height))) {
                    bestSize = sizeList.get(i);
                }
            }
        }

        return bestSize;
    }

    public Camera.Size getBestPictureSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size bestSize = null;
        List<Camera.Size> sizeList = parameters.getSupportedPictureSizes();

        bestSize = sizeList.get(sizeList.size() - 1);

        for (int i = 1; i < sizeList.size(); i++) {
            if ((sizeList.get(i).width * sizeList.get(i).height) >
                    (bestSize.width * bestSize.height)) {
                if (isAspectRatioMatch((double) width / height,
                        (double) (sizeList.get(i).width) / (sizeList.get(i).height))) {
                    bestSize = sizeList.get(i);
                }
            }
        }

        return bestSize;
    }

    public boolean isAspectRatioMatch(double ratio1, double ratio2) {
        if (Math.abs(ratio1 - ratio2) > 0.1) {
            return false;
        }
        return true;
    }

    public void listenOnAccelerometer() {

        if (AccelerometerManager.isSupported(this)) {


            AccelerometerManager.startListening(this);
        }
    }

    public void onShake(float force) {

        Toast.makeText(getBaseContext(), "Motion detected",
                Toast.LENGTH_SHORT).show();

        if (AccelerometerManager.isListening()) {

            AccelerometerManager.stopListening();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mCamera.takePicture(null, null, getpicture);

                listenOnAccelerometer();
            }
        }, 1000);


    }

    @Override
    protected void onPause() {
        super.onPause();
        AccelerometerManager.stopListening();

    }

    @Override
    public void onResume() {
        super.onResume();

        Helper.hideNavBar(this);

        Toast.makeText(getBaseContext(), "onResume Accelerometer Started",
                Toast.LENGTH_SHORT).show();

        listenOnAccelerometer();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

    }
}

class AccelerometerManager {

    private static Context aContext = null;


    private static float threshold = 10.0f;
    private static int interval = 200;

    private static Sensor sensor;
    private static SensorManager sensorManager;

    private static AccelerometerListener listener;


    private static Boolean supported;

    private static boolean running = false;


    public static boolean isListening() {
        return running;
    }


    public static void stopListening() {
        running = false;
        try {
            if (sensorManager != null && sensorEventListener != null) {
                sensorManager.unregisterListener(sensorEventListener);
            }
        } catch (Exception e) {
        }
    }


    public static boolean isSupported(Context context) {
        aContext = context;
        if (supported == null) {
            if (aContext != null) {


                sensorManager = (SensorManager) aContext.
                        getSystemService(Context.SENSOR_SERVICE);

                // Get all sensors in device
                List<Sensor> sensors = sensorManager.getSensorList(
                        Sensor.TYPE_ACCELEROMETER);

                supported = new Boolean(sensors.size() > 0);


            } else {
                supported = Boolean.FALSE;
            }
        }
        return supported;
    }


    public static void startListening(AccelerometerListener accelerometerListener) {

        sensorManager = (SensorManager) aContext.
                getSystemService(Context.SENSOR_SERVICE);

        // Take all sensors in device
        List<Sensor> sensors = sensorManager.getSensorList(
                Sensor.TYPE_ACCELEROMETER);

        if (sensors.size() > 0) {

            sensor = sensors.get(0);
            running = sensorManager.registerListener(
                    sensorEventListener, sensor,
                    SensorManager.SENSOR_DELAY_GAME);

            listener = accelerometerListener;
        }


    }

    private static SensorEventListener sensorEventListener =
            new SensorEventListener() {

                private long now = 0;
                private long timeDiff = 0;
                private long lastUpdate = 0;
                private long lastShake = 0;

                private float x = 0;
                private float y = 0;
                private float z = 0;
                private float lastX = 0;
                private float lastY = 0;
                private float lastZ = 0;
                private float force = 0;

                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }

                public void onSensorChanged(SensorEvent event) {
                    // use the event timestamp as reference
                    // so the manager precision won't depends
                    // on the AccelerometerListener implementation
                    // processing time
                    now = event.timestamp;

                    x = event.values[0];
                    y = event.values[1];
                    z = event.values[2];

                    // if not interesting in shake events
                    // just remove the whole if then else block
                    if (lastUpdate == 0) {
                        lastUpdate = now;
                        lastShake = now;
                        lastX = x;
                        lastY = y;
                        lastZ = z;
                        Toast.makeText(aContext, "No Motion detected",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        timeDiff = now - lastUpdate;

                        if (timeDiff > 0) {

                    /*force = Math.abs(x + y + z - lastX - lastY - lastZ)
                                / timeDiff;*/
                            force = Math.abs(x + y + z - lastX - lastY - lastZ);

                            if (Float.compare(force, threshold) > 0) {
                                //Toast.makeText(Accelerometer.getContext(),
                                //(now-lastShake)+"  >= "+interval, 1000).show();

                                if (now - lastShake >= interval) {

                                    // trigger shake event
                                    listener.onShake(force);
                                } else {
                                    Toast.makeText(aContext, "No Motion detected",
                                            Toast.LENGTH_SHORT).show();

                                }
                                lastShake = now;
                            }
                            lastX = x;
                            lastY = y;
                            lastZ = z;
                            lastUpdate = now;
                        } else {
                            Toast.makeText(aContext, "No Motion detected", Toast.LENGTH_SHORT).show();
                        }
                    }
                    // trigger change event
                    listener.onAccelerationChanged(x, y, z);
                }

            };
}

interface AccelerometerListener {

    public void onAccelerationChanged(float x, float y, float z);

    public void onShake(float force);

}