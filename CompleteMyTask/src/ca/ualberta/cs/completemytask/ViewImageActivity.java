package ca.ualberta.cs.completemytask;

import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Gallery;

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
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST_CODE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		Bundle extras = data.getExtras();
	    Bitmap newPhoto = (Bitmap) extras.get("data");
	    
	    if (newPhoto!=null){
			MyPhoto image = new MyPhoto();
			image.setContent(newPhoto);
			int taskPosition = TaskManager.getInstance().getCurrentTaskPosition();
			TaskManager.getInstance().getTaskAt(taskPosition).addPhoto(image);
			Log.v(TAG,getString(TaskManager.getInstance().getTaskAt(taskPosition).getNumberOfPhotos()));
		}
	}

	/*public void takePhoto(){
		Int	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
		File folderF = new File(folder);
		if (!folderF.exists()) {
			folderF.mkdir();
		}

		String imageFilePath = folder + "/" + String.valueOf(System.currentTimeMillis()) +".jpg";
		File imageFile = new File(imageFilePath);
		imageFileUri = Uri.fromFile(imageFile);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
		startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK){    			
				newPhoto = BitmapFactory.decodeFile(imageFileUri.getPath());
			}
		}
	}ent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
		File folderF = new File(folder);
		if (!folderF.exists()) {
			folderF.mkdir();
		}

		String imageFilePath = folder + "/" + String.valueOf(System.currentTimeMillis()) +".jpg";
		File imageFile = new File(imageFilePath);
		imageFileUri = Uri.fromFile(imageFile);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
		startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK){    			
				newPhoto = BitmapFactory.decodeFile(imageFileUri.getPath());
			}
		}
	}*/

	/**
	 * Closes the task
	 * 
	 * @param view
	 */
	public void close(View view) {
		this.finish();
	}
}
