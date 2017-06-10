package com.example.jasper.ccxapp.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup.LayoutParams;
import android.widget.TextView;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.adapter.FriendAdapter;
import com.example.jasper.ccxapp.adapter.ViewPagerAdapter;
import com.example.jasper.ccxapp.db.friendDB;
import com.example.jasper.ccxapp.ui.FriendActivity;
import com.example.jasper.ccxapp.ui.UserDetailActivity;
import com.example.jasper.ccxapp.interfaces.UserBackListUserInfo;
import com.example.jasper.ccxapp.interfaces.UserBackListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

import static com.example.jasper.ccxapp.util.ShowProcessUtil.hideProgressDialog;
import static com.example.jasper.ccxapp.util.ShowProcessUtil.showProgressDialog;

@SuppressLint("HandlerLeak") public class FriendFragment extends Fragment implements OnClickListener, OnPageChangeListener {

    public static final String TAG = "FriendFragment";
    private static final int WHAT_SET_CURSOR_POSITION = 0;

    private static FriendFragment friFragInstance;

    private LayoutInflater inflater;

    private View friFragLayout, cursorView;
    private ViewPager viewPager;

    private int titleTxtWidth;
    private int cursorWidth, cursorHeight = 10;
    private int curCursorPos, lastCursorPos;

    private ArrayList<View> childViewsOfViewPager = new ArrayList<View>();
    private Handler handler;
    private Runnable cursorPosRunnable;



    public static FriendFragment getInstance(){
        if(friFragInstance == null) {
            friFragInstance = new FriendFragment();
        }
        return friFragInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        friFragLayout = inflater.inflate(R.layout.fragment_friend, container, false);

        initHandler();
        initCursorPositionRunnable();

        initMsgTitle();
        initCursor();
        initViewPager();

        return friFragLayout;
    }

    private void initMsgTitle() {
        TextView oldTxt =
                (TextView) friFragLayout.findViewById(R.id.title_old);
        TextView friendTxt =
                (TextView) friFragLayout.findViewById(R.id.title_friend);

        oldTxt.setOnClickListener(this);
        friendTxt.setOnClickListener(this);

        titleTxtWidth = getTitleTextViewWidth();
    }

    private void initCursor() {

        cursorView = friFragLayout.findViewById(R.id.cursor_FriendFragment);

        cursorWidth = getCursorWidth();
        LayoutParams params = new LayoutParams(cursorWidth, cursorHeight);

        cursorView.setLayoutParams(params);
        setCursorPosition(curCursorPos);
    }

    private void initViewPager() {

        viewPager = (ViewPager) friFragLayout.findViewById(R.id.viewPager_FriendFragment);

        View oldView = inflater.inflate(R.layout.fragment_myold, null);
        View youngView = inflater.inflate(R.layout.fragment_myfriend, null);

        getFriends();
        childViewsOfViewPager.add(oldView);
        childViewsOfViewPager.add(youngView);


        viewPager.setAdapter(new ViewPagerAdapter(childViewsOfViewPager));
        viewPager.setOnPageChangeListener(this);

    }


    private void setCursorPosition(int position) {
        lastCursorPos = curCursorPos;
        curCursorPos = position;
        new Thread(cursorPosRunnable).start();
    }

