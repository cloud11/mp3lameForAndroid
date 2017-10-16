package com.cloud.lame.testformp3lame;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.cloud.lame.mp3lame.LameHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        System.out.println(LameHelper.getInstance().lameVersion());
        File yygypath = this.getFilesDir();

        String yygypathstr = yygypath.toString();
        System.out.println(yygypathstr);
        String wavFile = yygypathstr +"/" +"test.wav";
        String mp3File = yygypathstr +"/" +"test.mp3";

        System.out.println(mp3File);
        InputStream inputStream = null;
        //this.getAssets().open("qdreamer.wav");
        try {

            inputStream = this.getAssets().open("test.wav");

        } catch (IOException e) {

            Log.e("tag", e.getMessage());

        }
        try {
            FileOutputStream fos = openFileOutput("test.wav",
                    this.MODE_PRIVATE);
            //String inputFileContext = "hello android";
            byte buf[] = new byte[1024];
            int len;
            while((len = inputStream.read(buf))!= -1)
            {
                fos.write(buf, 0, len);
            }

            inputStream.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        LameHelper.getInstance().convertmp3(wavFile,mp3File);
        System.out.println("success convert");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
