/*
 *
 * Copyright (c) 2010-2014 EVE GROUP PTE. LTD.
 *
 */


package control.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.rtspplayer.sample.R;

import java.nio.ByteBuffer;

import veg.mediaplayer.sdk.MediaPlayer;
import veg.mediaplayer.sdk.MediaPlayer.PlayerNotifyCodes;
import veg.mediaplayer.sdk.MediaPlayer.PlayerState;
import veg.mediaplayer.sdk.MediaPlayerConfig;

public class PLayerActivity extends FragmentActivity implements OnClickListener, MediaPlayer.MediaPlayerCallback {
    private static final String TAG = "MediaPlayerTest";
    private Button btnConnect;

    private boolean playing = false;
    private MediaPlayer player = null;

    // callback from Native Player
    @Override
    public int OnReceiveData(ByteBuffer buffer, int size, long pts) {
        Log.e(TAG, "Form Native Player OnReceiveData: size: " + size + ", pts: " + pts);
        return 0;
    }


    // All event are sent to event handlers
    @Override
    public int Status(int arg) {

        PlayerNotifyCodes status = PlayerNotifyCodes.forValue(arg);

        Log.e(TAG, "Form Native Player status: " + arg);
        switch (PlayerNotifyCodes.forValue(arg)) {
            default:
                Message msg = new Message();
                msg.obj = status;
        }

        return 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String strUrl;

        setTitle(R.string.app_name);
        super.onCreate(savedInstanceState);

        getActionBar().hide();

        setContentView(R.layout.activity_player);
        SharedSettings.getInstance(this).loadPrefSettings();
        SharedSettings.getInstance().savePrefSettings();

//        playerHwStatus = (TextView) findViewById(R.id.playerHwStatus);

        player = (MediaPlayer) findViewById(R.id.playerView);

        player.getSurfaceView().setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = player.getSurfaceView().getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

        player.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN: {
                        if (player.getState() == PlayerState.Paused)
                            player.Play();
                        else if (player.getState() == PlayerState.Started)
                            player.Pause();
                    }
                }

                return true;
            }
        });

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btnConnect = (Button) findViewById(R.id.button_connect);
        btnConnect.setOnClickListener(this);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.main_view);
        layout.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getWindow() != null && getWindow().getCurrentFocus() != null && getWindow().getCurrentFocus().getWindowToken() != null)
                    inputManager.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

//		playerStatusText.setText("DEMO VERSION");
        setShowControls();
    }

    public void onClick(View v) {
        if (player != null) {
            player.getConfig().setConnectionUrl("rtsp://admin:123456@mamcafe.quickddns.com:554/cam/realmonitor?channel=1&subtype=0");
            if (player.getConfig().getConnectionUrl().isEmpty())
                return;

            player.Close();
            if (playing) {
                setUIDisconnected();
            } else {
                SharedSettings sett = SharedSettings.getInstance();
                boolean bPort = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
                int aspect = bPort ? 1 : sett.rendererEnableAspectRatio;

                MediaPlayerConfig conf = new MediaPlayerConfig();

                player.setVisibility(View.INVISIBLE);

                conf.setConnectionUrl(player.getConfig().getConnectionUrl());

                conf.setConnectionNetworkProtocol(sett.connectionProtocol);
                conf.setConnectionDetectionTime(sett.connectionDetectionTime);
                conf.setConnectionBufferingTime(sett.connectionBufferingTime);
                conf.setDecodingType(sett.decoderType);
                conf.setRendererType(sett.rendererType);
                conf.setSynchroEnable(sett.synchroEnable);
                conf.setSynchroNeedDropVideoFrames(sett.synchroNeedDropVideoFrames);
                conf.setEnableColorVideo(sett.rendererEnableColorVideo);
                conf.setEnableAspectRatio(aspect);
                conf.setDataReceiveTimeout(30000);
                conf.setNumberOfCPUCores(0);

                conf.setRecordPath("");
                conf.setRecordFlags(0);
                conf.setRecordSplitTime(0);
                conf.setRecordSplitSize(0);

                player.Open(conf, this);
                btnConnect.setText("Disconnect");

                playing = true;
            }
        }
    }

    protected void onPause() {
        super.onPause();
        if (player != null)
            player.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null)
            player.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (player != null)
            player.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null)
            player.onStop();
    }

    @Override
    public void onBackPressed() {
        player.Close();
        if (!playing) {
            super.onBackPressed();
            return;
        }

        setUIDisconnected();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (player != null)
            player.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (player != null)
            player.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        if (player != null)
            player.onDestroy();

        stopProgressTask();
        System.gc();

        super.onDestroy();
    }

    protected void setUIDisconnected() {
        setTitle(R.string.app_name);
        btnConnect.setText("Connect");
        playing = false;
    }

    protected void setHideControls() {
        btnConnect.setVisibility(View.GONE);
    }

    protected void setShowControls() {
        setTitle(R.string.app_name);

        btnConnect.setVisibility(View.VISIBLE);
    }

    private void showStatusView() {
        player.setVisibility(View.INVISIBLE);
//        playerHwStatus.setVisibility(View.INVISIBLE);

    }

    private void showVideoView() {
        player.setVisibility(View.VISIBLE);
//        playerHwStatus.setVisibility(View.VISIBLE);

        SurfaceHolder sfhTrackHolder = player.getSurfaceView().getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

        setTitle("");
    }

    private void startProgressTask(String text) {
        stopProgressTask();
    }

    private void stopProgressTask() {
        setTitle(R.string.app_name);
    }

    private class StatusProgressTask extends AsyncTask<String, Void, Boolean> {
        String strProgressTextSrc;
        String strProgressText;
        boolean stop = false;

        public StatusProgressTask(String text) {
            stop = false;
            strProgressTextSrc = text;
        }

        public void stopTask() {
            stop = true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                if (stop) return true;

                strProgressText = strProgressTextSrc + "...";

                Runnable uiRunnable = null;
                uiRunnable = new Runnable() {
                    public void run() {
                        if (stop) return;

                        synchronized (this) {
                            this.notify();
                        }
                    }
                };

                int nCount = 4;
                do {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        stop = true;
                    }

                    if (stop) break;

                    if (nCount <= 3) {
                        strProgressText = strProgressTextSrc;
                        for (int i = 0; i < nCount; i++)
                            strProgressText = strProgressText + ".";
                    }

                    synchronized (uiRunnable) {
                        runOnUiThread(uiRunnable);
                        try {
                            uiRunnable.wait();
                        } catch (InterruptedException e) {
                            stop = true;
                        }
                    }

                    if (stop) break;

                    nCount++;
                    if (nCount > 3) {
                        nCount = 1;
                        strProgressText = strProgressTextSrc;
                    }
                }

                while (!isCancelled());
            } catch (Exception e) {
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    static public <T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T... params) {
        {
            task.execute(params);
        }
    }
}
