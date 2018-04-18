package CollectionCLI;

import Plot.Event;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Scanner;

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

    static Event FromJson(String s) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(s, Event.class);
    }

    private static String multilineJson(Scanner scn) {
        StringBuilder sb = new StringBuilder();
        String first = scn.nextLine();
        sb.append(first);
            int countOpen = first.length() - first.replace("{", "").length();
            int countClose = first.length() - first.replace("}", "").length();
            int diff = countOpen-countClose;
            if (diff!=0) {
                do {
                    String string = scn.nextLine();
                    countOpen = string.length() - string.replace("{", "").length();
                    countClose = string.length() - string.replace("}", "").length();
                    diff = diff + countOpen - countClose;
                    sb.append(string);
                } while (diff > 0);
            }
            System.out.println("finished entering command");
        return sb.toString();
    }

    static ArrayList<String> parseCmd() throws WrongArgsException, StringIndexOutOfBoundsException{
        Scanner scn = new Scanner(System.in);
        String cmd = multilineJson(scn);
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
