package database;

import actor.Actor;
import actor.ActorsAwards;
import comparator.ActorAwardCmp;
import comparator.ActorNameCmp;
import comparator.ActorRatingCmp;
import fileio.ActorInputData;
import utils.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActorDB {
    private final HashMap<String, Actor> actorDB = new HashMap<>();
    private final TreeSet<Actor> ratedActors =
            new TreeSet<>(new ActorRatingCmp());
    private final TreeSet<Actor> ascendingActors =
            new TreeSet<>(new ActorNameCmp());
    private final TreeSet<Actor> awardedActors = new TreeSet<>(new ActorAwardCmp());

    public void populateActorDB(final List<ActorInputData> actorDB) {
        for (ActorInputData actor : actorDB) {
            Actor newActor = new Actor(
                    actor.getName(),
                    actor.getCareerDescription(),
                    actor.getFilmography(),
                    actor.getAwards()
            );
            this.actorDB.put(newActor.getName(), newActor);
            ascendingActors.add(newActor);
            awardedActors.add(newActor);
        }
    }

    public void addRating(final List<String> names, final String title,
                          final double rating) {
        for (String name : names) {
            Actor tmp = actorDB.get(name);
            if (tmp != null) {
                ratedActors.remove(tmp);
                tmp.addActorRating(title, rating);
                ratedActors.add(tmp);
            }
        }
    }

    private boolean validAwards(final Actor actor, final List<String> awards) {
        for (String award : awards) {
            if (!actor.getAwards().containsKey(Utils.stringToAwards(award))) {
                return false;
            }
        }
        return true;
    }

    private boolean validDescription(final Actor actor,
                                     final List<String> description) {
        Matcher m;
        Pattern p;
        for (String s : description) {
            p = Pattern.compile("[ ,!.'(-]" + Pattern.quote(s) + "[ ,!.')-]", Pattern.CASE_INSENSITIVE);
            m = p.matcher(actor.getCareerDescription());
            if (!m.find()) {
                return false;
            }
        }
        return true;
    }

    public String getTopK(final String query,
                          final String sortOrder, final List<String> description,
                          final List<String> awards, final int k) {
        Actor tmp;
        Iterator<Actor> iterator;
        List<String> list = new ArrayList<>();
        switch (query) {
            case "average":
                if (sortOrder.equals("desc")) {
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
                if (sortOrder.equals("desc")) {
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
                if (sortOrder.equals("desc")) {
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
