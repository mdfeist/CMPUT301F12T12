package ca.ualberta.cs.completemytask;

public interface HandleInBackground {
	public void onPreExecute();
	public void onPostExecute();
	public int handleInBackground(Object o);
}
