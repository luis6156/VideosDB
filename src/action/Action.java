package action;

import database.*;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.List;

public class Action {
    public static void chooseAction(VideoDB videoDB, ActorDB actorDB,
                                    MovieDB movieDB,
                                    ShowDB showDB,
                                    UserDB userDB,
                                    List<ActionInputData> actions, Writer fileWriter, JSONArray array) throws IOException {
        for (ActionInputData action : actions) {
            switch (action.getActionType()) {
                case "command":
                    Command.chooseCommand(actorDB, movieDB, showDB, userDB,
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
                    Recommend.chooseRec(videoDB, movieDB, showDB, userDB,
                            action, fileWriter, array);
                    break;
                default:
                    break;
            }
        }
    }
}
