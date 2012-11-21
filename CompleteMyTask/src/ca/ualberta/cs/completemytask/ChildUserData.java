package ca.ualberta.cs.completemytask;

public class ChildUserData extends UserData {
	protected long parentID;
	protected long localParentId;
	
	ChildUserData() {
		super();
		this.localParentId = 0;
	}
	
	/**
	 * Gets the id of the task.
	 * @return id
	 */
	public long getParentId() {
		return this.parentID;
	}
	
	/**
	 * Set the id of the task.
	 * @param id
	 */
	public void setParentId(long id) {
		this.parentID = id;
	}
	
	public long getLocalParentId() {
		return this.localParentId;
	}
	
	public void setLocalParentId(long id) {
		this.localParentId = id;
	}

}
