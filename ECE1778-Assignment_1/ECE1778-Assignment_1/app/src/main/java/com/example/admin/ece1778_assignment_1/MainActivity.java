package com.example.admin.ece1778_assignment_1;

import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
private TextView text;
    private Button button;
    private Button display;
    private ImageView img;
    private int count1 = 1;
    private int count = 0;
    private Handler handler = new Handler(){
        @Override
    public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                text.setText("You have clicked "+ count + " times");
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        button.setOnClickListener(new ButtonClickListener());
        img.setVisibility(View.INVISIBLE);
        display.setOnClickListener(new ButtonClickListener(){
            @Override
            public void onClick(View v){
                count1++;
                if (count1%2==0){
                 img.setVisibility(View.VISIBLE);
                }else {
                    img.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    private void init(){
        text = (TextView)findViewById(R.id.textView);
        button =(Button)findViewById(R.id.button);
        display = (Button)findViewById(R.id.button2);
        img = (ImageView)findViewById(R.id.imageView);
    }
    class ButtonClickListener implements View.OnClickListener{
            @Override
    public void onClick(View v){
                Message msg = handler.obtainMessage();
                if(v.getId() == R.id.button){
                    count++;
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST + 1, 5, "Reset").setIcon(
                R.drawable.reset_munu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case Menu.FIRST + 1:
                img.setVisibility(View.INVISIBLE);
                text.setText("Not Click yet");
                count = 0;
                count1 = 1;
                break;
        }
        return false;
    }
}


