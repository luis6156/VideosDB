package action;

import database.UserDB;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;
import video.MovieDB;
import video.ShowDB;
import video.Video;

import java.io.IOException;
import java.util.List;

public class Action {
    public static void chooseAction(MovieDB movieDB, ShowDB showDB,
                                    UserDB userDB,
                                    List<ActionInputData> actions, Writer fileWriter, JSONArray array) throws IOException {
        for (ActionInputData action : actions) {
            switch (action.getActionType()) {
                case "command":
                    Command.chooseCommand(movieDB, showDB, userDB, action,
                            fileWriter,
                            array);
                    break;
                case "query":
                    Query.chooseQuery(movieDB, showDB, userDB, action,
                            fileWriter,
                            array);
                    break;
                default:
                    break;
            }
        }
    }
}
