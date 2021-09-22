package com.gits.smzone.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.gits.smzone.R;

public class Util {
	public static String PREF_SMZONE = "smzone";
	public static String PREF_KEY_IS_SERVICE_STARTED= "is_service_started";
	public static String PREF_LICENSE_VALIDATED= "license_validated";
	
	public static boolean intToBool(int val){
		return(val>0);
	}

	public static int boolToInt(boolean val){
		if(val)
			return 1;
		else
			return 0;
	}
	
	public static final void shareApp(Context c) {
		Intent send = new Intent(Intent.ACTION_SEND);
		send.setType("text/plain");
		send.putExtra(Intent.EXTRA_TEXT, c.getString(R.string.share_app));
		//send.putExtra(Intent.EXTRA_SUBJECT, title);
		try {
			c.startActivity(Intent.createChooser(send, "Share via"));
		} catch(android.content.ActivityNotFoundException ex) {
			// if no app handles it, do nothing
			Toast.makeText(c, R.string.share_not_found, Toast.LENGTH_SHORT);
		}
	}
	
	public static String md5(String s) {
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();
	        
	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i=0; i<messageDigest.length; i++)
	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	        return hexString.toString();
	        
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
}