    private int getTitleTextViewWidth() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int screenWidth = outMetrics.widthPixels;
        return screenWidth/2;
    }
    private int getCursorWidth() {

        return titleTxtWidth/2;
    }

    private void initHandler() {

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FriendFragment.WHAT_SET_CURSOR_POSITION:

                        cursorView.setX(msg.arg1);
                        break;

                    default:
                        break;
                }

            };
        };

    }

    private void initCursorPositionRunnable() {

        cursorPosRunnable = new Runnable() {

            @Override
            public void run() {
                calcXAndSendMsg();
            }

            private void calcXAndSendMsg() {
                try{
                    int cusorCurrentX = titleTxtWidth*lastCursorPos+(titleTxtWidth-cursorWidth)/2;
                    int cursorTargetX = titleTxtWidth*curCursorPos+(titleTxtWidth-cursorWidth)/2;

                    if(lastCursorPos == curCursorPos) {
                        handler.obtainMessage(WHAT_SET_CURSOR_POSITION, cusorCurrentX, 0).sendToTarget();
                    } else {
                        while(cusorCurrentX != cursorTargetX) {

                            if(cusorCurrentX < cursorTargetX) {
                                cusorCurrentX+=15;
                            } else {
                                cusorCurrentX-=15;
                            }
                            handler.obtainMessage(WHAT_SET_CURSOR_POSITION, cusorCurrentX, 0).sendToTarget();
                            Thread.sleep(5);
                        }
                    }
                }catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }
	private void getFriends() {
		if(!showProgressDialog(getActivity(), "系统提示", "信息加载中，请稍后")){
			return;
		}
		friendDB.searchfriend(new UserBackListUserInfo() {
			@Override
			public void showResult(boolean result, List<UserInfo> message) {
				hideProgressDialog();
                if(result){
					showFriends(message);
				}else{
					showDialog("查询好友出错");
				}
		}
		});
	}
    private void showFriends(List<UserInfo> message) {
		final List<UserInfo> userInfosOld = new ArrayList<>();
		final List<UserInfo> userInfosYoung = new ArrayList<>();
		for(UserInfo userInfo:message){
            try {
                if (userInfo.getRegion().equals("old")) {
                    userInfosOld.add(userInfo);
                } else {
                    userInfosYoung.add(userInfo);
                }
            }catch (Exception e){
                userInfosYoung.add(userInfo);
            }
		}
        ListView lv = (ListView) friFragLayout.findViewById(R.id.all_friend_lv);
        FriendAdapter adapter = new FriendAdapter(getActivity(), userInfosOld);
        lv.setAdapter(adapter);
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				new AlertDialog.Builder(getActivity()).setTitle("系统提示").setMessage("确认删除该老人？")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								friendDB.deletefriend(userInfosOld.get(position), new UserBackListener() {
                                    @Override
                                    public void showResult(boolean result, String message) {
										if(result){
                                            showDialog("删除成功！");
                                            getFriends();
                                        }else{
                                            showDialog("删除失败！");
                                        }
                                    }
                                });
							}
						}).setNegativeButton("取消", null).show();
				return true;
			}
		});
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = getIntent(userInfosOld.get(position));
				startActivity(i);
			}
		});

        ListView lv2 = (ListView) friFragLayout.findViewById(R.id.all_young_friend_lv);
        FriendAdapter adapter2 = new FriendAdapter(getActivity(), userInfosYoung);
        lv2.setAdapter(adapter2);
        lv2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getActivity()).setTitle("系统提示").setMessage("确认删除该好友？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                friendDB.deletefriend(userInfosYoung.get(position), new UserBackListener() {
                                    @Override
                                    public void showResult(boolean result, String message) {
                                        if(result){
                                            showDialog("删除好友成功！");
                                            getFriends();
                                        }else{
                                            showDialog("删除好友失败！");
                                        }
                                    }
                                });
                            }
                        }).setNegativeButton("取消", null).show();
                return true;
            }
        });
		lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = getIntent(userInfosYoung.get(position));
				startActivity(i);
			}
		});
	}



    private Intent getIntent(UserInfo userDetail){
        Intent i = new Intent(getActivity(), UserDetailActivity.class);
        File avatarFile = userDetail.getAvatarFile();
        i.putExtra("headImage", BitmapFactory.decodeFile(String.valueOf(avatarFile)));
        i.putExtra("userName", userDetail.getUserName());
        i.putExtra("nickName", userDetail.getNickname());
        UserInfo.Gender sex2 = userDetail.getGender();
        String sex;
        if(sex2.equals("female")){
            sex = "女";
        }else {
            sex = "男";
        }
        i.putExtra("sex", sex);
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        i.putExtra("birthday", date.format(userDetail.getBirthday()).toString());
        i.putExtra("address", userDetail.getAddress());
        i.putExtra("explain", userDetail.getSignature());
        return i;
    }
    private void showDialog(String message) {
        new android.app.AlertDialog.Builder(getActivity()).setTitle("系统提示").setMessage(message)
                .setPositiveButton("确定", null).show();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_old:
                viewPager.setCurrentItem(0);
                break;
            case R.id.title_friend:
                viewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }



    @Override
    public void onPageScrollStateChanged(int state) {

    }
    int offset;
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {
        setCursorPosition(position);
    }

}
