package action;

import database.ActorDB;
import database.MovieDB;
import database.ShowDB;
import database.VideoDB;
import database.UserDB;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.List;

public class Action {
    public static void chooseAction(final VideoDB videoDB, final ActorDB actorDB,
                                    final MovieDB movieDB,
                                    final ShowDB showDB,
                                    final UserDB userDB,
                                    final List<ActionInputData> actions,
                                    final Writer fileWriter,
                                    final JSONArray array) throws IOException {
        for (ActionInputData action : actions) {
            switch (action.getActionType()) {
                case "command":
                    Command.chooseCommand(actorDB, videoDB, movieDB, showDB,
                            userDB,
                            action,
                            fileWriter,
                            array);
                    break;
                case "query":
                    Query.chooseQuery(actorDB, movieDB, showDB, userDB, action,
                            fileWriter,
                            array);
                    break;
                case "recommendation":
                    Recommend.chooseRec(videoDB, userDB,
                            action, fileWriter, array);
                    break;
                default:
                    break;
            }
        }
    }
}
