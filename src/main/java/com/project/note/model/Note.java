package com.project.note.model;

import java.sql.Timestamp;

public class Note {

    private int userId;
    private int noteId;
    private Timestamp datecreated;
    private String content;
    private String title;

    public Note() {
    }

    public Note(Timestamp datecreated, String content, String title, int userId) {
        this.datecreated = datecreated;
        this.content = content;
        this.title = title;
        this.userId = userId;
    }

    public Timestamp getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(Timestamp datecreated) {
        this.datecreated = datecreated;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
}
