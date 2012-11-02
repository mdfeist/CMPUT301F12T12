package ca.ualberta.cs.completemytask;

import java.io.File;

import android.net.Uri;
import android.os.AsyncTask;
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
	Gallery photoGallery;
	ImageAdapter adapter;
	private Task task;
	Uri imageFileUri;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_image);

		int position = TaskManager.getInstance().getCurrentTaskPosition();
		task = TaskManager.getInstance().getTaskAt(position);

		photoGallery = (Gallery) findViewById(R.id.ImageGallery);
		adapter = new ImageAdapter(this);

		for(int i=0; i<task.getNumberOfPhotos(); i++){
			adapter.addBitmap(task.getPhotoAt(i).getContent());
		}
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

	private void addImageToTask(Bitmap b){
		MyPhoto image = new MyPhoto();
		image.setContent(b);

		User user = null;

		if (Settings.getInstance().hasUser()) {
			user = Settings.getInstance().getUser();
		} else {
			user = new User("Unknown");
		}

		image.setUser(user);
		image.setParentId(task.getId());

		sync(image);
		
		adapter.addBitmap(b);
		photoGallery.setAdapter(adapter);
	}

	/**
	 * Syncs the image to the task
	 * @param image
	 */
	public void sync(final MyPhoto image) {
		class SyncTaskAsyncTask extends AsyncTask<String, Void, Long>{

			@Override
			protected void onPreExecute() {
			}

			@Override
			protected Long doInBackground(String... params) {

				DatabaseManager.getInstance().syncData(image);
				task.addPhoto(image);

				if (task.isLocal()) {
					TaskManager.getInstance().saveLocalData();
				}

				return (long) 1;
			}

			@Override
			protected void onProgressUpdate(Void... voids) {

			}

			@Override
			protected void onPostExecute(Long result) {
				super.onPostExecute(null);
			}        
		}

		SyncTaskAsyncTask syncTask = new SyncTaskAsyncTask();
		syncTask.execute(); 
	}

	/**
	 * Checks the returned activities for a captured image, then adds the image
	 * to the current task
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		if(requestCode == CAPTURE_IMAGE_REQUEST_CODE){
			Bitmap b = getBitmap(intent);

			ImageView i = (ImageView) findViewById(R.id.TaskImageView);
			i.setImageBitmap(b);

			addImageToTask(b);
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
