import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SlayDynamicIterative {

  public static void main(String[] args) {

    List<Integer> values = new ArrayList<>();
    Scanner scan = new Scanner(System.in);
    while (scan.hasNextLine()) {
      values.add(Integer.parseInt(scan.nextLine()));
    }

    //remember[i] is the optimal solution for inputs from index 0 to i - 1
    int[] remember = new int[values.size() + 1];
    remember[0] = 0; //Add a zero value to make index 0 and 1 cases identical

    for (int i = 0; i < values.size(); i++) {
      if (i <= 1) {
        //Do not consider the doubling case
        remember[i + 1] = values.get(i) + remember[i];
      } else {
        //We will always get the experience of this monster at least once
        remember[i + 1] = values.get(i);

        //Add the maximum of not using the double and from using the double
        remember[i + 1] += Math.max(remember[i],
            remember[i - 2] + values.get(i - 2) + values.get(i - 1) + values.get(i));
      }
    }

    //Return the value that represents using entire input
    System.out.println(remember[values.size()]);
  }

}
