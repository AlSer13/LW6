package Plot;

import java.util.ArrayList;
import java.util.List;

public class Home extends Place {
    private List<WTPcharacter> owner = new ArrayList<>(); //обитатели дома
    Home(String name, WTPcharacter owner){
        super(name);
        this.owner.add(owner);
    }

    void addOwner (WTPcharacter owner){
        if (!this.owner.contains(owner))
            this.owner.add(owner);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Home home = (Home) o;

        return owner != null ? owner.equals(home.owner) : home.owner == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }
}


