package com.left.im.ui;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.left.im.R;
import com.left.im.base.ImageLoaderFactory;
import com.left.im.base.ParentWithNaviActivity;
import com.left.im.bean.Hypnotist;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * 催眠大师
 *
 * @author :left
 * @project:HypnotistActivity
 * @date :2017-04-25-18:23
 */
public class HypnotistActivity extends ParentWithNaviActivity {

    String musicAddress;
    Uri musicUri;
    int musicNum = 1;
    int oldmusicNum = 1;
    int musicTotal = 0;
    MediaPlayer mediaPlayer = null;
    @Bind(R.id.musicPhoto)
    ImageView musicPhoto;
    @Bind(R.id.musicName)
    TextView musicName;
    @Bind(R.id.playlast)
    ImageView playlast;
    @Bind(R.id.playstop)
    ImageView play;
    @Bind(R.id.playnext)
    ImageView playnext;
    @Bind(R.id.playtime)
    TextView playtime;
    @Bind(R.id.alltime)
    TextView alltime;
    @Bind(R.id.musicseekbar)
    SeekBar seekBar;
    @Bind(R.id.hypnotist)
    LinearLayout HypnotistLayout;
    boolean isPaused = false, isDestory = false;
    Handler handler;
    Context context;

    @Override
    protected String title() {
        return "催眠大师";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hypnotist);
        initNaviView();
        context = this;
        play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isPaused) {
                    mediaPlayer.start();
                    play.setImageResource(R.mipmap.ic_play);
                    isPaused = false;
                } else {
                    mediaPlayer.pause();
                    play.setImageResource(R.mipmap.ic_pause);
                    isPaused = true;
                }
            }
        });
        playlast.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                oldmusicNum = musicNum;
                musicNum--;
                searchMusic();
            }
        });
        playnext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                oldmusicNum = musicNum;
                musicNum++;
                searchMusic();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                //将media进度设置为当前seekbar的进度
                mediaPlayer.seekTo(arg0.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {

            }
        });
        getCount();
    }

    private void searchMusic() {
        BmobQuery<Hypnotist> query = new BmobQuery<Hypnotist>();
        query.addWhereEqualTo("musicNum", musicNum);
        query.findObjects(this, new FindListener<Hypnotist>() {

            @Override
            public void onSuccess(List<Hypnotist> object) {
                if (object != null && object.size() > 0) {
                    //设置UI元素
                    musicName.setText(object.get(0).getMusicName());
                    ImageLoaderFactory.getLoader().load(musicPhoto, object.get(0).getMusicPhoto(), R.mipmap.music, null);
                    musicAddress = object.get(0).getMusic().getFileUrl(context);
                    musicUri = Uri.parse(musicAddress);
                    playMusic();
                } else
                    //以排除没有音乐之后得点多次才返回的问题
                    musicNum = oldmusicNum;
            }

            @Override
            public void onError(int code, String msg) {
                log(msg);
            }
        });
    }

    //播放相应的音乐
    private void playMusic() {
        stopCurrentMediaPlayer();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(HypnotistActivity.this, musicUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer arg0) {
                //设置UI元素
                Date date = new Date(mediaPlayer.getDuration());
                SimpleDateFormat aFormat = new SimpleDateFormat("mm:ss");
                alltime.setText(aFormat.format(date));
                //将进度条的最大长度设置为媒体文件的播放时间，便于两者的同步
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                handler = new Handler() {
                    public void handleMessage(Message msg) {
                        if (!isDestory) {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            Date date = new Date(mediaPlayer.getCurrentPosition());
                            SimpleDateFormat aFormat = new SimpleDateFormat("mm:ss");
                            playtime.setText(aFormat.format(date));
                        }
                    }
                };//实现消息传递
                DelayThread delaythread = new DelayThread(100);
                delaythread.start();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
                oldmusicNum = musicNum;
                musicNum++;
                searchMusic();
            }
        });
    }

    private void getCount() {
        //查询Hypnotist表中总记录数并返回所有记录信息
        String bql = "select count(*),* from Hypnotist";
        new BmobQuery<Hypnotist>().doSQLQuery(this, bql, new SQLQueryListener<Hypnotist>() {

            @Override
            public void done(BmobQueryResult<Hypnotist> result, BmobException e) {
                if (e == null) {
                    musicTotal = result.getCount();//这里得到符合条件的记录数
                    Random random = new Random();
                    //因为序号从1开始编起，所以要+1
                    musicNum = random.nextInt(musicTotal) + 1;
                    searchMusic();
                } else {
                    log(e.getMessage());
                }
            }
        });
    }

    private void stopCurrentMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        isDestory = true;
        stopCurrentMediaPlayer();
        super.onDestroy();
    }

    private class DelayThread extends Thread {
        int milliseconds;

        public DelayThread(int i) {
            milliseconds = i;
        }

        public void run() {
            while (!isDestory) {
                try {
                    sleep(milliseconds);
                    //设置音乐进度读取频率
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }
    }

}
