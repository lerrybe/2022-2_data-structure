/*
 * Six Degrees of Kevin Bacon
 */

import java.lang.*;
import java.util.*;
import java.io.*;

public class MovieNet {
  static HashMap<String, HashMap<String, Node>> memo = new HashMap<>();
  static final String KevinBacon = "Bacon, Kevin";
  HashMap<String, HashSet<String>> movieActorMap;
  HashMap<String, HashSet<String>> actorMovieMap;
  HashMap<String, HashSet<String>> actorActorMap;
  LinkedList<String[]> movielines;
  int INF = 2000000000;

  // Each instance of movielines is String[] such that
  //	String[0] = title of movie
  //	String[1..n-1] = list of actors

  // TODO: Constructor
  public MovieNet(LinkedList<String[]> movielines) {
    this.movielines = movielines;
    this.movieActorMap = null;
    this.actorMovieMap = null;
    this.actorActorMap = null;
  }

  /*============================================================================*/

  public void setInitialMovieActorMap(LinkedList<String[]> movielines) {
    if (movielines == null || movielines.isEmpty() || movieActorMap != null) {
      return;
    }
    HashMap<String, HashSet<String>> result = new HashMap<>();
    for (String[] targetArray: movielines) {
      if (targetArray != null && targetArray.length != 0) {
        String movieTitle = targetArray[0];
        if (targetArray.length > 1) {
          String[] actorsArray = Arrays.copyOfRange(targetArray, 1, targetArray.length);
          HashSet<String> actorSet = new HashSet<>(Arrays.asList(actorsArray));
          if (result.containsKey(movieTitle)) {
            result.get(movieTitle).addAll(actorSet);
          } else {
            result.put(movieTitle, actorSet);
          }
        }
      }
    }
    this.movieActorMap = result;
  }

  public void setInitialActorMovieMap(LinkedList<String[]> movielines) {
    if (movielines == null || movielines.isEmpty() || actorMovieMap != null) {
      return;
    }
    HashMap<String, HashSet<String>> result = new HashMap<>();
    for (String[] targetArray: movielines) {
      if (targetArray != null && targetArray.length != 0) {
        String movieTitle = targetArray[0];
        if (targetArray.length > 1) {
          String[] actorsArray = Arrays.copyOfRange(targetArray, 1, targetArray.length);
          for (String actor: actorsArray) {
            if (result.containsKey(actor)) {
              result.get(actor).add(movieTitle);
            } else {
              HashSet<String> movieSet = new HashSet<>();
              movieSet.add(movieTitle);
              result.put(actor, movieSet);
            }
          }
        }
      }
    }
    this.actorMovieMap = result;
  }

  public void setInitialActorActorMap(LinkedList<String[]> movielines) {
    if (movielines == null || movielines.isEmpty() || actorActorMap != null) {
      return;
    }
    HashMap<String, HashSet<String>> result = new HashMap<>();
    for (String[] targetArray: movielines) {
      if (targetArray != null && targetArray.length != 0) {
        if (targetArray.length > 1) {
          String[] actorsArray = Arrays.copyOfRange(targetArray, 1, targetArray.length);
          for (String actor: actorsArray) {
            HashSet<String> actorSet = new HashSet<>(Arrays.asList(actorsArray));
            actorSet.remove(actor);
            if (result.containsKey(actor)) {
              result.get(actor).addAll(actorSet);
            } else {
              result.put(actor, actorSet);
            }
          }
        }
      }
    }
    this.actorActorMap = result;
  }


  // TODO: [Q1]
  public String[] moviesby(String[] actors) {
    if (actors == null) {
      return null;
    }
    setInitialActorMovieMap(this.movielines);

    if (!actorMovieMap.containsKey(actors[0])) {
      return null;
    }
    HashSet<String> resultSet = new HashSet<>();
    resultSet.addAll(actorMovieMap.get(actors[0])); // initialize
    for (int i = 0; i < actors.length - 1; i++) {
      if (!actorMovieMap.containsKey(actors[i + 1])) {
        return null;
      }
      resultSet.retainAll(actorMovieMap.get(actors[i + 1]));
      if (resultSet.size() == 0) {
        return null;
      }
    }
    String[] resultArr = resultSet.toArray(new String[0]);
    return resultArr;
  }

  // TODO: [Q2]
  public String[] castin(String[] titles) {
    if (titles == null) {
      return null;
    }
    setInitialMovieActorMap(this.movielines);
    if (!movieActorMap.containsKey(titles[0])) {
      return null;
    }
    HashSet<String> resultSet = new HashSet<>();
    resultSet.addAll(movieActorMap.get(titles[0])); // initialize
    for (int i = 0; i < titles.length - 1; i++) {
      // when inValid movieTitle
      if (!movieActorMap.containsKey(titles[i + 1])) {
        return null;
      }
      resultSet.retainAll(movieActorMap.get(titles[i + 1]));
      if (resultSet.size() == 0) {
        return null;
      }
    }
    String[] resultArr = resultSet.toArray(new String[0]);
    return resultArr;
  }

