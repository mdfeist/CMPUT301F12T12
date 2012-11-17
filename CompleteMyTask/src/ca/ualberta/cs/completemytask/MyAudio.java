package ca.ualberta.cs.completemytask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

//import android.content.Context;
import android.util.Base64;
import android.util.Log;

/**
 * A audio file.
 * 
 * @author Michael Feist and Ian Watts
 *
 */
@SuppressWarnings("serial")
public class MyAudio extends ChildUserData implements UserContent<File> {
	protected final String TAG = "MyAudio";
	
	File audio;
	
	public MyAudio() {
		this.audio = null;
	}

	public File getContent() {
		return this.audio;
	}

	public void setContent(File content) {
		this.audio = content;
	}

	public String getStringFromFile(File audioFile) {
		/*
		 * This functions converts audio file to a string which can be
		 * JSONified.
		 */
		
		// Ian try this code
		// Mike
		byte []buffer = new byte[(int) audioFile.length()];
		InputStream ios = null;
		
		try {
			ios = new FileInputStream(audioFile);
			if (ios.read(buffer) == -1) {
				throw new IOException("EOF reached while trying to read the whole file");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.w(TAG, e.getStackTrace().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.w(TAG, e.getStackTrace().toString());
		} finally {
			try {
				if (ios != null)
					ios.close();
			} catch (IOException e) {
				Log.w(TAG, e.getStackTrace().toString());
			}
		}
		
		String encodedString = Base64.encodeToString(buffer, Base64.URL_SAFE);
		
		Log.v(TAG, encodedString);
		
		return encodedString;
	}
	
	
	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();

		String userName = "Unknown";
		
		if (hasUser()) {
			userName = getUser().getUserName();
		}
	
		try {
			json.put( "type", "Audio");
			json.put( "user", userName);
			json.put( "audio", "'" + getStringFromFile(this.audio) + "'");
			json.put( "parentID", this.parentID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json.toString();
	}
	
}
