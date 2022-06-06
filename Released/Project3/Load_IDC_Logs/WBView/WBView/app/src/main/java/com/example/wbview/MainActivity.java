package com.example.wbview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

//import com.example.wbview.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    WebView webView;
ProgressBar progressBar;
int yPos=0;
    ImageView refresh;
    boolean doubleBackToExitPressedOnce = false;
 String urls="http://124.124.79.122:1020/testIDC/log/";
    int savedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView=findViewById(R.id.webView);
//        progressBar=findViewById(R.id.progressBar);
        refresh=findViewById(R.id.refresh);
        savedLocation=webView.getScrollY();
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,          Animation.RELATIVE_TO_SELF, 0.5f);

       rotate.setDuration(300);
     rotate.setStartOffset(20);
        rotate.setInterpolator(new LinearInterpolator());
//        progressBar.setMax(100);
//        public static final String USER_AGENT =
        webView.getSettings().setJavaScriptEnabled(true);
    webView.setWebViewClient(new Callback());
//       webView.getSettings().setUserAgentString("Chrome/56.0.0 Mobile");

//     webView.loadUrl("https://accounts.google.com/ServiceLogin/signinchooser?service=wise&passive=1209600&continue=https%3A%2F%2Fdrive.google.com%2F%3Ftab%3Dro&followup=https%3A%2F%2Fdrive.google.com%2F%3Ftab%3Dro&emr=1&flowName=GlifWebSignIn&flowEntry=ServiceLogin");

     webView.setWebChromeClient(new WebChromeClient(){
         @Override
         public void onProgressChanged(WebView view, int newProgress) {

             if (newProgress==100){
//                 progressBar.setVisibility(View.INVISIBLE);
                 jumpToY( savedLocation );


             }else {
//                 progressBar.setVisibility(View.VISIBLE);
                 rotate.setRepeatMode(Animation.INFINITE);
                 refresh.startAnimation(rotate);
             }
             super.onProgressChanged(view, newProgress);
         }
     });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        checkDownloadPermission();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.loadUrl(urls);

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setMimeType(mimetype);
                String cookie= CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie",cookie);
                request.addRequestHeader("userAgent",userAgent);
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                request.setDescription("Downloading file...");
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }
            BroadcastReceiver onComplete = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Toast.makeText(getApplicationContext(), "Downloading Complete", Toast.LENGTH_SHORT).show();
                }
            };
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
                refresh.startAnimation(rotate);
            }
        });
        final Handler handler = new Handler();
        Runnable refresh = new Runnable() {
            @Override
            public void run() {

                webView.reload();

                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(refresh, 5000);
    }

    private void jumpToY(int savedLocation) {
        webView.postDelayed( new Runnable () {
            @Override
            public void run() {
                webView.scrollTo(0, savedLocation);
            }
        }, 200);
    }


    private void checkDownloadPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }


    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
            urls=url;

            return super.shouldOverrideUrlLoading(view, url);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
         refresh.animate().cancel();
         webView.pageDown(true);

        }

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    onBackPressed();
                    if (webView.canGoBack()) {
//                        if(webView.getUrl().equals("https://drive.google.com/drive/mobile/my-drive?tab=ro&pli=1")){
//                            alertShow();
//                        }else {
                        webView.goBack();
                        }

                    }
                    return true;


            }


        return super.onKeyDown(keyCode, event);
    }
//@Override
//public boolean onKeyLongPress(int keyCode, KeyEvent event) {
//    if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//        System.out.println("Back button long pressed");
//        alertShow();
//        return true;
//    }
//    return super.onKeyLongPress(keyCode, event);
//}

//    public void onBackPressed() {
//      alertShow();
//    }

    private void alertShow() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)

//set icon

//set title
                .setTitle("Are you sure to Exit")
//set message

//set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        finish();
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                })
//set negative button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
//                        Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                    }
                }).show();
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            alertShow();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 500);
    }

}