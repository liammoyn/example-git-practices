import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SlayRecursive {

  public static void main(String[] args) {
    //Get input values
    List<Integer> values = new ArrayList<>();
    Scanner scan = new Scanner(System.in);
    while (scan.hasNextLine()) {
      values.add(Integer.parseInt(scan.nextLine()));
    }

    int ans = recurOnList(values, values.size() - 1);
    System.out.println(ans);
  }

  private static int recurOnList(List<Integer> values, int index) {
    if (index < 0) {
      return 0;
    }

    //If the first or second element then we can't use a special move
    if (index <= 1) {
      return values.get(index) + recurOnList(values, index - 1);
    }

    //The optimal solution assuming we don't use our move on this monster
    int dontDouble = values.get(index) +
        recurOnList(values, index - 1);

    //The optimal solution if we assume we do use our move on this monster
    int doDouble = (2 * values.get(index)) +
        values.get(index - 1) +
        values.get(index - 2) +
        recurOnList(values, index - 3);

    //The optimal solution is the max value we got from doubling and not doubling
    return Math.max(dontDouble, doDouble);
  }

}
