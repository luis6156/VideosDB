package comparator;

import actor.Actor;

import java.util.Comparator;

public class ActorAwardCmp implements Comparator<Actor> {
    // Ascending order
    @Override
    public int compare(final Actor self, final Actor other) {
        int result, sumSelf = 0, sumOther = 0;

        for (Integer awards : self.getAwards().values()) {
            sumSelf += awards;
        }
        for (Integer awards : other.getAwards().values()) {
            sumOther += awards;
        }

        result = Integer.compare(sumSelf, sumOther);

        if (result != 0) {
            return result;
        }

        return self.getName().compareTo(other.getName());
    }
}
