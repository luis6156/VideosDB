package comparator;

import user.User;

import java.util.Comparator;

public class UserActivityCmp implements Comparator<User> {
    // Ascending order (activity, alphabetical)
    @Override
    public int compare(final User self, final User other) {
        int result = Integer.compare(self.getActivity(), other.getActivity());

        // Check if first criteria fails
        if (result != 0) {
            return result;
        }

        // Second criteria
        return self.getUsername().compareTo(other.getUsername());
    }
}
