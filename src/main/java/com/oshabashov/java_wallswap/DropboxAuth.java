package com.oshabashov.java_wallswap;

import com.dropbox.core.*;
import com.dropbox.core.v2.DbxClientV2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Handles the endpoints related to authorizing this app with the Dropbox API
 * via OAuth 2.
 */
public class DropboxAuth {
    private final Common common;

    public DropboxAuth(Common common) {
        this.common = common;
    }

    /**
     * Start the process of getting a Dropbox API access token for the user's Dropbox account.
     *
     * @return Authorization URL of website user can use to authorize your app.
     */
    public String doStart(HttpServletRequest request) {
        DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder()
          // After we redirect the user to the Dropbox website for authorization,
          // Dropbox will redirect them back here.
          .withRedirectUri(getRedirectUri(request), getSessionStore(request))
          .build();
        return getWebAuth(request).authorize(authRequest);
    }

    private String getRedirectUri(final HttpServletRequest request) {
        return common.getUrl(request, "/oauth2callback");
    }

    private DbxSessionStore getSessionStore(final HttpServletRequest request) {
        // Select a spot in the session for DbxWebAuth to store the CSRF token.
        HttpSession session    = request.getSession(true);
        String      sessionKey = "dropbox-auth-csrf-token";
        return new DbxStandardSessionStore(session, sessionKey);
    }

    private DbxWebAuth getWebAuth(final HttpServletRequest request) {
        return new DbxWebAuth(common.getRequestConfig(request), common.dbxAppInfo);
    }

    /**
     * The Dropbox API authorization page will redirect the user's browser to this page.
     */
    public void doFinish(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DbxException {
        DbxAuthFinish authFinish;
        try {
            authFinish = getWebAuth(request).finishFromRedirect(
              getRedirectUri(request),
              getSessionStore(request),
              request.getParameterMap()
                                                               );
        } catch (DbxWebAuth.BadRequestException e) {
            common.log.println("On /oauth2callback: Bad request: " + e.getMessage());
            response.sendError(400);
            return;
        } catch (DbxWebAuth.BadStateException e) {
            // Send them back to the start of the auth flow.
            response.sendRedirect(common.getUrl(request, "/login"));
            return;
        } catch (DbxWebAuth.CsrfException e) {
            common.log.println("On /oauth2callback: CSRF mismatch: " + e.getMessage());
            response.sendError(403);
            return;
        } catch (DbxWebAuth.NotApprovedException e) {
            common.log.println("Not approved by user");
            response.sendError(403);
            return;
        } catch (DbxWebAuth.ProviderException e) {
            common.log.println("On /oauth2callback: Auth failed: " + e.getMessage());
            response.sendError(503, "Error communicating with Dropbox.");
            return;
        } catch (DbxException e) {
            common.log.println("On /oauth2callback: Error getting token: " + e);
            response.sendError(503, "Error communicating with Dropbox.");
            return;
        }

        // We have an Dropbox API access token now
        String accessToken = authFinish.getAccessToken();

        // Get current user info from Dropbox API
        DbxClientV2 client = new DbxClientV2(
          common.getRequestConfig(request),
          accessToken, common.dbxAppInfo.getHost()
        );

        // Create User and store it in Database
        try {
            common.createUserByDropbox(client.users().getCurrentAccount(), accessToken);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
