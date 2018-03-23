package com.left.im.util;

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

import com.left.im.R;

import java.util.List;


public class LabelView extends View {
    private static final int DIRECTION_LEFT = 0;
    private static final int DIRECTION_RIGHT = 1;
    private static final int DIRECITON_TOP = 2;
    private static final int DIRECTION_BOTTOM = 3;

    private boolean isStatic;
    private int[][] mLocations;
    private int[][] mDirections;
    private int[][] mSpeeds;
    private int[][] mTextWidthAndHeight;
    private String[] mLabels;
    private int[] mFontSizes;
    private int[] mColorSchema = {0XFFFF0000, 0XFF00FF00, 0XFF0000FF, 0XFFCCCCCC, 0XFFFFFFFF};

    private int mTouchSlop;
    private int mDownX = -1;
    private int mDownY = -1;
    private int mDownIndex = -1;
    private Paint mPaint;

    private Thread mThread;

    private OnItemClickListener mListener;
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


    private void run() {
        if (mThread != null && mThread.isAlive()) {
            return;
        }

        mThread = new Thread(mStartRunning);
        mThread.start();
    }


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


    private boolean hasContents() {
        return mLabels != null && mLabels.length > 0;
    }


    public void setLabels(List<String> labels) {
        setLabels((String[]) labels.toArray());
    }


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


    public void setColorSchema(int[] colorSchema) {
        mColorSchema = colorSchema;
    }


    public void setSpeeds(int[][] speeds) {
        mSpeeds = speeds;
    }


    public void setOnItemClickListener(OnItemClickListener l) {
        getParent().requestDisallowInterceptTouchEvent(true);
        mListener = l;
    }

    public interface OnItemClickListener {
        public void onItemClick(int index, String label);
    }
}
