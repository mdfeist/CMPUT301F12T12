package ca.ualberta.cs.completemytask;

@SuppressWarnings("serial")
public class ChildUserData extends UserData {
	protected String parentID;
	protected long localParentId;
	
	ChildUserData() {
		super();
		this.parentID = null;
		this.localParentId = 0;
	}
	
	/**
	 * Gets the id of the task.
	 * @return id
	 */
	public String getParentId() {
		return this.parentID;
	}
	
	/**
	 * Set the id of the task.
	 * @param id
	 */
	public void setParentId(String id) {
		this.parentID = id;
	}
	
	public long getLocalParentId() {
		return this.localParentId;
	}
	
	public void setLocalParentId(long id) {
		this.localParentId = id;
	}

	@Override
	public String toJSON() {
		return null;
	}
}
