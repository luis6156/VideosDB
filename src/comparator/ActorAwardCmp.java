package comparator;

import actor.Actor;

import java.util.Comparator;

public class ActorAwardCmp implements Comparator<Actor> {
    // Descending order
    @Override
    public int compare(Actor self, Actor other) {
        int result = Integer.compare(self.getAwards().size(),
                other.getAwards().size());
        if (result != 0) return -result;
        return self.getName().compareTo(other.getName());
    }
}
