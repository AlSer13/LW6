package Plot;

import java.io.Serializable;

public class WTPcharacter implements Moveable, Serializable {
    private String name;
    private Place location;
    //private Home home;
    //private TreeMap<GregorianCalendar, Event> plans = new TreeMap<>();

    public WTPcharacter(String name) { //создаем персонажа и его собственный дом
        this.name = name;
        //Home home = new Home(this.name + "'s home", this);
        //this.home = home;
        //this.location = home; //местоположение по умолчанию - дом
    }

    WTPcharacter(String name, Home home) { //создаем персонажа и селим его в существующий дом
        this.name = name;
        //this.setHome(home);
      //  this.location = home;
    }

    public void moveTo(Place location) {
        this.location = location;
        System.out.println(this.getName() + " перемещается в " + location.name);
    }

 /*   public void SetANameplate(Place location, String inscription){
        location.nameplate = location.new Nameplate(location, inscription, this);
    }*/

    public String getName() {
        return name;
    }

   /* public Home getHome() {
        return home;
    }

    private void setHome(Home home) {
        if ((this.home == null) || (!this.home.equals(home))) { //если персонаж не живет в этом доме
            this.home = home; //поселить его туда
            home.addOwner(this); //и добавить его в список владельцев дома
        }
    }*/

    public void say(Phrase p) {
        p.say();
    }

    public void say(String text) {
        say(() -> System.out.println(this.name + ": " + text));
    }
    /*public void ask(WTPcharacter who, String s){
            this.say(who.name + s);
            System.out.println("Что же ответит " + who.getName() + "?");
            do {
                Scanner in = new Scanner(System.in);
                who.answer = in.next();
            } while(!ANSWERS.contains(who.answer));
    }

    //plans

    /*Event getPlans(GregorianCalendar d) {
        return plans.get(d);
    }

    public void addPlan(Event e) throws TooBuisyException {
        GregorianCalendar d = e.getDate();
        e.addParticipant(this);
        this.plans.put(d, e);
    }

    public boolean isFree(GregorianCalendar beg, GregorianCalendar end) {
        GregorianCalendar floor = plans.floorKey(beg);
        GregorianCalendar ceiling = plans.ceilingKey(end);
        return (floor==null)||(end == null)||(ceiling.after(end)||(beg.after(floor)));
    }*/
    // equals and hashcode


}