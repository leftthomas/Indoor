package com.indoor.more.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.indoor.im.R;

import java.util.List;

/**
 * android鏍囩浜?  by loader
 *
 * @author 浜撴枌 (qibin0506@gmail.com)
 *         <p>
 *         <p>
 *         浣跨敤鏂规硶锛?<p>
 *         鍦▁ml涓厤缃細<p>
 *         &lt;org.loader.labelview.LabelView<p>
 *         xmlns:label="http://schemas.android.com/apk/res/org.loader.labelview"<p>
 *         android:layout_marginTop="20dp"<p>
 *         android:id="@+id/lv"<p>
 *         android:layout_below="@id/et_input"<p>
 *         android:layout_width="wrap_content"<p>
 *         android:layout_height="wrap_content"<p>
 *         label:is_static="false"<p>
 *         android:background="@android:color/white"/><p>
 *         <p>
 *         setLabels璁剧疆鏍囩锛?<p>
 *         setColorSchema璁剧疆閰嶈壊鏂规锛? <p>
 *         setSpeeds璁剧疆姣忎竴涓殑x/y閫熷害锛?<p>
 *         setOnItemClick鍙互鐩戝惉item鐨刢lick浜嬩欢<p>
 *         浣跨敤鏃讹紝鍒繕浜哻opy values鐩綍涓嬬殑attrs.xml鍒颁綘鐨勯」鐩腑<p>
 *         鍙弬鑰僤emo涓殑MainActivity.java鏂囦欢<p>
 */

public class LabelView extends View {
    private static final int DIRECTION_LEFT = 0; // 鍚戝乏
    private static final int DIRECTION_RIGHT = 1; // 鍚戝彸
    private static final int DIRECITON_TOP = 2; // 鍚戜笂
    private static final int DIRECTION_BOTTOM = 3; // 鍚戜笅

    private boolean isStatic; // 鏄惁闈欐锛? 榛樿false锛? 鍙敤骞瞲ml 锛? label:is_static="false"

    private int[][] mLocations; // 姣忎釜label鐨勪綅缃? x/y
    private int[][] mDirections; // 姣忎釜label鐨勬柟鍚? x/y
    private int[][] mSpeeds; // 姣忎釜label鐨剎/y閫熷害 x/y
    private int[][] mTextWidthAndHeight; // 姣忎釜labeltext鐨勫ぇ灏? width/height

    private String[] mLabels; // 璁剧疆鐨刲abels
    private int[] mFontSizes; // 姣忎釜label鐨勫瓧浣撳ぇ灏?
    // 榛樿閰嶈壊鏂规
    private int[] mColorSchema = {0XFFFF0000, 0XFF00FF00, 0XFF0000FF, 0XFFCCCCCC, 0XFFFFFFFF};

    private int mTouchSlop; // 鏈?灏弔ouch
    private int mDownX = -1;
    private int mDownY = -1;
    private int mDownIndex = -1; // 鐐瑰嚮鐨刬ndex

    private Paint mPaint;

    private Thread mThread;

    private OnItemClickListener mListener; // item鐐瑰嚮浜嬩欢
    private Runnable mStartRunning = new Runnable() {
        @Override
        public void run() {
            for (; ; ) {
                SystemClock.sleep(100);

                for (int i = 0; i < mLabels.length; i++) {
                    if (mLocations[i][0] <= getPaddingLeft()) {
                        mDirections[i][0] = DIRECTION_RIGHT;
                    }

                    if (mLocations[i][0] >= getMeasuredWidth()
                            - getPaddingRight() - mTextWidthAndHeight[i][0]) {
                        mDirections[i][0] = DIRECTION_LEFT;
                    }

                    if (mLocations[i][1] <= getPaddingTop() + mTextWidthAndHeight[i][1]) {
                        mDirections[i][1] = DIRECTION_BOTTOM;
                    }

                    if (mLocations[i][1] >= getMeasuredHeight() - getPaddingBottom()) {
                        mDirections[i][1] = DIRECITON_TOP;
                    }

                    int xSpeed = 1;
                    int ySpeed = 2;

                    if (i < mSpeeds.length) {
                        xSpeed = mSpeeds[i][0];
                        ySpeed = mSpeeds[i][1];
                    } else {
                        xSpeed = mSpeeds[i - mSpeeds.length][0];
                        ySpeed = mSpeeds[i - mSpeeds.length][1];
                    }

                    mLocations[i][0] += mDirections[i][0] == DIRECTION_RIGHT ? xSpeed : -xSpeed;
                    mLocations[i][1] += mDirections[i][1] == DIRECTION_BOTTOM ? ySpeed : -ySpeed;
                }

                postInvalidate();
            }
        }
    };

