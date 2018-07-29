package com.rbk.imra35;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity
{
    private int progressStatus = 0;

    private ProgressBar progressBar;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        if(!isConnected(SplashActivity.this)) buildDialog(SplashActivity.this).show();
        else
        {
            //Toast.makeText(SplashActivity.this,"Welcome", Toast.LENGTH_SHORT).show();
            progressBar=(ProgressBar) findViewById(R.id.progressBar2);


            Thread splash=new Thread()
            {
                @Override
                public void run()
                {


                    while (progressStatus<=100)
                    {
                        progressStatus=progressStatus+1;

                        try
                        {
                            sleep(20);

                        }
                        catch (Exception e)
                        {

                        }


                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                progressBar.setProgress(progressStatus);

                            }
                        });

                    }


                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();



                }

            };
            splash.start();

        }



    }


    public boolean isConnected(Context context)
    {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting())
        {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;

        }
        else
        {
            return false;
        }

        return false;
    }

    public AlertDialog.Builder buildDialog(Context c)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }
}
