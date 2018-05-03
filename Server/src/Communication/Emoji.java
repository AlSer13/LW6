package Communication;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class Emoji {
    public static String thinking = "\n▒▒▒▒▒▒▒▒▄▄▄▄▄▄▄▄▒▒▒▒▒▒▒▒\n" +
            "▒▒▒▒▒▄█▀▀░░░░░░▀▀█▄▒▒▒▒▒\n" +
            "▒▒▒▄█▀▄██▄░░░░░░░░▀█▄▒▒▒\n" +
            "▒▒█▀░▀░░▄▀░░░░▄▀▀▀▀░▀█▒▒\n" +
            "▒█▀░░░░███░░░░▄█▄░░░░▀█▒\n" +
            "▒█░░░░░░▀░░░░░▀█▀░░░░░█▒\n" +
            "▒█░░░░░░░░░░░░░░░░░░░░█▒\n" +
            "▒█░░██▄░░▀▀▀▀▄▄░░░░░░░█▒\n" +
            "▒▀█░█░█░░░▄▄▄▄▄░░░░░░█▀▒\n" +
            "▒▒▀█▀░▀▀▀▀░▄▄▄▀░░░░▄█▀▒▒\n" +
            "▒▒▒█░░░░░░▀█░░░░░▄█▀▒▒▒▒\n" +
            "▒▒▒█▄░░░░░▀█▄▄▄█▀▀▒▒▒▒▒▒\n" +
            "▒▒▒▒▀▀▀▀▀▀▀▒▒▒▒▒▒▒▒▒▒▒▒▒";
    public static String alexey = "║║║║║║║║║║║║║║║║║║║║╗║║║║║║╣║╣╗║╣╣╣║║╗╗║║║║║║║║║║║║║║║║║║║║\n" +
            "║║║║║║║║║║║║║║║║╣╣╣╣╣║╣╣║╣║╗║╣╣╣╣╣╣╣╣╣╣╣║╗╗║║║║║║║║║║║║║║║║\n" +
            "║║║║║║║║║║║║║║╣╣╣╣╣║╣║║║╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣║║╝║║║║║║║║║║║║║║\n" +
            "╗║║║║║║║║╗╗║╣╣╣║╣╣╣║╣║║╣╣╣╣║║╣╣╣╣╣╣╣╣╣╣╣╣║╣╣╗║║║║║║║║║║║║║║\n" +
            "║║╗║║║║║║║╣╣╣╣╣╣╣╣╣║║║║║║╗╗╗╝║║║╗║╣╣╣╣║╣║║╣║╣╣╗╗╗║║╗║║╗║║║║\n" +
            "╗╗╗║║║╗║║║╣╣╣╣╣╣╣║╣║╗╗╝╝╛╝╝╝╝╝╝╗║║╗║║╣╣║╣╣║║╣╗║╣╗╝╗║║║║║║║║\n" +
            "║╗╗╗║╗║╣╣╣╣╣╣╣╣╣╣║║╗╗╝╝╝╛╛╛╛╛╛╛╛╛╛╗║║╗║╣╝║║║╣╗╗╗╣║╗║║║║║║║║\n" +
            "╗╗╗║║║╣╣╣╣╣╣╣╣╣╣║║╗╗╝╝╝╝╝╛╛╛╛╛╛╛╛╛╛╝╗║║║║║╣╣║║║╗║╣║║║║║║║║║\n" +
            "╗║║║╣╣╣╣╣╣╣╣╣╣╣╣║╗╗╝╝╝╝╝╛╛╛╛╛╛╛╛╛╛╛╛╝╗║╗╗║╣╣╣║║╗╗║║╣║║║║║║║\n" +
            "╗║║╣║╣╣╣╣╣╣╣╣╣╣║║╗╗╝╝╝╝╝╝╛╛╛╛╛╛╛╛╛╛╛╛╛╗║║║║╣║╣╣╝╗║║║║║║╗║║║\n" +
            "║║╣║╣╣╣╣╣╣╣╣╣╣║║║╗╗╗╝╝╝╝╝╛╛╛╛╛╛╛╛╛╛╛╛╛╛╗╗║║╣║║║╣║╗╗║║║╗║║║╗\n" +
            "║║╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣║║╗╝╝╝╝╛╝╝╝╝╝╝╝╛╛╗╗╗╣║║║║║╗║╗╣║║╗╗║╗\n" +
            "║║╣╣╣╣╣╣╣╣╣╣╣╣╣║║║╣║║╣╣╣╣╣╣╣╣║║║╣╣╣╣║╣╣╣║╗╗║╣║╣║║║╗║║║╣║║╗║\n" +
            "║╗╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣║║║╣╣╣╣╣║╣║║║╣║║║╗║║║╝║╣╣╣╣╣║║║╣╣║╣║╗╗\n" +
            "║║║╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣║╣╣╣╣╝╝╣╣╣╣╣╣║║║║║║╣║║╣║╣╗╣║║║╣║║║║║╗\n" +
            "╗║╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╗╛╛╛║╣╣╣╣╣╣╣║║║║║║╣╣╣╣║╣║╗║║║╗║╗╗\n" +
            "╗╗╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣╣║╝╛╛╛╛╣╣╣╣╣╣║║║║║╣╣╣╣╣╣╣╣╗╗╗╣╗╣╗║║\n" +
            "╗╗║╣╣╣╣╣╣╣╣╣║║╣╣╣╣╣╣╣╣╣║║╗╝╛╛╛╛╝╣╗╣║║║║║║║╗╣╣╝╣╣╣║║║╗╗║╗╗║╗\n" +
            "╗╗╣╣╣╣╣╣╣╣╣╣║║║║║║║╗╝╝╗║║║╝╝╛╛╛╛╗║║║╣╣║║║║╣╗╝╝║╣╣╣║╗╗╗║╗║╗║\n" +
            "╗╗╣╣╣╣╣╣╣╣╣╣║║║║╗╝╝╝╝║╣║╣╣║║║╣╣╝╝╝╛╛╗║║║║╝╛╗╗╝╣╣╣╣║║║║╗║║╗╗\n" +
            "╗║╣╣╣╣╣╣╣╣║║║║║║╗╗╝╝║║║╣║║║╗╝╝╗╛╛╛╝╛╛╛╛╛╝╗╝║╗╗╣╣╣╣╣║╗║╗║╗╗╗\n" +
            "╗║╗╣╣╣╣╣╣╣║║║║║║║╗╗║║║║║║╝╝╝╝╛╛╛╛╛╛╝╛╛╛╛╝╝╝╗╗╗╣╣╣║╣╗║╗╗╝╗║╗\n" +
            "╗╗╗║╣╣╣╣╣╣╣╣╣║║║║╗║║║║╣╣╣║║╗╗╗╝╛╛╛╛╛╛╛╛╛╗╗╝╗╝╣╣╣╣╣║║╗║╗╗║╗╗\n" +
            "╗╗╗╗╗╣╣╣╣╣╣╣╣║║║║╗║║╣║╗║╗╝╝╝╝╝╝╝║║╗╝╛╛╛╛╗╝║╗╝╣╣╣╣╣║║║╗║╗║║║\n" +
            "╗╗╗╗╗╗║╣╣╣╣╣╣╣║║║║╗╗╝╗╗╗╗╗╗╝╝╛╛╛╛╛╛╛╛╛╛╝╗╝╗╗╣╣╣╣╣╣║╗║╗║╗╗║╗\n" +
            "╗╗╗╗╗╗╗║╣╣╣╣╣╣╣║║║║║╗╗╗╗╗╗╗╗╗╝╝╝╛╛╛╛╝╝╝╗╝║╗╝╣║╣╣╣╣║╗║║║╗╗╗╗\n" +
            "╗╗╗╗╗╗╗╗║╣╣╣╣╣╣╣║║║║║╗╝╝╝╝╝╛╛╛╛╛╛╝╛╝╝╝╗╗╝╗╗╣╣╣╣╣╣╣║║║║╣╗╗╗╗\n" +
            "╗╗╗╗╗╗╗╗╣╣╣╣╣╣╣╣║║║║║║╗╝╝╝╝╛╛╛╛╛╛╛╝╝╝╗╗╗║╝║╣╣╣╣╣╣╣║║╗╗╣║╗╗╗\n" +
            "╗╗╗╗╗╗╗╗╗╣╣╣╣╣╣║╣║║║║║║║║╗╝╗╝╝╝╝╝╗╗╝╗║╝║╗╗║╣╣╣╣╣╣╣╣║╗╗╣║╗╗╗\n" +
            "╗╗╗╗╗╗╗╗╗╣╣╣╣╣╣╣║║║║║║║║║║║║║║║║║║║║║╗║╗╗╗║╣╣╣╣╣╣╣╣║╗╗║╗╗╗╗\n";

    static HashMap<String, String> eng = new HashMap<>();
    static HashMap<String, String> rus = new HashMap<>();
    static HashMap<Boolean, HashMap<String, String>> lang = new HashMap<>();


    public static void playGame(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        boolean rusMod = false;
        out.writeObject(Emoji.alexey);
        eng.put("s", "｡◕‿◕｡");
        rus.put("s", "｡◕‿◕｡");
        eng.put("u", "(ಠ_ಠ) Hello, Alexander");
        rus.put("u", "(╯°□°）╯︵ ┻━┻ Здравствуйте, Александр");
        eng.put("h", "He's right next to you");
        rus.put("h", "Он прямо рядом с вами");
        eng.put("t", "Translated");
        rus.put("t", "Переведено");
        eng.put("task", "\nWhose face is encrypted in ASCII Art above?\nS for Serdyukov, U for Uchvatov, H for hint, T for Google translation, Task to repeat the task.");
        rus.put("task", "\nПолезное лицо зашифровано в ASCII Art выше?\nS для Сердюкова, U для Ухватова, H для подсказки, T для перевода Google, Task чтобы повторить задание.");
        lang.put(false, eng);
        lang.put(true, rus);
        String inputLine = "task";
        out.writeObject(lang.get(rusMod).get(inputLine.toLowerCase()));
        out.flush();
        do {

            try {
                ArrayList<String> cmd = (ArrayList<String>) in.readObject();
                inputLine = cmd.get(0);
                if (inputLine.equals("t")) {
                    rusMod = !rusMod;
                }

            } catch (Throwable throwable) {
                out.writeObject("Something went wrong. Please try again.");
                out.flush();
            }
            if (lang.get(rusMod).containsKey(inputLine.toLowerCase())) {
                out.writeObject(lang.get(rusMod).get(inputLine.toLowerCase()));
                out.flush();
            } else {
                out.writeObject("Should be S/U/H/T/Task");
                out.flush();
            }

        } while (!inputLine.toLowerCase().equals("s"));
        /*System.out.println("(╯°□°）╯︵ ┻━┻");
        Method[] chmetods = CollectionHandler.class.getMethods();
        Arrays.stream(chmetods).forEach(m -> Arrays
                .stream(m.getParameters())
                .forEach(parameter -> {
            if (parameter.getType() == Event.class) {
                System.out.print("\"" + m.getName() + "\", ");
            }
        }));*/

    }
}
