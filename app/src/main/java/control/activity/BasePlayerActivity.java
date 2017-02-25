package control.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.rtspplayer.sample.R;

import java.nio.ByteBuffer;

import veg.mediaplayer.sdk.MediaPlayer;
import veg.mediaplayer.sdk.MediaPlayerConfig;

/**
 * Created by doba on 2/25/17.
 */

public class BasePlayerActivity extends FragmentActivity implements MediaPlayer.MediaPlayerCallback {

    private static final String TAG = "BasePlayerActivity";

    private boolean playing = false;
    protected MediaPlayer player = null;

    protected void preparePlayer(){
        player.getSurfaceView().setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = player.getSurfaceView().getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

        player.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN: {
                        if (player.getState() == MediaPlayer.PlayerState.Paused)
                            player.Play();
                        else if (player.getState() == MediaPlayer.PlayerState.Started)
                            player.Pause();
                    }
                }

                return true;
            }
        });

//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        RelativeLayout layout = (RelativeLayout) findViewById(R.id.main_view);
//        layout.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (getWindow() != null && getWindow().getCurrentFocus() != null && getWindow().getCurrentFocus().getWindowToken() != null)
//                    inputManager.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
//                return true;
//            }
//        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();

        SharedSettings.getInstance(this).loadPrefSettings();
        SharedSettings.getInstance().savePrefSettings();

    }

    protected void setUIDisconnected() {
        playing = false;
    }

    public void onStartStream() {
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
//                btnConnect.setText("Disconnect");

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

//        stopProgressTask();
        System.gc();

        super.onDestroy();
    }

    @Override
    public int Status(int arg) {

        MediaPlayer.PlayerNotifyCodes status = MediaPlayer.PlayerNotifyCodes.forValue(arg);
//        if (handler == null || status == null)
//            return 0;

        Log.e(TAG, "Form Native Player status: " + arg);
        switch (MediaPlayer.PlayerNotifyCodes.forValue(arg)) {
            default:
                Message msg = new Message();
                msg.obj = status;
//                handler.removeMessages(mOldMsg);
//                mOldMsg = msg.what;
//                handler.sendMessage(msg);
        }

        return 0;
    }

    @Override
    public int OnReceiveData(ByteBuffer byteBuffer, int i, long l) {
        return 0;
    }
}
