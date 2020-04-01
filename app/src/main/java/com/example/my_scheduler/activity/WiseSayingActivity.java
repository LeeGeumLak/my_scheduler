package com.example.my_scheduler.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.my_scheduler.R;
import com.example.my_scheduler.data.SayingRecyclerData;
import com.example.my_scheduler.recycler_adapter.SayingRecyclerAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WiseSayingActivity extends AppCompatActivity {
    private static final String TAG = "WiseSayingActivity";

    private Button back_btn;
    private Button btn_play, btn_stop, btn_pause, btn_prev, btn_next;

    private TextView play_time;

    private MediaPlayer mp;
    private SeekBar seekBar;

    private int songs[];
    private int playing = 0;

    private RecyclerView recyclerView = null ;
    private SayingRecyclerAdapter adapter = null ;
    private LinearLayoutManager layoutManager;

    private boolean thread_condition = true;

    ArrayList<SayingRecyclerData> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wise_saying);

        // 사이드바로 가기 버튼
        back_btn = findViewById(R.id.back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sidebar_intent = new Intent(WiseSayingActivity.this, SidebarActivity.class);

                // 인텐트 시작
                startActivity(sidebar_intent);

                finish();
            }
        });

        // 리스트 초기화
        init_list();

        // 리사이클러뷰 초기화
        recyclerView = findViewById(R.id.saying_recycler) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        adapter = new SayingRecyclerAdapter(list) ;
        adapter.setData(list);
        recyclerView.setAdapter(adapter) ;

        // 리사이클러뷰에 LinearLayoutManager 지정. (horizontal)
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        // 음악 플레이어
        btn_play = findViewById(R.id.button_play);
        btn_stop = findViewById(R.id.button_stop);
        btn_pause = findViewById(R.id.button_pause);
        btn_prev = findViewById(R.id.button_prev);
        btn_next = findViewById(R.id.button_next);

        play_time = findViewById(R.id.play_time_text);

        songs = new int[5];
        songs[0] = R.raw.asmr_1;
        songs[1] = R.raw.asmr_2;
        songs[2] = R.raw.asmr_3;
        songs[3] = R.raw.asmr_4;
        songs[4] = R.raw.asmr_5;

        mp = new MediaPlayer();
        mp = MediaPlayer.create(this, songs[playing]);

        seekBar = findViewById(R.id.playbar);
        seekBar.setVisibility(ProgressBar.VISIBLE);
        seekBar.setMax(mp.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    mp.seekTo(progress);
                }
                int m = progress / 60000;
                int s = (progress % 60000) / 1000;
                String strTime = String.format("%02d:%02d", m, s);
                play_time.setText(strTime);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(mp != null) {
                    mp.stop();
                }*/
                mp = MediaPlayer.create(WiseSayingActivity.this, songs[ playing ]);
                mp.start();

                Thread();
            }
        });

        /*playing = (playing+1)%songs.length;
        if( mp!=null ) {
            mp.stop();
        }
        mp = MediaPlayer.create(WiseSayingActivity.this, songs[ playing ]);
        mp.start();*/

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                try {
                    mp.prepare();
                } catch(IOException ie) {
                    ie.printStackTrace();
                }
                mp.seekTo(0);
            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.pause();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playing++;
                if(playing <= 4) {
                    mp.stop();
                    mp = MediaPlayer.create(WiseSayingActivity.this, songs[ playing ]);
                    seekBar.setVisibility(ProgressBar.VISIBLE);
                    seekBar.setMax(mp.getDuration());

                    mp.start();
                    Thread();
                }
                else {
                    playing = 4;
                }
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playing--;
                if(playing >= 0) {
                    mp.stop();
                    mp = MediaPlayer.create(WiseSayingActivity.this, songs[ playing ]);
                    seekBar.setVisibility(ProgressBar.VISIBLE);
                    seekBar.setMax(mp.getDuration());

                    mp.start();
                    Thread();
                }
                else {
                    playing = 0;
                }
            }
        });
    }

    public void Thread(){
        Runnable task = new Runnable() {
            @Override
            public void run(){
                // 음악이 재생중일때
                while (mp.isPlaying()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    seekBar.setProgress(mp.getCurrentPosition());

                }

            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public void init_list() {
        list.add(new SayingRecyclerData(R.drawable.wise_1, R.string.wise_saying_1, R.string.saying_man_1));
        list.add(new SayingRecyclerData(R.drawable.wise_2, R.string.wise_saying_2, R.string.saying_man_2));
        list.add(new SayingRecyclerData(R.drawable.wise_3, R.string.wise_saying_3, R.string.saying_man_3));
        list.add(new SayingRecyclerData(R.drawable.wise_4, R.string.wise_saying_4, R.string.saying_man_4));
        list.add(new SayingRecyclerData(R.drawable.wise_5, R.string.wise_saying_5, R.string.saying_man_5));
        list.add(new SayingRecyclerData(R.drawable.wise_6, R.string.wise_saying_6, R.string.saying_man_6));
        list.add(new SayingRecyclerData(R.drawable.wise_7, R.string.wise_saying_7, R.string.saying_man_7));
        list.add(new SayingRecyclerData(R.drawable.wise_8, R.string.wise_saying_8, R.string.saying_man_8));
        list.add(new SayingRecyclerData(R.drawable.wise_9, R.string.wise_saying_9, R.string.saying_man_9));
        list.add(new SayingRecyclerData(R.drawable.wise_10, R.string.wise_saying_10, R.string.saying_man_10));
        list.add(new SayingRecyclerData(R.drawable.wise_11, R.string.wise_saying_11, R.string.saying_man_11));
        list.add(new SayingRecyclerData(R.drawable.wise_12, R.string.wise_saying_12, R.string.saying_man_12));
        list.add(new SayingRecyclerData(R.drawable.wise_13, R.string.wise_saying_13, R.string.saying_man_13));
        list.add(new SayingRecyclerData(R.drawable.wise_14, R.string.wise_saying_14, R.string.saying_man_14));
        list.add(new SayingRecyclerData(R.drawable.wise_15, R.string.wise_saying_15, R.string.saying_man_15));
        list.add(new SayingRecyclerData(R.drawable.wise_16, R.string.wise_saying_16, R.string.saying_man_16));
        list.add(new SayingRecyclerData(R.drawable.wise_17, R.string.wise_saying_17, R.string.saying_man_17));

    }

    @Override
    protected void onStart() {
        super.onStart();

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

        if (mp != null) {
            mp.release();
            mp = null;
        }

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy");

        if (mp != null) {
            mp.release();
            mp = null;
        }

        thread_condition = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i(TAG, "onRestart");
    }
}
