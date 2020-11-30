package action;

import database.UserDB;
import database.VideoDB;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.IOException;

public class Recommend {
    public static void chooseRec(final VideoDB videoDB,
                                 final UserDB userDB,
                                 final ActionInputData action,
                                 final Writer fileWriter,
                                 final JSONArray array) throws IOException {
        String username = action.getUsername();
        String message = null;
        switch (action.getType()) {
            case "standard":
                message =
                        videoDB.getUnwatchedVideo(userDB.getHistory(username));
                break;
            case "best_unseen":
                message = videoDB.getBestVideo(userDB.getHistory(username));
                break;
            case "popular":
                if (userDB.checkUserType(username)) {
                    message =
                            videoDB.getPopularVideo(userDB.getHistory(username));
                } else {
                    message = "PopularRecommendation cannot be applied!";
                }
                break;
            case "favorite":
                if (userDB.checkUserType(username)) {
                    message =
                            videoDB.getFavoriteVideo(userDB.getHistory(username));
                } else {
                    message = "FavoriteRecommendation cannot be applied!";
                }
                break;
            case "search":
                if (userDB.checkUserType(username)) {
                    message =
                            videoDB.getSearchedVideo(action.getGenre(),
                                    userDB.getHistory(username));
                } else {
                    message = "SearchRecommendation cannot be applied!";
                }
                break;
            default:
                break;
        }
        array.add(array.size(), fileWriter.writeFile(action.getActionId(), "",
                message));
    }
}
