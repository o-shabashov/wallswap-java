package com.oshabashov.java_wallswap.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Wallpaper {
  private Integer id;
  private String  thumb_url;
  private String  url;

  public Wallpaper(String thumb_url, String url) {
    this.thumb_url = thumb_url;
    this.url = url;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getThumb_url() {
    return thumb_url;
  }

  public void setThumb_url(String thumb_url) {
    this.thumb_url = thumb_url;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public static List<Wallpaper> findAll() throws ClassNotFoundException, SQLException {
    String myDriver = "org.gjt.mm.mysql.Driver";
    String myUrl    = "jdbc:mysql://localhost/wallswap?autoReconnect=true&useSSL=false";
    Class.forName(myDriver);
    Connection conn = DriverManager.getConnection(myUrl, "root", "root");

    String          query      = "SELECT thumb_url,url FROM wallpaper";
    Statement       st         = conn.createStatement();
    ResultSet       rs         = st.executeQuery(query);
    List<Wallpaper> wallpapers = new ArrayList<>();

    while (rs.next()) {
      wallpapers.add(new Wallpaper(rs.getString("thumb_url"), rs.getString("url")));
    }
    st.close();

    return wallpapers;
  }
}
