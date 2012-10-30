package ca.ualberta.cs.completemytask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * A photo.
 * 
 * @author Michael Feist, Devon Waldon
 *
 */
@SuppressWarnings("serial")
public class MyPhoto extends UserData implements UserContent<Bitmap> {

	Bitmap image;
	
	public MyPhoto() {
		
	}

	public Bitmap getContent() {
		return image;
	}

	public void setContent(Bitmap content) {
		image = content;
	}

	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void createFake(Context context){
		BitmapFactory.decodeResource(context.getResources(),R.drawable.img1);
	}
	
	
	
}
