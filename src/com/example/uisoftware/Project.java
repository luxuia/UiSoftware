package com.example.uisoftware;;

public class Project {

	public int _id;
	public String time;

	public int LEU;
	public int NIT;
	public int UBG;
	
	public int PRO;
	public int PH;
	public int BLD;
	
	public int SG;
	public int KET;
	public int BIL;
	
	public int GLU;
	public int VC;
	
	public int[] values = {LEU, NIT, UBG, PRO, PH, BLD, SG, KET, BIL, GLU,VC};
	
	public int getValue(int i) {
		// TODO Auto-generated method stub
		
		
		return values[i];
		
	}
}
