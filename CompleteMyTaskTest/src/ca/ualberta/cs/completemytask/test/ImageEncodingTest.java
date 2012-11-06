package ca.ualberta.cs.completemytask.test;

import android.graphics.Bitmap;
import junit.framework.TestCase;

public class ImageEncodingTest extends TestCase {

	public void testMyPhoto(){
		MyPhotoTest t = new MyPhotoTest();
		
		int[] colours = {1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5};		
		Bitmap bitmap = Bitmap.createBitmap(colours, 5, 5, Bitmap.Config.ARGB_8888);
		
		assertTrue("SHOULD NEVER FAIL",bitmap.sameAs(bitmap));
		assertTrue("Bitmap changed on encode/decode",t.compare(bitmap));
		
		boolean compareBitmaps = t.getBitmapFromString(t.getStringFromBitmap(bitmap)).sameAs(bitmap);
		assertTrue("Bitmap changed in actual MyPhoto code",compareBitmaps);
	}
}
