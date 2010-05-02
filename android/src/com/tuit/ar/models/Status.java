package com.tuit.ar.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.tuit.ar.databases.Model;

public class Status extends Model {
	private static final String[] columns = new String[]{
			"date", "favorited", "id", "in_reply_to_screen_name", "in_reply_to_status_id", "in_reply_to_user_id", "message", "source", "user_id", "is_home", "is_reply", "belongs_to_user"
		};
	private JSONObject dataSourceJSON;
	private Date createDate;
	private long dateMillis = 0;
	private boolean favorited = false;
	private long id = 0;
	private String in_reply_to_screen_name;
	private long in_reply_to_status_id = 0;
	private long in_reply_to_user_id = 0;
	private String message;
	private String source;
	private long user_id = 0;
	private User user;
	private boolean is_home;
	private boolean is_reply;
	private long belongs_to_user;

	public Status(Cursor query) {
		super();
		setDateMillis(query.getLong(0) * 1000);
		setFavorited(query.getInt(1) == 1);
		setId(query.getLong(2));
		setInReplyToScreenName(query.getString(3));
		setInReplyToStatusId(query.getLong(4));
		setInReplyToUserId(query.getLong(5));
		setMessage(query.getString(6));
		setSource(query.getString(7));
		setUserId(query.getLong(8));
		setHome(query.getInt(9) == 1);
		setReply(query.getInt(10) == 1);
		setBelongsToUser(query.getLong(11));
	}

	public Status(JSONObject object) {
		super();
		this.dataSourceJSON = object;
	}

