package Plot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

public class Event implements Comparable<Event> {
    private List<WTPcharacter> Participants = new ArrayList<>();
    public GregorianCalendar date;
    public Place place;
    public String name;

    public Event(String name, Place place, GregorianCalendar date, WTPcharacter... participants){
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

    public GregorianCalendar getDate() {
        return date;
    }

    @Override
    public int compareTo(Event o) {
        return date.compareTo(o.getDate());
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
