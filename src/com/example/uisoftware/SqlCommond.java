package com.example.uisoftware;

public class SqlCommond {

	/**
	 * @param args
	 */
	
	public String sqlQuery;
	
	public int sYear;
	public int sMonth;
	public int sDay;
	
	public int eYear;
	public int eMonth;
	public int eDay;
	
	public boolean sCheack;
	public boolean eCheack;
	
	public String name;
	public int 		id;
	public boolean nameCheck;
	public boolean idCheck;
	
	public String command;
	
	
	public  SqlCommond() {
		id = -1;
		command = "SELECT * FROM " + Constants.TABLE_NAME;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
