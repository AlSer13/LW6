package CollectionCLI;

import Plot.Event;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.*;

import static CollectionCLI.Instruments.extractFileName;
import static CollectionCLI.Instruments.extractFilePath;

/**
 * This class provides main methods and fields for operating with collection
 */

public class CollectionHandler {
    public static final Set<String> objComms = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("add", "remove", "insert", "remove_all", "add_if_min", "remove_greater", "add_if_max", "remove_lower")));
    public Stack<Event> Events = new Stack<>();
    public File file;
    boolean order = true;
    Scanner scan;
    Calendar initDate = new GregorianCalendar();
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();

    /**
     * Constructor sets the date of initialization
     */
    public CollectionHandler() {
        initDate = GregorianCalendar.getInstance();
    }

    /**
     * The method reloads collection Events from the file
     */
    public synchronized String load() throws IOException {
        Events.clear();
        this.Import(file);
        return "Collection loaded";
    }

    /**
     * A method to read new collection from a
     *
     * @param file, a parameter for the file from where to import
     */
    public synchronized String Import(File file) throws IOException {
        FileReader fr = new FileReader(file);
        scan = new Scanner(fr);
        StringBuilder sb = new StringBuilder();
        while (scan.hasNextLine()) {
            sb.append(scan.nextLine());
        }
        String json = sb.toString();
        Events = gson.fromJson(json, new TypeToken<Stack<Event>>() {
        }.getType());
        fr.close();
        return "Collection imported";
    }

    /**
     * Override the Import method to access the file via
     *
     * @param path parameter
     */
    public synchronized String Import(String path) throws IOException {
        File file = new File(extractFilePath(path), extractFileName(path));
        Import(file);
        return "Collection imported";
    }

    /**
     * Removes all object equal to the parameter
     *
     * @param e, given object
     */
    public String removeAll(Event e) {
        Events.removeIf(p -> p.equals(e));
        return ("All equal to " + e.name + "remove");
    }

    /**
     * reorders the Collection in a reverse order
     */
    public String reorder() {
        if (order) {
            Events.sort(Comparator.reverseOrder());
            order = false;
        } else {
            Events.sort(Comparator.naturalOrder());
            order = true;
        }
        return "Collection reordered";
    }

    /**
     * Pops from the Stack
     */
    public String removeLast() {
        if (!Events.isEmpty()) {
            Events.pop();
            return "Last element removed";
        } else return ("Collection is empty");
    }

    /**
     * Removes the element with zero index
     */
    public String removeFirst() {
        if (!Events.isEmpty()) {
            Events.removeElementAt(0);
            return ("First element removed");
        } else return ("Collection is empty");
    }

    /**
     * removes an element at i index
     *
     * @param i index
     */
    public String remove(int i) {
        if (!Events.isEmpty()) {
            Events.removeElementAt(i);
            return ("Removed element at " + i);
        } else return ("Collection is empty");
    }

    /**
     * Removes an element by it's value
     *
     * @param e Example event
     */
    public String remove(Event e) {
        if (!Events.removeIf(a -> a.compareTo(e) == 0)) {
            return ("No such element");
        } else
            return ("Element removed");
    }

    public String sort(){
        Events.sort(Comparator.naturalOrder());
        return (order ? "Natural order" : "Reverse order");
    }

    /**
     * Removes elements that are greater than e
     *
     * @param e the first comparable
     */
    public String removeGreater(Event e) {
        if (Events.removeIf(a -> a.compareTo(e) < 0)) {
            return "Removed successfully";
        } else return "No such element";
    }

    /**
     * Removes elements that are less than e
     *
     * @param e the first comparable
     */
    public String removeLower(Event e) {
        if (Events.removeIf(a -> a.compareTo(e) > 0)) {
            return "Removed successfully";
        } else return "No such element";
    }

    /**
     * Saves the collection to a file of it's CollectionHandler
     */
    public synchronized String save() {
        try {
            PrintWriter pw = new PrintWriter(file);
            String read = gson.toJson(Events);
            pw.print(read);
            pw.close();
            return ("Collection saved");
        } catch (FileNotFoundException e) {
            return (e.getMessage());
        }


    } //remake

    /**
     * provides basic information about the collection
     */
    public String info() {
        return ("Class: " + Events.getClass().getName() + "\n" +
        "Initialized: " + initDate.getTime().toString() + "\n" +
        "Capacity: " + Events.capacity() + "\n" +
        "Size: " + Events.size());

    }

    /**
     * prints all elements of the Collection to StdOut
     */
    public String contents() {
        if (!Events.isEmpty()) {
            StringBuilder s = new StringBuilder();
            Events.forEach((e)->{if (e.name!=null) s.append(e.name).append("\n"); });
            return s.toString();
        } else return("Collection is empty");
    }

    /**
     * adds to the collection if it's greater than the maximum element
     *
     * @param e an Event to push
     */
    public String addIfMax(Event e) {
        boolean b = true;
        for (Event event : Events) {
            if (e.compareTo(event) < 0) {
                b = false;
            }
        }
        if (b) {
            Events.push(e);
            return "Добавлено";
        } else return "Не добавено";

    }

    /**
     * adds e to the collection if it's less than the element
     *
     * @param e an Event to push
     */
    public String addIfMin(Event e) {
        boolean b = true;
        for (Event event : Events) {
            if (e.compareTo(event) > 0) {
                b = false;
            }
        }
        if (b) {
            Events.push(e);
            return "Добавлено";
        } else return "Не добавлено";

    }

    /**
     * pushes to the stack
     *
     * @param e
     */
    public String add(Event e) {
        Events.push(e);
        return "Element added";
    }

    /**
     * inserts an element e at the index i
     *
     * @param i index
     * @param e element
     */
    public String insert(int i, Event e) {
        Events.insertElementAt(e, i);
        return "Element inserted";
    }

    /**
     * removes all the elements and sets the size to 0
     */
    public String clear() {
        Events.removeAllElements();
        return "Cleared";
    }

    /**
     * finishes the execution of the program
     */
    public String exit() {
        save();
        return ("Saved");
    }

}
