package ca.ualberta.cs.completemytask.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import ca.ualberta.cs.completemytask.MyPhoto;

public class MyPhotoTest extends MyPhoto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * For Testing
	 * @param b
	 * @return
	 */
	public boolean compare(Bitmap encodedBitmap){
		
		final int COMPRESSION_QUALITY = 80;
		
		ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
		encodedBitmap.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
				byteArrayBitmapStream);
		byte[] encodedByteArray = byteArrayBitmapStream.toByteArray();
		
		String encodedString = Base64.encodeToString(encodedByteArray, Base64.DEFAULT);
		byte[] decodedByteArray = Base64.decode(encodedString, Base64.DEFAULT);
		
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedByteArray);
		Bitmap decodedBitmap = BitmapFactory.decodeStream(byteArrayInputStream);
		
		boolean compareBitmap = encodedBitmap.sameAs(decodedBitmap);		
		String testBitmapResult;
		if(compareBitmap==true){
			testBitmapResult = " ";
		}
		else{
			testBitmapResult = " NOT ";
		}
		Log.v(TAG, "Bitmaps are" + testBitmapResult + "the same");
		
		return compareBitmap;
	}
}
