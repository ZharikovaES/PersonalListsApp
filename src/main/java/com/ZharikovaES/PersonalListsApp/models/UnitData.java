package com.ZharikovaES.PersonalListsApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

@MappedSuperclass
public class UnitData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "title")
    private String title;

    @JsonIgnore
    @Column(name = "user_id", insertable = false, updatable =false)
    private Long userId;

    @JsonIgnore
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dateUpdate;

    @JsonIgnore
    @Column(name = "date_update_tz")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dateUpdateTZ;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    protected User user;

    @JsonIgnore
    @Transient
    protected String dateUpdateTZStr;

    @JsonIgnore
    @Transient
    protected SimpleDateFormat formatForDate = new SimpleDateFormat("HH:mm:ss ' ' E dd.MM.yyyy");

    protected UnitData() {
        dateUpdate = new Date();
    }

    protected UnitData(User user, String title) {
        dateUpdate = new Date();
        dateUpdateTZ = Calendar.getInstance(TimeZone.getTimeZone(user.getTimezoneID())).getTime();
        this.user = user;
        this.title = title;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateUpdateTZ() {
        return dateUpdateTZ;
    }

    public void setDateUpdateTZ(Date dateUpdateTZ) {
        this.dateUpdateTZ = dateUpdateTZ;
    }

    public void setDateUpdateByUser() {
        this.dateUpdate = new Date();
        Calendar calendar = getNewCalendar();
        this.dateUpdateTZ = calendar.getTime();
    }
    public void setDateUpdateTZByUser() {
        Calendar calendar = getNewCalendar();
        this.dateUpdateTZ = calendar.getTime();
    }
    public Calendar getNewCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateUpdate);
        TimeZone timeZone = TimeZone.getTimeZone(user.getTimezoneID());
        calendar.setTimeZone(timeZone);
        return calendar;
    }

    @JsonGetter("date_update")
    public String dateToString() {
        dateUpdateTZStr = formatForDate.format(dateUpdateTZ);
        return dateUpdateTZStr;
    }

    public SimpleDateFormat getFormatForDate() {
        return formatForDate;
    }

    public void setFormatForDate(SimpleDateFormat formatForDate) {
        this.formatForDate = formatForDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateUpdateTZStr() {
        return dateUpdateTZStr;
    }

    public void setDateUpdateTZStr(String dateUpdateTZStr) {
        this.dateUpdateTZStr = dateUpdateTZStr;
    }

}
