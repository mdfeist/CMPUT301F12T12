package ca.ualberta.cs.completemytask.background;

import android.os.AsyncTask;

public class BackgroundTask {
	
	public BackgroundTask() {
		
	}
	
	public void runInBackGround(final HandleInBackground data) {
		class BackgroundAsyncTask extends AsyncTask<String, Void, Long>{
    		
    		@Override
    		protected void onPreExecute() {
    			data.onPreExecute();
    		}
    		
	        @Override
	        protected Long doInBackground(String... params) {
	        	return (long) data.handleInBackground(null);
			}
	        
	        @Override
	        protected void onProgressUpdate(Void... voids) {
	        	
	        }
	        
	        @Override
	        protected void onPostExecute(Long result) {
	            super.onPostExecute(null);
	            
	            data.onPostExecute();
	        }        
		}
		
		BackgroundAsyncTask run = new BackgroundAsyncTask();
		run.execute(); 
	}

}
