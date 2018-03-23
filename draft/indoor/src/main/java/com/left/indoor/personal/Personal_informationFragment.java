package com.left.indoor.personal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.left.indoor.bean.IndoorUser;
import com.left.indoor.map.R;
import com.left.indoor.myview.RoundImageView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.GetListener;

@SuppressLint("HandlerLeak") public class Personal_informationFragment extends Fragment{

	TextView nickname;
	TextView email;
	TextView location;
	View rootView;
	RoundImageView headsculpture;
	private IndoorUser cuser;
    /*
     * Function   :   实现handleMessage()方法，用于接收Message，刷新UI
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshUI();
        }
    };
    /*
     * Function   :   实现run()方法，每1秒发送一条Message给Handler
     */
    private Runnable mRunnable = new Runnable() {
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    mHandler.sendMessage(mHandler.obtainMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    
	@Override
	public View onCreateView(LayoutInflater inflater,
		ViewGroup container, Bundle savedInstanceState) {
        //rootView为Personal_informationFragment中所有控件集合
        rootView = inflater.inflate(R.layout.personal_informationfragment, container, false);
        //获取TextView控件和RoundImageview控件
        nickname= (TextView) rootView.findViewById(R.id.nickname);
		email= (TextView) rootView.findViewById(R.id.email);
		location= (TextView) rootView.findViewById(R.id.location);
		headsculpture =(RoundImageView) rootView.findViewById(R.id.headsculpture);
		cuser = BmobUser.getCurrentUser(getActivity().getApplicationContext(),IndoorUser.class);
		nickname.setText(cuser.getUsername());
		email.setText(cuser.getEmail());
        //显示图片
        BmobQuery<IndoorUser> query=new BmobQuery<IndoorUser>();
		query.getObject(getActivity().getApplicationContext(),
				cuser.getObjectId(), new GetListener<IndoorUser>() {

			@Override
			public void onSuccess(IndoorUser arg0) {
                BmobFile bmobFile = arg0.getSculpture();//只能这样获取，不能直接get，否则会闪退
                location.setText(arg0.getPosition());
				bmobFile.loadImage(getActivity().getApplicationContext(), headsculpture);
            }

                    @Override
			public void onFailure(int arg0, String arg1) {
			}
		});
		new Thread(mRunnable).start();
		return rootView;
	}

    //刷新UI
    private void refreshUI() {
    }    
}

