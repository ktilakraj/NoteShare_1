package com.tilak.datamodels;

import org.json.JSONObject;


public class SideMenuitems {
	
    private String menuid;
    private String menuName;
    private String menuNameDetail;
    public int resourceId;
    public String CreatedTime,ModifiedTime,ReminderTime,TimeBomb;
    public String colours;
    public boolean isdefaultNote;
    
    
    
	public boolean isIsdefaultNote() {
		return isdefaultNote;
	}

	public void setIsdefaultNote(boolean isdefaultNote) {
		this.isdefaultNote = isdefaultNote;
	}

	public String getCreatedTime() {
		return CreatedTime;
	}

	public String getColours() {
		return colours;
	}

	public void setColours(String colours) {
		this.colours = colours;
	}

	public void setCreatedTime(String createdTime) {
		CreatedTime = createdTime;
	}

	public String getModifiedTime() {
		return ModifiedTime;
	}

	public void setModifiedTime(String modifiedTime) {
		ModifiedTime = modifiedTime;
	}

	public String getReminderTime() {
		return ReminderTime;
	}

	public void setReminderTime(String reminderTime) {
		ReminderTime = reminderTime;
	}

	public String getTimeBomb() {
		return TimeBomb;
	}

	public void setTimeBomb(String timeBomb) {
		TimeBomb = timeBomb;
	}

	public int getResourceId() {
		return resourceId;
	}

	public String getMenuNameDetail() {
		return menuNameDetail;
	}

	public void setMenuNameDetail(String menuNameDetail) {
		this.menuNameDetail = menuNameDetail;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public SideMenuitems () {
		
	}	
        
    public SideMenuitems (JSONObject json) {
    
        this.menuid = json.optString("menuid");
        this.menuName = json.optString("menuName");
        //this.resourceId=json.optInt("resourceid");

    }
    
    public String getMenuid() {
        return this.menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }


    
}
