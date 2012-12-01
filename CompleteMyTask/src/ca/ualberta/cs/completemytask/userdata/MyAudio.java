package ca.ualberta.cs.completemytask.userdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.util.Base64;
import android.util.Log;

/**
 * Class to contain an Audio file.  The audio is stored as a 
 * byte[] along with a string for the file's title.
 * 
 * @author Michael Feist and Ian Watts
 *
 */
public class MyAudio extends ChildUserData implements UserContent<byte[]> {
	protected final String TAG = "MyAudio";
	byte[] audio;
	String audioName;

	public MyAudio() {
		this.audio = null;
		this.audioName = null;
	}

	public byte[] getContent() {
		return this.audio;
	}

	public void setContent(byte[] content) {
		this.audio = content;
	}
	
	public String getAudioName() {
		return this.audioName;
	}

	public void setAudioName(String content) {
		this.audioName = content;
	}

	//Might be redundant since the functionality is in View and Capture Audio now
	/**
	 * This functions converts audio file to a string which can be
	 * JSONified.
	 */
	public String getStringFromFile(File audioFile) {
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
	 * Takes a byte array (our representation of audio file) and return
	 * a string that can be used for JSON
	 * @param audioByte
	 * @return String
	 */
	public String getStringFromByte(byte[] audioByte) {
		String encodedString = Base64.encodeToString(audioByte, Base64.URL_SAFE);
		return encodedString;
	}
	
	public String getStringFromAudio() {
		return getStringFromByte(audio);
	}

	/**
	 * Takes a string, decodes it and then writes it as
	 * a file to the SD card
	 * 
	 * @param audioString
	 * @return Audio File
	 */
	public File getAudioFromString(String audioString) {
		byte[] decodedByteArray = Base64.decode(audioString, Base64.URL_SAFE);
		
		//This will be split into a new part
		File decodedAudio = new File(android.os.Environment.getExternalStorageDirectory()+"/Record/test.3gp");
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

		return decodedAudio;
	}

	/**
	 * Takes a String (used in the JSON object) and returns
	 * a byte array that we use to represent audio
	 * @param audioByte
	 * @return String
	 */
	public byte[] getByteFromString(String audioString) {
		byte[] decodedByteArray = Base64.decode(audioString, Base64.URL_SAFE);
		return decodedByteArray;
	}
	
	/**
	 * Decodes a string into an audio file and then
	 * sets "audio" in my MyAudio to the decoded bitmap.
	 * @param audioString
	 */
	public void setAudioFromString(String audioString) {
		this.audio = getByteFromString(audioString);
	}
	
}

