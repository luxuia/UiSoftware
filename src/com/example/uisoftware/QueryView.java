package com.example.uisoftware;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.example.uisoftware.R;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import android.os.Bundle;
import android.os.Environment;
import android.renderscript.Sampler.Value;
import android.R.color;
import android.R.integer;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QueryView extends Activity {

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		projects = mySqLiteManager.query(((MainView) getParent())
				.getSqlCommond());
		mCommond = ((MainView) getParent()).getSqlCommond();
		adapter.notifyDataSetChanged();

	}

	private static final String TAG = "QueryView";
	private SQLiteManager mySqLiteManager = null;

	private HVListView mListView;
	private LayoutInflater mInflater;
	private List<Project> projects;
	private DataAdapter adapter;

	private SqlCommond mCommond;

	private ArrayList<String> desc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query);

		mySqLiteManager = new SQLiteManager(this);
		mCommond = ((MainView) getParent()).getSqlCommond();
		projects = mySqLiteManager.query(mCommond);

		mListView = (HVListView) findViewById(android.R.id.list);
		mListView.mListHead = (LinearLayout) findViewById(R.id.head);

		desc = new ArrayList<String>();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String ss = "";

				Project p = projects.get(arg2);
				for (int i = 0; i < 11; i++) {
					int value = p.getValue(i);
					switch (i) {
					case 0: {
						if (value > 10)
							ss = "���ͳ��������ж���������С�����ס����򲡵� ,";
						break;
					}
					case 1: {
						if (value > 0)
							ss += "����, ";
						break;
					}
					case 2: {
						if (value <= 1 && value > 0)
							ss += "ͬʱ�е����ߣ�Ҫ�������ಡ�ͳ�Ѫ, ";
						break;
					}
					case 3: {
						if (value <= 20 && value >= 0)
							ss += "��·��Ⱦ, ";
						break;
					}
					case 4: {
						if (value <= 7 && value > 0)
							ss += "������С�����ס��������Բ���, ";
						
						break;
					}
					case 5: {
						if (value > 0 && value < 3)
							ss += "���򲡡��׿���֫�˷ʴ�֢��, ";
						break;
					}
					case 6: {
						if (value < 2 && value >= 1)
							ss += "��ϸ���Ի������Ի���, ";
						break;
					}
					case 7: {
						if (value < 0)
							ss += "���ж������򲡡�Ż�¡���к, ";
						break;
					}
					case 8: {
						if (value <= 1 && value > 0)
							ss += "�����������������·��Ⱦ��, ";
						break;
					}
					case 9: {
						if (value < 0)
							ss += "����ɫ������ǡ�Ѫ��ɫ�Ⱦ�˵��������, ";
						break;
					}
					case 10: {
						if (value <= 0)
							ss += "���߳�����Ƶ��Ż�¡������Լ��ж���, ";
						break;
					}
					}
				}

				// if (desc.get(arg2).equals(""))
				Toast.makeText(getApplicationContext(),
						ss, Toast.LENGTH_LONG).show();

			}
		});

		adapter = new DataAdapter();
		mListView.setAdapter(adapter);

		mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

	}

	static class ViewHolder {
		public TextView[] TVdata;

		public ViewHolder() {
			TVdata = new TextView[11];
		}
	}

	private class DataAdapter extends BaseAdapter {

		public int getCount() {
			return projects.size();// �̶���ʾ50������
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {

				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.queryitem, null);
				holder.TVdata[0] = (TextView) convertView
						.findViewById(R.id.item1);
				for (int i = 0; i < 10; i++) {
					holder.TVdata[i + 1] = (TextView) convertView
							.findViewById(R.id.item2 + i);
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			String dateString = projects.get(position).time.trim();
			Log.e(TAG, dateString);
			/*
			 * Date date = new Date(projects.get(position).time.trim());
			 * SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm");
			 */
			dateString = dateString.substring(5, 16);
			holder.TVdata[0].setText(dateString);

			Log.e(TAG, " " + mCommond.id + "idCheck " + mCommond.idCheck
					+ "nameCheck " + mCommond.nameCheck);

			String ss = "";
			if ((mCommond.idCheck || mCommond.nameCheck) && mCommond.id >= 0) {
				for (int i = 1; i < 11; i++) {
					if (i != mCommond.id + 1) {
						findViewById(R.id.item2 + i - 1).setVisibility(
								View.GONE);
						holder.TVdata[i].setVisibility(View.GONE);
					} else {
						findViewById(R.id.item2 + i - 1).setVisibility(
								View.VISIBLE);
						holder.TVdata[i].setVisibility(View.VISIBLE);
						holder.TVdata[i].setBackgroundColor(chooseColor(
								position, i,
								projects.get(position).getValue(i), ss));
						holder.TVdata[i].setText(""
								+ projects.get(position).getValue(i));
					}

					Log.e(TAG,
							"pos: " + position + ", value"
									+ projects.get(position).getValue(i));

				}
				desc.add(ss);

				Log.e(TAG, ss);
			} else {
				for (int i = 1; i < 11; i++) {
					findViewById(R.id.item2 + i - 1)
							.setVisibility(View.VISIBLE);
					holder.TVdata[i].setVisibility(View.VISIBLE);

					holder.TVdata[i].setBackgroundColor(chooseColor(position,
							i, projects.get(position).getValue(i), ss));

					holder.TVdata[i].setText(""
							+ projects.get(position).getValue(i));
					Log.e(TAG,
							"pos: " + position + ", value"
									+ projects.get(position).getValue(i));
				}
				desc.add(ss);
				Log.e(TAG, ss);

			}
			/*
			 * for (Project project : projects) { for (int i = 0; i < 9; i++) {
			 * 
			 * // Log.e(TAG, "Project " + keysString[i] + //
			 * project.getValue(i)); // Log.e(TAG, "Project " + keysString[i] +
			 * // project.getValue(i)); ((TextView)
			 * convertView.findViewById(R.id.item2 + i)) .setText(""); //
			 * ((TextView) convertView.findViewById(R.id.item2 + //
			 * i)).setText(project.getValue(i) // "����" + position + "��" + (i +
			 * 2) + "��" // );
			 * 
			 * // Log.e(TAG, "Successed to set item " + (i+2) + " value " + //
			 * project.getValue(i));
			 * 
			 * } }
			 */
			// У��������ͬʱ���º����ҹ������ִ�λ�����
			View child = ((ViewGroup) convertView).getChildAt(1);
			int head = mListView.getHeadScrollX();
			if (child.getScrollX() != head) {
				child.scrollTo(mListView.getHeadScrollX(), 0);
			}
			return convertView;
		}

		private int chooseColor(int pos, int i, int value, String ss) {
			// TODO Auto-generated method stub

			Log.e(TAG, "COLOR" + i);

			switch (i) {
			case 0: {
				if (value < 10)
					return Color.GREEN;
				else {

					ss += "���ͳ��������ж���������С�����ס����򲡵� ,";

					return Color.RED;
				}
			}
			case 1: {
				if (value < 0)
					return Color.GREEN;
				else {

					ss += "����, ";

					return Color.RED;
				}
			}
			case 2: {
				if (value <= 1 && value > 0)
					return Color.GREEN;
				else {
					ss += "ͬʱ�е����ߣ�Ҫ�������ಡ�ͳ�Ѫ, ";

					return Color.RED;
				}
			}
			case 3: {
				if (value <= 20 && value >= 0)
					return Color.GREEN;
				else {
					ss += "��·��Ⱦ, ";

					return Color.RED;
				}

			}
			case 4: {
				if (value <= 7 && value > 0)
					return Color.GREEN;
				else {
					ss += "������С�����ס��������Բ���, ";

					return Color.RED;
				}
			}
			case 5: {
				if (value > 0 && value < 3)
					return Color.GREEN;
				else {
					ss += "���򲡡��׿���֫�˷ʴ�֢��, ";

					return Color.RED;
				}
			}
			case 6: {
				if (value < 2 && value >= 1)
					return Color.GREEN;
				else {
					ss += "��ϸ���Ի������Ի���, ";

					return Color.RED;

				}
			}
			case 7: {
				if (value < 0)
					return Color.GREEN;
				else {
					ss += "���ж������򲡡�Ż�¡���к, ";

					return Color.RED;
				}
			}
			case 8: {
				if (value <= 1 && value > 0)
					return Color.GREEN;
				else {
					ss += "�����������������·��Ⱦ��, ";

					return Color.RED;
				}
			}
			case 9: {
				if (value < 0)
					return Color.GREEN;
				else {
					ss += "����ɫ������ǡ�Ѫ��ɫ�Ⱦ�˵��������, ";

					return Color.RED;
				}
			}
			case 10: {
				if (value <= 0)
					return Color.GREEN;
				else {

					ss += "���߳�����Ƶ��Ż�¡������Լ��ж���, ";

					return Color.RED;
				}
			}

			default:
				return 0xffffff;
			}

		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return position;
		}

	}

	protected void onDestory() {
		super.onDestroy();
		mySqLiteManager.closeDB();
	}

	public void query(View view) {

		projects = mySqLiteManager.query(((MainView) getParent())
				.getSqlCommond());
		mCommond = ((MainView) getParent()).getSqlCommond();
		adapter.notifyDataSetChanged();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_query, menu);
		return true;
	}

	public void SaveExcel(View view) {
		try {
			if (Environment.getExternalStorageState() != null) {// �����������̽�ն��Ƿ���sdcard!
				Log.v("STxt", "��SD��");
				File path = new File(Environment.getExternalStorageDirectory()
						.getPath() + "/luxuia");// ����Ŀ¼
				File f = new File(
						path.getPath() + "/"
								+ projects.get(projects.size() - 1).time
										.substring(0, 4) + ".xls");// �����ļ�
				if (!path.exists()) {// Ŀ¼���ڷ���false
					path.mkdirs();// ����һ��Ŀ¼
				}
				if (!f.exists()) {// �ļ����ڷ���false
					f.createNewFile();
				}
				FileOutputStream fout = new FileOutputStream(f);// �����ݴ���sd����
				WritableWorkbook wwb = Workbook.createWorkbook(fout);
				if (wwb != null) {
					WritableSheet wSheet = wwb.createSheet(
							"" + (new Date().getMonth() + 1), 0);

					Label tmp = new Label(1, 1, "Time");
					wSheet.addCell(tmp);

					for (int i = 0; i < 11; i++) {
						Label tmpLabel = new Label(1, i + 2,
								Constants.keysString[i]);
						wSheet.addCell(tmpLabel);
					}

					for (int i = 0; i < projects.size(); i++) {
						for (int j = 0; j < 11; j++) {
							Label tmpLabel = new Label(i + 2, j + 2, ""
									+ projects.get(i).values[j]);
							wSheet.addCell(tmpLabel);
						}
					}
					wwb.write();
					wwb.close();
				}
				Toast.makeText(getApplicationContext(), "������" + f.getAbsoluteFile(), Toast.LENGTH_SHORT).show();
			}
		} catch (IOException ex) {

		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void SaveText(View view) {
		try {
			if (Environment.getExternalStorageState() != null) {// �����������̽�ն��Ƿ���sdcard!
				Log.v("STxt", "��SD��");
				File path = new File(Environment.getExternalStorageDirectory()
						.getPath()+ "/luxuia/");// ����Ŀ¼
				File f = new File(
						path.getPath() + "/" + projects.get(projects.size() - 1).time
										.substring(0, 10) + ".txt");// �����ļ�
				
				Log.e(TAG, f.getPath() + "   , " + path.getPath());
				
				
				if (!path.exists()) {// Ŀ¼���ڷ���false
					path.mkdirs();// ����һ��Ŀ¼
				}
				if (!f.exists()) {// �ļ����ڷ���false
					f.createNewFile();
				}
				FileOutputStream fout = new FileOutputStream(f);// �����ݴ���sd����

				OutputStreamWriter writer = new OutputStreamWriter(fout);
				
				
				Log.e(TAG, "start to append message");
				for (int i = 0; i < 11; i++) {
					writer.append(Constants.keysString[i] + "\t");
					Log.e(TAG, Constants.keysString[i] + "\t");
				}
				writer.append("\n");
				if ((mCommond.idCheck || mCommond.nameCheck)
						&& mCommond.id >= 0) {
					for (int j = 0; j < projects.size(); ++j) {
						for (int i = 0; i < 11; i++) {
							
							if (i != mCommond.id + 1) {
							} else {
								writer.append(projects.get(j).getValue(i)
										+ "\n");
								Log.e(TAG, projects.get(j).getValue(i)+ "\n");
								
							}
						}

					}
				} else {
					for (int j = 0; j < projects.size(); j++) {
						for (int i = 0; i < 11; i++) {
							writer.append("" + projects.get(j).getValue(i) + "\t");
							Log.e(TAG, projects.get(j).getValue(i)+ "\n");
						}
						writer.append("\n");
					}
				}

				writer.flush();
				writer.close();
			
				Toast.makeText(getApplicationContext(), "������" + f.getAbsoluteFile(),
					Toast.LENGTH_SHORT).show();
			}
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "FileNotFoundException");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "IOException");
		}

		System.out.printf("Success to Save file\n");

	}

}
