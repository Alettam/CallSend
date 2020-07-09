package com.example.callsend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE=11;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS=12;

    private Button mCallBtn;
    private Button mSendBtn;
    private EditText mNumberEdTxt;
    private String numberPhone;
    private EditText mTextEdTxt;
    private String textSms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        mCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callByNumber();
            }
        });

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
            }
        });
    }

    private void sendSms() {
        readView();
        if (TextUtils.isEmpty(numberPhone)) {
            Toast.makeText(this,"Введите номер телефона",Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(textSms)) {
            Toast.makeText(this,"Введите текст СМС",Toast.LENGTH_LONG).show();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(numberPhone, null, textSms,
                    null, null);
        }
    }


    }

    private void callByNumber() {
        readView();
        if(TextUtils.isEmpty(numberPhone)){
            Toast.makeText(this,"Введите номер телефона",Toast.LENGTH_LONG).show();
        }else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else {
                Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numberPhone));
                startActivity(dialIntent);
            }
        }
    }

    private void readView(){
        numberPhone = mNumberEdTxt.getText().toString();
        textSms = mTextEdTxt.getText().toString();
    }

    private void initView() {
        mNumberEdTxt = findViewById(R.id.numberEdTxt);
        mTextEdTxt = findViewById(R.id.textEdTxt);
        mCallBtn = findViewById(R.id.callBtn);
        mSendBtn = findViewById(R.id.sendBtn);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    callByNumber();
                }else {
                    Toast.makeText(this,"Нет разрешения на звонки",Toast.LENGTH_LONG).show();
                }
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    sendSms();
                }else {
                    Toast.makeText(this,"Нет разрешения на отправку смс",Toast.LENGTH_LONG).show();
                }
        }
    }
}
