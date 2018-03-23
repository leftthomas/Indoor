package com.left.indoor.guide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.left.indoor.bean.IndoorUser;
import com.left.indoor.map.R;
import com.left.indoor.myview.RoundImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

@SuppressLint("SdCardPath") public class SetsculptureActivity extends Activity {
	
	public static final int CAMERA  = 0x01; 
	private final String IMAGE_TYPE = "image/*";
    private final int IMAGE_CODE = 0;   //这里的IMAGE_CODE是自己任意定义的
    Bitmap bm;
    String filename;
    private TextView back;
	private TextView next;
	private Button choosefromphoto;
	private Button choosefromtakephoto;
	private RoundImageView sculpture;
	private IndoorUser cuser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setsculpture);
		back=(TextView) findViewById(R.id.back);
		next=(TextView) findViewById(R.id.next);
		choosefromphoto=(Button) findViewById(R.id.choosefromphoto);
		choosefromtakephoto=(Button) findViewById(R.id.choosefromtakephoto);
		sculpture=(RoundImageView) findViewById(R.id.sculpture);
		cuser = BmobUser.getCurrentUser(this,IndoorUser.class);
		bm=null;
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
                //保存数据
                cuser.update(SetsculptureActivity.this);
				Intent backintent;
				backintent=new Intent(SetsculptureActivity.this,ImprovepersonaldataActivity.class);
				startActivity(backintent);
				finish();
			}
		});
		next.setOnClickListener(new OnClickListener() {		
   			@Override
   			public void onClick(View arg0) {
   				if(bm==null)
                    Toast.makeText(SetsculptureActivity.this, "请设置你的头像", Toast.LENGTH_SHORT).show();
                else{
                    //保存数据
                    cuser.update(SetsculptureActivity.this);
   					uploaddata();
   				}		
    	    }
    	});
		choosefromphoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent photo = new Intent(Intent.ACTION_GET_CONTENT);
				photo.setType(IMAGE_TYPE);
				startActivityForResult(photo, IMAGE_CODE);
			}
		});
		choosefromtakephoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(camera, CAMERA);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义的一个常量
          return;
      }
        //外界的程序访问ContentProvider所提供的数据可以通过ContentResolver接口
      ContentResolver resolver = getContentResolver();
        //此处用于判断接收的Activity是不是你想要的那个
      if (requestCode == IMAGE_CODE) {
          try {
              Uri originalUri = data.getData();        //获得图片的uri 
              bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
              //显示图片
              sculpture.setImageBitmap(bm);
              FileOutputStream fout = null;
              File file = new File("/sdcard/indoor/"+cuser.getUsername());
              file.mkdirs();
              filename=file.getPath()+"/"+"sculpture.jpg";
              try {
            	  fout = new FileOutputStream(filename);
                  bm.compress(Bitmap.CompressFormat.JPEG, 100, fout);
              } catch (FileNotFoundException e) {
            	  e.printStackTrace();
              }finally{
            	  try {
            		  fout.flush();
                      fout.close();
                      } catch (IOException e) {
                    	  e.printStackTrace();
                    	  }
            	  }
          }catch (IOException e) {
          }
      }
      
      if(requestCode == CAMERA && resultCode == Activity.RESULT_OK && null != data){
	  String sdState=Environment.getExternalStorageState();
      if(!sdState.equals(Environment.MEDIA_MOUNTED)){
    	  return;
      }
      Bundle bundle = data.getExtras();
          //获取相机返回的数据，并转换为图片格式
      bm = (Bitmap)bundle.get("data");
      FileOutputStream fout = null;
      File file = new File("/sdcard/indoor/"+cuser.getUsername());;
      file.mkdirs();
      filename=file.getPath()+"/"+"sculpture.jpg";
      try {
    	  fout = new FileOutputStream(filename);
          bm.compress(Bitmap.CompressFormat.JPEG, 100, fout);
          //显示图片
          sculpture.setImageBitmap(bm);
      } catch (FileNotFoundException e) {
    	  e.printStackTrace();
      }finally{
    	  try {
    		  fout.flush();
              fout.close();
              } catch (IOException e) {
            	  e.printStackTrace();
            	  }
    	  }
      }
   }

    //将指定路径下的图片转换为Bitmap
    public Bitmap convertToBitmap(String path, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
		scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		opts.inSampleSize = (int)scale;
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
		}
	
	public void uploaddata(){
        //将本地头像上传云端，使得两地同步
        final BmobFile bmobFile = new BmobFile(new File(filename));
		bmobFile.uploadblock(SetsculptureActivity.this, new UploadFileListener() {
		    @Override
		    public void onSuccess() {
		    	cuser.setSculpture(bmobFile);
		    	cuser.update(SetsculptureActivity.this);
		    	Intent nextintent;
   	  			nextintent=new Intent(SetsculptureActivity.this,MainActivity.class);
   	   			startActivity(nextintent);
   	   			finish();
		    }
	
		    @Override
		    public void onProgress(Integer value) {
		    }
	
		    @Override
		    public void onFailure(int code, String msg) {
		    }
		});
	}
}