    public LabelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LabelView, defStyleAttr, 0);
        isStatic = ta.getBoolean(R.styleable.LabelView_is_static, false);
        ta.recycle();

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!hasContents()) {
            return;
        }

        for (int i = 0; i < mLabels.length; i++) {
            mPaint.setTextSize(mFontSizes[i]);

            if (i < mColorSchema.length) mPaint.setColor(mColorSchema[i]);
            else mPaint.setColor(mColorSchema[i - mColorSchema.length]);

            canvas.drawText(mLabels[i], mLocations[i][0], mLocations[i][1], mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                mDownIndex = getClickIndex();
                break;
            case MotionEvent.ACTION_UP:
                int nowX = (int) ev.getX();
                int nowY = (int) ev.getY();
                if (nowX - mDownX < mTouchSlop && nowY - mDownY < mTouchSlop
                        && mDownIndex != -1 && mListener != null) {
                    mListener.onItemClick(mDownIndex, mLabels[mDownIndex]);
                }

                mDownX = mDownY = mDownIndex = -1;
                break;
        }

        return true;
    }

    /**
     * 鑾峰彇褰撳墠鐐瑰嚮鐨刲abel鐨勪綅缃?
     *
     * @return label鐨勪綅缃紝娌℃湁鐐逛腑杩斿洖-1
     */
    private int getClickIndex() {
        Rect downRect = new Rect();
        Rect locationRect = new Rect();
        for (int i = 0; i < mLocations.length; i++) {
            downRect.set(mDownX - mTextWidthAndHeight[i][0], mDownY
                    - mTextWidthAndHeight[i][1], mDownX
                    + mTextWidthAndHeight[i][0], mDownY
                    + mTextWidthAndHeight[i][1]);

            locationRect.set(mLocations[i][0], mLocations[i][1],
                    mLocations[i][0] + mTextWidthAndHeight[i][0],
                    mLocations[i][1] + mTextWidthAndHeight[i][1]);

            if (locationRect.intersect(downRect)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 寮?鍚瓙绾跨▼涓嶆柇鍒锋柊浣嶇疆骞秔ostInvalidate
     */
    private void run() {
        if (mThread != null && mThread.isAlive()) {
            return;
        }

        mThread = new Thread(mStartRunning);
        mThread.start();
    }

    /**
     * 鍒濆鍖栦綅缃?佹柟鍚戙?乴abel瀹介珮
     * 骞跺紑鍚嚎绋?
     */
    private void init() {
        if (!hasContents()) {
            return;
        }

        int minX = getPaddingLeft();
        int minY = getPaddingTop();
        int maxX = getMeasuredWidth() - getPaddingRight();
        int maxY = getMeasuredHeight() - getPaddingBottom();

        Rect textBounds = new Rect();

        for (int i = 0; i < mLabels.length; i++) {
            int[] location = new int[2];
            location[0] = minX + (int) (Math.random() * maxX);
            location[1] = minY + (int) (Math.random() * maxY);

            mLocations[i] = location;
            mFontSizes[i] = 15 + (int) (Math.random() * 30);
            mDirections[i][0] = Math.random() > 0.5 ? DIRECTION_RIGHT : DIRECTION_LEFT;
            mDirections[i][1] = Math.random() > 0.5 ? DIRECTION_BOTTOM : DIRECITON_TOP;

            mPaint.setTextSize(mFontSizes[i]);
            mPaint.getTextBounds(mLabels[i], 0, mLabels[i].length(), textBounds);
            mTextWidthAndHeight[i][0] = textBounds.width();
            mTextWidthAndHeight[i][1] = textBounds.height();
        }

        if (!isStatic) run();
    }

    /**
     * 鏄惁璁剧疆label
     *
     * @return true or false
     */
    private boolean hasContents() {
        return mLabels != null && mLabels.length > 0;
    }

    /**
     * 璁剧疆labels
     *
     * @param labels
     * @see setLabels(String[] labels)
     */
    public void setLabels(List<String> labels) {
        setLabels((String[]) labels.toArray());
    }

    /**
     * 璁剧疆labels
     *
     * @param labels
     */
    public void setLabels(String[] labels) {
        mLabels = labels;
        mLocations = new int[labels.length][2];
        mFontSizes = new int[labels.length];
        mDirections = new int[labels.length][2];
        mTextWidthAndHeight = new int[labels.length][2];

        mSpeeds = new int[labels.length][2];
        for (int speed[] : mSpeeds) {
            speed[0] = speed[1] = 1;
        }

        requestLayout();
    }

    /**
     * 璁剧疆閰嶈壊鏂规
     *
     * @param colorSchema
     */
    public void setColorSchema(int[] colorSchema) {
        mColorSchema = colorSchema;
    }

    /**
     * 璁剧疆姣忎釜item鐨剎/y閫熷害
     * <p>
     * speeds.length > labels.length 蹇界暐澶氫綑鐨?
     * <p>
     * speeds.length < labels.length 灏嗛噸澶嶄娇鐢?
     *
     * @param speeds
     */
    public void setSpeeds(int[][] speeds) {
        mSpeeds = speeds;
    }

    /**
     * 璁剧疆item鐐瑰嚮鐨勭洃鍚簨浠?
     *
     * @param l
     */
    public void setOnItemClickListener(OnItemClickListener l) {
        getParent().requestDisallowInterceptTouchEvent(true);
        mListener = l;
    }

    /**
     * item鐨勭偣鍑荤洃鍚簨浠?
     */
    public interface OnItemClickListener {
        public void onItemClick(int index, String label);
    }
}
