package com.example.uisoftware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothDevice;

public class ClsUtils {

	/**
	 * @param args
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchMethodException 
	 */
	
	static public boolean createBond(Class btClass, BluetoothDevice device) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Method createBondMethod = btClass.getMethod("createBond");
		boolean returnValue = (Boolean) createBondMethod.invoke(device);
		return returnValue;
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
