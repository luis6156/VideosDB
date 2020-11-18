package action;

import database.*;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.IOException;

public class Command {
    public static void chooseCommand(ActorDB actorDB,
                                     VideoDB videoDB, MovieDB movieDB,
                                     ShowDB showDB,
                                     UserDB userDB,
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
                    movieDB.addViews(videoDB, title);
                } else if (showDB.isShow(title)) {
                    showDB.addViews(videoDB, title);
                } else {
                    break;
                }
                message = userDB.addViews(action.getUsername(), title);
                break;
            case "rating":
                if (action.getSeasonNumber() == 0) {
                    movieDB.addRating(title, action.getGrade());
                    actorDB.addRating(movieDB.getMovieActors(title), movieDB,
                            showDB);
                    message = userDB.addRatingMovie(action.getUsername(),
                            title, action.getGrade());
                } else {
                    showDB.addRating(title, action.getSeasonNumber(),
                            action.getGrade());
                    actorDB.addRating(showDB.getShowActors(title), movieDB,
                            showDB);
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
