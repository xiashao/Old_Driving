package car.ccut.com.vehicle.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import car.ccut.com.vehicle.MyApplication;
import car.ccut.com.vehicle.R;
import car.ccut.com.vehicle.service.MusicService;
import car.ccut.com.vehicle.service.VoiceRecognition;
import car.ccut.com.vehicle.util.MusicUtils;

public class DrivingActivity extends Activity implements OnClickListener {
    private Button mFrontImageButton, mPauseImageButton, mNextImageButton;
    private ImageButton voice,call;
    private TextView tv_songName, tv_singerName;
    private SeekBar seekBar1;// 播放进度条
    private MusicService mService;

    final int RIGHT = 0;
    final int LEFT = 1;
    private boolean macControl=true;
    private GestureDetector gestureDetector;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qqtest);
        MyApplication application = (MyApplication) getApplication();
        mService = application.getmService();
        mService.setCurrentListItme(0);
        mService.playMusic(MusicUtils.getAllSongs(this).get(0).getUrl());
        initView();
        setListener();
        gestureDetector = new GestureDetector(DrivingActivity.this,onGestureListener);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.System.putInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 1);
//得到是否开启
                int flag = Settings.System.getInt(getContentResolver(),
                        Settings.System.ACCELEROMETER_ROTATION, 0);
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:")));
            }
        });
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mac();
            }
        });

    }
    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();

                    if (x > 0) {
                        doResult(RIGHT);
                    } else if (x < 0) {
                        doResult(LEFT);
                    }
                    return true;
                }
            };
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void doResult(int action) {

        switch (action) {
            case RIGHT:
                mService.nextMusic();
                break;

            case LEFT:
                mService.frontMusic();
                break;

        }
    }
    private void initView() {
        mFrontImageButton = (Button) findViewById(R.id.LastImageButton);
        mPauseImageButton = (Button) findViewById(R.id.PauseImageButton);
        mNextImageButton = (Button) findViewById(R.id.NextImageButton);
        tv_songName = (TextView) findViewById(R.id.tv_songName);
        tv_singerName = (TextView) findViewById(R.id.tv_singerName);
        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        call=(ImageButton)findViewById(R.id.call);
        voice=(ImageButton)findViewById(R.id.voice);
        // 启动
        handler.post(updateThread);
    }

    Handler handler = new Handler();
    Runnable updateThread = new Runnable() {
        public void run() {
            // 获得歌曲的长度并设置成播放进度条的最大值
            seekBar1.setMax(mService.getDuration());
            // 获得歌曲现在播放位置并设置成播放进度条的值
            seekBar1.setProgress(mService.getCurrent());

            tv_songName.setText(mService.getSongName());
            tv_singerName.setText(mService.getSingerName());
            // 每次延迟100毫秒再启动线程
            handler.postDelayed(updateThread, 100);
        }
    };

    private void setListener() {
        // 暂停or开始
        mPauseImageButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                mService.pausePlay();
                if (mService.isPlay()) {
                    mPauseImageButton.setBackgroundResource(R.drawable.music_pause_bg);
                } else {
                    mPauseImageButton.setBackgroundResource(R.drawable.music_play_bg);
                }
            }
        });

        // 下一首
        mNextImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mService.nextMusic();
            }
        });
        // 上一首
        mFrontImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mService.frontMusic();
            }
        });
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // fromUser判断是用户改变的滑块的值
                if (fromUser == true) {
                    mService.movePlay(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
    public void setTitle (String title) {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);
    }
    public void mac() {
        Intent intent = new Intent(this, VoiceRecognition.class);
        if (mService.isPlay()) {
            mService.pausePlay();
            mPauseImageButton.setBackgroundResource(R.drawable.music_pause_bg);
        } else {
            mPauseImageButton.setBackgroundResource(R.drawable.music_play_bg);
        }
        if (macControl) {
            startService(intent);
            System.out.println("service已启动");

        } else {
            stopService(intent);
        }
    }
    /**
     * 格式化时间，将其变成00:00的形式
     */
    public String formatTime(int time) {
        int secondSum = time / 1000;
        int minute = secondSum / 60;
        int second = secondSum % 60;

        String result = "";
        if (minute < 10)
            result = "0";
        result = result + minute + ":";
        if (second < 10)
            result = result + "0";
        result = result + second;
        return result;
    }
    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }
}
