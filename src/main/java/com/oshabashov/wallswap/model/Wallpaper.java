package com.oshabashov.wallswap.model;

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

}
