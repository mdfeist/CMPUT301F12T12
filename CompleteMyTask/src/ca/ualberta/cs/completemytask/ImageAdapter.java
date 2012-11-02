package ca.ualberta.cs.completemytask;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

//FROM http://raivoratsep.com/114/android-gallery-tutorial-working-example/
//Original author: Raivo

public class ImageAdapter extends BaseAdapter {
	int mGalleryItemBackground;
	private Context mContext;

	private List<Bitmap> bitmaps = new ArrayList<Bitmap>();

	public ImageAdapter(Context c) {
		mContext = c;
	}

	public int getCount() {
		return bitmaps.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView i = new ImageView(mContext);
		
		i.setImageBitmap(bitmaps.get(position));
		//i.setLayoutParams(new Gallery.LayoutParams(150, 150));
		i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		//i.setBackgroundResource(mGalleryItemBackground);

		return i;
	}
	
	public void addBitmap(Bitmap b){
		bitmaps.add(b);
	}
}
