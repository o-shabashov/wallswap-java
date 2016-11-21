package com.oshabashov.java_wallswap;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.util.LangUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;


public class Common {
  public final PrintWriter log;
  public final DbxAppInfo  dbxAppInfo;

  public Common(PrintWriter log, DbxAppInfo dbxAppInfo) {
    this.log = log;
    this.dbxAppInfo = dbxAppInfo;
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

}