  // TODO: [Q3]
  public String[] pairmost(String[] actors) {
    setInitialActorMovieMap(this.movielines);
    setInitialMovieActorMap(this.movielines);
    if (actors == null || actors.length == 0 || actors.length == 1) {
      return null;
    }

    HashMap<String, HashMap<String, Integer>> count = new HashMap<>();
    String maxStandard = null;
    String maxTarget = null;
    int maxCount = 0;

    for (int i = 0; i < actors.length - 1; i++) {
      String standardActor = actors[i];
      if (!actorMovieMap.containsKey(standardActor)) {
        continue;
      }
      count.put(standardActor, new HashMap<>());
      for (int j = i + 1; j < actors.length; j++) {
        String targetActor = actors[j];
        count.get(standardActor).put(targetActor, 0);

        HashSet<String> movies = actorMovieMap.get(standardActor);
        for (String movieTitle: movies) {
          if (movieActorMap.containsKey(movieTitle)) {
            if (movieActorMap.get(movieTitle).contains(targetActor)) {
              int newCount = count.get(standardActor).get(targetActor) + 1;
              count.get(standardActor).put(targetActor, newCount);
            }
          }
        }
      }
      HashMap<String, Integer> target = count.get(standardActor);
      for (String key: target.keySet()) {
        if (target.get(key) > maxCount) {
          maxCount = target.get(key);
          maxStandard = standardActor;
          maxTarget = key;

        }
      }
    }
    if (maxCount == 0 || maxStandard == null || maxTarget == null) {
      return null;
    }
    return new String[]{ maxStandard, maxTarget };
  }

  // TODO: [Q4]
  public int Bacon(String actor) {
    if (actor == null) {
      return -1;
    }
    if (actor.equals(KevinBacon)) {
      return 0;
    }
    setInitialActorActorMap(this.movielines);
    if (actorActorMap == null) {
      return -1;
    }
    if (!actorActorMap.containsKey(KevinBacon) || !actorActorMap.containsKey(actor)) {
      return -1;
    }
    HashMap<String, Node> dist = dijkstraAll(KevinBacon, this.actorActorMap);
    if (!dist.containsKey(actor)) {
      return -1;
    }
    if (dist.get(actor).shortestDist == INF) {
      return -1;
    }

    return dist.get(actor).shortestDist;
  }

  // TODO: [Q5]
  public int distance(String src, String dst) {
    if (src == null || dst == null) {
      return -1;
    }
    if (src.equals(dst)) {
      return 0;
    }
    setInitialActorActorMap(this.movielines);
    if (actorActorMap == null) {
      return -1;
    }
    if (!actorActorMap.containsKey(src) || !actorActorMap.containsKey(dst)) {
      return -1;
    }
    HashMap<String, Node> dist = dijkstraAll(src, this.actorActorMap);
    if (!dist.containsKey(dst)) {
      return -1;
    }
    if (dist.get(dst).shortestDist == INF) {
      return -1;
    }
    return dist.get(dst).shortestDist;
  }

  // TODO: [Q6]
  public int npath(String src, String dst) {
    if (src == null || dst == null) {
      return 0;
    }
    if (src.equals(dst)) {
      return 0;
    }
    setInitialActorActorMap(this.movielines);
    if (actorActorMap == null) {
      return 0;
    }
    if (!actorActorMap.containsKey(src) || !actorActorMap.containsKey(dst)) {
      return 0;
    }
    HashMap<String, Node> dist = dijkstraAll(src, this.actorActorMap);
    if (!dist.containsKey(dst)) {
      return 0;
    }
    if (dist.get(dst).shortestDist == INF) {
      return 0;
    }
    return dist.get(dst).nPath;
  }

  // TODO: [Q7]
  public String[] apath(String src, String dst) {
    if (src == null || dst == null) {
      return null;
    }
    if (src.equals(dst)) {
      return new String[] { src };
    }
    setInitialActorActorMap(this.movielines);
    if (actorActorMap == null) {
      return null;
    }
    if (!actorActorMap.containsKey(src) || !actorActorMap.containsKey(dst)) {
      return null;
    }
    HashMap<String, Node> dist = dijkstraAll(src, this.actorActorMap);
    if (!dist.containsKey(dst)) {
      return null;
    }
    if (dist.get(dst).shortestDist == INF) {
      return null;
    }

    if (dist.get(dst).aPath == null || dist.get(dst).aPath.size() == 0) {
      return null;
    }
    return dist.get(dst).aPath.toArray(new String[0]);
  }

