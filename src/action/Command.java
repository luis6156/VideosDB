package action;

import database.*;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.IOException;

public class Command {
    public static void chooseCommand(ActorDB actorDB,
                                     VideoDB videoDB, MovieDB movieDB,
                                     ShowDB showDB,
                                     UserDB userDB,
                                     ActionInputData action, Writer fileWriter, JSONArray array) throws IOException {
        String title = action.getTitle();
        String message = null;
        switch (action.getType()) {
            case "favorite":
                message = userDB.addFavorites(movieDB, showDB,
                        action.getUsername(),
                        title);
                break;
            case "view":
                message = userDB.addViews(videoDB, movieDB, showDB,
                        action.getUsername(),
                        title);
                break;
            case "rating":
                if (action.getSeasonNumber() == 0) {
                    message = userDB.addRatingMovie(actorDB, videoDB, movieDB,
                            action.getUsername(),
                            title, action.getGrade());
                } else {
                    message = userDB.addRatingShow(actorDB ,videoDB, showDB,
                            action.getUsername(),
                            title,
                            action.getSeasonNumber(), action.getGrade());
                }
                break;
            default:
                break;
        }
        array.add(array.size(), fileWriter.writeFile(action.getActionId(), "", message));
    }
}
