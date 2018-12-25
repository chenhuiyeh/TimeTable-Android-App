package com.chenhuiyeh.module_cache_data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static com.chenhuiyeh.module_cache_data.model.InboxItem.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class InboxItem {
    static final String TABLE_NAME= "inbox_table";

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;

    public InboxItem(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    @Ignore
    public InboxItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
