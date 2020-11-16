package action;

import database.UserDB;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;
import video.MovieDB;
import video.ShowDB;

import java.io.IOException;

public class Command {
    public static void chooseCommand(MovieDB movieDB, ShowDB showDB, UserDB userDB,
                                     ActionInputData action, Writer fileWriter, JSONArray array) throws IOException {
        String title = action.getTitle();
        String message = null;
        switch (action.getType()) {
            case "favorite":
                if (movieDB.isMovie(title)) {
                    movieDB.addFavorites(title);
                } else if (showDB.isShow(title)) {
                    showDB.addFavorites(title);
                } else {
                    break;
                }
                message = userDB.addFavorites(action.getUsername(),
                        title);
                break;
            case "view":
                if (movieDB.isMovie(title)) {
                    movieDB.addViews(title);
                } else if (showDB.isShow(title)) {
                    showDB.addViews(title);
                } else {
                    break;
                }
                message = userDB.addViews(action.getUsername(), title);
                break;
            case "rating":
                if (action.getSeasonNumber() == 0) {
                    movieDB.addRating(title, action.getGrade());
                    message = userDB.addRatingMovie(action.getUsername(),
                            title, action.getGrade());
                } else {
                    showDB.addRating(title, action.getSeasonNumber(),
                            action.getGrade());
                    message = userDB.addRatingShow(action.getUsername(),
                            title,
                            action.getSeasonNumber(), action.getGrade());
                }
                break;
            default:
                break;
        }
        array.add(array.size(), fileWriter.writeFile(action.getActionId(), "", message));
    }
}
