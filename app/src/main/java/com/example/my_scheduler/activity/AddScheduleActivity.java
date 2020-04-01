package com.example.my_scheduler.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.my_scheduler.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddScheduleActivity extends AppCompatActivity {

    private Button back_btn;
    private Button input_schedule_btn;
    private Button get_image_btn;

    private EditText start_date_edit;
    private EditText end_date_edit;
    private EditText alarm_date_edit;

    private EditText start_time_edit;
    private EditText end_time_edit;
    private EditText set_alarm;

    private EditText title_edit;
    private EditText content_edit;

    private EditText location_edit;

    private ImageView image_edit;

    private CheckBox alarm_check_box;

    private String title_str;
    private String content_str;
    private String start_date_str;
    private String end_date_str;
    private String start_time_str;
    private String end_time_str;
    private String alarm_date_str;
    private String alarm_time_str;
    private String location_str;
    private String image_path_str;

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker_start = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel_start();
        }
    };

    DatePickerDialog.OnDateSetListener myDatePicker_end = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel_end();
        }
    };

    DatePickerDialog.OnDateSetListener myDatePicker_alarm = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel_alarm();
        }
    };

    private static final String TAG = "AddScheduleActivity";
    private Boolean isPermission = true;
    private static final int PICK_FROM_ALBUM = 1;
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        // 메인화면으로 다시 돌아가는 버튼
        back_btn = findViewById(R.id.back);

        // 일정 입력 후, 메인화면으로 이동
        input_schedule_btn = findViewById(R.id.input_schedule);

        // 일정 제목
        title_edit = findViewById(R.id.TitleEdit);

        // 일정 내용
        content_edit = findViewById(R.id.ContentEdit);

        // 일정 시작 날짜
        start_date_edit = findViewById(R.id.start_date_edit);

        start_date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddScheduleActivity.this, myDatePicker_start, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // 일정 시작 시간
        start_time_edit = findViewById(R.id.start_time_edit);
        start_time_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String state = "AM";
                        // 선택한 시간이 12를 넘을경우 "PM"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            state = "PM";
                        }
                        // EditText에 출력할 형식 지정


                        start_time_edit.setText(state + " " + selectedHour + "시 " + selectedMinute + "분");
                        start_time_str = start_time_edit.getText().toString();
                    }
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("시간 설정");
                mTimePicker.show();
            }
        });


        // 일정 종료 날짜
        end_date_edit = findViewById(R.id.end_date_edit);
        end_date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddScheduleActivity.this, myDatePicker_end, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // 일정 종료 시간
        end_time_edit = findViewById(R.id.end_time_edit);
        end_time_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String state = "AM";
                        // 선택한 시간이 12를 넘을경우 "PM"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            state = "PM";
                        }
                        // EditText에 출력할 형식 지정
                        end_time_edit.setText(state + " " + selectedHour + "시 " + selectedMinute + "분");
                        end_time_str = end_time_edit.getText().toString();
                    }
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("시간 설정");
                mTimePicker.show();
            }
        });

        alarm_check_box = findViewById(R.id.alarm_check_box);
        /*alarm_check_box.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !(((CheckBox)v).isChecked()) ) {
                    alarm_date_edit.setClickable(true);
                    set_alarm.setClickable(true);
                }
            }
        });*/

        // 알람 설정
        alarm_date_edit = findViewById(R.id.alarm_date_select);
        alarm_date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarm_check_box.isChecked()) {
                    new DatePickerDialog(AddScheduleActivity.this, myDatePicker_alarm, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        set_alarm = findViewById(R.id.alarm_time_select);
        set_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarm_check_box.isChecked()) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(AddScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            String state = "AM";
                            // 선택한 시간이 12를 넘을경우 "PM"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
                            if (selectedHour > 12) {
                                selectedHour -= 12;
                                state = "PM";
                            }
                            // EditText에 출력할 형식 지정
                            set_alarm.setText(state + " " + selectedHour + "시 " + selectedMinute + "분");
                            alarm_time_str = set_alarm.getText().toString();
                        }
                    }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker 출현
                    mTimePicker.setTitle("시간 설정");
                    mTimePicker.show();
                }
            }
        });

        // 위치 입력
        location_edit = findViewById(R.id.edit_location);

        // 이미지 입력 버튼
        get_image_btn = findViewById(R.id.get_image);

        get_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tedPermission();

                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                if(isPermission) {
                    goToAlbum();
                }
                else {
                    Toast.makeText(v.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                }
            }

        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main_intent = new Intent(AddScheduleActivity.this, MainActivity.class);

                // 인텐트 시작
                startActivity(main_intent);

                finish();
            }
        });

        input_schedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_str = title_edit.getText().toString();
                content_str = content_edit.getText().toString();
                alarm_date_str = alarm_date_edit.getText().toString();
                location_str = location_edit.getText().toString();

                Intent main_intent = new Intent(AddScheduleActivity.this, MainActivity.class);
                main_intent.putExtra("title", title_str);
                main_intent.putExtra("content", content_str);
                main_intent.putExtra("start_date", start_date_str);
                main_intent.putExtra("end_date", end_date_str);
                main_intent.putExtra("start_time", start_time_str);
                main_intent.putExtra("end_time", end_time_str);
                main_intent.putExtra("alarm_date", alarm_date_str);
                main_intent.putExtra("alarm_time", alarm_time_str);
                main_intent.putExtra("location", location_str);
                main_intent.putExtra("image_path", image_path_str);

                setResult(RESULT_OK, main_intent);

                // 인텐트 시작
                //startActivity(main_intent);

                finish();
            }
        });
    }

    private void updateLabel_start() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText et_date = findViewById(R.id.start_date_edit);
        et_date.setText(sdf.format(myCalendar.getTime()));
        start_date_str = sdf.format(myCalendar.getTime());

    }

    private void updateLabel_end() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText et_date = findViewById(R.id.end_date_edit);
        et_date.setText(sdf.format(myCalendar.getTime()));
        end_date_str = sdf.format(myCalendar.getTime());

    }

    private void updateLabel_alarm() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText et_date = findViewById(R.id.alarm_date_select);
        et_date.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }
            return;
        }

        Uri photoUri = data.getData();
        Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

        Cursor cursor = null;

        try {
            //Uri 스키마를 content:/// 에서 file:/// 로  변경한다.
            String[] proj = {MediaStore.Images.Media.DATA};

            assert photoUri != null;
            cursor = getContentResolver().query(photoUri, proj, null, null, null);

            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();
            tempFile = new File(cursor.getString(column_index));
            Log.d(TAG, "tempFile Uri : " + Uri.fromFile(tempFile));

        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        setImage();
    }

    // 앨범에서 이미지 가져오기
    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    // tempFile을 bitmap으로 변환 후, ImageView에 설정
    private void setImage() {
        image_edit = findViewById(R.id.imageView);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());

        image_edit.setImageBitmap(originalBm);
        image_path_str = tempFile.getAbsolutePath();

        /*  tempFile 사용 후 null 처리
         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
         *  기존에 데이터가 남아 있게 되면 원치 않게 삭제됨
         */
        tempFile = null;
    }

    // 권한 설정
    private void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                isPermission = true;
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
                isPermission = false;
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        if(intent != null) {
            title_edit.setText(intent.getStringExtra("title"));
            content_edit.setText(intent.getStringExtra("content"));
            start_date_edit.setText(intent.getStringExtra("start_date"));
            end_date_edit.setText(intent.getStringExtra("end_date"));

            title_str = intent.getStringExtra("title");
            content_str = intent.getStringExtra("content");
            start_date_str = intent.getStringExtra("start_date");
            end_date_str = intent.getStringExtra("end_date");

        }

        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i(TAG, "onPause");

        if(TextUtils.isEmpty(title_str)) { // 제목이 비어있는지 판단
            Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
            //return;
        }
        else if(TextUtils.isEmpty(start_date_str)) { // 일정 시작 날짜가 비어있는지 판단
            Toast.makeText(this, "일정 시작 날짜를 입력해주세요", Toast.LENGTH_SHORT).show();
            //return;
        }
        else if(TextUtils.isEmpty(end_date_str)) { // 일정 종료 날짜가 비어있는지 판단
            Toast.makeText(this, "일정 종료 날짜를 입력해주세요", Toast.LENGTH_SHORT).show();
            //return;
        }
        else {
            Toast.makeText(this, "일정이 입력되었습니다", Toast.LENGTH_SHORT).show();
            //return;
        }
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
