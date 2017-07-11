package com.pixelall.pixelsdk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pixelall.pixellib.PixelCallBack;
import com.pixelall.pixellib.PixelCode;
import com.pixelall.pixellib.PixelResult;
import com.pixelall.pixellib.PixelSDK;
import com.pixelall.pixellib.util.Base64Utils;
import com.pixelall.pixellib.util.RSAUtils;

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

    //String privateString="MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAw0GSYcoAY2n/hJpdnV2Cywp2t+AS8f4yDAXPt3jfmlxaOhB65T8WRsF+GHkgtDnsF/H5J6MDRI9m6D16wprr5QIDAQABAkEAt3L2G1Z0wvKmFWoLJnzjE+0C2YN3iVFwqAcVv6WbQCR8O/VFbI1bUsWS+pGiVrlqJTlEXcI9izaJDdn7VbESQQIhAOn79L4TUxQkF8pWM0s6IYHMWmI4D+I0WBed88A7yv7dAiEA1aDDDNzi65vnL5AyNM0GHB8XdmCorT5D6hnW38nSnKkCIQDk1Ci1XzbHosi1c/n0HyS3yP+3wLYf9isU5b+Fh7Rt7QIgdsnP6+UfoVets/sAj++5iAWZ7E9PPBY1eYUowIPfQxECICJ/1JyWZc5Mqy63LFid+wOGpBbHY7zulnts5kFApl4G";
    String privateString="MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDzr2QHpjbHC2iQgWRuV6EbiM6J" +
            "V8jfbkO9uwNt1hNrhAibAlU8lD2xsyynAljsSZllV1MCaupc74+ZLZk80dD2ASdfagNCnoYwyysQ" +
            "iIFN2DqaNsmCfrf8BtsHW3rjQQ6NHXrRKIP6KpADm0pNm+yFBirajDEUfwlKAyEM4jlaqBbiAK5V" +
            "miT4N+R9vJLWGuakx8p7pzWlUbw9aE6/sAd08Flo5Kk6pJ8EZTSJRdQI70oWD5KnvYWCoIlc0XWn" +
            "izTIYT0HkSJ33I1cHbs2SZU7BfGHM9c19eNUeva1erOFl2iSc0Ug7EAZ5i+GeKGt+lGq+JtnzlTr" +
            "sPOQ7YRUd+EBAgMBAAECggEACixYULlvr/+Z7e548uJVp4Cenr07dZh024bjLcKyLNrmQRJn3I+T" +
            "w8dnJdHDRJoN/V0X6hHsltSpegudShy4TIzfx+v1FuLnV0IkGhyYyVctmyKOZxRDOhJPMo9vIe2l" +
            "Xd7NMc4vNFJykLP69iJ8TVFkmJshsWlcSeq3hoIrQs2My5QvnQ2/XPFg9jrAJPeUL03ge2JEGSiM" +
            "iMmHhitogKn5SrgDoEvv7alzo9r3xnKipTdTi3RZ9vyV0dU/yly+yKzRXmDVVarGs4MDSRa8iJoR" +
            "s0ecVwEekyr7kK850BomFuTxl6gK5lWvNqcAPrPsBcMHqCMcpvRScdIVs/NQAQKBgQD9wFOGu2ST" +
            "hcXml7dBORiJHQt1ZdggICPeGGCTReCNMMa/gHKkxlL8tsr5UuUER6gjR/AVQ6bPSWM4xpZQWHiw" +
            "FguZoBUY8Fo/4ZvRZf9bbMXwCUdNuN0IPJQrxBE1CSCM9OApx7CIx+QYOU5r/7bt0i3C9uNkfEea" +
            "Mz1Iq7HQkQKBgQD12DpUZg76vTrLyHr9zbCqu/DAAhb9/sGLrjGfDz5irjkjMy1Oh9JcihrbpsoE" +
            "BStad9tNOW6IQBytjbXBCofue1qv1JCmHTTLZKO1x93pBoVniobFgFQ3gsv2w314j447VK7B15VN" +
            "LgYQKKdRl2rOuvCDj1lGUuA8WMX8N6ZBcQKBgHAQUxMKiVV5W7gFwsHERGixchELxFITv6NjIquH" +
            "za6tHukOAhNxHs9KrwNDL68fMks6hvXtJasf3vkBAvtCxzIE2mSjRQ35f/H8YgMtR4QJ2rSydFIH" +
            "1V3A2zMk6PCCMmTt9F2fOgB+PubP0Cl0CkanbtxOiogjNsAL5wHECchxAoGAJMMYtdYtF0GEsURh" +
            "X+KKMH26Gnw1g/W8a2647NgStB7pXGIivEODKWNdcMsYGlHLkdOqb3nPFYDAyRMPwrYYbYj0nqZi" +
            "mvRzGJcMA/rHLDlVaY5FaPMvpL1iUpwY/xY4ZpbbKxFZuhjPv0rU+lZyGkfJ/2JIL0PlVCDSOkz4" +
            "B4ECgYBn8tOGTWP6En7q4b7LDYnAuvtso9d32/cbkhQm7dyPR9OmmptVeBsBK6gCk+4S7itsJ/N0" +
            "2uN1WKlXdigSZ+wyYbLkCrX8C0qTSqj4v1Uls98Eqr+zHynfdCOLLnvNzaV9eoS7ENIDpdF4ErIy" +
            "Um+8vv7V4p8/yah5Qrl+zDyIZw==";
