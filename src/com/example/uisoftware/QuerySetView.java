package com.example.uisoftware;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import com.example.uisoftware.R;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class QuerySetView extends Activity {

	private static final String TAG = "QuerySetting";
	private SQLiteManager mySqLiteManager = null;

	private int mYear;
	private int mMonth;
	private int mDay;
	
	private int mSYear;
	private int mSMonth;
	private int mSDay;
	
	private int mEYear;
	private int mEMonth;
	private int mEDay;
	
	private TextView datePick;
	private TextView datePick1;
	private TextView datePick2;
	
	
	private Spinner nameSpinner;
	private Spinner idSpinner;
	
	private ArrayAdapter<String> nameDataAdapter;
	private ArrayAdapter<Integer> idDataAdapter;
	
	private Integer[] idData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
	private String selectedName;
	private int	   selectedId;
	private Button queryButton;
	private CheckBox nameCheckBox;
	private CheckBox idCheckBox;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_queryset_view);
		mySqLiteManager = new SQLiteManager(this);
		
		Calendar calendar = Calendar.getInstance();
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH)+1;
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
		
		mSYear = 2012;
		mSMonth = 0;
		mSDay = 30;
		
		mEYear = 2012;
		mEMonth = 11;
		mEDay = 30;
		
		
		datePick = (TextView) findViewById(R.id.datePick);
		datePick.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(QuerySetView.this, mDateSetListener, mYear, mMonth, mDay).show();
				//updateDatePick();
			}
		});
		
		datePick1 = ((TextView) findViewById(R.id.startDate));
		datePick1.setOnClickListener( new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new DatePickerDialog(QuerySetView.this, mDateSetListener1, mSYear, mSMonth, mSDay).show();
				//updateDatePick1();
			}
		});
		
		
		datePick2 = ((TextView) findViewById(R.id.endDate));
		datePick2.setOnClickListener( new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new DatePickerDialog(QuerySetView.this, mDateSetListener2, mEYear, mEMonth, mEDay).show();
				//updateDatePick2();
			}
		});
		
		
		updateDatePick();
		
		
		nameSpinner = (Spinner) findViewById(R.id.nameSpinner);
		
		
		
		nameDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constants.keysString);
		nameDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		nameSpinner.setAdapter(nameDataAdapter);
		nameSpinner.setOnItemSelectedListener( new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				selectedName = Constants.keysString[arg2];
				selectedId = arg2;
				
				((Spinner) findViewById(R.id.idSpinner)).setSelection(arg2);
				
				Log.e(TAG, selectedName);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		nameSpinner.setVisibility(View.VISIBLE);
		
		idSpinner = (Spinner) findViewById(R.id.idSpinner);
		idDataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, idData);
		idDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		idSpinner.setAdapter(idDataAdapter);
		idSpinner.setOnItemSelectedListener( new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				selectedId = arg2;
				((Spinner) findViewById(R.id.nameSpinner)).setSelection(arg2);
				Log.e(TAG, ""+selectedName);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		idSpinner.setVisibility(View.VISIBLE);
		
		
		nameCheckBox = (CheckBox) findViewById(R.id.nameCheck);
		idCheckBox = (CheckBox) findViewById(R.id.idCheck);
		
		
		queryButton = (Button) findViewById(R.id.queryButton);
		queryButton.setOnClickListener( setCommond );
	}

	public void add(View view) {
		ArrayList<Project> projects = new ArrayList<Project>();

		Project project = new Project();
		
		Random random = new Random();
		for (int i = 0; i < 11; i++) {
			project.values[i] = random.nextInt(10)-5;
		}
		projects.add(project);

		mySqLiteManager.add(projects);
	}

	
	private View.OnClickListener setCommond = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SqlCommond sql = new SqlCommond();
			
			String sqlString = "SELECT *";
			
			/*if (nameCheckBox.isChecked()) {
				sqlString = sqlString + "_id, time, " + selectedName;
			} else if (idCheckBox.isChecked()) {
				sqlString = sqlString + "_id, time, " + Constants.keysString[selectedId];
			} else {
				sqlString += "*";
			}*/
			sqlString = sqlString + " FROM " + Constants.TABLE_NAME;
			
			CheckBox c1 = (CheckBox) findViewById(R.id.startCheck);
			CheckBox c2 = (CheckBox) findViewById(R.id.endCheck);
			
			if (c1.isChecked() && !c2.isChecked()) {
				sqlString += " WHERE time >= '" + new Timestamp(mSYear-1900, mSMonth, mSDay, 0, 0, 0, 0).toString()+"'";
			} else if ( !c1.isChecked() && c2.isChecked()) {
				sqlString += " WHERE time <= '" + new Timestamp(mEYear-1900, mEMonth, mEDay, 0, 0, 0, 0).toString()+"'";
			} else if (c1.isChecked() && c2.isChecked()) {
				sqlString += " WHERE time >= '" + new Timestamp(mSYear-1900, mSMonth, mSDay, 0, 0, 0, 0).toString() + "'"
						+ " AND time <= '" + new Timestamp(mEYear-1900, mEMonth, mEDay, 0, 0, 0, 0).toString() + "'";
			}
			sql.command = sqlString;
			
			sql.name = selectedName;
			sql.id = selectedId;
			sql.nameCheck = nameCheckBox.isChecked();
			sql.idCheck = idCheckBox.isChecked();

			Log.e(TAG, sqlString);
			
			((MainView)getParent()).setSqlCommond(sql);
			
		}
	};
	
	
	public void delete(View view) {
		Timestamp ts = new Timestamp(mYear-1900, mMonth, mDay, 0, 0, 0, 0);
		
		
		if (((ToggleButton)findViewById(R.id.dateToggle)).isChecked()) {
			mySqLiteManager.delete(ts, "<=");
		} else {
			mySqLiteManager.delete(ts, ">=");
		}
		
		
	}
	
	
	public void updateDatePick() {
		datePick.setText(""+ mYear+"/" + mMonth + "/" + mDay);
	}
	
	public void updateDatePick1() {
		datePick1.setText(""+ mSYear+"/" + mSMonth + "/" + mSDay);
	}
	
	public void updateDatePick2() {
		datePick2.setText(""+ mEYear+"/" + mEMonth + "/" + mEDay);
	}
	
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = 
			new DatePickerDialog.OnDateSetListener() {
		
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			mYear = year;
			mMonth = monthOfYear+1;
			mDay = dayOfMonth;
			
			updateDatePick();
		}
	};
	
	private DatePickerDialog.OnDateSetListener mDateSetListener1 = 
			new DatePickerDialog.OnDateSetListener() {
		
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			mSYear = year;
			mSMonth = monthOfYear+1;
			mSDay = dayOfMonth;
			updateDatePick1();
		}
	};
	
	private DatePickerDialog.OnDateSetListener mDateSetListener2 = 
			new DatePickerDialog.OnDateSetListener() {
		
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			mEYear = year;
			mEMonth = monthOfYear+1;
			mEDay = dayOfMonth;
			updateDatePick2();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_history_view, menu);
		return true;
	}
}
