package CollectionCLI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.*;
import Graphics.Unit;

import static CollectionCLI.Instruments.extractFileName;
import static CollectionCLI.Instruments.extractFilePath;

/**
 * This class provides main methods and fields for operating with collection
 */

public class CollectionHandler {
    public static final Set<String> objComms = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("add", "remove", "insert", "remove_all", "add_if_min", "remove_greater", "add_if_max", "remove_lower")));
    public Stack<Unit> units = new Stack<>();
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
     * The method reloads collection units from the file
     */
    public synchronized String load() throws IOException {
        units.clear();
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
        units = gson.fromJson(json, new TypeToken<Stack<Unit>>() {
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
    public String removeAll(Unit e) {
        units.removeIf(p -> p.equals(e));
        return ("All equal to " + e.getName() + "remove");
    }

    /**
     * reorders the Collection in a reverse order
     */
    public String reorder() {
        if (order) {
            units.sort(Comparator.reverseOrder());
            order = false;
        } else {
            units.sort(Comparator.naturalOrder());
            order = true;
        }
        return "Collection reordered";
    }

    /**
     * Pops from the Stack
     */
    public String removeLast() {
        if (!units.isEmpty()) {
            units.pop();
            return "Last element removed";
        } else return ("Collection is empty");
    }

    /**
     * Removes the element with zero index
     */
    public String removeFirst() {
        if (!units.isEmpty()) {
            units.removeElementAt(0);
            return ("First element removed");
        } else return ("Collection is empty");
    }

    /**
     * removes an element at i index
     *
     * @param i index
     */
    public String remove(int i) {
        if (!units.isEmpty()) {
            units.removeElementAt(i);
            return ("Removed element at " + i);
        } else return ("Collection is empty");
    }

    /**
     * Removes an element by it's value
     *
     * @param e Example character
     */
    public String remove(Unit e) {
        if (!units.removeIf(a -> a.compareTo(e) == 0)) {
            return ("No such element");
        } else
            return ("Element removed");
    }

    public String sort(){
        units.sort(Comparator.naturalOrder());
        return (order ? "Natural order" : "Reverse order");
    }

    /**
     * Removes elements that are greater than e
     *
     * @param e the first comparable
     */
    public String removeGreater(Unit e) {
        if (units.removeIf(a -> a.compareTo(e) < 0)) {
            return "Removed successfully";
        } else return "No such element";
    }

    /**
     * Removes elements that are less than e
     *
     * @param e the first comparable
     */
    public String removeLower(Unit e) {
        if (units.removeIf(a -> a.compareTo(e) > 0)) {
            return "Removed successfully";
        } else return "No such element";
    }

    /**
     * Saves the collection to a file of it's CollectionHandler
     */
    public synchronized String save() {
        try {
            PrintWriter pw = new PrintWriter(file);
            String read = gson.toJson(units);
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
        return ("Class: " + units.getClass().getName() + "\n" +
        "Initialized: " + initDate.getTime().toString() + "\n" +
        "Capacity: " + units.capacity() + "\n" +
        "Size: " + units.size());

    }

    /**
     * prints all elements of the Collection to StdOut
     */
    public String contents() {
        if (!units.isEmpty()) {
            StringBuilder s = new StringBuilder();
            units.forEach((e)->{if (e.getName()!=null) s.append(e.getName()).append("\n"); });
            return s.toString();
        } else return("Collection is empty");
    }

    /**
     * adds to the collection if it's greater than the maximum element
     *
     * @param e a Character to push
     */
    public String addIfMax(Unit e) {
        boolean b = true;
        for (Unit character : units) {
            if (e.compareTo(character) < 0) {
                b = false;
            }
        }
        if (b) {
            units.push(e);
            return "Добавлено";
        } else return "Не добавено";

    }

    /**
     * adds e to the collection if it's less than the element
     *
     * @param e a Character to push
     */
    public String addIfMin(Unit e) {
        boolean b = true;
        for (Unit character : units) {
            if (e.compareTo(character) > 0) {
                b = false;
            }
        }
        if (b) {
            units.push(e);
            return "Добавлено";
        } else return "Не добавлено";

    }

    /**
     * pushes to the stack
     *
     * @param e
     */
    public String add(Unit e) {
        units.push(e);
        return "Element added";
    }

    /**
     * inserts an element e at the index i
     *
     * @param i index
     * @param e element
     */
    public String insert(int i, Unit e) {
        units.insertElementAt(e, i);
        return "Element inserted";
    }

    /**
     * removes all the elements and sets the size to 0
     */
    public String clear() {
        units.removeAllElements();
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
