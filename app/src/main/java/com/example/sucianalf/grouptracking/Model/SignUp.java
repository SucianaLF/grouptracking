package com.example.sucianalf.grouptracking.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class SignUp {

    private String username;
    private String email;
    private String noTelp;
    private String alamat;
    private String password;

    public SignUp() {

    }

    public SignUp(String username, String email, String noTelp, String alamat, String password){
        this.username = username;
        this.email = email;
        this.noTelp = noTelp;
        this.alamat = alamat;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
