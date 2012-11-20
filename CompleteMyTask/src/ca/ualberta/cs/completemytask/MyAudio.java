package ca.ualberta.cs.completemytask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

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
	
	
	/**
	 * Takes a string, decodes it and then writes it as
	 * a file to the SD card
	 * 
	 * @param audioString
	 * @return Audio File
	 */
	public File getAudioFromString(String audioString) {
		/*
		 * This Function converts the String back to Bitmap
		 * */

		byte[] decodedByteArray = Base64.decode(audioString, Base64.URL_SAFE);
		
		//This line needs replacing --> Outputstream?
		//File decodedAudio = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
		//Will clean up tomorrow
		File decodedAudio = new File("temp");
		try {
			FileOutputStream fos = new FileOutputStream(decodedAudio);
			fos.write(decodedByteArray);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* 
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedByteArray);
		Bitmap decodedBitmap = BitmapFactory.decodeStream(byteArrayInputStream);
		*/
		return decodedAudio;
	}
	
	/**
	 * Decodes a string into an audio file and then
	 * sets "audio" in my MyAudio to the decoded bitmap.
	 * @param audioString
	 */
	public void setImageFromString(String audioString) {
		this.audio = getAudioFromString(audioString);
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
	
	/**
	 * From the given JSONObject retrieve the needed
	 * info for the audio File
	 * @param A JSONObject of the audio File
	 * @return A File
	 */
	public void decodeAudio(JSONObject data) {
		
		Log.v(TAG, "Decoding Audio Data");
		
		String userName = "Unknown";
		String audioString = "";
		String parentID = "";
		
		try {
			userName = data.getString("user");
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get user.");
			userName = "Unknown";
		}
		
		try {
			audioString = data.getString("audio");
			audioString = audioString.substring(1, audioString.length() - 1);
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get image.");
			audioString = "";
		}
		
		try {
			parentID = data.getString("parentID");
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get parentID.");
			parentID = "";
		}
		
		Log.v(TAG, audioString);
		
		User user = new User(userName);
		this.setUser(user);
		this.setImageFromString(audioString);
		this.setParentId(parentID);
	}

/*Will this work with a file?  Not able to toss it in Drawable... Make one in 
	public void createFake(Context context){
		BitmapFactory.decodeResource(context.getResources(),R.drawable.img1);
	}
*/
}
