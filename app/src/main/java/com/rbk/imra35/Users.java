package com.rbk.imra35;

/**
 * Created by Linus on 29/05/18.
 */

public class Users
{

    public String fullname,status,profileimage;

    public Users()
    {


    }

    public Users(String fullname, String status, String profileimage)
    {
        this.fullname = fullname;
        this.status = status;
        this.profileimage = profileimage;
    }


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }
}
