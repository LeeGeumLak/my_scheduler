package com.example.my_scheduler.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.my_scheduler.R;
import com.example.my_scheduler.applock_core.AppLock;
import com.example.my_scheduler.applock_core.LockManager;

public class SidebarActivity extends AppCompatActivity implements OnClickListener {

    static final String TAG = "SidebarActivity";

    private TextView data_backup_btn;
    private TextView data_reset_btn;
    private TextView app_evaluate_btn;
    private TextView wise_saying_btn;
    private TextView meditate_btn;
    private TextView alarm_btn;

    private ImageButton sidebar_cancel_btn;

    private Button btOnOff;
    private Button btChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar);


        // 임시 설정 중
        // 비밀번호 설정 액티비티
        btOnOff = findViewById(R.id.bt_on_off);
        btOnOff.setOnClickListener(this);

        btChange = findViewById(R.id.bt_change);
        btChange.setOnClickListener(this);

        updateUI();

        // 데이터 백업/복구 액티비티 이동
        data_backup_btn = findViewById(R.id.data_backup);

        data_backup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backup_intent = new Intent(SidebarActivity.this, MakeBackupActivity.class);

                // 인텐트 시작
                startActivity(backup_intent);
            }
        });


        // 데이터 초기화 액티비티 이동
        data_reset_btn = findViewById(R.id.data_reset);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setIcon(R.drawable.ic_launcher_background);
        builder.setMessage("정말 초기화하시겠습니까?");
        //builder.setTitle("경고");
        builder.setCancelable(false);

        // 확인버튼
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SidebarActivity.this, "초기화 완료!", Toast.LENGTH_SHORT).show();
            }
        });

        // 취소버튼
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SidebarActivity.this, "초기화 취소!", Toast.LENGTH_SHORT).show();
            }
        });

        data_reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 메인 다이얼로그 생성
                AlertDialog alert = builder.create();
                // 다이얼로그 보기
                alert.show();

            }
        });

        // 명언/힐링 한마디 액티비티 이동
        wise_saying_btn = findViewById(R.id.wise_saying);

        wise_saying_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wise_saying_intent = new Intent(SidebarActivity.this, WiseSayingActivity.class);

                // 인텐트 시작
                startActivity(wise_saying_intent);
            }
        });

        // 명상하기 액티비티 이동
        meditate_btn = findViewById(R.id.meditate);

        meditate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent meditate_intent = new Intent(SidebarActivity.this, MeditateActivity.class);

                // 인텐트 시작
                startActivity(meditate_intent);
            }
        });

        // 알람기능 액티비티 이동
        alarm_btn = findViewById(R.id.alarm);

        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alarm_intent = new Intent(SidebarActivity.this, AlarmActivity.class);

                // 인텐트 시작
                startActivity(alarm_intent);
            }
        });

        // 평가하기 액티비티 이동 (구글 플레이 화면)
        app_evaluate_btn = findViewById(R.id.app_evaluate);

        app_evaluate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent evaluate_intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.teamnovamember.co.kr/index.php"));

                // 인텐트 시작
                startActivity(evaluate_intent);
            }
        });


        // 다시 메인 액티비티로 이동
        sidebar_cancel_btn = findViewById(R.id.sidebar_cancel);

        sidebar_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main_intent = new Intent(SidebarActivity.this, MainActivity.class);

                // 인텐트 시작
                startActivity(main_intent);

                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btOnOff)) {
            int type = LockManager.getInstance().getAppLock().isPasscodeSet() ? AppLock.DISABLE_PASSLOCK : AppLock.ENABLE_PASSLOCK;
            Intent intent = new Intent(this, AppLockActivity.class);
            intent.putExtra(AppLock.TYPE, type);
            startActivityForResult(intent, type);
        }
        else if (view.equals(btChange)) {
            Intent intent = new Intent(this, AppLockActivity.class);
            intent.putExtra(AppLock.TYPE, AppLock.CHANGE_PASSWORD);
            intent.putExtra(AppLock.MESSAGE, "현재 비밀번호를 입력하세요");
            startActivityForResult(intent, AppLock.CHANGE_PASSWORD);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case AppLock.DISABLE_PASSLOCK:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "비밀번호 삭제 완료", Toast.LENGTH_SHORT).show();
                }
                break;
            case AppLock.ENABLE_PASSLOCK:
            case AppLock.CHANGE_PASSWORD:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "비밀번호 설정 완료", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

        updateUI();
    }

    private void updateUI() {
        if (LockManager.getInstance().getAppLock().isPasscodeSet()) {
            btOnOff.setText("삭제");
            btChange.setEnabled(true);
        }
        else {
            btOnOff.setText("설정");
            btChange.setEnabled(false);
        }
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