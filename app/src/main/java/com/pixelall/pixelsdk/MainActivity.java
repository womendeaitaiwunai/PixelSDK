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
//
//import com.pixelall.pixellib.PixelCallBack;
//import com.pixelall.pixellib.PixelCode;
//import com.pixelall.pixellib.PixelResult;
//import com.pixelall.pixellib.PixelSDK;
//import com.pixelall.pixellib.util.Base64Utils;
//import com.pixelall.pixellib.util.RSAUtils;

import com.pixelall.pixellib.PixelCallBack;
import com.pixelall.pixellib.PixelCode;
import com.pixelall.pixellib.PixelResult;
import com.pixelall.pixellib.PixelSDK;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

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
            public void checkResult(int type, PixelResult pixelResult) {
                if (type == PixelSDK.TYPE_CHECK_FACE ){
                    if (pixelResult.getResultCode() == PixelCode.SUCCESS) {
                        Log.i("开始-->", "检测成功，开始上传 ");
                        pixelSDK.startUpload();
                    } else Toast.makeText(MainActivity.this, "错误信息:"+pixelResult.getResultMessage(), Toast.LENGTH_SHORT).show();
                } else if (type==PixelSDK.TYPE_UPLOAD_BITMAP){
                    Log.i("结束", "结果信息: "+pixelResult.getResultMessage());
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                //httpUrlConnectionPut("http://www.cnblogs.com/");
            }
        }).start();
    }

    public String httpUrlConnectionPut(String httpUrl) {
        String result = "";
        URL url = null;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url != null) {
            try {
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestProperty("content-type", "application/json");
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setConnectTimeout(5 * 1000);
                //设置请求方式为 POST
                urlConn.setRequestMethod("POST");
                urlConn.setRequestProperty("Content-Type", "application/octet-stream");
                urlConn.setRequestProperty("Charset", "UTF-8");


                OutputStream os = urlConn.getOutputStream();
                String param = new String();
                param = "CorpID=" + "123123" +
                        "&LoginName=" + "lxl"+
                        "&send_no=" + "13126445293" +
                        "&msg=" + java.net.URLEncoder.encode("哈哈哈","UTF-8"); ;
                os.write(param.getBytes());

                DataOutputStream dos = new DataOutputStream(os);
                //写入请求参数
                //这里要注意的是，在构造JSON字符串的时候，实践证明，最好不要使用单引号，而是用“\”进行转义，否则会报错
                // 关于这一点在上面给出的参考文章里面有说明
                String jsonParam = "{\"appid\":6,\"appkey\":\"0cf0vGD/ClIrVmvVT/r5hEutH5M=\",\"openid\":200}";
                dos.writeBytes(jsonParam);
                dos.flush();
                dos.close();

                if (urlConn.getResponseCode() == 200) {
                    InputStreamReader isr = new InputStreamReader(urlConn.getInputStream());
                    BufferedReader br = new BufferedReader(isr);
                    String inputLine = null;
                    while ((inputLine = br.readLine()) != null) {
                        result += inputLine;
                    }
                    isr.close();
                    urlConn.disconnect();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.jiami:
//                try {
//                    String jiamiString = Base64Utils.encode(RSAUtils.encryptData(e1.getText().toString().getBytes(),RSAUtils.loadPublicKey(publicString)));
//                    e2.setText(jiamiString);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case R.id.jiemi:
//                try{
//                    byte[] jiemiString=RSAUtils.decryptData(Base64Utils.decode(e2.getText().toString()),RSAUtils.loadPrivateKey(privateString));
//                    e3.setText(new String(jiemiString));
//                }catch (Exception e){
//
//                }
//
//                break;
//            case R.id.huoqu:
//                KeyPair keyPair= RSAUtils.generateRSAKeyPair();
//                 privateKey=keyPair.getPrivate();
//                 publicKey=keyPair.getPublic();
//
//                pri.setText(Base64Utils.encode(privateKey.getEncoded()));
//                Log.i("得到的私用",Base64Utils.encode(privateKey.getEncoded()));
//                pub.setText(Base64Utils.encode(publicKey.getEncoded()));
//                Log.i("得到的公有", Base64Utils.encode(publicKey.getEncoded()));
//                break;
            case R.id.mine1:
                Bitmap bitmap1= BitmapFactory.decodeResource(getResources(),R.mipmap.mine1);
                pixelSDK.checkBitmap(bitmap1);
                break;

            case R.id.mine2:
                Bitmap bitmap2= BitmapFactory.decodeResource(getResources(),R.mipmap.mine2);
                pixelSDK.checkBitmap(bitmap2);
                break;

            case R.id.mine3:
                Bitmap bitmap3= BitmapFactory.decodeResource(getResources(),R.mipmap.mine3);
                pixelSDK.checkBitmap(bitmap3);
                break;
            case R.id.mine4:
                Bitmap bitmap4= BitmapFactory.decodeResource(getResources(),R.mipmap.mine4);
                pixelSDK.checkBitmap(bitmap4);
                break;
        }
    }
}
