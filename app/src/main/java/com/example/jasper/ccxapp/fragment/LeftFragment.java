package com.example.jasper.ccxapp.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jasper.ccxapp.R;


public class LeftFragment extends Fragment{

	private View favoritesView;
	private View friendView;
	private View ringView;
	private TextView myName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_main_left, null);
		findViews(view);
		
		return view;
	}
	
	
	public void findViews(View view) {

		favoritesView = view.findViewById(R.id.tvMyFavorite);
		friendView = view.findViewById(R.id.tvMyFriend);
		ringView = view.findViewById(R.id.tvRing);
		myName = (TextView) view.findViewById(R.id.myName);

		showUser();
		
		favoritesView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(getActivity(), );
//				startActivity(intent);
			}
		});
//
		friendView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("aaaaa", "aaaa");
				//startActivity(new Intent(getActivity(), FriendActivity.class));
			}
		});
//		ringView.setOnClickListener(this);
	}

	private void showUser() {
//		try {
//            // 创建File对象
//            File file = new File(getActivity().getFilesDir(), "info.properties");
//            // 创建FileIutputStream 对象
//            FileInputStream fis = new FileInputStream(file);
//            // 创建属性对象
//            Properties pro = new Properties();
//            // 加载文件
//            pro.load(fis);
//            // 关闭输入流对象
//            fis.close();
//            if(!pro.get("userName").equals("")){
//            	myName.setText(pro.get("userName").toString());
//            	ApplicationInfo appInfo = getActivity().getApplicationInfo();
//            	int resID = getResources().getIdentifier("wwj_748", "drawable", appInfo.packageName);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void ifLogin() {
//		try {
//            // 创建File对象
//            File file = new File(getActivity().getFilesDir(), "info.properties");
//            // 创建FileIutputStream 对象
//            FileInputStream fis = new FileInputStream(file);
//            // 创建属性对象
//            Properties pro = new Properties();
//            // 加载文件
//            pro.load(fis);
//            // 关闭输入流对象
//            fis.close();
//            if(pro.get("userName").toString().equals("")){
//            	return false;
//            }else{
//            	return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
	}
}
