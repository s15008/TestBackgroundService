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
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by apple on 2017/01/27.
 */

public class MyServer extends Service {
    private static final String TAG = "MyServer";

    private Handler mServiceHandler;

    @Override
    public void onCreate() {
        // HandlerThreadの生成と実行
        HandlerThread serviceThread = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
        serviceThread.start();
        // Handlerの生成
        mServiceHandler = new Handler(serviceThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // ハンドラーに処理をポストする
        mServiceHandler.post(new Runnable() {
            @Override
            public void run() {
                final int LISTEN_PORT = 9000;
                final char END_MARK = '.';

                try {
                    ServerSocket serverSocket = new ServerSocket(LISTEN_PORT);
                    Log.d(TAG, "サーバー動いてます");
                    Socket socket = serverSocket.accept();
                    Reader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Writer out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    StringBuilder sb = new StringBuilder(4096);
                    int c;
                    while ((c = in.read()) != -1) {
                        if (c == END_MARK) {
                            break;
                        }
                        sb.append((char) c);
                    }

                    out.write(sb.toString().toUpperCase());
                    out.flush();

                    in.close();
                    out.close();
                    socket.close();
                    serverSocket.close();
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
