package comparator;

import user.User;

import java.util.Comparator;

public class UserActivityCmp implements Comparator<User> {
    private final boolean isAscending;

    public UserActivityCmp(boolean isAscending) {
        this.isAscending = isAscending;
    }

    // Ascending/Descending order
    @Override
    public int compare(User self, User other) {
        int result;

        result = Integer.compare(self.getActivity(), other.getActivity());

        if (result != 0) {
            if (isAscending) {
                return result;
            }
            return -result;
        }

        if (isAscending) {
            return self.getUsername().compareTo(other.getUsername());
        }
        return -self.getUsername().compareTo(other.getUsername());
    }
}
