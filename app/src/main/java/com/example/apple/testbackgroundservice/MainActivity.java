package com.example.apple.testbackgroundservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // サーバーサービスの起動
        Intent serverIntent = new Intent(this, MyServer.class);
        startService(serverIntent);

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clientIntent = new Intent(getBaseContext(), MyClient.class);
                startService(clientIntent);
            }
        });

    }
}
