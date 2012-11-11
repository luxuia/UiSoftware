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
	private int flag = 3; // ͨ�������ת��ͬ��ҳ�棬��ʾ��ͬ�Ĳ˵���
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
		// ��ʾ���ҳ��
		showView(flag);
		connect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = 0;
				showView(flag);
				if (D)
					Toast.makeText(getApplicationContext(), "����ҳ��",
							Toast.LENGTH_SHORT).show();
			}

		});
		query.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = 1;
				showView(flag);
				if (D)
					Toast.makeText(getApplicationContext(), "��ѯҳ��",
							Toast.LENGTH_SHORT).show();
			}
		});
		history.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = 2;
				showView(flag);
				if (D)
					Toast.makeText(getApplicationContext(), "��ѯ����ҳ��",
							Toast.LENGTH_SHORT).show();
			}
		});
		home.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = 3;

				
				
				long secondTime = System.currentTimeMillis();
				if (secondTime - firstTime > 800) {// ������ΰ���ʱ��������800���룬���˳�
					Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����...",
							Toast.LENGTH_SHORT).show();
					firstTime = secondTime;// ����firstTime
				} else {
					
					Log.e(TAG, "f:" + firstTime + "  s:" +secondTime);
					System.exit(0);// �����˳�����
				}

				showView(flag);
				if (D)
					Toast.makeText(getApplicationContext(), "��ҳ",
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
	 * ��ʼ��������
	 */
	public void initMainView() {
		// headview = (LinearLayout) findViewById(R.id.head);
		bodyView = (LinearLayout) findViewById(R.id.containerBody);
		connect = (Button) findViewById(R.id.connect);
		query = (Button) findViewById(R.id.query);
		history = (Button) findViewById(R.id.history);
		home = (Button) findViewById(R.id.home);
	}

	// ������������ʾ��������

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
