package ca.ualberta.cs.completemytask;

import android.content.Context;
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

	private Integer[] mImageIds = {
			R.drawable.img1,
			R.drawable.img2,
			R.drawable.img3,
	};

	public ImageAdapter(Context c) {
		mContext = c;

		/*TypedArray a = c.obtainStyledAttributes(R.styleable.HelloGallery);
		mGalleryItemBackground = a.getResourceId(
				R.styleable.HelloGallery_android_galleryItemBackground, 0);
		a.recycle();*/
	}

	public int getCount() {
		return mImageIds.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView i = new ImageView(mContext);

		i.setImageResource(mImageIds[position]);
		i.setLayoutParams(new Gallery.LayoutParams(150, 100));
		i.setScaleType(ImageView.ScaleType.FIT_XY);
		i.setBackgroundResource(mGalleryItemBackground);

		return i;
	}
}
