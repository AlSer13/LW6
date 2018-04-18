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
    public CollectionHandler(){
        initDate = GregorianCalendar.getInstance();
    }

    /**
     * The method reloads collection Events from the file
     *
     */
    public void load() throws IOException {
        Events.clear();
        this.Import(file);
    }

    /**
     * A method to read new collection from a
     * @param file, a parameter for the file from where to import
     */
    public void Import(File file) throws IOException {
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
    }

    /**
     * Override the Import method to access the file via
     * @param path parameter
     */
    public void Import(String path) throws IOException{
        File file = new File(extractFilePath(path), extractFileName(path));
        Import(file);
    }

    /**
     * Removes all object equal to the parameter
     * @param e,  given object
     */
    public void removeAll(Event e) {
        for (Event event : Events) {
            if (event.equals(e))
                Events.remove(event);
        }
    }

    /**
     * reorders the Collection in a reverse order
     */
    public void reorder(){
        if(order){
        Events.sort(Comparator.reverseOrder());
        order = false;
        } else {
            Events.sort(Comparator.naturalOrder());
            order = true;
        }
    }

    /**
     * Pops from the Stack
     */
    public void removeLast(){
        if (!Events.isEmpty()) {
            Events.pop();
        } else System.out.println("Collection is empty");
    }

    /**
     * Removes the element with zero index
     */
    public void removeFirst(){
        if (!Events.isEmpty()) {
            Events.removeElementAt(0);
        } else System.out.println("Collection is empty");
    }

    /**
     * removes an element at i index
     * @param i index
     */
    public void remove(int i){
        if (!Events.isEmpty()) {
            Events.removeElementAt(i);
        } else System.out.println("Collection is empty");
    }

    /**
     * Removes an element by it's value
     * @param e Example event
     */
    public void remove(Event e){
        if (!Events.removeIf(a -> a.compareTo(e) == 0)){
            System.out.println("No such element");
        }
    }

    /**
     * Removes elements that are greater than e
     * @param e the first comparable
     */
    public void removeGreater(Event e){
        Events.removeIf(a -> a.compareTo(e) < 0);
    }

    /**
     * Removes elements that are less than e
     * @param e the first comparable
     */
    public void removeLower(Event e){
        Events.removeIf(a -> a.compareTo(e) > 0);
    }

    /**
     * Saves the collection to a file of it's CollectionHandler
     */
    public void save(){
        try {
            PrintWriter pw = new PrintWriter(file);
            String read = gson.toJson(Events);
            pw.print(read);
            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }


    } //remake

    /**
     * provides basic information about the collection
     */
    public void info(){
        System.out.println("Class: " + Events.getClass().getName());
        System.out.println("Initialized: " + initDate.getTime().toString());
        System.out.println("Capacity: " + Events.capacity());
        System.out.println("Size: " + Events.size());

    } //remake

    /**
     * prints all elements of the Collection to StdOut
     */
    public void contents(){
        if (!Events.isEmpty()) {
            for (Event event : Events) {
                if (event.name != null) {
                    System.out.println(event.name);
                } else {
                    System.out.println("null");
                }
            }
        } else System.out.println("Collection is empty");
    }

    /**
     * adds to the collection if it's greater than the maximum element
     * @param e an Event to push
     */
    public void addIfMax(Event e){
        boolean b = true;
        for (Event event : Events){
            if (e.compareTo(event)<0){
                b=false;
            }
        }
        if (b){
            Events.push(e);
        }

    }

    /**
     * adds e to the collection if it's less than the element
     * @param e an Event to push
     */
    public void addIfMin(Event e){
        boolean b = true;
        for (Event event : Events){
            if (e.compareTo(event)>0){
                b=false;
            }
        }
        if (b){
            Events.push(e);
        }

    }

    /**
     * pushes to the stack
     * @param e
     */
    public void add(Event e){
        Events.push(e);
    }

    /**
     * inserts an element e at the index i
     * @param e element
     * @param i index
     */
    public void insert(int i, Event e){
        Events.insertElementAt(e,i);
    }

    /**
     * removes all the elements and sets the size to 0
     */
    public void clear() {
        Events.removeAllElements();
    }

    /**
     * finishes the execution of the program
     */
    public void exit(){
        save();
    }

}
