package ca.ualberta.cs.completemytask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

//import android.content.Context;
import android.util.Base64;

/**
 * A audio file.
 * 
 * @author Michael Feist and Ian Watts
 *
 */
@SuppressWarnings("serial")
public class MyAudio extends UserData implements UserContent<File> {

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
		
		//audioFile.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
		//		byteArrayBitmapStream);
		
		//Something to compress the audio?
		ByteArrayOutputStream byteArrayAudioStream = new ByteArrayOutputStream();
		
		//The Broken stuff.  Needs to change the File into a ByteArray/String
		//Some example code I was looking at.
//		InputStream is = Context.openInputStream(audioFile);
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		byte[] b = new byte[1024];
//		int bytesRead;
//		while ((bytesRead = is.read(b)) != -1) {
//			byteArrayAudioStream.write(b, 0, bytesRead);
//		}
//		byte[] bytes = byteArrayAudioStream.toByteArray();
		
		
		byte[] encodedByteArray = byteArrayAudioStream.toByteArray();
		
		String encodedString = Base64.encodeToString(encodedByteArray, Base64.URL_SAFE);

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
