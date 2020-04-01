package com.example.my_scheduler.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_scheduler.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MakeBackupActivity extends AppCompatActivity {

    private static final String TAG = "AddDiaryActivity";

    private Button back_btn;
    private Button make_backup_btn;
    private Button restore_btn;
    private Button delete_btn;

    public static ArrayList<String> items;
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_backup);

        back_btn = findViewById(R.id.back);
        back_btn.setOnClickListener(listener);

        make_backup_btn = findViewById(R.id.make_backup_btn);
        make_backup_btn.setOnClickListener(listener);

        restore_btn = findViewById(R.id.restore_btn);
        restore_btn.setOnClickListener(listener);

        delete_btn = findViewById(R.id.delete_btn);
        delete_btn.setOnClickListener(listener);

        // 빈 데이터 리스트 생성
        items = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, items);

        listView = findViewById(R.id.backup_data_list);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.back:
                    Intent sidebar_intent = new Intent(MakeBackupActivity.this, SidebarActivity.class);

                    // 인텐트 시작
                    startActivity(sidebar_intent);

                    finish();
                    break;

                case R.id.make_backup_btn:
                    long date = System.currentTimeMillis();
                    Date mDate = new Date(date);

                    SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
                    String getTime = dateForm.format(mDate);

                    items.add(getTime + ".db");
                    adapter.notifyDataSetChanged();

                    Toast.makeText(v.getContext(), "백업 파일이 생성되었습니다", Toast.LENGTH_LONG).show();
                    break;

                case R.id.restore_btn:
                    int position = listView.getCheckedItemPosition();
                    if (position != ListView.INVALID_POSITION) {
                        Toast.makeText(v.getContext(), "해당 백업 파일로 복구했습니다", Toast.LENGTH_LONG).show();
                    }
                    break;

                case R.id.delete_btn:
                    position = listView.getCheckedItemPosition();
                    if(position != ListView.INVALID_POSITION) {
                        items.remove(position);
                        listView.clearChoices();
                        adapter.notifyDataSetChanged();

                        Toast.makeText(v.getContext(), "백업 파일이 삭제되었습니다", Toast.LENGTH_LONG).show();
                    }
                    break;
            }

            TextView noData_text = findViewById(R.id.no_data);
            if(items.size() > 0) {
                noData_text.setVisibility(View.GONE);
            }
            else {
                noData_text.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        adapter.notifyDataSetChanged() ;

        TextView noData_text = findViewById(R.id.no_data);
        if(items.size() > 0) {
            noData_text.setVisibility(View.GONE);
        }
        else {
            noData_text.setVisibility(View.VISIBLE);
        }

        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i(TAG, "onRestart");
    }
}
