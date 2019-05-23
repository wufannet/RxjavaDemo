package com.zaozaofan.rxjavademo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zaozaofan.lib.MyClass;
import com.zaozaofan.lib.RxJava1;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        MyClass.main(null);
//        2019-05-22 16:37:02.882 18408-18408/com.zaozaofan.rxjavademo I/System.out: hello
//        2019-05-22 16:37:02.882 18408-18408/com.zaozaofan.rxjavademo I/System.out: 0
//        2019-05-22 16:37:02.882 18408-18408/com.zaozaofan.rxjavademo I/System.out: Linux
//        2019-05-22 16:37:02.882 18408-18408/com.zaozaofan.rxjavademo I/System.out: http://www.android.com/


    }

    public void onClick(View view) {
        startApp(this,"com.google.android.apps.authenticator2");
    }



    public void startApp(Context context,String packageName){
        if(isAppInstalled(context,packageName)){
            startActivity(getPackageManager().getLaunchIntentForPackage(packageName));
        }else{
            goToMarket(context,packageName);
        }

    }

    /**
     * 检测某个应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }




    /**
     * 去市场下载页面
     */
    public void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
        }
    }


    public void interval(View view) {
        RxJava1.interval();
    }

    public void intervalRange(View view) {
        RxJava1.intervalRange();
    }
}
