package com.indoor.more.view;

import java.util.List;
import com.indoor.im.R;
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
/**
 * android标签�?  by loader
 * @author 亓斌 (qibin0506@gmail.com)
 * <p>
 * 
 * 使用方法�?<p>
	 在xml中配置：<p>
	 &lt;org.loader.labelview.LabelView<p>
	        xmlns:label="http://schemas.android.com/apk/res/org.loader.labelview"<p>
	        android:layout_marginTop="20dp"<p>
	        android:id="@+id/lv"<p>
	        android:layout_below="@id/et_input"<p>
	        android:layout_width="wrap_content"<p>
	        android:layout_height="wrap_content"<p>
	        label:is_static="false"<p>
	        android:background="@android:color/white"/><p>
	       <p>
	 setLabels设置标签�?<p>
	 setColorSchema设置配色方案�? <p>
	 setSpeeds设置每一个的x/y速度�?<p>
	 setOnItemClick可以监听item的click事件<p>
	 使用时，别忘了copy values目录下的attrs.xml到你的项目中<p>
	 可参考demo中的MainActivity.java文件<p>
 */

public class LabelView extends View {
	private static final int DIRECTION_LEFT = 0; // 向左
	private static final int DIRECTION_RIGHT = 1; // 向右
	private static final int DIRECITON_TOP = 2; // 向上
	private static final int DIRECTION_BOTTOM = 3; // 向下
	
	private boolean isStatic; // 是否静止�? 默认false�? 可用干xml �? label:is_static="false"
	
	private int[][] mLocations; // 每个label的位�? x/y
	private int[][] mDirections; // 每个label的方�? x/y
	private int[][] mSpeeds; // 每个label的x/y速度 x/y
	private int[][] mTextWidthAndHeight; // 每个labeltext的大�? width/height
	
	private String[] mLabels; // 设置的labels
	private int[] mFontSizes; // 每个label的字体大�?
	// 默认配色方案
	private int[] mColorSchema = {0XFFFF0000, 0XFF00FF00, 0XFF0000FF, 0XFFCCCCCC, 0XFFFFFFFF};
	
	private int mTouchSlop; // �?小touch
	private int mDownX = -1;
	private int mDownY = -1;
	private int mDownIndex = -1; // 点击的index
	
	private Paint mPaint;
	
	private Thread mThread;
	
	private OnItemClickListener mListener; // item点击事件
	
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
		if(!hasContents()) {
			return;
		}
		
		for (int i = 0; i < mLabels.length; i++) {
			mPaint.setTextSize(mFontSizes[i]);
			
			if(i < mColorSchema.length) mPaint.setColor(mColorSchema[i]);
			else mPaint.setColor(mColorSchema[i-mColorSchema.length]);
			
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
	 * 获取当前点击的label的位�?
	 * @return label的位置，没有点中返回-1
	 */
	private int getClickIndex() {
		Rect downRect = new Rect();
		Rect locationRect = new Rect();
		for(int i=0;i<mLocations.length;i++) {
			downRect.set(mDownX - mTextWidthAndHeight[i][0], mDownY
					- mTextWidthAndHeight[i][1], mDownX
					+ mTextWidthAndHeight[i][0], mDownY
					+ mTextWidthAndHeight[i][1]);
			
			locationRect.set(mLocations[i][0], mLocations[i][1],
					mLocations[i][0] + mTextWidthAndHeight[i][0],
					mLocations[i][1] + mTextWidthAndHeight[i][1]);
			
			if(locationRect.intersect(downRect)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * �?启子线程不断刷新位置并postInvalidate
	 */
	private void run() {
		if(mThread != null && mThread.isAlive()) {
			return;
		}
		
		mThread = new Thread(mStartRunning);
		mThread.start();
	}
	
	private Runnable mStartRunning = new Runnable() {
		@Override
		public void run() {
			for(;;) {
				SystemClock.sleep(100);
				
				for (int i = 0; i < mLabels.length; i++) {
					if (mLocations[i][0] <= getPaddingLeft()) {
						mDirections[i][0] = DIRECTION_RIGHT;
					}
					
					if (mLocations[i][0] >= getMeasuredWidth()
							- getPaddingRight() - mTextWidthAndHeight[i][0]) {
						mDirections[i][0] = DIRECTION_LEFT;
					}
					
					if(mLocations[i][1] <= getPaddingTop() + mTextWidthAndHeight[i][1]) {
						mDirections[i][1] = DIRECTION_BOTTOM;
					}
					
					if (mLocations[i][1] >= getMeasuredHeight() - getPaddingBottom()) {
						mDirections[i][1] = DIRECITON_TOP;
					}
					
					int xSpeed = 1;
					int ySpeed = 2;
					
					if(i < mSpeeds.length) {
						xSpeed = mSpeeds[i][0];
						ySpeed = mSpeeds[i][1];
					}
					else { 
						xSpeed = mSpeeds[i-mSpeeds.length][0];
						ySpeed = mSpeeds[i-mSpeeds.length][1];
					}
					
					mLocations[i][0] += mDirections[i][0] == DIRECTION_RIGHT ? xSpeed : -xSpeed;
					mLocations[i][1] += mDirections[i][1] == DIRECTION_BOTTOM ? ySpeed : -ySpeed;
				}
				
				postInvalidate();
			}
		}
	};
	
	/**
	 * 初始化位置�?�方向�?�label宽高
	 * 并开启线�?
	 */
	private void init() {
		if(!hasContents()) {
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
		
		if(!isStatic) run();
	}
	
	/**
	 * 是否设置label
	 * @return true or false
	 */
	private boolean hasContents() {
		return mLabels != null && mLabels.length > 0;
	}

	/**
	 * 设置labels
	 * @see setLabels(String[] labels)
	 * @param labels
	 */
	public void setLabels(List<String> labels) {
		setLabels((String[]) labels.toArray());
	}
	
	/**
	 * 设置labels
	 * @param labels
	 */
	public void setLabels(String[] labels) {
		mLabels = labels;
		mLocations = new int[labels.length][2];
		mFontSizes = new int[labels.length];
		mDirections = new int[labels.length][2];
		mTextWidthAndHeight = new int[labels.length][2];
		
		mSpeeds = new int[labels.length][2];
		for(int speed[] : mSpeeds) {
			speed[0] = speed[1] = 1;
		}
		
		requestLayout();
	}
	
	/**
	 * 设置配色方案
	 * @param colorSchema
	 */
	public void setColorSchema(int[] colorSchema) {
		mColorSchema = colorSchema;
	}
	
	/**
	 * 设置每个item的x/y速度
	 * <p>
	 * speeds.length > labels.length 忽略多余�?
	 * <p>
	 * speeds.length < labels.length 将重复使�?
	 * 
	 * @param speeds
	 */
	public void setSpeeds(int[][] speeds) {
		mSpeeds = speeds;
	}
	
	/**
	 * 设置item点击的监听事�?
	 * @param l
	 */
	public void setOnItemClickListener(OnItemClickListener l) {
		getParent().requestDisallowInterceptTouchEvent(true);
		mListener = l;
	}
	
	/**
	 * item的点击监听事�?
	 */
	public interface OnItemClickListener {
		public void onItemClick(int index, String label);
	}
}
