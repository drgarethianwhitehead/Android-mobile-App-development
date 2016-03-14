package com.example.admin.ece1778_assignment_3;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyApp extends Application {

    private static MyApp mInstance;
    public LocationService locationService;
    private double lat;
    private double lng;
    private SimpleDateFormat sdf;

    public static MyApp getInstance() {

        return mInstance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mInstance = this;
        sdf = new SimpleDateFormat("yyyyMMddHHmmss");



//        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
//        locationService.start();

    }

    public void stopLocation() {

        if (locationService != null) {

            locationService.stop();
        }

    }

    public String getPicName() {


        getGpsLocation();
        return sdf.format(new Date()) + ".jpg";
    }
    public Location getGpsLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lng = location.getLongitude();
            lat = location.getLatitude();
            return location;
        }
        return null;
    }

    private LocationListener mListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub


                lng = getGpsLocation().getLongitude();
                lat = getGpsLocation().getLatitude();

                Log.i("CTag", lng + "," + lat);
        }


        public void onStatusChanged(String provider, int status, Bundle extras) {

        }


        public void onProviderEnabled(String provider) {

        }


        public void onProviderDisabled(String provider) {

        }

        };

    };


