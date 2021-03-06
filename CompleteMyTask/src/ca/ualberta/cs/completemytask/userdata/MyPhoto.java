package ca.ualberta.cs.completemytask.userdata;

import java.io.ByteArrayOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import ca.ualberta.cs.completemytask.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

/**
 * A photo.
 * 
 * @author Michael Feist, Devon Waldon
 *
 */
public class MyPhoto extends ChildUserData implements UserContent<Bitmap> {
	protected final String TAG = "MyPhoto";

	Bitmap image;

	public MyPhoto() {
		this.image = null;
	}

	/**
	 * Gets the image stored in MyPhoto.
	 * @return The image
	 */
	public Bitmap getContent() {
		return this.image;
	}

	/**
	 * Set the image in MyPhoto to the given
	 * image.
	 */
	public void setContent(Bitmap content) {
		this.image = content;
	}

	/**
	 * Posted by Manav on Dec 17, 2011 in Learn, Tutorials |
	 * <br/>
	 * <a href="http://mobile.cs.fsu.edu/converting-images-to-json-objects/" >
	 * http://mobile.cs.fsu.edu/converting-images-to-json-objects/
	 * </a>
	 * @param bitmap image
	 * @return string of encoded image
	 */
	public String getStringFromBitmap(Bitmap bitmapPicture) {
		/*
		 * This functions converts Bitmap picture to a string which can be
		 * JSONified.
		 */
		final int COMPRESSION_QUALITY = 25;
		
		ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
		bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
				byteArrayBitmapStream);
		byte[] encodedByteArray = byteArrayBitmapStream.toByteArray();
		
		String encodedString = Base64.encodeToString(encodedByteArray, Base64.URL_SAFE);

		return encodedString;
	}

	/**
	 * Posted by Manav on Dec 17, 2011 in Learn, Tutorials |
	 * <br/>
	 * <a href="http://mobile.cs.fsu.edu/converting-images-to-json-objects/" >
	 * http://mobile.cs.fsu.edu/converting-images-to-json-objects/
	 * </a>
	 * @param string of encoded image
	 * @return bitmap image
	 */
	public Bitmap getBitmapFromString(String imageString) {
		/*
		 * This Function converts the String back to Bitmap
		 * */

		byte[] decodedByteArray = Base64.decode(imageString, Base64.URL_SAFE);
		Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
		/*
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedByteArray);
		Bitmap decodedBitmap = BitmapFactory.decodeStream(byteArrayInputStream);
		*/
		return decodedBitmap;
	}

	/**
	 * Decodes a string into a bitmap image and then
	 * sets"image" in my MyPhoto to the decoded bitmap.
	 * @param imageString
	 */
	public void setImageFromString(String imageString) {
		this.image = getBitmapFromString(imageString);
	}
	
	public String getImageAsString() {
		return getStringFromBitmap(this.image);
	}

	/**
	 * Returns a string of the photo in JSON form.
	 * @return A string in JSON form
	 */
	public String toJSON() {
		JSONObject json = new JSONObject();

		String userName = "Unknown";

		if (hasUser()) {
			userName = getUser().getUserName();
		}

		try {
			json.put( "type", "Photo");
			json.put( "user", userName);
			json.put( "image", "'" + getStringFromBitmap(this.image) + "'");
			json.put( "parentID", this.parentID);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json.toString();
	}
	
	/**
	 * From the given JSONObject retrieve the needed
	 * info for the photo
	 * @param A JSONObject of the photo
	 * @return A photo
	 */
	public void decodePhoto(JSONObject data) {
		
		Log.v(TAG, "Decoding Image Data");
		
		String userName = "Unknown";
		String imageString = "";
		long parentID = 0;
		
		try {
			userName = data.getString("user");
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get user.");
			userName = "Unknown";
		}
		
		try {
			imageString = data.getString("image");
			imageString = imageString.substring(1, imageString.length() - 1);
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get image.");
			imageString = "";
		}
		
		try {
			parentID = data.getLong("parentID");
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get parentID.");
			parentID = 0;
		}
		
		Log.v(TAG, imageString);
		
		User user = new User(userName);
		this.setUser(user);
		this.setImageFromString(imageString);
		this.setParentId(parentID);
	}

	public void createFake(Context context){
		BitmapFactory.decodeResource(context.getResources(),R.drawable.img1);
	}
}
