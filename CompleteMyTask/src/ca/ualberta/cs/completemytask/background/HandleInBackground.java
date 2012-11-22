package ca.ualberta.cs.completemytask.background;

public interface HandleInBackground {
	public void onPreExecute();
	public void onPostExecute(int result);
	public void onUpdate(int result);
	public boolean handleInBackground(Object o);
}