	public User getUser() {
		if (user != null) return user;
		if (dataSourceJSON != null) { 
			try {
				return user = new User(dataSourceJSON.getJSONObject("user"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		ArrayList<User> searchUser = User.select("id = ?", new String[] { String.valueOf(getUserId()) }, null, null, null, "1");
		if (searchUser.size() > 0) return user = searchUser.get(0);
		return null;
	}

	public boolean isFavorited() {
		if (favorited != false) return favorited;
		try {
			return favorited = dataSourceJSON.getBoolean("text");
		} catch (Exception e) {
			return false;
		}
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public String getInReplyToScreenName() {
		if (in_reply_to_screen_name != null) return in_reply_to_screen_name;
		try {
			return in_reply_to_screen_name = dataSourceJSON.getString("in_reply_to_screen_name");
		} catch (Exception e) {
			return null;
		}
	}

	public void setInReplyToScreenName(String inReplyToScreenName) {
		in_reply_to_screen_name = inReplyToScreenName;
	}

	public long getInReplyToStatusId() {
		if (in_reply_to_status_id != 0) return in_reply_to_status_id;
		try {
			return in_reply_to_status_id = dataSourceJSON.getLong("in_reply_to_status_id");
		} catch (Exception e) {
			return 0;
		}
	}

	public void setInReplyToStatusId(long inReplyToStatusId) {
		in_reply_to_status_id = inReplyToStatusId;
	}

	public long getInReplyToUserId() {
		if (in_reply_to_user_id != 0) return in_reply_to_user_id;
		try {
			return in_reply_to_user_id = dataSourceJSON.getLong("in_reply_to_user_id");
		} catch (Exception e) {
			return 0;
		}
	}

	public void setInReplyToUserId(long inReplyToUserId) {
		in_reply_to_user_id = inReplyToUserId;
	}

	public String getSource() {
		if (source != null) return source;
		try {
			return source = dataSourceJSON.getString("source");
		} catch (Exception e) {
			return null;
		}
	}

	public void setSource(String source) {
		this.source = source;
	}

	public long getUserId() {
		if (user_id != 0) return user_id;
		try {
			return user_id = dataSourceJSON.getJSONObject("user").getLong("id");
		} catch (Exception e) {
			return 0;
		}
	}

	public void setUserId(long userId) {
		user_id = userId;
	}

	public long getId() {
		if (id != 0) return id;
		try {
			return id = dataSourceJSON.getLong("id");
		} catch (Exception e) {
			return 0;
		}
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessage() {
		if (message != null) return message;
		try {
			return message = dataSourceJSON.getString("text");
		} catch (Exception e) {
			return null;
		}
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isHome() {
		return is_home;
	}

	public void setHome(boolean isHome) {
		is_home = isHome;
	}

	public boolean isReply() {
		return is_reply;
	}

	public void setReply(boolean isReply) {
		is_reply = isReply;
	}

	public long getBelongsToUser() {
		return belongs_to_user;
	}

	public void setBelongsToUser(long belongsToUser) {
		belongs_to_user = belongsToUser;
	}

	@SuppressWarnings("unchecked")
	static public ArrayList<Status> select(String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
		return (ArrayList<Status>)Model.select(Status.class, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}

	public String getUsername() {
		try {
			return getUser().getScreenName();
		} catch (Exception e) {
			return null;
		}
	}

	public Date getDate() {
		try {
			if (createDate != null) return createDate;
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d hh:mm:ss Z yyyy");
			return createDate = sdf.parse(dataSourceJSON.getString("created_at"));
		} catch (Exception e) {
			return null;
		}
	}

	public void setDateMillis(long dateMillis) {
		this.dateMillis = dateMillis;
	}

	public long getDateMillis() {
		if (dateMillis == 0) {
			Date date = this.getDate();
			if (date == null) return (long)0;
			dateMillis = date.getTime();
		}
		return dateMillis;
	}

	static public String calculateElapsed(long millis) {
		    int ageInSeconds = (int)((System.currentTimeMillis() - millis) / 1000);
		    
		    if (ageInSeconds < 0) {
		        return "just now";
		    }
		    if (ageInSeconds < 60) {
		        int n = ageInSeconds;
		        return "about " + n + " second" + (n > 1 ? "s" : "") + " ago";
		    }
		    if (ageInSeconds < 60 * 60) {
		        int n = (int) Math.floor(ageInSeconds/60);
		        return "about " + n + " minute" + (n > 1 ? "s" : "") + " ago";
		    }
		    if (ageInSeconds < 60 * 60 * 24) {
		        int n = (int) Math.floor(ageInSeconds/60/60);
		        return "about " + n + " hour" + (n > 1 ? "s" : "") + " ago";
		    }
		    if (ageInSeconds < 60 * 60 * 24 * 7) {
		        int n = (int) Math.floor(ageInSeconds/60/60/24);
		        return "about " + n + " day" + (n > 1 ? "s" : "") + " ago";
		    }
		    if (ageInSeconds < 60 * 60 * 24 * 31) {
		        int n = (int) Math.floor(ageInSeconds/60/60/24/7);
		        return "about " + n + " week" + (n > 1 ? "s" : "") + " ago";
		    }
		    if (ageInSeconds < 60 * 60 * 24 * 365) {
		        int n = (int) Math.floor(ageInSeconds/60/60/24/31);
		        return "about " + n + " month" + (n > 1 ? "s" : "") + " ago";
		    }
		    int n = (int)Math.floor(ageInSeconds/60/60/24/365);
		    return n + " year" + (n > 1 ? "s" : "") + " ago";
	}

	@Override
	public long replace() {
		try {
			getUser().replace();
		} catch (SQLiteException e) {}
		return super.replace();
	}

	@Override
	protected ContentValues getValues() {
		ContentValues fields = new ContentValues();
		fields.put(columns[0], getDateMillis());
		fields.put(columns[1], isFavorited());
		fields.put(columns[2], getId());
		fields.put(columns[3], getInReplyToScreenName());
		fields.put(columns[4], getInReplyToStatusId());
		fields.put(columns[5], getInReplyToUserId());
		fields.put(columns[6], getMessage());
		fields.put(columns[7], getSource());
		fields.put(columns[8], getUserId());
		return fields;
	}
}
