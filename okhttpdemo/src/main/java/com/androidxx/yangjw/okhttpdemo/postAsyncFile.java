package com.androidxx.yangjw.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class postAsyncFile extends AppCompatActivity {

    private Button btnAsyncFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_async_file);
        btnAsyncFile = (Button) findViewById(R.id.btn_async_file);
        btnAsyncFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAsyncFile();
            }
        });
    }

    private void postAsyncFile() {

    }
}
