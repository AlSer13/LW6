package Plot;

import java.io.Serializable;
import java.util.*;

public class Event implements Comparable<Event>, Serializable {
    private ArrayList<WTPcharacter> Participants = new ArrayList<>();
    transient public GregorianCalendar date;
    public Place place;
    public String name;

    public Event(String name, Place place, GregorianCalendar date, WTPcharacter... participants) {
        this.name = name;
        this.place = place;
        this.date = date;
        this.Participants.addAll(Arrays.asList(participants));
    }

    protected List<WTPcharacter> getParticipants() {
        return Participants;
    }

    /* public void addParticipant(WTPcharacter participant) throws TooBuisyException {
         GregorianCalendar end = date;
         end.add(Calendar.HOUR, 1);
         if (participant.isFree(date, end)) {
             this.Participants.add(participant);
         } else throw new TooBuisyException();
     }*/
    public void go() throws NullPointerException {

        if (Participants != null) {
            Participants
                    .stream()
                    .filter(Objects::nonNull)
                    .forEachOrdered((p) -> {
                        System.out.println(p.getName() + " is going to do "
                                + this.name
                                + "with ");
                        Participants.stream()
                                .filter(Objects::nonNull)
                                .filter(p1 -> !p1.equals(p))
                                .forEach(p1 -> System.out.println(p1.getName()));
                        System.out.println((this.place == null || this.place.name == null) ? "nowhere" : "In" + this.place.name);
                    });
        } else System.out.println("Nobody's going to do " + this.name);
        //to override
    }

    public GregorianCalendar getDate() {
        return date;
    }

    @Override
    public int compareTo(Event o) {
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (date != null ? !date.equals(event.date) : event.date != null) return false;
        if (place != null ? !place.equals(event.place) : event.place != null) return false;
        return name != null ? name.equals(event.name) : event.name == null;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (place != null ? place.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
