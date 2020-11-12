package action;

import database.UserDB;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.List;

public class Command {
    public static void chooseCommand(UserDB userDB, List<ActionInputData> actions, Writer fileWriter, JSONArray array) throws IOException {
        String message = null;
        for (ActionInputData action : actions) {
            switch (action.getType()) {
                case "favorite":
                    message = userDB.addFavorites(action.getUsername(), action.getTitle());
                    break;
                case "view":
                    message = userDB.addViews(action.getUsername(), action.getTitle());
                    break;
                default:
                    break;
            }
            array.add(array.size(), fileWriter.writeFile(action.getActionId(), "", message));
        }
    }
}
