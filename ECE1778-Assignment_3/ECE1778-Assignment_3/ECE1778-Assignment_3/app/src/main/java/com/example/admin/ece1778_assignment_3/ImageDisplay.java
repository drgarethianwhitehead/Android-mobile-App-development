package com.example.admin.ece1778_assignment_3;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by admin on 2016/2/6.
 */
public class ImageDisplay extends android.support.v4.app.FragmentActivity {
    private static final int ITEM_1=Menu.FIRST;

    ArrayList<String> itemList = new ArrayList<String>();
    GridView gridview;
    double lng;
    double lat;
    MyApp mm;
    String timeStamp1 = new SimpleDateFormat("ddMMyyyy_HHmmSS").format(new Date());
    public class ImageAdapter extends BaseAdapter {

        private Context mContext;


        public ImageAdapter(Context c) {
            mContext = c;
        }

        void add(String path) {
            itemList.add(path);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return itemList.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            getGpsLocation1();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    mContext.LAYOUT_INFLATER_SERVICE);
            View v;
            TextView textView;
            ImageView imageView;

            if (convertView == null) {  // if it's not recycled, initialize some attributes
                v = new View(mContext);
                v = inflater.inflate(R.layout.grid_item_layout, null);
                textView = (TextView) v.findViewById(R.id.text);
                textView.setText("Longitude: "+ lng + " Latitude: " + lat + " Time: " + timeStamp1);
                imageView = (ImageView) v.findViewById(R.id.image);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                Bitmap bitmap = BitmapFactory.decodeFile(
                        Helper.getFiles()[position].getAbsolutePath(), options);
                imageView.setImageBitmap(bitmap);
            } else {
                v = (View) convertView;
            }

            return v;
        }


        public Location getGpsLocation1() {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ContextCompat.checkSelfPermission(mContext,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {

                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                lng = location.getLongitude();
                lat = location.getLatitude();
                return location;
            }
            return null;
        }

        public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

            Bitmap bm;
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(path, options);

            return bm;
        }

        public int calculateInSampleSize(

                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                if (width > height) {
                    inSampleSize = Math.round((float) height / (float) reqHeight);
                } else {
                    inSampleSize = Math.round((float) width / (float) reqWidth);
                }
            }

            return inSampleSize;
        }


    }


    ImageAdapter myImageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageview);

        gridview = (GridView) findViewById(R.id.gridView);
        myImageAdapter = new ImageAdapter(this);
        gridview.setAdapter(myImageAdapter);


        File targetDirector = new File(Helper.STORAGE_PATH);
        if(!targetDirector.exists()){

            targetDirector.mkdirs();
        }
    Log.d("CTag",targetDirector.getAbsolutePath());
        File[] files = targetDirector.listFiles();

        if(files!=null){
            for (File file : files) {
                Log.d("CTag","...s.s.");
                myImageAdapter.add(file.getAbsolutePath());
            }
        }
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Helper.deleteFile(position);
                Toast.makeText(getBaseContext(), "Delete success",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ImageDisplay.this, ImageDisplay.class);
                startActivity(intent);

                return false;
            }
        });

    }


}
