# Wallswap-Java

The project is just for fun and test programming skills. It consists of two parts - the server and crawler.

### Server:
1. Runs on 8080 port;
2. Gets the list of wallpapers from database;
3. Render the html page with the list of wallpaper;
4. Allows you to authenticate the user through OAuth2 Dropbox, gets access_token and stores the user in a database;

### Crawler:
1. Parses website [https://wallhaven.cc](https://wallhaven.cc) and collects direct URL's at the wallpaper;
2. Saves list of wallpapers in the database;
3. Upload wallpapers for each user in Dropbox directory.

## Installation
1. Create MySQL database `wallswap` and import `wallswap.sql`
2. Create [Dropbox App](https://www.dropbox.com/developers/apps/create) and fill `app.json`
```json
{
  "key": "APP_KEY_HERE",
  "secret": "APP_SECRET_HERE"
}
```

3. Redirect URL for Dropbox callback:
```
http://localhost:8080/oauth2callback
```

4. Install gradle dependencies:
```bash
cd wallswap-java
gradle build
```

5. Run server:
```bash
gradle bootRun
```

6. Run crawl once a week:
See [https://github.com/o-shabashov/wallswap-java-crawler](https://github.com/o-shabashov/wallswap-java-crawler)

## Made with
1. [Spring](https://spring.io/)
2. MySQL
