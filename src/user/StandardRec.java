package user;

import database.UserDB;
import database.VideoDB;

public class StandardRec {
    public static String standard(VideoDB videoDB, UserDB userDB,
                               String username) {
        return videoDB.getUnwatchedVideo(userDB.getHistory(username));
    }
}
