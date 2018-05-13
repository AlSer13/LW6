package Communication;

import CollectionCLI.CollectionHandler;
import Plot.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;

import static CollectionCLI.CollectionHandler.objComms;

class Protocol {
    public static Command commands;
    private boolean isValid;


    CollectionHandler ch;

    Protocol(CollectionHandler ch) {
        this.ch = ch;
    }
    static Time time = new Time();

    String processResponse(ArrayList<String> cmd, Event event) {
        time.markTime();
        String output = "";
        try {
            if (event != null) {
                String s = event.name;
                s.toLowerCase();
            }
            isValid = true;
            switch (cmd.get(0)) {
                case "hello": {
                    output = ("What's up?");
                }
                break;
                case "help": {
                    output = ("•\tremove_last: удалить последний элемент из коллекции\n" +
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
                            "•\tload: перечитать коллекцию из файла\n" +
                            "•\tcontents: содержание коллекции\n" +
                            "•\tquit: закончить сеанс клиента");
                }
                break;
                case "remove_last": {
                    output = ch.removeLast();
                }
                break;
                case "import": {
                    output = ch.Import(cmd.get(1));
                }
                break;
                case "remove_all": {
                    output = ch.removeAll(event);
                }
                break;
                case "reorder": {
                    output = ch.reorder();
                }
                break;
                case "save": {
                    output = ch.save();
                }
                break;
                case "remove": {
                    try {
                        int arg = Integer.parseInt(cmd.get(1));
                        output = ch.remove(arg);
                    } catch (NumberFormatException e) {
                        output = ch.remove(event); //перегрузить
                    }
                }
                case "info": {
                    output = ch.info();
                }
                break;
                case "add_if_max": {
                    output = ch.addIfMax(event);
                }
                break;
                case "remove_greater": {
                    output = ch.removeGreater(event);
                }
                break;
                case "insert": {
                    output = ch.insert(Integer.parseInt(cmd.get(1)), event); //!!!!!!
                }
                break;
                case "add_if_min": {
                    output = ch.addIfMin(event);
                }
                break;
                case "remove_first": {
                    output = ch.removeFirst();
                }
                break;
                case "remove_lower": {
                    output = ch.removeLower(event);
                }
                break;
                case "clear": {
                    output = ch.clear();
                }
                break;
                case "add": {
                    output = ch.add(event);
                }
                break;
                case "load": {
                    output = ch.load();
                }
                break;
                case "contents": {
                    output = ch.contents();
                }
                break;
                case "quit":
                    output = "Quitting...";
                    break;
                case "null":
                    output = "";
                    break;
                default: {
                    output = ("No such command");
                    isValid = false;
                }
                break;
            }
            commands.submitCommand(isValid);
        } catch (EmptyStackException e) {
            output = (e.getMessage());
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            output = ("Wrong arguments");
        } catch (SecurityException e) {
            output = ("Permission denied");
        } catch (NullPointerException e) {
            output = ("Null names are not permitted");
        } catch (IOException e) {
            output = ("Wrong path");
        } catch (Throwable e) {
            output = ("Unknown error");
        } finally {
            if (output.equals("")) {
                output = "Command processed";
            }
        }
        return output;
    }
}
