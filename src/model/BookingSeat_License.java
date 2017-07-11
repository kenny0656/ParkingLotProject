package model;

import java.util.Date;

/**
 * Created by admin on 2017/7/2.
 */
public class BookingSeat_License {
    private int bookingSeatId;
    private int seatId;
    private int licenseId;
    private Date startTime;
    private Date endTime;
    private String userName;
    private String licenseNumber;

    public int getBookingSeatId() {
        return bookingSeatId;
    }

    public void setBookingSeatId(int bookingSeatId) {
        this.bookingSeatId = bookingSeatId;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(int licenseId) {
        this.licenseId = licenseId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
}
