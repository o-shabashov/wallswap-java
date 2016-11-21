package com.oshabashov.java_wallswap.model;

public class User {
  public String email;
  public String access_token;
  public String auth_key;
  public String dropbox_id;

  public User(String email, String access_token, String auth_key, String dropbox_id) {
    this.email = email;
    this.access_token = access_token;
    this.auth_key = auth_key;
    this.dropbox_id = dropbox_id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAccess_token() {
    return access_token;
  }

  public void setAccess_token(String access_token) {
    this.access_token = access_token;
  }

  public String getAuth_key() {
    return auth_key;
  }

  public void setAuth_key(String auth_key) {
    this.auth_key = auth_key;
  }

  public String getDropbox_id() {
    return dropbox_id;
  }

  public void setDropbox_id(String dropbox_id) {
    this.dropbox_id = dropbox_id;
  }
}
