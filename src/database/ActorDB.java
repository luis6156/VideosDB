package database;

import actor.Actor;
import comparator.ActorAwardCmp;
import comparator.ActorNameCmp;
import comparator.ActorRatingCmp;
import fileio.ActorInputData;
import utils.Utils;


import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActorDB {
    private final HashMap<String, Actor> actorDB = new HashMap<>();
    private final TreeSet<Actor> ratedActors = new TreeSet<>(new ActorRatingCmp());
    private final TreeSet<Actor> ascendingActors = new TreeSet<>(new ActorNameCmp());
    private final TreeSet<Actor> awardedActors = new TreeSet<>(new ActorAwardCmp());

    /**
     * Constructor that assigns values to Actor class attributes and updates databases with its
     * data.
     *
     * @param actors list of MovieInputData to be translated to Movie class
     */
    public void populateActorDB(final List<ActorInputData> actors) {
        for (ActorInputData actor : actors) {
            Actor newActor = new Actor(
                    actor.getName(),
                    actor.getCareerDescription(),
                    actor.getFilmography(),
                    actor.getAwards()
            );
            // Put Actor into ActorDB
            actorDB.put(newActor.getName(), newActor);
            // Add actor to ascendingActors TreeSet (Filter Description Query, alphabetical order)
            ascendingActors.add(newActor);
            // Add actor to awardedActors TreeSet (Awards Query)
            awardedActors.add(newActor);
        }
    }

    /**
     * Adds rating to all actors from a video and recalculates their total rating.
     *
     * @param names  list of actors' names
     * @param title  title of the video that was rated
     * @param rating value of the rating
     */
    public void addRating(final List<String> names, final String title, final double rating) {
        for (String name : names) {
            Actor tmp = actorDB.get(name);
            // If actor is in database update its total rating
            if (tmp != null) {
                ratedActors.remove(tmp);
                tmp.addActorRating(title, rating);
                ratedActors.add(tmp);
            }
        }
    }

    /**
     * Checks if actor has all awards (Awards Query)
     *
     * @param actor  actor to be queried
     * @param awards list of awards filter
     * @return true if actor has all awards, otherwise false
     */
    private boolean validAwards(final Actor actor, final List<String> awards) {
        for (String award : awards) {
            if (!actor.getAwards().containsKey(Utils.stringToAwards(award))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if actor has all keywords in description using regex (Filter Description Query)
     *
     * @param actor       actor to be queried
     * @param description list of keywords filter
     * @return true if actor has all keywords in description, otherwise false
     */
    private boolean validDescription(final Actor actor, final List<String> description) {
        Matcher m;
        Pattern p;
        for (String s : description) {
            // Prefixes and suffixes that a word could have, case insensitive
            p = Pattern.compile("[ ,!.'(-]" + Pattern.quote(s) + "[ ,!.')-]",
                    Pattern.CASE_INSENSITIVE);
            m = p.matcher(actor.getCareerDescription());
            if (!m.find()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Function used for Actor Query command.
     *
     * @param query       query type (average, filter_description, awards)
     * @param orderType   order type (asc/desc)
     * @param description description keywords filter for the actor
     * @param awards      awards filter for the actor
     * @param k           number of actors to be returned
     * @return query operation or null if wrong query
     */
    public String getTopK(final String query, final String orderType,
                          final List<String> description, final List<String> awards, final int k) {
        Actor tmp;
        Iterator<Actor> iterator;
        List<String> list = new ArrayList<>();

        /*
        Select TreeSet based on query. If order type is descending, iterate from last element,
        otherwise from head. If the filters apply to the actor, add it to the solution and check
        "k" boundary of solution.
         */
        switch (query) {
            case "average":
                if (orderType.equals("desc")) {
                    iterator = ratedActors.descendingIterator();
                } else {
                    iterator = ratedActors.iterator();
                }
                while (iterator.hasNext()) {
                    tmp = iterator.next();
                    list.add(tmp.getName());
                    if (list.size() == k) {
                        return list.toString();
                    }
                }
                return list.toString();
            case "filter_description":
                if (orderType.equals("desc")) {
                    iterator = ascendingActors.descendingIterator();
                } else {
                    iterator = ascendingActors.iterator();
                }
                while (iterator.hasNext()) {
                    tmp = iterator.next();
                    if (validDescription(tmp, description)) {
                        list.add(tmp.getName());
                    }
                    if (list.size() == k) {
                        return list.toString();
                    }
                }
                return list.toString();
            case "awards":
                if (orderType.equals("desc")) {
                    iterator = awardedActors.descendingIterator();
                } else {
                    iterator = awardedActors.iterator();
                }
                while (iterator.hasNext()) {
                    tmp = iterator.next();
                    if (validAwards(tmp, awards)) {
                        list.add(tmp.getName());
                    }
                    if (list.size() == k) {
                        return list.toString();
                    }
                }
                return list.toString();
            default:
                return null;
        }
    }
}
