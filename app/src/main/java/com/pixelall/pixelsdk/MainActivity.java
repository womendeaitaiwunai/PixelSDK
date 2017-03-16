package com.pixelall.pixelsdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pixelall.pixellib.util.Base64Utils;
import com.pixelall.pixellib.util.RSAUtils;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText e1,e2,e3;
    private TextView pri,pub;
    private PrivateKey privateKey;
    PublicKey publicKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1= (EditText) findViewById(R.id.first);
        e2= (EditText) findViewById(R.id.next);
        e3= (EditText) findViewById(R.id.three);

        findViewById(R.id.jiami).setOnClickListener(this);
        findViewById(R.id.huoqu).setOnClickListener(this);
        findViewById(R.id.jiemi).setOnClickListener(this);

        pri= (TextView) findViewById(R.id.pri);
        pub= (TextView) findViewById(R.id.pub);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jiami:
                String jiamiString=Base64Utils.encode(RSAUtils.encryptData(e1.getText().toString().getBytes(),publicKey));
                e2.setText(jiamiString);
                break;
            case R.id.jiemi:
                byte[] jiemiString=RSAUtils.decryptData(Base64Utils.decode(e2.getText().toString()),privateKey);
                e3.setText(new String(jiemiString));
                break;
            case R.id.huoqu:
                KeyPair keyPair=RSAUtils.generateRSAKeyPair();
                 privateKey=keyPair.getPrivate();
                 publicKey=keyPair.getPublic();

                pri.setText(Base64Utils.encode(privateKey.getEncoded()));
                pub.setText(Base64Utils.encode(publicKey.getEncoded()));
                break;
        }
    }
}
