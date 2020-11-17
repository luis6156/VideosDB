package comparator;

import actor.Actor;

import java.util.Comparator;

public class ActorDescCmp implements Comparator<Actor> {
    // Descending order
    @Override
    public int compare(Actor self, Actor other) {
        return -self.getName().compareTo(other.getName());
    }
}
