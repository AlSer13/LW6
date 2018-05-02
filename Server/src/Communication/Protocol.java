package Communication;

import CollectionCLI.CollectionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;

import static CollectionCLI.Instruments.FromJson;

class Protocol {
    CollectionHandler ch;
    Protocol(CollectionHandler ch){
        this.ch = ch;
    }
    String processResponse(ArrayList<String> cmd) {
        String output = "";
        try {
            switch (cmd.get(0)) {
                case "hello": {
                    output = ("What's up?");
                } break;
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
                } break;
                case "remove_last": {
                    output = ch.removeLast();
                }
                break;
                case "import": {
                    output = ch.Import(cmd.get(1));
                }
                break;
                case "remove_all": {
                    output = ch.removeAll(FromJson(cmd.get(1)));
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
                        output = ch.remove(FromJson(cmd.get(1))); //перегрузить
                    }
                }
                case "info": {
                    output = ch.info();
                }
                break;
                case "add_if_max": {
                    output = ch.addIfMax(FromJson(cmd.get(1)));
                }
                break;
                case "remove_greater": {
                    output = ch.removeGreater(FromJson(cmd.get(1)));
                }
                break;
                case "insert": {
                    output = ch.insert(Integer.parseInt(cmd.get(1)), FromJson(cmd.get(2)));
                }
                break;
                case "add_if_min": {
                    output = ch.addIfMin(FromJson(cmd.get(1)));
                }
                break;
                case "remove_first": {
                    output = ch.removeFirst();
                }
                break;
                case "remove_lower": {
                    output = ch.removeLower(FromJson(cmd.get(1)));
                }
                break;
                case "clear": {
                    output = ch.clear();
                }
                break;
                case "add": {
                    output = ch.add(FromJson(cmd.get(1)));
                }
                break;
                case "load": {
                    output = ch.load();
                }
                break;
                case "contents": {
                    output = ch.contents();
                } break;
                case "quit":
                    output = "Quiting...";
                    break;
                case "null":
                    output = "";
                    break;
                default: {
                    output = ("No such command");
                }
                break;
            }
        } catch (EmptyStackException e) {
            output = (e.getMessage());
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            output = ("Wrong arguments");
        } catch (com.google.gson.JsonSyntaxException e) {
            output = ("Wrong Json format");
        } catch (SecurityException e) {
            output = ("Permission denied");
        } catch (NullPointerException e) {
            output = ("Null names are not permitted");
        } catch (IOException e) {
            output = ("Wrong path");
        } catch (Throwable e){
            output = ("Unknown error");
        } finally {
            if (output.equals("")) {
                output = "Command applied";
            }
        }
        return output;
    }
}
