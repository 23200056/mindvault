package com.example.mindvault.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

// Flashcard.java
@Entity
public class Flashcard implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private long timestamp;

    private String state = "None";

    // Default constructor (required by Room)
    public Flashcard() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    // Getters and setters with proper return types
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    // Parcelable implementation
    protected Flashcard(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        timestamp = in.readLong();
        state = in.readString();
    }

    public static final Creator<Flashcard> CREATOR = new Creator<Flashcard>() {
        @Override
        public Flashcard createFromParcel(Parcel in) {
            return new Flashcard(in);
        }

        @Override
        public Flashcard[] newArray(int size) {
            return new Flashcard[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeLong(timestamp);
        dest.writeString(state);
    }
}