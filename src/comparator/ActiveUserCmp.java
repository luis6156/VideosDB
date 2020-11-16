package comparator;

import user.User;
import video.Video;

import java.util.Comparator;

public class ActiveUserCmp implements Comparator<User> {
    // Ascending order
    @Override
    public int compare(User self, User other) {
        int result = Integer.compare(self.getActivity(), other.getActivity());
        if (result != 0) return result;
        return self.getUsername().compareTo(other.getUsername());
    }
}
