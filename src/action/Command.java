package action;

import database.UserDB;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.IOException;

public class Command {
    public static void chooseCommand(UserDB userDB, ActionInputData action, Writer fileWriter, JSONArray array) throws IOException {
        String message = null;
        switch (action.getType()) {
            case "favorite":
                message = userDB.addFavorites(action.getUsername(), action.getTitle());
                break;
            case "view":
                message = userDB.addViews(action.getUsername(), action.getTitle());
                break;
            case "rating":
                if (action.getSeasonNumber() == 0) {
                    message = userDB.addRatingMovie(action.getUsername(),
                            action.getTitle(), action.getGrade());
                } else {
                    message = userDB.addRatingShow(action.getUsername(),
                            action.getTitle(),
                            action.getSeasonNumber(), action.getGrade());
                }
                break;
            default:
                break;
        }
        array.add(array.size(), fileWriter.writeFile(action.getActionId(), "", message));
    }
}
