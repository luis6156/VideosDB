package comparator;

import actor.Actor;

import java.util.Comparator;

public class ActorAscCmp implements Comparator<Actor> {
    // Ascending order
    @Override
    public int compare(Actor self, Actor other) {
        return self.getName().compareTo(other.getName());
    }
}
