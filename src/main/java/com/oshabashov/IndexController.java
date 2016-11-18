package com.oshabashov;

import com.oshabashov.model.Wallpaper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;

@Controller
public class IndexController {

  @RequestMapping(value = "/")
  public String getWallpapers(Model model) throws SQLException, ClassNotFoundException {
    model.addAttribute("isGuest", true);
    model.addAttribute("wallpapers", Wallpaper.findAll());
    return "index";
  }
}
