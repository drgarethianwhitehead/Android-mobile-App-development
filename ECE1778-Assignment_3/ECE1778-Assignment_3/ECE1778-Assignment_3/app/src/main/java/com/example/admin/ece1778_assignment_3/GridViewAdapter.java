package com.example.admin.ece1778_assignment_3;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * Created by admin on 2016/2/6.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context mContext;

    public GridViewAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
//        return mThumbIds.length;
        Log.d("DEBUG", Integer.toString(Helper.getFileLength()));
        return Helper.getFileLength();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                mContext.LAYOUT_INFLATER_SERVICE);
        View v;
        TextView textView;
        ImageView imageView;


        if (convertView == null) {
            v = new View(mContext);
            v = inflater.inflate(R.layout.grid_item_layout, null);
            textView = (TextView) v.findViewById(R.id.text);
            textView.setText("TESTING");
            imageView = (ImageView) v.findViewById(R.id.image);

            // loading the bitmap to the imageview
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
}