//    String publicString="MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAsPniMpP3bJ4loZo4129vQl7+rQrOKy6O" +
//        "Njlz84up+HsQoJc3Bobv++HLxQx9y0T1zPp6NT8zyWUNFq+kqk5dkgtr3Ap2DjijXDeYS8HWkg8u" +
//        "wGLh9Cav7RdEyJRobUpniGpft/pGxQuh46NM4PmvIuQ3wdOrfm7gycc0n0pK4HxKT4fShoWXZzv6" +
//        "WyBiHGbcwj/CpnaWMs5p+YbVo5Hqf2Ew+WEa2N65i0owHBf8x6T+Nwx9VEMRshDESUSTVIfHEc2F" +
//        "5dERmdvRTckrVWgxO6TyGmCKEBh0fsKyh4xiuI8NoFbx49vJv2I4cnyoln94x+Daiw3kEC01npbt" +
//        "migD+ZgFvvXkgp/lpDoHlAgkbzthmvn8oN8N9/p8AJnQ6OeJC7cxkEkoOTnZC/J+M5iqmYEBNvQH" +
//        "UcfUVPK7z4vL9O/VmxSQIL4aMwcFSbEHoRlTLMTOOtz+cxn9jkFrLNf0qd3dzPyPv3l/EDrVoqqw" +
//        "Mgub+kZkN5SH7gsAdCbATQQNYtFJXpo4x6RUnwyXvpReJW0hLPnUwRjMeV09dwl55zi4OR6Plujo" +
//        "/xc5TQb7QTFM1KsDkX7rtVcwlCGJO6Mm7mJ5gn4dqCZmR1uYeO8kn36pdpEK7rDyP9L2UX3hHcjc" +
//        "jdpgCPd1GCvFGNll+faXY6F/dfQPsv8hb9NIj6BVVt8CAwEAAQ==";
    //String publicString="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMNBkmHKAGNp/4SaXZ1dgssKdrfgEvH+MgwFz7d435pcWjoQeuU/FkbBfhh5ILQ57Bfx+SejA0SPZug9esKa6+UCAwEAAQ==";
    String publicString="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA869kB6Y2xwtokIFkblehG4jOiVfI325D" +
            "vbsDbdYTa4QImwJVPJQ9sbMspwJY7EmZZVdTAmrqXO+PmS2ZPNHQ9gEnX2oDQp6GMMsrEIiBTdg6" +
            "mjbJgn63/AbbB1t640EOjR160SiD+iqQA5tKTZvshQYq2owxFH8JSgMhDOI5WqgW4gCuVZok+Dfk" +
            "fbyS1hrmpMfKe6c1pVG8PWhOv7AHdPBZaOSpOqSfBGU0iUXUCO9KFg+Sp72FgqCJXNF1p4s0yGE9" +
            "B5Eid9yNXB27NkmVOwXxhzPXNfXjVHr2tXqzhZdoknNFIOxAGeYvhnihrfpRqvibZ85U67DzkO2E" +
            "VHfhAQIDAQAB";

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

        ContextThemeWrapper contextThemeWrapper=new ContextThemeWrapper(MainActivity.this,null);
        contextThemeWrapper.setTheme(R.style.Theme_AppCompat_Light_Dialog);


        mine1= (ImageView) findViewById(R.id.mine1);
        mine2= (ImageView) findViewById(R.id.mine2);
        mine3= (ImageView) findViewById(R.id.mine3);
        mine4= (ImageView) findViewById(R.id.mine4);

        mine1.setOnClickListener(this);
        mine2.setOnClickListener(this);
        mine3.setOnClickListener(this);
        mine4.setOnClickListener(this);

        //初始化SDK
        pixelSDK=new PixelSDK(MainActivity.this);
        //检测和上传图片的回调
        pixelSDK.setCheckCallBack(new PixelCallBack() {
            @Override
            public void checkResult(int type, PixelResult pixelResult) {
                if (type == PixelSDK.TYPE_CHECK_FACE &&pixelResult.getResultCode() == PixelCode.SUCCESS){
                    Toast.makeText(MainActivity.this, "检测成功，正在上传图片", Toast.LENGTH_SHORT).show();
                    //检测通过，开始上传照片
                    pixelSDK.startUpload();
                } else if (type==PixelSDK.TYPE_UPLOAD_BITMAP&&pixelResult.getResultCode() == PixelCode.SUCCESS){
                    //上传成功，得到检测订单号
                    Toast.makeText(MainActivity.this, "得到的订单是"+pixelResult.getResultMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    //其他的错误  eg:app_key和company不对应，人脸多，没人脸===错误
                    Toast.makeText(MainActivity.this, "错误信息:"+pixelResult.getResultMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                    e.printStackTrace();
                }

                break;
            case R.id.huoqu:
                KeyPair keyPair= RSAUtils.generateRSAKeyPair();
                 privateKey=keyPair.getPrivate();
                 publicKey=keyPair.getPublic();

                try{
                    pri.setText(Base64Utils.encode(privateKey.getEncoded()));
                    Log.i("得到的私用",Base64Utils.encode(privateKey.getEncoded()));
                    pub.setText(Base64Utils.encode(publicKey.getEncoded()));
                    Log.i("得到的公有", Base64Utils.encode(publicKey.getEncoded()));
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
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
