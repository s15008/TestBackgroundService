package com.example.apple.testbackgroundservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

/**
 * Created by apple on 2017/01/27.
 */

public class MyService extends Service {
    private static final String TAG = "MyService";

    private Handler mServiceHandler;
    private static final String[] messages = {"{\"name\":\"gorilla\"}", "{\"name\":\"hippopotamus\"}"};

    @Override
    public void onCreate() {
        // HandlerThreadの生成と実行
        HandlerThread serviceThread = new HandlerThread("MyService", Process.THREAD_PRIORITY_BACKGROUND);
        serviceThread.start();

        // Handlerの生成
        mServiceHandler = new Handler(serviceThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mServiceHandler.post(new Runnable() {
            @Override
            public void run() {
                final String SERVER_HOST = "localhost";
                final int SERVER_PORT = 9000;

                try {
                    Socket socket = new Socket(SERVER_HOST, SERVER_PORT);

                    Reader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Writer out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    for (String message : messages) {
                        out.write(message);
                    }
                    out.write('.');
                    out.flush();

                    char[] buf = new char[4096];
                    while (in.read(buf) != -1) {
                        Log.e(TAG, new String(buf));
                    }

                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });

        return START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
