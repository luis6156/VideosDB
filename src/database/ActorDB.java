package database;

import actor.Actor;
import actor.ActorsAwards;
import comparator.ActorAwardCmp;
import comparator.ActorAscCmp;
import comparator.ActorDescCmp;
import comparator.ActorRatingCmp;
import fileio.ActorInputData;

import java.util.*;

public class ActorDB {
    HashMap<String, Actor> actorDB = new HashMap<>();
    SortedSet<Actor> ratedActors = new TreeSet<>(new ActorRatingCmp());
    SortedSet<Actor> awardedActors = new TreeSet<>(new ActorAwardCmp());
    SortedSet<Actor> ascendingActors = new TreeSet<>(new ActorAscCmp());
    SortedSet<Actor> descendingActors = new TreeSet<>(new ActorDescCmp());

    public void populateActorDB(List<ActorInputData> actorDB) {
        for (ActorInputData actor : actorDB) {
            Actor newActor = new Actor(
                    actor.getName(),
                    actor.getCareerDescription(),
                    actor.getFilmography(),
                    actor.getAwards()
            );
            this.actorDB.put(newActor.getName(), newActor);
            awardedActors.add(newActor);
            ascendingActors.add(newActor);
            descendingActors.add(newActor);
        }
    }

    public void addRating(List<String> names, String title, double rating) {
        for (String name : names) {
            Actor tmp = actorDB.get(name);
            if (tmp != null) {
                ratedActors.remove(tmp);
                tmp.addActorRating(title, rating);
                ratedActors.add(tmp);
            }
        }
    }

    public boolean validAwards(Actor actor, List<String> awards) {
        for (String award : awards) {
            if (!actor.getAwards().containsKey(ActorsAwards.valueOf(award))) {
                return false;
            }
        }
        return true;
    }

    public boolean validDescription(Actor actor,
                                    List<String> description) {
        for (String s : description) {
            if (!actor.getCareerDescription().contains(s)) {
                return false;
            }
        }
        return true;
    }

    public String getTopK(String query,
                          String sortOrder, List<String> description,
                          List<String> awards, int k) {
        List<String> list = new ArrayList<>();
        switch (query) {
            case "average":
                for (Actor actor : ratedActors) {
                    if (actor.getActorRating() != 0) {
                        list.add(actor.getName());
                    }
                    if (list.size() == k) {
                        break;
                    }
                }
                break;
            case "filter_description":
                if (sortOrder.equals("desc")) {
                    for (Actor actor : descendingActors) {
                        if (validDescription(actor, description)) {
                            list.add(actor.getName());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                } else {
                    for (Actor actor : ascendingActors) {
                        if (validDescription(actor, description)) {
                            list.add(actor.getName());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                }
                break;
            case "awards":
                for (Actor actor : awardedActors) {
                    if (validAwards(actor, awards)) {
                        list.add(actor.getName());
                    }
                    if (list.size() == k) {
                        break;
                    }
                }
                break;
            default:
                break;
        }
        return list.toString();
    }
}
