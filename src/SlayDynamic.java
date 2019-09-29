import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SlayDynamic {

  public static void main(String[] args) {
    //Get input values
    List<Integer> values = new ArrayList<>();
    Scanner scan = new Scanner(System.in);
    while (scan.hasNextLine()) {
      values.add(Integer.parseInt(scan.nextLine()));
    }

    Integer[] memo = new Integer[values.size()]; //memo[i] is the solution for input from index 0 to i
    int ans = recurOnList(values, values.size() - 1, memo);
    System.out.println(ans);
  }

  private static int recurOnList(List<Integer> values, int index, Integer[] memo) {
    if (index < 0) {
      return 0;
    }

    //If we've seen this value already return what we calculated
    if (memo[index] != null) {
      return memo[index];
    }

    //If the first or second element then we can't use a special move
    if (index <= 1) {
      memo[index] = values.get(index) + recurOnList(values, index - 1, memo);
      return memo[index];
    }

    //The optimal solution assuming we don't use our move on this monster
    int dontDouble = values.get(index) +
        recurOnList(values, index - 1, memo);

    //The optimal solution if we assume we do use our move on this monster
    int doDouble = (2 * values.get(index)) +
        values.get(index - 1) +
        values.get(index - 2) +
        recurOnList(values, index - 3, memo);

    //The optimal solution is the max value we got from doubling and not doubling
    memo[index] = Math.max(dontDouble, doDouble);
    return memo[index];
  }

}
