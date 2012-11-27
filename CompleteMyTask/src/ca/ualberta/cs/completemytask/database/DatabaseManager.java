package ca.ualberta.cs.completemytask.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.ualberta.cs.completemytask.saving.LocalSaving;
import ca.ualberta.cs.completemytask.userdata.Comment;
import ca.ualberta.cs.completemytask.userdata.MyPhoto;
import ca.ualberta.cs.completemytask.userdata.Task;
import ca.ualberta.cs.completemytask.userdata.TaskManager;
import ca.ualberta.cs.completemytask.userdata.User;

import android.util.Log;

/**
 * A singleton to handle all the database oppertations.
 * 
 * @author Michael Feist
 * 
 */
public class DatabaseManager {

	// For testing Don't use ///////////////////////
	public static boolean testSyncComplete = false;
	// /////////////////////////////////////////////

	private static final String TAG = "DatabaseManager";
	//private static final String URL = "http://10.0.2.2:8888/TaskServer/";
	private static final String URL = "http://cmput301t12.net78.net/";
	private static DatabaseManager instance = null;

	private static String login_tag = "login";
	private static String register_tag = "register";
	private static String update_email_tag = "update_email";
	private static String list_tasks_attachments_tag = "list_tasks_attachments";
	private static String list_tasks_tag = "list_tasks";
	private static String sync_task_tag = "sync_task";
	private static String list_comments_tag = "list_comments";
	private static String sync_comment_tag = "sync_comment";
	private static String list_photos_tag = "list_photos";
	private static String sync_photo_tag = "sync_photo";
	private static String complete_task_tag = "complete_task";
	private static String list_notifications_tag = "list_notifications";
	private static String delete_notification_tag = "delete_notification";

	public static final String KEY_SUCCESS = "success";
	public static final String KEY_ERROR = "error";
	public static final String KEY_ERROR_MSG = "error_msg";

	private static String LAST_DATE = "";

	private JSONParser jsonParser;

	private Map<Long, Task> tasks;
	private Map<Long, Comment> comments;
	private Map<Long, MyPhoto> photos;

	protected DatabaseManager() {
		this.jsonParser = new JSONParser();
		this.tasks = new HashMap<Long, Task>();
		this.comments = new HashMap<Long, Comment>();
		this.photos = new HashMap<Long, MyPhoto>();
	}

	/**
	 * Returns the singleton's instance.
	 * @return instances
	 */
	public static DatabaseManager getInstance() {
		if (instance == null) {
			instance = new DatabaseManager();
		}
		return instance;
	}

