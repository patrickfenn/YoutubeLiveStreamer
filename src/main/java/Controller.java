import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;
public class Controller {
    @FXML
    protected ListProperty<String> listProperty = new SimpleListProperty<>();
    @FXML
    private ComboBox<String> combo1;
    @FXML
    private Button button1;
    @FXML
    private CheckBox check1;
    @FXML
    private ListView<String> listview1;
    private ArrayList<Streamer> streamerList;

    public ArrayList<Streamer> getStreamerList() {
        return streamerList;
    }

    public void setStreamerList(ArrayList<Streamer> streamerList) {
        this.streamerList = streamerList;
    }

    public void updateListView(ArrayList<String> items) {
        if (items.isEmpty() == true) {
            return;
        }
        listProperty.set(FXCollections.observableArrayList(items));
        listview1.itemsProperty().bind(listProperty);
    }

    public void button1Clicked(ActionEvent ae) throws IOException {
        if (listview1.getSelectionModel().isEmpty() == true) {
            return;
        }
        if (combo1.getSelectionModel().isEmpty() == true) {
            return;
        }

        String streamselect = listview1.getSelectionModel().getSelectedItem();
        String playerSelect = combo1.getSelectionModel().getSelectedItem();
        Streamer choice = null;
        for (Streamer foo : streamerList) {
            if (foo.isLive() == false) {
                continue;
            }
            if (streamselect.contains(foo.toString())) {
                choice = foo;
                break;
            }
        }
        if (check1.isSelected() == true) {
            //launch popout chat
            Launcher.launchBrowser(choice.getPopoutChatLink());
        }
        if (playerSelect.contains("Youtube")) {
            //launch youtube stream
            Launcher.launchBrowser(choice.getYoutubeLink());
        } else if (playerSelect.contains("Streamlink")) {
            //launch streamlink stream
            Launcher.launchStreamlink(choice.getYoutubeLink());
        } else {
            return;
        }

    }

    public void button2Clicked(ActionEvent ae) throws IOException {
        if (listview1.getSelectionModel().isEmpty() == true) {
            return;
        }

        String streamselect = listview1.getSelectionModel().getSelectedItem();
        Streamer choice = null;
        for (Streamer foo : streamerList) {
            if (streamselect.contains(foo.toString())) {
                choice = foo;
                break;
            }
        }

        Launcher.launchBrowser(choice.getNeatclipLink());

    }

    public void clearListView() {
        listview1.getItems().clear();
    }
}
