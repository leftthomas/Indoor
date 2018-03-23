package com.left.indoor.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ViewSwitcher.ViewFactory;

import com.left.indoor.bean.Configure;
import com.left.indoor.bean.IndoorUser;
import com.left.indoor.map.R;
import com.left.indoor.myview.ImageAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

@SuppressWarnings("deprecation")
public class WallpapersettingActivity extends Activity 
implements OnItemSelectedListener,ViewFactory{
	private ImageView back;
	private IndoorUser cuser;
	private Configure cu_Configure;
    //准备数据源
    private int[]res ={R.drawable.wallpaper1,R.drawable.wallpaper2,
			R.drawable.wallpaper3,R.drawable.wallpaper4,R.drawable.wallpaper5,
			R.drawable.wallpaper6,R.drawable.wallpaper7,R.drawable.wallpaper8,
			R.drawable.wallpaper9,R.drawable.wallpaper10,R.drawable.wallpaper11,
			R.drawable.wallpaper12,R.drawable.wallpaper13,R.drawable.wallpaper14,
			R.drawable.wallpaper15,R.drawable.wallpaper16,R.drawable.wallpaper17,
			R.drawable.wallpaper18,R.drawable.wallpaper19,R.drawable.wallpaper20,
			R.drawable.wallpaper21,R.drawable.wallpaper22,R.drawable.wallpaper23,
			R.drawable.wallpaper24,R.drawable.wallpaper25,R.drawable.wallpaper26,
			R.drawable.wallpaper27,R.drawable.wallpaper28,R.drawable.wallpaper29,
			R.drawable.wallpaper30,R.drawable.wallpaper31,R.drawable.wallpaper32,
			R.drawable.wallpaper33,R.drawable.wallpaper34,R.drawable.wallpaper35,
			R.drawable.wallpaper36,R.drawable.wallpaper37,R.drawable.wallpaper38,
			R.drawable.wallpaper39,R.drawable.wallpaper40,R.drawable.wallpaper41,
			R.drawable.wallpaper42,R.drawable.wallpaper43,R.drawable.wallpaper44,
			R.drawable.wallpaper45,R.drawable.wallpaper46,R.drawable.wallpaper47,
			R.drawable.wallpaper48,R.drawable.wallpaper49,R.drawable.wallpaper50,
			R.drawable.wallpaper51,R.drawable.wallpaper52,R.drawable.wallpaper53,
			R.drawable.wallpaper54,R.drawable.wallpaper55,R.drawable.wallpaper56,
			R.drawable.wallpaper57,R.drawable.wallpaper58,R.drawable.wallpaper59,
			R.drawable.wallpaper60,R.drawable.wallpaper61,R.drawable.wallpaper62,
			R.drawable.wallpaper63,R.drawable.wallpaper64,R.drawable.wallpaper65,
			R.drawable.wallpaper66,R.drawable.wallpaper67,R.drawable.wallpaper68,
			R.drawable.wallpaper69,R.drawable.wallpaper70,R.drawable.wallpaper71,
			R.drawable.wallpaper72,R.drawable.wallpaper73,R.drawable.wallpaper74,
			R.drawable.wallpaper75,R.drawable.wallpaper76,R.drawable.wallpaper77,
			R.drawable.wallpaper78,R.drawable.wallpaper79,R.drawable.wallpaper80,
			R.drawable.wallpaper81,R.drawable.wallpaper82,R.drawable.wallpaper83,
			R.drawable.wallpaper84,R.drawable.wallpaper85,R.drawable.wallpaper86,
			R.drawable.wallpaper87,R.drawable.wallpaper88,R.drawable.wallpaper89,
			R.drawable.wallpaper90,R.drawable.wallpaper91,R.drawable.wallpaper92,
			R.drawable.wallpaper93,R.drawable.wallpaper94,R.drawable.wallpaper95,
			R.drawable.wallpaper96,R.drawable.wallpaper97,R.drawable.wallpaper98,
			R.drawable.wallpaper99,R.drawable.wallpaper100,R.drawable.wallpaper101,
			R.drawable.wallpaper102,R.drawable.wallpaper103,R.drawable.wallpaper104,
			R.drawable.wallpaper105,R.drawable.wallpaper106,R.drawable.wallpaper107,
			R.drawable.wallpaper108,R.drawable.wallpaper109,R.drawable.wallpaper110,
			R.drawable.wallpaper111,R.drawable.wallpaper112,R.drawable.wallpaper113,
			R.drawable.wallpaper114,R.drawable.wallpaper115,R.drawable.wallpaper116};
	private Gallery gallery;
	private ImageAdapter adapter;
	private ImageSwitcher is;
	private Button ok;
	private int whichwallpaper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallpapersetting);
		back=(ImageView) findViewById(R.id.back);
		gallery=(Gallery) findViewById(R.id.gallery);
		is=(ImageSwitcher) findViewById(R.id.is);
		ok=(Button) findViewById(R.id.ok);
		whichwallpaper=0;
        //加载适配器
        adapter=new ImageAdapter(res, this);
		gallery.setAdapter(adapter);
		gallery.setOnItemSelectedListener(this);
		is.setFactory(this);
		is.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		is.setOutAnimation(this, android.R.anim.fade_out);
		cuser = BmobUser.getCurrentUser(WallpapersettingActivity.this,IndoorUser.class);
		findMyConfigures();
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
                //保存数据
                cu_Configure.setWhichwallpaper(whichwallpaper);
				cu_Configure.update(WallpapersettingActivity.this);
				finish();
			}
		});
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		is.setBackgroundResource(res[arg2%res.length]);
		whichwallpaper=arg2%res.length;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

	@Override
	public View makeView() {
		ImageView image=new ImageView(this);
		image.setScaleType(ScaleType.FIT_CENTER);
		return image;
	}

	/**
     * 查询我的所有配置信息
     */
	private void findMyConfigures(){
	    BmobQuery<Configure> configures = new BmobQuery<Configure>();
	    /**
         * 注意这里的查询条件
         * 第一个参数：是User表中的configures字段名
         * 第二个参数：是指向User表中的某个用户的BmobPoint
         * er对象
         */
	    configures.addWhereRelatedTo("configures", new BmobPointer(cuser));
	    configures.findObjects(this, new FindListener<Configure>() {

	        @Override
	        public void onSuccess(List<Configure> arg0) {     
	            for (Configure configure : arg0) {
	            	cu_Configure=configure;
	            }
	        }

	        @Override
	        public void onError(int arg0, String arg1) {
	        }
	    });
	}
}
