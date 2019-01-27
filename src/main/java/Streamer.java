public class Streamer {

    private String channelID, channelTitle, streamTitle, streamID;
    private boolean live;

    public String toString() {
        return this.channelTitle + " | " + this.streamTitle;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getStreamTitle() {
        return streamTitle;
    }

    public void setStreamTitle(String streamTitle) {
        this.streamTitle = streamTitle;
    }

    public String getStreamID() {
        return streamID;
    }

    public void setStreamID(String streamID) {
        this.streamID = streamID;
    }

    public String getYoutubeLink() {
        String url = "https://www.youtube.com/watch?v=";
        url += streamID;
        return url;
    }

    public String getPopoutChatLink() {
        String url = "https://www.youtube.com/live_chat?v=";
        url += streamID;
        return url;
    }

    public String getNeatclipLink() {
        String url = "https://upload.neatclip.com/clipper?stream=";
        url += getYoutubeLink();
        return url;
    }
}