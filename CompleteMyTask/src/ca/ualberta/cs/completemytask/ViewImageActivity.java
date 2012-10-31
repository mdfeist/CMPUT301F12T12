package ca.ualberta.cs.completemytask;

import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 
 * 
 * @author Devon Waldon
 *
 */
public class ViewImageActivity extends Activity {

	private static final int CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final String TAG = "ViewImageActivity";
	Uri imageFileUri;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_image);

		Gallery photoGallery = (Gallery) findViewById(R.id.ImageGallery);
		ImageAdapter adapter = new ImageAdapter(this);

		photoGallery.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_view_image, menu);
		return true;
	}

	public void takePhoto(View view){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
		File folderF = new File(folder);
		if (!folderF.exists()) {
			folderF.mkdir();
		}

		String imageFilePath = folder + "/" + String.valueOf(System.currentTimeMillis()) +".jpg";
		File imageFile = new File(imageFilePath);
		imageFileUri = Uri.fromFile(imageFile);

		intent.putExtra(MediaStore.ACTION_IMAGE_CAPTURE, imageFileUri);
		startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
	}

	private Bitmap getBitmap(Intent intent) {
		Bundle extras = intent.getExtras();
		Bitmap newPhoto = (Bitmap) extras.get("data");
		return newPhoto;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		if(requestCode == CAPTURE_IMAGE_REQUEST_CODE){
			Toast.makeText(getApplicationContext(), "Result!", Toast.LENGTH_SHORT).show();
			Bitmap b = getBitmap(intent);
			ImageView i = (ImageView) findViewById(R.id.TaskImageView);
			i.setImageBitmap(b);
			Toast.makeText(getApplicationContext(), "Set", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Closes the task
	 * 
	 * @param view
	 */
	public void close(View view) {
		this.finish();
	}
}
