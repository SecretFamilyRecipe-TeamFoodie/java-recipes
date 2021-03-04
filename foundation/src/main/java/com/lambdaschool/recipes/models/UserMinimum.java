package com.lambdaschool.recipes.models;

public class UserMinimum {

    private String username;
    private String password;
    private String useremail;

    public UserMinimum() {
    }

    public UserMinimum(String username, String password, String useremail) {
        this.username = username;
        this.password = password;
        this.useremail = useremail;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }
}