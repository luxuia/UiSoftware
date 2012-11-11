package com.example.uisoftware;

import com.example.uisoftware.R;

import android.os.Bundle;
import android.app.ActivityGroup;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainView extends ActivityGroup {

	public final static String TAG = "luxuia";
	public final static boolean D = false;

	// @SuppressWarnings("unused")
	private static LinearLayout bodyView;
	private Button connect, query, history, home;
	private View nowView = null;
	private int flag = 3; // 通过标记跳转不同的页面，显示不同的菜单项
	private long firstTime = 0;

	private SqlCommond mSqlCommond;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		initMainView();
		// 显示标记页面
		showView(flag);
		connect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = 0;
				showView(flag);
				if (D)
					Toast.makeText(getApplicationContext(), "连接页面",
							Toast.LENGTH_SHORT).show();
			}

		});
		query.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = 1;
				showView(flag);
				if (D)
					Toast.makeText(getApplicationContext(), "查询页面",
							Toast.LENGTH_SHORT).show();
			}
		});
		history.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = 2;
				showView(flag);
				if (D)
					Toast.makeText(getApplicationContext(), "查询设置页面",
							Toast.LENGTH_SHORT).show();
			}
		});
		home.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = 3;

				
				
				long secondTime = System.currentTimeMillis();
				if (secondTime - firstTime > 800) {// 如果两次按键时间间隔大于800毫秒，则不退出
					Toast.makeText(getApplicationContext(), "再按一次退出程序...",
							Toast.LENGTH_SHORT).show();
					firstTime = secondTime;// 更新firstTime
				} else {
					
					Log.e(TAG, "f:" + firstTime + "  s:" +secondTime);
					System.exit(0);// 否则退出程序
				}

				showView(flag);
				if (D)
					Toast.makeText(getApplicationContext(), "主页",
							Toast.LENGTH_SHORT).show();
			}
		});

	}

	public void setSqlCommond(SqlCommond sqlCommond) {
		mSqlCommond = sqlCommond;
	}

	public SqlCommond getSqlCommond() {
		if (mSqlCommond == null)
			return new SqlCommond();
		return mSqlCommond;
	}

	/*
	 * 初始化主界面
	 */
	public void initMainView() {
		// headview = (LinearLayout) findViewById(R.id.head);
		bodyView = (LinearLayout) findViewById(R.id.containerBody);
		connect = (Button) findViewById(R.id.connect);
		query = (Button) findViewById(R.id.query);
		history = (Button) findViewById(R.id.history);
		home = (Button) findViewById(R.id.home);
	}

	// 在主界面中显示其他界面

	private void fadeIn() {
		if (nowView != null) {
			Animation anim = AnimationUtils.loadAnimation(this,
					android.R.anim.fade_in);
			nowView.startAnimation(anim);
		}
	}

	private void fadeOut() {
		if (nowView != null) {
			Animation anim = AnimationUtils.loadAnimation(this,
					android.R.anim.fade_out);
			nowView.startAnimation(anim);
		}
	}

	public void showView(int flag) {
		switch (flag) {
		case 0:

			fadeOut();
			bodyView.removeAllViews();
			// Intent intent = new Intent(MainView.this, ConnectView.class);

			nowView = getLocalActivityManager().startActivity(
					"one",
					new Intent(MainView.this, ConnectView.class)
							.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
					.getDecorView();

			fadeIn();

			bodyView.addView(nowView);

			// Intent v = new Intent(MainView.this, ConnectView.class);
			// startActivity(v);
			// one.setBackgroundResource(R.drawable.frame_button_background);
			// two.setBackgroundResource(R.drawable.frame_button_nopressbg);
			// three.setBackgroundResource(R.drawable.frame_button_nopressbg);
			// four.setBackgroundResource(R.drawable.frame_button_nopressbg);

			if (D)
				Log.e(TAG, "Connect");
			break;
		case 1:
			fadeOut();
			bodyView.removeAllViews();

			nowView = getLocalActivityManager().startActivity(
					"two",
					new Intent(MainView.this, QueryView.class)
							.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
					.getDecorView();

			fadeIn();
			bodyView.addView(nowView);

			// one.setBackgroundResource(R.drawable.frame_button_nopressbg);
			// two.setBackgroundResource(R.drawable.frame_button_background);
			// three.setBackgroundResource(R.drawable.frame_button_nopressbg);
			// four.setBackgroundResource(R.drawable.frame_button_nopressbg);
			break;
		case 2:
			fadeOut();
			bodyView.removeAllViews();

			nowView = getLocalActivityManager().startActivity(
					"three",
					new Intent(MainView.this, QuerySetView.class)
							.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
					.getDecorView();

			fadeIn();
			bodyView.addView(nowView);

			// one.setBackgroundResource(R.drawable.frame_button_nopressbg);
			// two.setBackgroundResource(R.drawable.frame_button_nopressbg);
			// three.setBackgroundResource(R.drawable.frame_button_background);
			// four.setBackgroundResource(R.drawable.frame_button_nopressbg);
			break;
		case 3:
			fadeOut();
			bodyView.removeAllViews();

			nowView = getLocalActivityManager().startActivity(
					"four",
					new Intent(MainView.this, HomeView.class)
							.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
					.getDecorView();

			fadeIn();
			bodyView.addView(nowView);
			// one.setBackgroundResource(R.drawable.frame_button_nopressbg);
			// two.setBackgroundResource(R.drawable.frame_button_nopressbg);
			// three.setBackgroundResource(R.drawable.frame_button_nopressbg);
			// four.setBackgroundResource(R.drawable.frame_button_background);
			break;
		default:
			break;
		}
	}

}
