package CollectionCLI;

import Graphics.Unit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Instruments {
    public static String extractFilePath(String path){
        if (path.contains("/")) {
            return path.substring(0, path.lastIndexOf('/') + 1);
        } else if (path.contains("\\")){
            return path.substring(0, path.lastIndexOf('\\') + 1);
        } else
            return ".";
    }

    public static String extractFileName(String path){
        if (path.contains("/")) {
            return path.substring(path.lastIndexOf('/') + 1, path.length());
        } else if (path.contains("\\")){
            return path.substring(path.lastIndexOf('\\') + 1, path.length());
        } else
            return path;
    }

    public static Unit fromJson(String s) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(s, Unit.class);
    }

    public static String multilineJson(BufferedReader in) throws IOException{
        StringBuilder sb = new StringBuilder();
        String first = in.readLine();
        sb.append(first);
            int countOpen = first.length() - first.replace("{", "").length();
            int countClose = first.length() - first.replace("}", "").length();
            int diff = countOpen-countClose;
            if (diff!=0) {
                do {
                    String string = in.readLine();
                    countOpen = string.length() - string.replace("{", "").length();
                    countClose = string.length() - string.replace("}", "").length();
                    diff = diff + countOpen - countClose;
                    sb.append(string);
                } while (diff > 0);
            }
        return sb.toString();
    }

    public static ArrayList<String> parseCmd(String cmd) throws WrongArgsException, StringIndexOutOfBoundsException{
        ArrayList<String> listArgs = new ArrayList<>();
        String text[] = cmd.split(" \\{",2);
        if (text.length>1) {
            String cArgs[];
            cArgs = text[1].split("} ");
            int length = cArgs.length;
            cArgs[length - 1] = cArgs[length - 1].substring(0, cArgs[length - 1].length() - 1);
            listArgs.add(text[0]);
            listArgs.add(cArgs[0]);
            if (length > 1) {
                for (int i = 1; i < length; i++) {
                    listArgs.add(cArgs[i].substring(1, cArgs[i].length()));
                }
            }
        }
        else if (cmd.trim().contains(" ")){
            throw (new WrongArgsException());
        } else {
            listArgs.add(text[0]);
        }
            return listArgs;
    }

    public static class WrongArgsException extends Exception {
        @Override
        public String getMessage() {
            return ("Wrong arguments format");
        }
    }
}
