package ca.ualberta.cs.completemytask.userdata;

/**
 * An interface for content such as
 * comments, photos, and audio clips.
 * 
 * @author Michael Feist
 *
 * @param <T> Content Type
 */

public interface UserContent<T> {
	public T getContent();
	public void setContent(T content);
}
