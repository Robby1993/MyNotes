package com.robinson.notewithreminder.model;

public class NoteItemModel {
    public long ID;
    public String DateTime;
    public String Title;
    public String Description;
    public String Time;
    public String Date;

    public NoteItemModel(long ID, String DateTime, String Title, String Description, String Time, String Date) {
        this.ID = ID;
        this.DateTime = DateTime;
        this.Title = Title;
        this.Description = Description;
        this.Time = Time;
        this.Date = Date;
    }
}
