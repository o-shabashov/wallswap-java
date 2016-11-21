package com.oshabashov.java_wallswap;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.util.LangUtil;
import com.dropbox.core.v2.users.FullAccount;
import com.oshabashov.java_wallswap.model.User;
import com.oshabashov.java_wallswap.model.Wallpaper;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Common {
  public final PrintWriter log;
  public final DbxAppInfo  dbxAppInfo;
  public final Connection  connection;

  public Common(PrintWriter log, DbxAppInfo dbxAppInfo) throws ClassNotFoundException, SQLException {
    String myDriver = "org.gjt.mm.mysql.Driver";
    String myUrl    = "jdbc:mysql://localhost/wallswap?autoReconnect=true&useSSL=false";
    Class.forName(myDriver);
    this.connection = DriverManager.getConnection(myUrl, "root", "root");
    this.log = log;
    this.dbxAppInfo = dbxAppInfo;
  }

  public List<Wallpaper> getAllWallpapers() throws ClassNotFoundException, SQLException {
    String          query      = "SELECT thumb_url,url FROM `wallpaper`";
    Statement       st         = connection.createStatement();
    ResultSet       rs         = st.executeQuery(query);
    List<Wallpaper> wallpapers = new ArrayList<>();

    while (rs.next()) {
      wallpapers.add(new Wallpaper(rs.getString("thumb_url"), rs.getString("url")));
    }
    st.close();

    return wallpapers;
  }

  public DbxRequestConfig getRequestConfig(HttpServletRequest request) {
    return DbxRequestConfig.newBuilder("Wallswap/0.1")
      .withUserLocaleFrom(request.getLocale())
      .build();
  }

  public String getUrl(HttpServletRequest request, String path) {
    URL requestUrl;
    try {
      requestUrl = new URL(request.getRequestURL().toString());
      return new URL(requestUrl, path).toExternalForm();
    } catch (MalformedURLException ex) {
      throw LangUtil.mkAssert("Bad URL", ex);
    }
  }

  public User createUserByDropbox(FullAccount account, String accessToken) throws SQLException {
    String            query        = "SELECT id FROM user WHERE dropbox_id = ?";
    PreparedStatement preparedStmt = connection.prepareStatement(query);
    preparedStmt.setString(1, account.getAccountId());
    ResultSet rs    = preparedStmt.executeQuery();
    int       count = 0;

    while (rs.next()) {
      count = rs.getInt(1);
    }
    preparedStmt.close();

    if (count == 0) {
      query = " INSERT INTO user (email, access_token, auth_key, dropbox_id) VALUES (?, ?, ?, ?)";
      preparedStmt = connection.prepareStatement(query);
      preparedStmt.setString(1, account.getEmail());
      preparedStmt.setString(2, accessToken);
      preparedStmt.setString(3, UUID.randomUUID().toString());
      preparedStmt.setString(4, account.getAccountId());
      preparedStmt.execute();

      connection.close();
    }

    // TODO replace by Hibernate ORM
    return new User(account.getEmail(), accessToken, UUID.randomUUID().toString(), account.getAccountId());
  }

}
