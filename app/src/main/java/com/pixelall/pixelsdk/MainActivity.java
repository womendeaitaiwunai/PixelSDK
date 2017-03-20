package com.pixelall.pixelsdk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pixelall.pixellib.PixelCallBack;
import com.pixelall.pixellib.PixelResult;
import com.pixelall.pixellib.PixelSDK;
import com.pixelall.pixellib.util.Base64Utils;
import com.pixelall.pixellib.util.RSAUtils;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText e1,e2,e3;
    private TextView pri,pub;
    private PrivateKey privateKey;
    PublicKey publicKey;
    private ImageView mine1,mine2,mine3,mine4;
    private PixelSDK pixelSDK;

  String privateString="MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEA5RIfZrHBuEZwXWh0s536FvMvk+20Zzz5C" +
          "EfwR3yvoT0zlhkFjYEh51/2ztqxsW3AHsZs7O2irO+qBB6gQt/kaQIDAQABAkEAiCxIzHSZM2F0RKLm1SvxUplITEj/eGuvovOY6/Y8Nb2a" +
          "7P9E+HCYWfE9j9pJzjFbFVdHyG63y+cal3tIHkW1cQIhAP0u3N255CAbYbaKxTWTmdYNI1E8O480EndoKoFUgFLtAiEA556U0YSvaD+ejdlpP2xqgBf" +
          "4HdPi+4sB3FWlBYmAu+0CIApzMbiRIKJWnvza03L3qaTVG/0RY" +
          "F/zxUNacE6wPy+tAiBwuBZImMAchcmN0t6LhSGXURLowTNXo2C2b9+tgCtsSQIgONf45mJHtrtp6IgNQuwc7VdJ4sgHlaS2aDInyWGsXJk=";

    String publicString="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAOUSH2axwbhGcF1odLOd+hbzL5PttGc8+QhH8Ed8r6E9M5YZBY2BIedf9s7asbFtwB7GbOztoqzvqgQeoELf5GkCAwEAAQ==";

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
        Log.i("privateString",privateString);
        Log.i("publicString",publicString);


        mine1= (ImageView) findViewById(R.id.mine1);
        mine2= (ImageView) findViewById(R.id.mine2);
        mine3= (ImageView) findViewById(R.id.mine3);
        mine4= (ImageView) findViewById(R.id.mine4);

        mine1.setOnClickListener(this);
        mine2.setOnClickListener(this);
        mine3.setOnClickListener(this);
        mine4.setOnClickListener(this);

        pixelSDK=new PixelSDK(MainActivity.this);
        pixelSDK.setCheckCallBack(new PixelCallBack() {
            @Override
            public void checkResult(PixelResult pixelResult) {
                if (pixelResult.getResultCode()==0x00){
                    Toast.makeText(MainActivity.this, "检测通过", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,pixelResult.getResultMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jiami:
                try {
                    String jiamiString = Base64Utils.encode(RSAUtils.encryptData(e1.getText().toString().getBytes(),RSAUtils.loadPublicKey(publicString)));
                    e2.setText(jiamiString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.jiemi:
                try{
                    byte[] jiemiString=RSAUtils.decryptData(Base64Utils.decode(e2.getText().toString()),RSAUtils.loadPrivateKey(privateString));
                    e3.setText(new String(jiemiString));
                }catch (Exception e){

                }

                break;
            case R.id.huoqu:
                KeyPair keyPair=RSAUtils.generateRSAKeyPair();
                 privateKey=keyPair.getPrivate();
                 publicKey=keyPair.getPublic();

                pri.setText(Base64Utils.encode(privateKey.getEncoded()));
                Log.i("得到的私用",Base64Utils.encode(privateKey.getEncoded()));
                pub.setText(Base64Utils.encode(publicKey.getEncoded()));
                Log.i("得到的公有",Base64Utils.encode(publicKey.getEncoded()));
                break;
            case R.id.mine1:
                Bitmap bitmap1= BitmapFactory.decodeResource(getResources(),R.mipmap.mine1);
                pixelSDK.start(bitmap1);
                bitmap1.recycle();
                System.gc();
                break;

            case R.id.mine2:
                Bitmap bitmap2= BitmapFactory.decodeResource(getResources(),R.mipmap.mine2);
                pixelSDK.start(bitmap2);
                bitmap2.recycle();
                break;

            case R.id.mine3:
                Bitmap bitmap3= BitmapFactory.decodeResource(getResources(),R.mipmap.mine3);
                pixelSDK.start(bitmap3);
                bitmap3.recycle();
                break;
            case R.id.mine4:
                Bitmap bitmap4= BitmapFactory.decodeResource(getResources(),R.mipmap.mine4);
                pixelSDK.start(bitmap4);
                bitmap4.recycle();
                break;
        }
    }
}
