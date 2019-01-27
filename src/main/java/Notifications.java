import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;


public class Notifications {
    private final static String iconURL = "/yls.png";
    private SystemTray tray;
    private Image image;
    private TrayIcon trayIcon;

    Notifications() throws AWTException, IOException {
        //initialize objects for desktop tray notifications.
        tray = SystemTray.getSystemTray();
        image = ImageIO.read(getClass().getResourceAsStream(iconURL));
        trayIcon = new TrayIcon(image, "Youtube Live Streamer");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Youtube Live Streamer");
        tray.add(trayIcon);
    }

    public void displayTrayChannels(ArrayList<String> channels) throws InterruptedException {
        for (String chan : channels) {
            trayIcon.displayMessage("Streamer Live!", chan + " is streaming.", TrayIcon.MessageType.INFO);
            Thread.sleep(3000);             //delay for notifications, else multiple notifications will be pushed at the same time overwriting previous messages.
        }

    }


}
