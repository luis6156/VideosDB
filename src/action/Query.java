package action;

import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;
import video.MovieDB;
import video.ShowDB;

import java.io.IOException;

public class Query {
    public static void chooseQuery(MovieDB movieDB, ShowDB showDB,
                                   ActionInputData action,
                                   Writer fileWriter, JSONArray array) throws IOException {
        String message = null;
        switch (action.getObjectType()) {
            case "movies":
                message = movieDB.getTopK(
                            action.getCriteria(),
                            action.getFilters().get(0).get(0),
                            action.getFilters().get(1).get(0),
                            action.getNumber());
                break;
            case "shows":
                message = showDB.getTopK(
                            action.getCriteria(),
                            action.getFilters().get(0).get(0),
                            action.getFilters().get(1).get(0),
                            action.getNumber());
                break;
            default:
                message = "[]";
                break;
        }
        array.add(array.size(), fileWriter.writeFile(action.getActionId(), ""
                , "Query result: " + message));
    }
}
