package action;

import database.MovieDB;
import database.ShowDB;
import database.UserDB;
import database.VideoDB;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.IOException;

public class Recommend {
    public static void chooseRec(VideoDB videoDB, MovieDB movieDB,
                                 ShowDB showDB,
                                 UserDB userDB, ActionInputData action,
                                 Writer fileWriter, JSONArray array) throws IOException {
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
                if (userDB.checkUserSub(username)) {
                    message =
                            videoDB.getPopularVideo(userDB.getHistory(username));
                } else {
                    message = "PopularRecommendation cannot be applied!";
                }
                break;
            case "favorite":
                if (userDB.checkUserSub(username)) {
                    message =
                            videoDB.getFavoriteVideo(userDB.getHistory(username));
                } else {
                    message = "FavoriteRecommendation cannot be applied!";
                }
                break;
            case "search":
                if (userDB.checkUserSub(username)) {
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
        array.add(array.size(), fileWriter.writeFile(action.getActionId(), ""
                , message));
    }
}
