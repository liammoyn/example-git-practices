import java.util.*;

public class InversionCounting {
  /** A Map from movie name to its place in critic 1's ranking */
  private static final Map<String, Integer> baseRankings = new HashMap<>();

  /** All of the movies that were rated five stars by either critic */
  private static final Set<String> fiveStars = new HashSet<>();

  public static void main(String[] args) {
    //The list of the second critic's rankings. Declare as null so that it can be
    // instantiated once we know how many movies we're processing
    String[] compareRankings = null;

    boolean firstCritic = true;
    int count = 0;

    Scanner scan = new Scanner(System.in);
    while (scan.hasNextLine()) {
      String line = scan.nextLine();

      //TODO: Remove, used for debugging purposes
      if (line.equals("go")) break;

      if (line.equals("Critic 1")) {
        continue;
      } else if (line.equals("Critic 2")) {
        firstCritic = false;

        //Assume second critic will have same number of movies as the first.
        compareRankings = new String[count];
        continue;
      }

      String[] ranking = line.split("/");

      if (ranking[0].equals("5")) {
        //If this movie has been rated 5 stars by anyone add it to the set.
        fiveStars.add(ranking[1]);
      }

      if (firstCritic) {
        //Add movie to first critic rankings, lower numbers are higher in the ranking
        baseRankings.put(ranking[1], ++count);
      } else {
        //Add movies to ranking in reverse order so that high rated movies are at the end of the array
        compareRankings[--count] = ranking[1];
      }
    }

    //Input is assumed to be valid so this check should never be true
    if (compareRankings == null) {
      throw new IllegalArgumentException("Line \"Critic 2\" was never parsed");
    }

    InvertReturn invertReturn = sortAndCount(compareRankings, 0, compareRankings.length);
    System.out.println(invertReturn.getTotalInversions());
  }

  /**
   * Sort the array of Strings based on {@code baseRankings} value from high to low while counting
   * the number of inversions. Strings included in {@code fiveStars} will count as double inversions.
   *
   * @param ranking The array of String to sort and count.
   * @param low The inclusive lower bound of the section of the array to sort.
   * @param high The exclusive upper bound of the section of the array to sort.
   * @return An InvertReturn object with the sorted list of movies, the number
   *         of inversions and the number of five star movies.
   */
  private static InvertReturn sortAndCount(String[] ranking, int low, int high) {
    InvertReturn returnValue = new InvertReturn();

    //Return an InvertReturn with a list of zero or one elements.
    if (low >= high - 1) {
      //Only one element so list is trivially sorted
      if (low == high - 1) {
        returnValue.addMovieToEnd(ranking[low]);
        if (fiveStars.contains(ranking[low])) {
          returnValue.addFiveStars(1);
        }
      }

      return returnValue;
    }

    //The mid point to split the left and right sides on
    int mid = (high + low) / 2;

    //Sort and count the bottom half of rankings and add their return values to this return
    InvertReturn bottomHalf = sortAndCount(ranking, low, mid);
    returnValue.addInversions(bottomHalf.getTotalInversions());
    returnValue.addFiveStars(bottomHalf.getFiveStars());
    LinkedList<String> bottomHalfMovies = bottomHalf.getMovies();

    //Sort and count the top half of rankings and add their return values to this return
    InvertReturn topHalf = sortAndCount(ranking, mid, high);
    returnValue.addInversions(topHalf.getTotalInversions());
    returnValue.addFiveStars(topHalf.getFiveStars());
    LinkedList<String> topHalfMovies = topHalf.getMovies();

    //The number of 5-star movies in the bottom half, used to track double counting inversions
    long bottomFiveStars = bottomHalf.getFiveStars();

    //While there are still movies to be processed
    while (!bottomHalfMovies.isEmpty() || !topHalfMovies.isEmpty()) {
      if (topHalfMovies.isEmpty()) { //If no more topHalf movies then add rest of bottom half
        returnValue.addMovieToEnd(bottomHalfMovies.pop());
      } else if (bottomHalfMovies.isEmpty()) { //If no more bottomHalf movies then add rest of top half
        returnValue.addMovieToEnd(topHalfMovies.pop());
      } else {
        //Get the ranking positions for the first element of the bottom and top movie lists
        int bottomRank = baseRankings.get(bottomHalfMovies.peek());
        int topRank = baseRankings.get(topHalfMovies.peek());

        if (bottomRank < topRank) { //Rankings are inverted
          String movie = topHalfMovies.pop();
          returnValue.addMovieToEnd(movie);

          //Count all inversions one time
          returnValue.addInversions(bottomHalfMovies.size());

          if (fiveStars.contains(movie)) {
            //If the top half movie is 5-star then every inversion counts as double
            returnValue.addInversions(bottomHalfMovies.size());
          } else {
            //If the top half is not 5-star then only the remaining 5-star bottom half movies count as double
            returnValue.addInversions(bottomFiveStars);
          }
        } else { //Rankings are not inverted
          String movie = bottomHalfMovies.pop();
          returnValue.addMovieToEnd(movie);

          if (fiveStars.contains(movie)) { //If this is a five star movie then lower our counter
            bottomFiveStars = bottomFiveStars - 1;
          }
        }
      }
    }

    return returnValue;
  }
}

/**
 * This is a wrapper class to contain all the values that countAndSort needs to return
 */
class InvertReturn {
  /** The list of movies in this return */
  private LinkedList<String> movies;
  /** The total number of inversions found so far */
  private long totalInversions;
  /** How many movies in this return that have been rated five stars by either critic */
  private long fiveStars;

  InvertReturn() {
    this.movies = new LinkedList<>();
    this.totalInversions = 0;
    this.fiveStars = 0;
  }

  void addMovieToEnd(String movie) {
    this.movies.add(movie);
  }

  void addInversions(long inversions) {
    this.totalInversions = this.totalInversions + inversions;
  }

  void addFiveStars(long fiveStars) {
    this.fiveStars = this.fiveStars + fiveStars;
  }

  LinkedList<String> getMovies() {
    return movies;
  }

  long getTotalInversions() {
    return totalInversions;
  }

  long getFiveStars() {
    return fiveStars;
  }
}
