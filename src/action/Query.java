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
    public static void chooseQuery(ActorDB actorDB, MovieDB movieDB,
                                   ShowDB showDB,
                                   UserDB userDB,
                                   ActionInputData action,
                                   Writer fileWriter, JSONArray array) throws IOException {
        String message = switch (action.getObjectType()) {
            case "movies" -> movieDB.getTopK(
                    action.getCriteria(),
                    action.getFilters().get(0).get(0),
                    action.getFilters().get(1).get(0),
                    action.getNumber()).toString();
            case "shows" -> showDB.getTopK(
                    action.getCriteria(),
                    action.getFilters().get(0).get(0),
                    action.getFilters().get(1).get(0),
                    action.getNumber()).toString();
            case "users" -> userDB.getTopK(action.getNumber());
            case "actors" -> actorDB.getTopK(
                    action.getCriteria(),
                    action.getSortType(),
                    action.getFilters().get(2),
                    action.getFilters().get(3),
                    action.getNumber()
            );
            default -> "[]";
        };
        array.add(array.size(), fileWriter.writeFile(action.getActionId(), ""
                , "Query result: " + message));
    }
}