	public JSONObject createUser(String username, String email, String password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", register_tag));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(URL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	public JSONObject login(String username, String password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", login_tag));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(URL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	public void updateEmail(String username, String email) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", update_email_tag));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("email", email));
		jsonParser.getJSONFromUrl(URL, params);
		// return json
		// Log.e("JSON", json.toString());
	}
	
	public void completeTask(long taskid, String from, String to, String message) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", complete_task_tag));
		params.add(new BasicNameValuePair("taskid", String.valueOf(taskid)));
		params.add(new BasicNameValuePair("from", from));
		params.add(new BasicNameValuePair("to", to));
		params.add(new BasicNameValuePair("message", message));
		jsonParser.getJSONFromUrl(URL, params);
	}
	
	public JSONObject getNotifications(String username) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", list_notifications_tag));
		params.add(new BasicNameValuePair("username", username));
		JSONObject json = jsonParser.getJSONFromUrl(URL, params);
		// return json
		//Log.e("JSON", json.toString());
		return json;
	}
	
	public void deleteNotifications(String id) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", delete_notification_tag));
		params.add(new BasicNameValuePair("id", id));
		jsonParser.getJSONFromUrl(URL, params);
	}
	
	public void setNumberOfAttachments(Task task) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", list_tasks_attachments_tag));
		params.add(new BasicNameValuePair("taskid",
				String.valueOf(task.getId())));
		JSONObject json = jsonParser.getJSONFromUrl(URL, params);

		Log.v("JSON", json.toString());

		// check for response
		try {
			if (json.getString(DatabaseManager.KEY_SUCCESS) != null) {
				String res = json.getString(DatabaseManager.KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {
					int num_comments = json.getInt("comments");
					int num_photos = json.getInt("photos");
					int num_audios = json.getInt("audios");
					
					task.setNumberOfAttachments(num_comments, num_photos, num_audios);
					
				} else {

					if (json.getString(DatabaseManager.KEY_ERROR) != null) {
						String res_error = json
								.getString(DatabaseManager.KEY_ERROR);
						if (Integer.parseInt(res_error) == 1) {
							String error = json
									.getString(DatabaseManager.KEY_ERROR_MSG);
							Log.e(TAG, error);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public boolean getTasks(int limit) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", list_tasks_tag));
		params.add(new BasicNameValuePair("limit", String.valueOf(limit)));
		params.add(new BasicNameValuePair("date", LAST_DATE));
		JSONObject json = jsonParser.getJSONFromUrl(URL, params);

		Log.v("JSON", json.toString());

		boolean finished = false;

		// check for response
		try {
			if (json.getString(DatabaseManager.KEY_SUCCESS) != null) {
				String res = json.getString(DatabaseManager.KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {
					JSONArray tasksArray = json.getJSONArray("tasks");

					if (tasksArray.length() < limit) {
						finished = true;
					}

					for (int i = 0; i < tasksArray.length(); i++) {
						JSONObject task = tasksArray.getJSONObject(i);

						int id = task.getInt("id");
						String date_created = task
								.getString("date_created");
						
						if (date_created.compareTo(LAST_DATE) > 0) {
							LAST_DATE = date_created;
						}

						if (!this.tasks.containsKey(Long.valueOf((long) id))) {
							String username = task.getString("username");
							String name = task.getString("name");
							String description = task.getString("description");

							boolean completed = (task.getInt("complete") == 1);
							boolean comment = (task.getInt("comment") == 1);
							boolean photo = (task.getInt("photo") == 1);
							boolean audio = (task.getInt("audio") == 1);

							
							String date_completed = task
									.getString("date_completed");
							
							User user = new User(username);

							Task t = new Task();
							t.setUser(user);
							t.setId(id);
							t.setName(name);
							t.setDescription(description);
							t.setComplete(completed);
							t.setRequirements(comment, photo, audio);
							t.setDate(date_created);
							t.setPublic(true);
							t.setLocal(false);

							this.tasks.put(Long.valueOf(t.getId()), t);
						}
					}
				} else {

					if (json.getString(DatabaseManager.KEY_ERROR) != null) {
						String res_error = json
								.getString(DatabaseManager.KEY_ERROR);
						if (Integer.parseInt(res_error) == 1) {
							String error = json
									.getString(DatabaseManager.KEY_ERROR_MSG);
							Log.e(TAG, error);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return finished;
	}

	public void getComments(Task task) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", list_comments_tag));
		params.add(new BasicNameValuePair("taskid",
				String.valueOf(task.getId())));
		JSONObject json = jsonParser.getJSONFromUrl(URL, params);

		Log.v("JSON", json.toString());

		// check for response
		try {
			if (json.getString(DatabaseManager.KEY_SUCCESS) != null) {
				String res = json.getString(DatabaseManager.KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {
					JSONArray commentsArray = json.getJSONArray("comments");

					for (int i = 0; i < commentsArray.length(); i++) {
						JSONObject comment = commentsArray.getJSONObject(i);

						int id = comment.getInt("id");

						if (!this.comments.containsKey(Long.valueOf((long) id))) {
							String username = comment.getString("username");
							String content = comment.getString("comment");
							String date_created = comment
									.getString("date_created");

							User user = new User(username);

							Comment c = new Comment();
							c.setUser(user);
							c.setId(id);
							c.setParentId(task.getId());
							c.setContent(content);
							c.setDate(date_created);

							task.addComment(c);

							this.comments.put(Long.valueOf(c.getId()), c);
						}
					}
				} else {

					if (json.getString(DatabaseManager.KEY_ERROR) != null) {
						String res_error = json
								.getString(DatabaseManager.KEY_ERROR);
						if (Integer.parseInt(res_error) == 1) {
							String error = json
									.getString(DatabaseManager.KEY_ERROR_MSG);
							Log.e(TAG, error);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void getPhotos(Task task) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", list_photos_tag));
		params.add(new BasicNameValuePair("taskid",
				String.valueOf(task.getId())));
		JSONObject json = jsonParser.getJSONFromUrl(URL, params);

		Log.v("JSON", json.toString());

		// check for response
		try {
			if (json.getString(DatabaseManager.KEY_SUCCESS) != null) {
				String res = json.getString(DatabaseManager.KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {
					JSONArray photosArray = json.getJSONArray("photos");

					for (int i = 0; i < photosArray.length(); i++) {
						JSONObject photo = photosArray.getJSONObject(i);

						int id = photo.getInt("id");

						if (!this.photos.containsKey(Long.valueOf((long) id))) {
							String username = photo.getString("username");
							String content = photo.getString("photo");
							String date_created = photo
									.getString("date_created");

							User user = new User(username);

							MyPhoto p = new MyPhoto();
							p.setUser(user);
							p.setId(id);
							p.setParentId(task.getId());
							p.setImageFromString(content);
							p.setDate(date_created);

							task.addPhoto(p);

							this.photos.put(Long.valueOf(p.getId()), p);
						}
					}
				} else {

					if (json.getString(DatabaseManager.KEY_ERROR) != null) {
						String res_error = json
								.getString(DatabaseManager.KEY_ERROR);
						if (Integer.parseInt(res_error) == 1) {
							String error = json
									.getString(DatabaseManager.KEY_ERROR_MSG);
							Log.e(TAG, error);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void syncTask(Task task) {

		String username = "";

		User user = task.getUser();
		if (user != null) {
			username = user.getUserName();
		}

		long u_id = task.getId();

		String name = task.getName();
		String description = task.getDescription();

		int complete = task.isComplete() ? 1 : 0;
		int comment = task.needsComment() ? 1 : 0;
		int photo = task.needsPhoto() ? 1 : 0;
		int audio = task.needsAudio() ? 1 : 0;

		String date_created = task.getDate();
		String date_completed = "";

		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", sync_task_tag));
		params.add(new BasicNameValuePair("id", String.valueOf(u_id)));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("description", description));
		params.add(new BasicNameValuePair("complete", String.valueOf(complete)));
		params.add(new BasicNameValuePair("comment", String.valueOf(comment)));
		params.add(new BasicNameValuePair("photo", String.valueOf(photo)));
		params.add(new BasicNameValuePair("audio", String.valueOf(audio)));
		params.add(new BasicNameValuePair("date_created", date_created));
		params.add(new BasicNameValuePair("date_completed", date_completed));

		JSONObject json = jsonParser.getJSONFromUrl(URL, params);

		Log.v("JSON", json.toString());

		// check for response
		try {
			if (json.getString(DatabaseManager.KEY_SUCCESS) != null) {
				String res = json.getString(DatabaseManager.KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {
					int id = json.getInt("id");
					task.setId((long) id);

					LocalSaving saver = new LocalSaving();
					saver.open();
					saver.saveTask(task);
					saver.close();

					this.tasks.put(Long.valueOf(task.getId()), task);

				} else {

					if (json.getString(DatabaseManager.KEY_ERROR) != null) {
						String res_error = json
								.getString(DatabaseManager.KEY_ERROR);
						if (Integer.parseInt(res_error) == 1) {
							String error = json
									.getString(DatabaseManager.KEY_ERROR_MSG);
							Log.e(TAG, error);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void syncComment(Comment comment) {

		if (comment.getId() == 0) {
			String username = "";

			User user = comment.getUser();
			if (user != null) {
				username = user.getUserName();
			}

			long taskid = comment.getParentId();

			String content = comment.getContent();

			String date_created = comment.getDate();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("action", sync_comment_tag));
			params.add(new BasicNameValuePair("taskid", String.valueOf(taskid)));
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("content", content));
			params.add(new BasicNameValuePair("date_created", date_created));

			JSONObject json = jsonParser.getJSONFromUrl(URL, params);

			Log.v("JSON", json.toString());

			// check for response
			try {
				if (json.getString(DatabaseManager.KEY_SUCCESS) != null) {
					String res = json.getString(DatabaseManager.KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						int id = json.getInt("id");
						comment.setId((long) id);

						LocalSaving saver = new LocalSaving();
						saver.open();
						saver.saveComment(comment);
						saver.close();

						this.comments.put(Long.valueOf(comment.getId()),
								comment);

					} else {

						if (json.getString(DatabaseManager.KEY_ERROR) != null) {
							String res_error = json
									.getString(DatabaseManager.KEY_ERROR);
							if (Integer.parseInt(res_error) == 1) {
								String error = json
										.getString(DatabaseManager.KEY_ERROR_MSG);
								Log.e(TAG, error);
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void syncPhoto(MyPhoto photo) {

		if (photo.getId() == 0) {
			String username = "";

			User user = photo.getUser();
			if (user != null) {
				username = user.getUserName();
			}

			long taskid = photo.getParentId();

			String content = photo.getImageAsString();

			String date_created = photo.getDate();;

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("action", sync_photo_tag));
			params.add(new BasicNameValuePair("taskid", String.valueOf(taskid)));
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("content", content));
			params.add(new BasicNameValuePair("date_created", date_created));

			JSONObject json = jsonParser.getJSONFromUrl(URL, params);

			Log.v("JSON", json.toString());

			// check for response
			try {
				if (json.getString(DatabaseManager.KEY_SUCCESS) != null) {
					String res = json.getString(DatabaseManager.KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						int id = json.getInt("id");
						photo.setId((long) id);

						LocalSaving saver = new LocalSaving();
						saver.open();
						saver.savePhoto(photo);
						saver.close();

						this.photos.put(Long.valueOf(photo.getId()), photo);

					} else {

						if (json.getString(DatabaseManager.KEY_ERROR) != null) {
							String res_error = json
									.getString(DatabaseManager.KEY_ERROR);
							if (Integer.parseInt(res_error) == 1) {
								String error = json
										.getString(DatabaseManager.KEY_ERROR_MSG);
								Log.e(TAG, error);
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Syncs a task based on it's location in the TaskManager.
	 * 
	 * @param Position
	 *            of task in TaskManager
	 */
	public void syncTaskToDatabase(int position) {
		Task task = TaskManager.getInstance().getTaskAt(position);
		syncTaskToDatabase(task);
	}

	/**
	 * Syncs the given task with the database.
	 * 
	 * @param A
	 *            task
	 */
	public void syncTaskToDatabase(Task task) {

		Log.v(TAG, "Syncing: " + task.getName());
		syncTask(task);

		Log.v(TAG, "Comments: " + task.getNumberOfComments());
		// Sync Comments
		for (int i = 0; i < task.getNumberOfComments(); i++) {
			Comment comment = task.getCommentAt(i);
			comment.setParentId(task.getId());
			syncComment(comment);

		}

		Log.v(TAG, "Photos: " + task.getNumberOfPhotos());
		// Sync Photos
		for (int i = 0; i < task.getNumberOfPhotos(); i++) {
			MyPhoto photo = task.getPhotoAt(i);
			photo.setParentId(task.getId());

		}

	}

	/**
	 * Sync all tasks with the database.
	 */
	public boolean syncDatabase() {
		testSyncComplete = false;

		boolean finished = this.getTasks(1);

		for (Task t : TaskManager.getInstance().getTaskArray()) {
			if (t.isPublic()) {
				this.tasks.put(Long.valueOf(t.getId()), t);
			}
		}

		for (Task t : this.tasks.values()) {
			Log.v(TAG, "Task: " + t.getName());
			TaskManager.getInstance().addTask(t);
		}

		testSyncComplete = true;

		return finished;
	}
}
