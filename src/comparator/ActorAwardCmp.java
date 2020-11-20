package comparator;

import actor.Actor;

import java.util.Comparator;

public class ActorAwardCmp implements Comparator<Actor> {
    boolean isAscending;

    public ActorAwardCmp(boolean isAscending) {
        this.isAscending = isAscending;
    }

    // Ascending/Descending order
    @Override
    public int compare(Actor self, Actor other) {
        int result, sumSelf = 0, sumOther = 0;

        for (Integer awards : self.getAwards().values()) {
            sumSelf += awards;
        }
        for (Integer awards : other.getAwards().values()) {
            sumOther += awards;
        }

        result = Integer.compare(sumSelf, sumOther);

        if (result != 0) {
            if (isAscending) {
                return result;
            }
            return -result;
        }

        if (isAscending) {
            return self.getName().compareTo(other.getName());
        } else {
            return -self.getName().compareTo(other.getName());
        }
    }
}
