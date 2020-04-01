package com.example.my_scheduler.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.my_scheduler.R;
import com.example.my_scheduler.async_task.MyAsyncTask;

public class MeditateActivity extends Activity {

    private static MeditateActivity instance;
    public static MeditateActivity getInstance() { return instance; }

    /** start of class members */
    MyAsyncTask asyncTask = new MyAsyncTask(); // extends AsyncTask

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_meditate);

        // execute AsyncTask in another thread, with parameters
        // (c.f. if no parameter is needed, taskTimer.execute(""))
        asyncTask.setTextView(R.id.text_timer);
        asyncTask.setTime(5);
        asyncTask.execute("");

    }
}
