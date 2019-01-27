import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.SearchListResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class YoutubeAPI {
    /**
     * Where client_secret.json is located
     **/
    private static final String clientsecretURL = "/client_secret.json";

    private static final String clientsecretPath = ".credentials/youtube-java-quickstart";
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "YoutubeLiveStreamer";

    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(

            System.getProperty(YoutubeAPI.class.getProtectionDomain().getCodeSource().getLocation().getPath()), clientsecretPath);
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();
    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */

    private static final List<String> SCOPES =
            Arrays.asList(YouTubeScopes.YOUTUBE_READONLY);
    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactoryEdit DATA_STORE_FACTORY;
    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactoryEdit(DATA_STORE_DIR);

        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    public static Credential authorize() throws IOException {
        // Load client secrets.

        Credential credential = null;
        InputStream in;

        try {
            in = YoutubeAPI.class.getResourceAsStream(clientsecretURL);
            GoogleClientSecrets clientSecrets =
                    GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
            // Build flow and trigger user authorization request.
            GoogleAuthorizationCodeFlow flow =
                    new GoogleAuthorizationCodeFlow.Builder(
                            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                            .setDataStoreFactory(DATA_STORE_FACTORY)
                            .setAccessType("offline")
                            .build();
            credential = new AuthorizationCodeInstalledApp(
                    flow, new LocalServerReceiver()).authorize("user");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return credential;
    }

    /**
     * Build and return an authorized API client service, such as a YouTube
     * Data API client service.
     *
     * @return an authorized API client service
     * @throws IOException
     */
    public static YouTube getYouTubeService() throws IOException {
        Credential credential = authorize();
        return new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    /**
     * Get youtube response for live streams.
     *
     * @param channelID
     * @return
     * @throws Exception
     */
    public static SearchListResponse getYoutubeResponse(String channelID) throws Exception {
        YouTube youtube = getYouTubeService();
        YouTube.Search.List search = youtube.search().list("snippet");
        search.setEventType("live");
        search.setMaxResults(Long.parseLong("1"));
        search.setType("video");
        search.setChannelId(channelID);
        SearchListResponse response = null;
        try {
            response = search.execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return response;
    }


}
