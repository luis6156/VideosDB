package action;

import database.ActorDB;
import database.UserDB;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;
import database.MovieDB;
import database.ShowDB;

import java.io.IOException;

public class Query {
    /**
     * Method used to add to output Query Action's success. If action type is unrecognised
     * adds "[]" to output.
     *
     * @param actorDB    used for Actor Query
     * @param movieDB    used for Video Query
     * @param showDB     used for Show Query
     * @param userDB     used for User Query
     * @param action     used for input criteria
     * @param fileWriter used to return JSONObject for output
     * @param array      used for writing to output
     * @throws IOException file IO exceptions
     */
    public static void chooseQuery(final ActorDB actorDB, final MovieDB movieDB,
                                   final ShowDB showDB, final UserDB userDB,
                                   final ActionInputData action, final Writer fileWriter,
                                   final JSONArray array) throws IOException {
        String message = switch (action.getObjectType()) {
            case "movies" -> movieDB.getTopK(
                    action.getCriteria(),
                    action.getSortType(),
                    action.getFilters().get(0).get(0),
                    action.getFilters().get(1).get(0),
                    action.getNumber());
            case "shows" -> showDB.getTopK(
                    action.getCriteria(),
                    action.getSortType(),
                    action.getFilters().get(0).get(0),
                    action.getFilters().get(1).get(0),
                    action.getNumber());
            case "users" -> userDB.getTopK(action.getSortType(),
                    action.getNumber());
            case "actors" -> actorDB.getTopK(
                    action.getCriteria(),
                    action.getSortType(),
                    action.getFilters().get(2),
                    action.getFilters().get(3),
                    action.getNumber()
            );
            default -> "[]";
        };
        array.add(array.size(), fileWriter.writeFile(action.getActionId(), "",
                "Query result: " + message));
    }
}
