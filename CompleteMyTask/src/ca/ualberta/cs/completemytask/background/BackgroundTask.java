package ca.ualberta.cs.completemytask.background;

import android.os.AsyncTask;

public class BackgroundTask {
	
	public BackgroundTask() {
		
	}
	
	public void runInBackGround(final HandleInBackground data) {
		class BackgroundAsyncTask extends AsyncTask<String, Long, Long>{
    		
    		@Override
    		protected void onPreExecute() {
    			data.onPreExecute();
    		}
    		
	        @Override
	        protected Long doInBackground(String... params) {
	        	while (!data.handleInBackground(null)) {
	        		 publishProgress();
	        	}
	        	return (long) 0;
			}
	        
	        @Override
	        protected void onProgressUpdate(Long ...longs) {
	        	int result = 0;
	        	if (longs.length > 0) {
	        		result = longs[0].intValue();
	        	}
	        	
	        	data.onUpdate(result);
	        }
	        
	        @Override
	        protected void onPostExecute(Long result) {
	            super.onPostExecute(null);
	            
	            data.onPostExecute(result.intValue());
	        }        
		}
		
		BackgroundAsyncTask run = new BackgroundAsyncTask();
		run.execute(); 
	}

}
