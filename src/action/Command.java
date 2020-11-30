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

public class Command {
    /**
     * Method used to add to output Command Action's success. If action type is unrecognised
     * adds null to output.
     *
     * @param actorDB    updates ActorDB
     * @param videoDB    updates VideoDB
     * @param movieDB    updates MovieDB
     * @param showDB     updates ShowDB
     * @param userDB     updates UserDB
     * @param action     used for input criteria
     * @param fileWriter used to return JSONObject for output
     * @param array      used for writing to output
     * @throws IOException file IO exceptions
     */
    public static void chooseCommand(final ActorDB actorDB, final VideoDB videoDB,
                                     final MovieDB movieDB, final ShowDB showDB,
                                     final UserDB userDB, final ActionInputData action,
                                     final Writer fileWriter,
                                     final JSONArray array) throws IOException {
        String title = action.getTitle();
        String message = null;
        switch (action.getType()) {
            case "favorite":
                message = userDB.addFavorites(videoDB, movieDB, showDB, action.getUsername(),
                        title);
                break;
            case "view":
                message = userDB.addViews(videoDB, movieDB, showDB, action.getUsername(), title);
                break;
            case "rating":
                if (action.getSeasonNumber() == 0) {
                    message = userDB.addRatingMovie(actorDB, videoDB, movieDB,
                            action.getUsername(), title, action.getGrade());
                } else {
                    message = userDB.addRatingShow(actorDB, videoDB, showDB, action.getUsername(),
                            title, action.getSeasonNumber(), action.getGrade());
                }
                break;
            default:
                break;
        }
        array.add(array.size(), fileWriter.writeFile(action.getActionId(), "", message));
    }
}
