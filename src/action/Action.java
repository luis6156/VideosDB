package action;

import database.UserDB;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.List;

public class Action {
    public static void chooseAction(UserDB userDB, List<ActionInputData> actions, Writer fileWriter, JSONArray array) throws IOException {
        for (ActionInputData action : actions) {
            switch (action.getActionType()) {
                case "command":
                    Command.chooseCommand(userDB, action, fileWriter, array);
                    break;
                case "query":
                    break;
                default:
                    break;
            }
        }
    }
}
