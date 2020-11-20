package database;

import actor.Actor;
import actor.ActorsAwards;
import comparator.ActorAwardCmp;
import comparator.ActorNameCmp;
import comparator.ActorRatingCmp;
import fileio.ActorInputData;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActorDB {
    HashMap<String, Actor> actorDB = new HashMap<>();
    SortedSet<Actor> ratedActorsAsc = new TreeSet<>(new ActorRatingCmp(true));
    SortedSet<Actor> ratedActorsDesc = new TreeSet<>(new ActorRatingCmp(false));
    SortedSet<Actor> ascendingActors = new TreeSet<>(new ActorNameCmp(true));
    SortedSet<Actor> descendingActors = new TreeSet<>(new ActorNameCmp(false));
    SortedSet<Actor> awardedActorsAsc = new TreeSet<>(new ActorAwardCmp(true));
    SortedSet<Actor> awardedActorsDesc =
            new TreeSet<>(new ActorAwardCmp(false));

    public void populateActorDB(List<ActorInputData> actorDB) {
        for (ActorInputData actor : actorDB) {
            Actor newActor = new Actor(
                    actor.getName(),
                    actor.getCareerDescription(),
                    actor.getFilmography(),
                    actor.getAwards()
            );
            this.actorDB.put(newActor.getName(), newActor);
            ascendingActors.add(newActor);
            descendingActors.add(newActor);
            awardedActorsAsc.add(newActor);
            awardedActorsDesc.add(newActor);
        }
    }

    public void addRating(List<String> names, String title, double rating) {
        for (String name : names) {
            Actor tmp = actorDB.get(name);
            if (tmp != null) {
                ratedActorsAsc.remove(tmp);
                ratedActorsDesc.remove(tmp);
                tmp.addActorRating(title, rating);
                ratedActorsAsc.add(tmp);
                ratedActorsDesc.add(tmp);
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

    public List<Actor> prepareAwardedActors(List<Actor> actors,
                                            List<String> awards) {
        List<Actor> solution = new ArrayList<>();

        boolean found = false;
        for (Actor actor : actors) {
            for (String award : awards) {
                if (!actor.getAwards().containsKey(ActorsAwards.valueOf(award))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                solution.add(actor);
            }
            found = false;
        }

        return solution;
    }

    public boolean validDescription(Actor actor,
                                    List<String> description) {
        Matcher m;
        Pattern p;
        for (String s : description) {
            p = Pattern.compile(".*\\b" + Pattern.quote(s) + "\\b.*");
            m = p.matcher(actor.getCareerDescription().toLowerCase());
            if (!m.find()) {
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
                if (sortOrder.equals("desc")) {
                    for (Actor actor : ratedActorsDesc) {
                        if (actor.getActorRating() != 0) {
                            list.add(actor.getName());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                } else {
                    for (Actor actor : ratedActorsAsc) {
                        if (actor.getActorRating() != 0) {
                            list.add(actor.getName());
                        }
                        if (list.size() == k) {
                            break;
                        }
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
                if (sortOrder.equals("desc")) {
                    for (Actor actor : awardedActorsDesc) {
                        if (validAwards(actor, awards)) {
                            list.add(actor.getName());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                } else {
                    for (Actor actor : awardedActorsAsc) {
                        if (validAwards(actor, awards)) {
                            list.add(actor.getName());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                }
                break;
            default:
                break;
        }
        return list.toString();
    }
}
