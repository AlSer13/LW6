package Plot;

public class Place {
    public String name;
   // private List<Place> nearby = new ArrayList<>();
   // public Nameplate nameplate;
    public Place(String name){
        this.name = name;
    }

   /* public void addNeighbours(Place... nearby){
        for(Place neighbour : nearby) {
            if (!this.nearby.contains(neighbour)){
                this.nearby.add(neighbour);
                neighbour.nearby.add(this);
            }

        }
    }
    public boolean isNeighbour(Place neighbour){
        return this.nearby.contains(neighbour);
    }

    public void showNeighbours() {
        for(Place neighbour : nearby){
            System.out.println(neighbour.name);
        }
    }

    public void showNeighbours(int i) {
        System.out.println(nearby.get(i).name);
    }

    public String getName() {
        return name;
    }

    class Nameplate { //табличка или указатель
        Place Location = Place.this;
        String inscription;
        WTPcharacter author;

        public Nameplate(Place location, String inscription, WTPcharacter author) {
            Location = location;
            this.inscription = inscription;
            this.author = author;
        }
    }*/

    //equals and hashcode


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        return name != null ? name.equals(place.name) : place.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
