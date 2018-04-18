package Plot;

public class ToyCharacter extends WTPcharacter {
    public Modifyer Presumable_WTP_Modifyer;
    private Animal animal;
    public Modifyer mod;

    public ToyCharacter(String animal, String name) {
        super(name);
        animal = animal.toUpperCase();
        this.animal = Animal.valueOf(animal);
    }

    public ToyCharacter(String animal, String name, Home home) {
        super(name, home);
        animal = animal.toUpperCase();
        this.animal = Animal.valueOf(animal);
    }

    {
        this.mod = Modifyer.NORMAL;
    }

    public void express_Opinion_About_WTP_Modifyer() {
        say("Я думаю что Винни-Пух — "+Presumable_WTP_Modifyer.toString()+" медведь.");
    }

    public enum Modifyer {
        I_DA,
        SUPER,
        MEGA,
        NORMAL
    }

    public enum Animal {
        PIG,
        RABBIT,
        TIGER,
        KANGAROO,
        BEAR
    }

//equals and hashcode


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ToyCharacter that = (ToyCharacter) o;

        if (Presumable_WTP_Modifyer != that.Presumable_WTP_Modifyer) return false;
        if (animal != that.animal) return false;
        return mod == that.mod;
    }

    @Override
    public int hashCode() {
        int result = Presumable_WTP_Modifyer != null ? Presumable_WTP_Modifyer.hashCode() : 0;
        result = 31 * result + (animal != null ? animal.hashCode() : 0);
        result = 31 * result + (mod != null ? mod.hashCode() : 0);
        return result;
    }
}