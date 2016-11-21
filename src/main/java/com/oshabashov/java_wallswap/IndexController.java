package com.oshabashov.java_wallswap;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxException;
import com.dropbox.core.json.JsonReader;
import com.oshabashov.java_wallswap.model.Wallpaper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.dropbox.core.util.StringUtil.jq;


@Controller
public class IndexController {
  private final Common      common;
  private final DropboxAuth dropboxAuth;
  private List<Wallpaper> allWallpapers = new ArrayList<>();

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private HttpServletResponse response;

  private IndexController() throws SQLException, ClassNotFoundException {
    String     argAppInfo = "app.json";
    DbxAppInfo dbxAppInfo = null; // TODO dirty hack, can`t imagine something smarter

    // Read app info file (contains app key and app secret)
    try {
      dbxAppInfo = DbxAppInfo.Reader.readFromFile(argAppInfo);
    } catch (JsonReader.FileLoadException ex) {
      System.err.println("Error loading " + jq(argAppInfo) + " : " + ex.getMessage());
    }
    System.out.println("Loaded app info from " + jq(argAppInfo));

    PrintWriter log = new PrintWriter(System.out, true);

    this.common = new Common(log, dbxAppInfo);
    this.dropboxAuth = new DropboxAuth(common);
  }

  @RequestMapping(value = "/")
  public String index(Model model) throws SQLException, ClassNotFoundException {
    if (this.allWallpapers.isEmpty()) {
      this.allWallpapers = common.getAllWallpapers();
    }
    System.out.println(request.getSession().getAttribute("isAuthenticated"));
    model.addAttribute("wallpapers", this.allWallpapers);
    model.addAttribute("isAuthenticated", request.getSession().getAttribute("isAuthenticated"));
    return "index";
  }

  @RequestMapping(value = "/login")
  public String login() {
    return "redirect:" + dropboxAuth.doStart(request);
  }

  @RequestMapping(value = "/oauth2callback")
  public String oauth2callback() throws IOException, ServletException, DbxException {
    dropboxAuth.doFinish(request, response);
    request.getSession().setAttribute("isAuthenticated", true);

    return "redirect:/";
  }
}
