package com.example.my_scheduler.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_scheduler.R;
import com.example.my_scheduler.data.ScheduleRecyclerData;
//import com.example.my_scheduler.data.SchedulerData;
import com.example.my_scheduler.recycler_adapter.HorizontalAdapter;
import com.example.my_scheduler.recycler_adapter.ScheduleRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";

    private Button sidebar_btn;
    private Button tips_btn;

    private Context context;

    private TextView textDate;
    private String time;
    private CalendarView calendarView;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d");

    private FloatingActionButton add_main_btn;
    private FloatingActionButton add_schedule_btn;
    private FloatingActionButton add_memo_btn;
    private FloatingActionButton add_diary_btn;

    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    //private ArrayList<ArrayList<ScheduleRecyclerData>> allScheduleList = new ArrayList();
    //private ArrayList<ArrayList<ScheduleRecyclerData>> allMemoList = new ArrayList();
    //private ArrayList<ArrayList<ScheduleRecyclerData>> allDiaryList = new ArrayList();

    String title;
    String content;
    String start_date;
    String end_date;
    String start_time;
    String end_time;
    String alarm_date;
    String alarm_time;
    String location;
    String image_path;

    // 메인에서 표시되는 정보이외에 정보까지 저장된 리스트
    private ArrayList<String> schedule_list_detail = new ArrayList<>();

    int item_position;

    RecyclerView recyclerView = null ;
    ScheduleRecyclerAdapter schedule_adapter = null ;

    // 메인 recyclerview 에 표시될 스케줄 리스트
    public static ArrayList<ScheduleRecyclerData> schedule_list = new ArrayList<>();

    // 메인 recyclerview 에 표시될 메모 리스트
    public static ArrayList<ScheduleRecyclerData> memo_list = new ArrayList<>();

    // 메인 recyclerview 에 표시될 일기 리스트
    public static ArrayList<ScheduleRecyclerData> diary_list = new ArrayList<>();

    // back 버튼 종료 이벤트 설정
    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 리사이클러뷰 초기화
        /*this.initializeData();
        RecyclerView view = findViewById(R.id.total_recycler);

        HorizontalAdapter horizontalAdapter = new HorizontalAdapter(this, allScheduleList);

        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        view.setAdapter(horizontalAdapter);*/

        // 리사이클러뷰 초기화
        recyclerView = findViewById(R.id.total_recycler) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        schedule_adapter = new ScheduleRecyclerAdapter(schedule_list) ;
        recyclerView.setAdapter(schedule_adapter) ;
        // 리사이클러뷰에 LinearLayoutManager 지정. (vertical)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 커스텀 아이템 클릭 리스너 오버라이드
        // 수정(그냥 클릭)
        schedule_adapter.setOnItemClickListener(new ScheduleRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                item_position = position;

                Intent edit_item_intent = new Intent(MainActivity.this, AddScheduleActivity.class);

                edit_item_intent.putExtra("title", schedule_list.get(position).getTitle());
                edit_item_intent.putExtra("content", schedule_list.get(position).getContent());
                edit_item_intent.putExtra("start_date", schedule_list.get(position).getStart());
                edit_item_intent.putExtra("end_date", schedule_list.get(position).getEnd());


                startActivityForResult(edit_item_intent, 4321);
            }
        });
        // 삭제(길게 클릭)
        schedule_adapter.setOnItemLongClickListener(new ScheduleRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int position) {
                item_position = position;

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("일정 삭제");
                builder.setMessage("일정을 삭제하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem(item_position);
                    }
                });
                builder.setNegativeButton("취소",null);
                builder.show();
            }
        });

        // context
        context = getApplicationContext();

        // 현재 날짜 설정
        textDate = findViewById(R.id.whenDate);
        Date date = new Date();
        time = format.format(date);
        textDate.setText(time);

        // 달력 날짜 선택 이벤트 설정
        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                time = year + "/" + (month + 1) + "/" + dayOfMonth;
                //
                textDate.setText(time); // 선택한 날짜로 설정
            }
        });
        
        // fab 애니메이션
        fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(context, R.anim.fab_close);

        // 사이드 바로 이동
        sidebar_btn = findViewById(R.id.sidebar);

        sidebar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sidebar_intent = new Intent(MainActivity.this, SidebarActivity.class);

                startActivity(sidebar_intent);
            }
        });

        // 도움말 페이지 이동
        tips_btn = findViewById(R.id.tips);

        tips_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tips_intent = new Intent(MainActivity.this, TipsActivity.class);

                startActivity(tips_intent);
            }
        });

        // 일정, 메모, 일기 추가 페이지 이동
        add_main_btn = findViewById(R.id.addSchedule_main);
        add_schedule_btn = findViewById(R.id.addSchedule_schedule);
        add_memo_btn = findViewById(R.id.addSchedule_memo);
        add_diary_btn = findViewById(R.id.addSchedule_diary);

        add_main_btn.setOnClickListener(this);
        add_schedule_btn.setOnClickListener(this);
        add_memo_btn.setOnClickListener(this);
        add_diary_btn.setOnClickListener(this);

    }

    /*public void initializeData() {
        ArrayList<ScheduleRecyclerData> scheduleList = new ArrayList();

        scheduleList.add(new ScheduleRecyclerData("일정이 없습니다.", "", "", ""));

        allScheduleList.add(scheduleList);


        ArrayList<ScheduleRecyclerData> memoList = new ArrayList();

        memoList.add(new ScheduleRecyclerData("메모가 없습니다.", "", "", ""));

        allScheduleList.add(memoList);


        ArrayList<ScheduleRecyclerData> diaryList = new ArrayList();

        diaryList.add(new ScheduleRecyclerData("일기가 없습니다.", "", "", ""));

        allScheduleList.add(diaryList);
    }*/

    public void addItem(String today_date, String title, String content, String start, String end ) {
        ScheduleRecyclerData item = new ScheduleRecyclerData(today_date, title, content, start, end);

        schedule_list.add(item);
    }

    public void setItem(String today_date, String title, String content, String start, String end ) {
        ScheduleRecyclerData item = new ScheduleRecyclerData(today_date, title, content, start, end);

        schedule_list.add(item_position, item);
    }

    public void deleteItem(int position) {
        schedule_list.remove(item_position);

        schedule_adapter.notifyItemRemoved(item_position);

        TextView noSchedule_text = findViewById(R.id.noSchedule);
        if(schedule_list.size() > 0) {
            noSchedule_text.setVisibility(View.GONE);
        }
        else {
            noSchedule_text.setVisibility(View.VISIBLE);
        }
    }

    public void updateSharedPreference() {
        String today_date = textDate.getText().toString();

        schedule_list_detail.add(today_date);
        schedule_list_detail.add(title);
        schedule_list_detail.add(content);
        schedule_list_detail.add(start_date);
        schedule_list_detail.add(end_date);
        schedule_list_detail.add(start_time);
        schedule_list_detail.add(end_time);
        schedule_list_detail.add(alarm_date);
        schedule_list_detail.add(alarm_time);
        schedule_list_detail.add(location);
        schedule_list_detail.add(image_path);

        saveSharedPreference(this, today_date, schedule_list_detail);
    }

    public static void saveSharedPreference(Context context, String key, ArrayList<String> arrayList) {
        SharedPreferences pref = context.getSharedPreferences("schedule_db",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();

        Set<String> set = new HashSet<>();
        set.addAll(arrayList);
        edit.putStringSet(key, set);

        edit.commit();
    }

    public static ArrayList<String> loadSharedPreference(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("schedule_db",Context.MODE_PRIVATE);
        Set<String> set = pref.getStringSet(key, null);

        return new ArrayList<>(set);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addSchedule_main:
                toggleFab();

                break;

            case R.id.addSchedule_schedule:
                toggleFab();

                Intent add_schedule_intent = new Intent(MainActivity.this, AddScheduleActivity.class);
                startActivityForResult(add_schedule_intent, 1234);

                /*Intent intent = getIntent();
                String title = intent.getStringExtra("title");
                String content = intent.getStringExtra("content");
                String start_date = intent.getStringExtra("start_date");
                String end_date = intent.getStringExtra("end_date");

                if(title != null) {
                    addItem(title, content, start_date, end_date);
                }*/

                break;

            case R.id.addSchedule_memo:
                toggleFab();

                Intent add_memo_intent = new Intent(MainActivity.this, AddMemoActivity.class);
                startActivityForResult(add_memo_intent, 1234);

                break;

            case R.id.addSchedule_diary:
                toggleFab();

                Intent add_diary_intent = new Intent(MainActivity.this, AddDiaryActivity.class);
                startActivityForResult(add_diary_intent, 1234);

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 일정 입력
        if(requestCode == 1234) {
            if (data != null && resultCode == Activity.RESULT_OK) {
                title = data.getStringExtra("title");
                content = data.getStringExtra("content");
                start_date = data.getStringExtra("start_date");
                end_date = data.getStringExtra("end_date");

                start_time = data.getStringExtra("start_time");
                end_time = data.getStringExtra("end_time");
                alarm_time = data.getStringExtra("alarm_time");
                alarm_date = data.getStringExtra("alarm_date");
                location = data.getStringExtra("location");
                image_path = data.getStringExtra("image_path");

                //System.out.println(title + " / " + content + " / " + start_date + " / " + end_date);

                if( (title != null) && (start_date != null) && (end_date != null) ) {
                    addItem(textDate.getText().toString(), title, content, start_date, end_date);

                    updateSharedPreference();
                }
            }
        }

        // 일정 수정
        else if(requestCode == 4321) {
            if (data != null && resultCode == Activity.RESULT_OK) {
                title = data.getStringExtra("title");
                content = data.getStringExtra("content");
                start_date = data.getStringExtra("start_date");
                end_date = data.getStringExtra("end_date");

                start_time = data.getStringExtra("start_time");
                end_time = data.getStringExtra("end_time");
                alarm_time = data.getStringExtra("alarm_time");
                alarm_date = data.getStringExtra("alarm_date");
                location = data.getStringExtra("location");
                image_path = data.getStringExtra("image_path");

                if( (title != null) && (start_date != null) && (end_date != null) ) {
                    schedule_list.remove(item_position);
                    setItem(textDate.getText().toString(), title, content, start_date, end_date);

                    updateSharedPreference();
                }
            }
        }

    }

    private void toggleFab() {
        if(isFabOpen) {
            add_main_btn.setImageResource(R.drawable.ic_add);
            add_schedule_btn.startAnimation(fab_close);
            add_memo_btn.startAnimation(fab_close);
            add_diary_btn.startAnimation(fab_close);
            isFabOpen = false;
        }
        else {
            add_main_btn.setImageResource(R.drawable.ic_close);
            add_schedule_btn.startAnimation(fab_open);
            add_memo_btn.startAnimation(fab_open);
            add_diary_btn.startAnimation(fab_open);
            isFabOpen = true;
        }
    }

    @Override
    public void onBackPressed() {

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
            finish();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        schedule_adapter.notifyDataSetChanged() ;

        TextView noSchedule_text = findViewById(R.id.noSchedule);
        if(schedule_list.size() > 0) {
            noSchedule_text.setVisibility(View.GONE);
        }
        else {
            noSchedule_text.setVisibility(View.VISIBLE);
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
