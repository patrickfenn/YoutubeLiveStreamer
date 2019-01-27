import java.io.*;
import java.util.ArrayList;


public class FileIO {


    /**
     * Parses file and returns an arraylist with elements that contain lines of the file
     * Arguments: URL of file
     **/
    public static ArrayList<String> getLines(String fileURL) throws IOException {

        String str;
        ArrayList<String> lines = new ArrayList<>();

        File file = new File(fileURL);

        if (file.exists() && !file.isDirectory()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                while ((str = br.readLine()) != null) {
                    lines.add(str);
                }
            }

        } else {
            file.createNewFile();
        }

        return lines;
    }


    /**
     * Writes a single line to a file
     * Arguments: URL of file, line to be written
     **/
    public static void writeLineToFile(String fileURL, String line) throws IOException {

        try (FileWriter fw = new FileWriter(fileURL, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(line);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
