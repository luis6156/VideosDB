package comparator;

import user.User;

import java.util.Comparator;

public class UserActivityCmp implements Comparator<User> {
    // Ascending order
    @Override
    public int compare(final User self, final User other) {
        int result;

        result = Integer.compare(self.getActivity(), other.getActivity());

        if (result != 0) {
            return result;
        }

        return self.getUsername().compareTo(other.getUsername());
    }
}
