import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Launcher {
    private static String getLivestreamerCommand(String youtubeURL) {
        String command = "streamlink ";
        command += youtubeURL;
        command += " best";
        return command;
    }

    public static void launchStreamlink(String youtubeURL) throws IOException {
        String command = getLivestreamerCommand(youtubeURL);
        Process p = Runtime.getRuntime().exec(command);
    }

    public static void launchBrowser(String URL) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(URL));
            } catch (IOException | URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + URL);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
