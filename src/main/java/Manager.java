import com.google.api.services.youtube.model.SearchListResponse;
import javafx.application.Platform;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Manager {
    private final String channelsURL = "channels.txt";          //name of text file to parse for channelIDs
    private final int autoUpdate = 5;                           //auto update interval (minutes), keep conservative or youtube api will limit it.

    private ArrayList<Streamer> streamerList;
    private ArrayList<String> channels;
    private Notifications notify;
    private ScheduledExecutorService service;
    private Controller control;

    public Manager(Controller c) throws IOException, AWTException {
        channels = FileIO.getLines(channelsURL);
        streamerList = new ArrayList<>();
        createStreamers();
        updateStreamers();
        notify = new Notifications();
        control = c;
        startAutoUpdate();
        control.setStreamerList(getStreamerList());
        control.updateListView(getStreamerStrings());
    }

    public ArrayList<Streamer> getStreamerList() {
        return streamerList;
    }

    public void setStreamerList(ArrayList<Streamer> streamerList) {
        this.streamerList = streamerList;
    }

    private void createStreamers() {
        Streamer foo;
        for (String chan : channels) {
            foo = new Streamer();
            foo.setChannelID(chan);
            streamerList.add(foo);
        }
    }

    private void updateStreamers() throws IOException {
        SearchListResponse response = null;

        for (Streamer foo : streamerList) {
            try {
                response = YoutubeAPI.getYoutubeResponse(foo.getChannelID());
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            if (response.getPageInfo().getTotalResults() == 0) {          //if there are no live streams on channel
                foo.setLive(false);
            } else {                                                       //if there is a live stream
                foo.setLive(true);
                foo.setChannelTitle(response.getItems().get(0).getSnippet().getChannelTitle());
                foo.setStreamTitle(response.getItems().get(0).getSnippet().getTitle());
                foo.setStreamID(response.getItems().get(0).getId().getVideoId());
            }
        }
    }

    /**
     * different from getLiveStreamers because this is an ArrayList of streamer toStrings(), for the viewList. What the user sees.
     */
    public ArrayList<String> getStreamerStrings() {
        ArrayList<String> myList = new ArrayList<>();
        for (Streamer foo : streamerList) {
            if (foo.isLive() == true) {
                myList.add(foo.toString());
            }
        }
        if (myList.isEmpty() == true) {
            myList.add("No streamers are live.");
        }
        return myList;
    }

    public ArrayList<String> getLiveStreamers() {                //used for tray notifications.
        ArrayList<String> live = new ArrayList<>();
        for (Streamer foo : streamerList) {
            if (foo.isLive() == true) {
                live.add(foo.getChannelTitle());
            }
        }

        return live;
    }

    /**
     * used for tray notifications, strings are only the channel username. Will return an array of streamers channels that weren't already live in streamerList.
     **/
    public ArrayList<String> getRecentStreamers() throws IOException {
        ArrayList<String> prev = getLiveStreamers(), after, recent = new ArrayList<>();
        updateStreamers();
        after = getLiveStreamers();

        for (String foo : after) {

            if (prev.contains(foo) == false) {
                recent.add(foo);
            }
        }

        return recent;
    }

    /**
     * Must use Platform.runLater() to be able to update the javafx gui with an autoupdate service from this class, otherwise exception will be thrown.
     */
    public void updateJavaFX() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                control.clearListView();
                control.updateListView(getStreamerStrings());
                control.setStreamerList(getStreamerList());
            }
        };
        Platform.runLater(runnable);
    }

    /**
     * This will start an autoupdate service which will automatically update the list data and streamer data within the controller class for JAVAFX.
     */
    private void startAutoUpdate() {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    // task to run goes here
                    ArrayList<String> test = getRecentStreamers();
                    updateStreamers();
                    updateJavaFX();

                    if (test.isEmpty() == false) {
                        notify.displayTrayChannels(test);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        service = Executors
                .newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, autoUpdate, autoUpdate, TimeUnit.MINUTES);
    }

}
