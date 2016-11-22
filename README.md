# Wallswap-Java

The project is just for fun and test programming skills. It consists of two parts - the server and crawler.

### Server:
1. Runs on 8080 port;
2. Gets the list of wallpapers from database;
3. Render the html page with the list of wallpaper;
4. Allows you to authenticate the user through OAuth2 Dropbox, gets access_token and stores the user in a database;

### Crawler:
See [https://github.com/o-shabashov/wallswap-java-crawler](https://github.com/o-shabashov/wallswap-java-crawler)

## Installation
* Create MySQL database `wallswap` and import `wallswap.sql`
* Create [Dropbox App](https://www.dropbox.com/developers/apps/create) and fill `app.json`
```json
{
  "key": "APP_KEY_HERE",
  "secret": "APP_SECRET_HERE"
}
```
* Redirect URL for Dropbox callback:
```
http://localhost:8080/oauth2callback
```
* Install gradle dependencies:
```bash
cd wallswap-java
gradle build
```
* Run server:
```bash
gradle bootRun
```

## Made with
1. [Spring](https://spring.io/)
2. MySQL
