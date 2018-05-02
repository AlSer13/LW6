package CollectionCLI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.NoSuchElementException;

import static CollectionCLI.Instruments.*;

public class Main {
    public static void main(String[] args) {
       /* CollectionHandler ch = new CollectionHandler();
        ShutdownHook shutdownHook = new ShutdownHook(ch);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        try {
            ch.file = new File(extractFilePath(args[0]), extractFileName(args[0]));
        } catch (NullPointerException e){
            System.out.println("You should enter the path");
            System.exit(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Not enough arguments");
        }
        try {
            ch.load();
        } catch (SecurityException e) {
            System.out.println("Permission to the file Denied");
        }
        catch (IOException | NullPointerException e){
            System.out.println("Wrong input file. Enter the command again.");
            System.exit(1);
        }
        String exitCheck = "";
        do {
            System.out.println("Enter a command:");
            try {
                ArrayList<String> cmd = parseCmd();
                exitCheck = cmd.get(0);
                try {
                    switch (cmd.get(0)) {
                        case "hello": {
                            System.out.println("What's up?");
                        } break;
                        case "help": {
                            System.out.println("•\tremove_last: удалить последний элемент из коллекции\n" +
                                    "•\timport {String path}: добавить в коллекцию все данные из файла\n" +
                                    "•\tremove_all {element}: удалить из коллекции все элементы, эквивалентные заданному\n" +
                                    "•\treorder: отсортировать коллекцию в порядке, обратном нынешнему\n" +
                                    "•\tsave: сохранить коллекцию в файл\n" +
                                    "•\tremove {int index}: удалить элемент, находящийся в заданной позиции коллекции\n" +
                                    "•\tinfo: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                                    "•\tremove {element}: удалить элемент из коллекции по его значению\n" +
                                    "•\tadd_if_max {element}: добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                                    "•\tremove_greater {element}: удалить из коллекции все элементы, превышающие заданный\n" +
                                    "•\tinsert {int index} {element}: добавить новый элемент в заданную позицию\n" +
                                    "•\tadd_if_min {element}: добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n" +
                                    "•\tremove_first: удалить первый элемент из коллекции\n" +
                                    "•\tremove_lower {element}: удалить из коллекции все элементы, меньшие, чем заданный\n" +
                                    "•\tclear: очистить коллекцию\n" +
                                    "•\tadd {element}: добавить новый элемент в коллекцию\n" +
                                    "•\tload: перечитать коллекцию из файла\n ");
                        }
                        case "remove_last": {
                            ch.removeLast();
                        }
                        break;
                        case "import": {
                            ch.Import(cmd.get(1));
                        }
                        break;
                        case "remove_all": {
                            ch.removeAll(FromJson(cmd.get(1)));
                        }
                        break;
                        case "reorder": {
                            ch.reorder();
                        }
                        break;
                        case "save": {
                            ch.save();
                        }
                        break;
                        case "remove": {
                            try {
                                int arg = Integer.parseInt(cmd.get(1));
                                ch.remove(arg);
                            } catch (NumberFormatException e) {
                                ch.remove(FromJson(cmd.get(1))); //перегрузить
                            }
                        }
                        case "info": {
                            ch.info();
                        }
                        break;
                        case "add_if_max": {
                            ch.addIfMax(FromJson(cmd.get(1)));
                        }
                        break;
                        case "remove_greater": {
                            ch.removeGreater(FromJson(cmd.get(1)));
                        }
                        break;
                        case "insert": {
                            ch.insert(Integer.parseInt(cmd.get(1)), FromJson(cmd.get(2)));
                        }
                        break;
                        case "add_if_min": {
                            ch.addIfMin(FromJson(cmd.get(1)));
                        }
                        break;
                        case "remove_first": {
                            ch.removeFirst();
                        }
                        break;
                        case "remove_lower": {
                            ch.removeLower(FromJson(cmd.get(1)));
                        }
                        break;
                        case "clear": {
                            ch.clear();
                        }
                        break;
                        case "add": {
                            ch.add(FromJson(cmd.get(1)));
                        }
                        break;
                        case "load": {
                            ch.load();
                        }
                        break;
                        case "contents": {
                            ch.contents();
                        } break;
                        case "exit":
                            ch.exit();
                            break;
                        default: {
                            System.out.println("No such command");
                        }
                        break;
                    }
                } catch (EmptyStackException e) {
                    System.out.println(e.getMessage());
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    System.out.println("Wrong arguments");
                } catch (com.google.gson.JsonSyntaxException e) {
                    System.out.println("Wrong Json format");
                } catch (SecurityException e) {
                    System.out.println("Permission denied");
                } catch (NullPointerException e) {
                    System.out.println("Null names are not permitted");
                } catch (IOException e) {
                    System.out.println("Wrong path");
                } catch (Throwable e){
                    System.out.println("Unknown error");
                }
            } catch (WrongArgsException e){
                System.out.println(e.getMessage());
            } catch (StringIndexOutOfBoundsException | NoSuchElementException e){
                System.out.println("Wrong command format");
            }
        } while(!exitCheck.equals("exit"));

    }
}

class ShutdownHook extends Thread{
    CollectionHandler ch;
    ShutdownHook(CollectionHandler inch) {
        this.ch = inch;
    }
    public void run(){
        try {
        ch.save();}
        catch (NullPointerException e){
            System.out.println("No file");
        }*/
    }
}
