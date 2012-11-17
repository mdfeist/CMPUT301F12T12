package ca.ualberta.cs.completemytask;

@SuppressWarnings("serial")
public class ChildUserData extends UserData {
	protected String parentID;
	
	ChildUserData() {
		super();
		this.parentID = null;
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

	@Override
	public String toJSON() {
		return null;
	}
}
