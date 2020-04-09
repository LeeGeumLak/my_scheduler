package com.example.my_scheduler.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.my_scheduler.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddMemoActivity extends AppCompatActivity {

    private Button back_btn;
    private Button input_memo_btn;
    private Button get_image_btn;

    private EditText date_edit;
    private EditText location_edit; // edit_location

    private EditText title_edit; // TitleEdit
    private EditText content_edit; // ContentEdit

    private ImageView image_edit; // imageView

    private String date_str;
    private String title_str;
    private String content_str;
    private String location_str;
    private String image_path_str;

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel();
        }
    };

    private static final String TAG = "AddMemoActivity";
    private Boolean isPermission = true;
    private static final int PICK_FROM_ALBUM = 1;
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        // 메인화면으로 다시 돌아가는 버튼
        back_btn = findViewById(R.id.back);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main_intent = new Intent(AddMemoActivity.this, MainActivity.class);

                // 인텐트 시작
                startActivity(main_intent);

                finish();
            }
        });

        // 일정 입력 후, 메인화면으로 이동
        input_memo_btn = findViewById(R.id.input_memo);

        input_memo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_str = title_edit.getText().toString();
                content_str = content_edit.getText().toString();
                location_str = location_edit.getText().toString();

                Intent main_intent = new Intent(AddMemoActivity.this, MainActivity.class);
                main_intent.putExtra("title", title_str);
                main_intent.putExtra("content", content_str);
                main_intent.putExtra("memo_date", date_str);
                main_intent.putExtra("location", location_str);
                main_intent.putExtra("image_path", image_path_str);

                setResult(RESULT_OK, main_intent);

                // 인텐트 시작
                //startActivity(main_intent);

                finish();
            }
        });

        // 메모 날짜
        date_edit = findViewById(R.id.date_select);
        date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddMemoActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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

        // 메모 위치
        location_edit = findViewById(R.id.edit_location);

        // 일정 제목
        title_edit = findViewById(R.id.TitleEdit);

        // 일정 내용
        content_edit = findViewById(R.id.ContentEdit);
    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText et_date = findViewById(R.id.date_select);
        et_date.setText(sdf.format(myCalendar.getTime()));
        date_str = sdf.format(myCalendar.getTime());

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
            date_edit.setText(intent.getStringExtra("memo_date"));

            title_str = intent.getStringExtra("title");
            content_str = intent.getStringExtra("content");
            date_str = intent.getStringExtra("memo_date");

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
        else if(TextUtils.isEmpty(date_str)) { // 메모 날짜가 비어있는지 판단
            Toast.makeText(this, "메모 날짜를 입력해주세요", Toast.LENGTH_SHORT).show();
            //return;
        }
        else {
            Toast.makeText(this, "메모가 입력되었습니다", Toast.LENGTH_SHORT).show();
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