  // TODO: [Q8]
  public int eccentricity(String actor) {
    if (actor == null) {
      return 0;
    }
    setInitialActorActorMap(this.movielines);
    if (actorActorMap == null) {
      return 0;
    }
    if (!actorActorMap.containsKey(actor)) {
      return 0;
    }
    HashMap<String, Node> dist = dijkstraAll(actor, this.actorActorMap);

    ArrayList<Integer> distances = new ArrayList<>();
    for (String key: dist.keySet()) {
      if (dist.get(key).shortestDist != INF) {
        distances.add(dist.get(key).shortestDist);
      }
    }

    if (distances.size() == 0) {
      return 0;
    }
    int max = Collections.max(distances);
    return max;
  }

  // TODO: [Q9]
  public float closeness(String actor) {
    if (actor == null) {
      return 0.0F;
    }
    setInitialActorActorMap(this.movielines);
    if (actorActorMap == null) {
      return 0.0F;
    }
    if (!actorActorMap.containsKey(actor)) {
      return 0.0F;
    }
    HashMap<String, Node> dist = dijkstraAll(actor, this.actorActorMap);
    float result = 0.0F;
    for (String key: dist.keySet()) {
      int intShortestDist = dist.get(key).shortestDist;
      if (intShortestDist != INF) {
        if (!dist.get(key).actor.equals(actor)) {
          float floatShortestDist = (float) intShortestDist;
          float denominator = (float) Math.pow((float) 2, floatShortestDist);
          result += ((float) 1) / denominator;
        }
      }
    }
    return result;
  }

  public HashMap<String, Node> dijkstraAll(String src, HashMap<String, HashSet<String>> actorActorMap) {
    if (memo.containsKey(src)) {
      return memo.get(src);
    }
    // graph: actorActorMap
    HashMap<String, Node> dist = new HashMap<>();
    HashMap<String, Boolean> visited = new HashMap<>();

    // initialize
    for (String key: actorActorMap.keySet()) {
      dist.put(key, new Node(key, INF));
      visited.put(key, false);
    }
    // when src
    ArrayList<String> initialAPath = new ArrayList<>();
    initialAPath.add(src);
    Node startNode = new Node(src, 0, 0, initialAPath);
    dist.put(src, startNode);

    PriorityQueue<Node> minHeap = new PriorityQueue<Node>(new Comparator<Node>() {
      @Override
      public int compare(Node n1, Node n2) {
        return n1.shortestDist > n2.shortestDist ? 1 : -1;
      }
    });
    minHeap.add(startNode);

    while(!minHeap.isEmpty()) {
      Node target = minHeap.poll();
      String currTarget = target.actor;
      if (visited.get(currTarget)) {
        continue;
      } else {
        visited.put(currTarget, true);
      }
      HashSet<String> targetSet = actorActorMap.get(currTarget);
      for (String nextTarget: targetSet) {
        if (dist.get(nextTarget).shortestDist > dist.get(currTarget).shortestDist + 1) {
          dist.get(nextTarget).shortestDist = dist.get(currTarget).shortestDist + 1;
          ArrayList<String> newAPath = new ArrayList<>();
          newAPath.addAll(dist.get(currTarget).aPath);
          newAPath.add(nextTarget);
          dist.get(nextTarget).aPath = newAPath;

          int newNPath = Math.max(1, dist.get(currTarget).nPath);
          dist.get(nextTarget).nPath = newNPath;

          minHeap.add(new Node(nextTarget, dist.get(nextTarget).shortestDist, dist.get(nextTarget).nPath, dist.get(nextTarget).aPath));
        } else if (dist.get(nextTarget).shortestDist == dist.get(currTarget).shortestDist + 1) {
          dist.get(nextTarget).nPath += dist.get(currTarget).nPath;
        }
      }
    }
    memo.put(src, dist);
    return dist;
  }

  // we will measure the time spent on the MovieNet database construction and query processing.
  /*============================================================================*/

  static class Node {
    String actor;
    int shortestDist;
    int nPath;
    ArrayList<String> aPath;

    public Node(String actor, int shortestDist) {
      this.actor = actor;
      this.shortestDist = shortestDist;
      this.nPath = 0;
      this.aPath = new ArrayList<>();
    }

    public Node(String actor, int shortestDist, int nPath, ArrayList<String> aPath) {
      this.actor = actor;
      this.shortestDist = shortestDist;
      this.nPath = nPath;
      this.aPath = aPath;
    }
  }
}
