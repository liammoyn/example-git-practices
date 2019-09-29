import java.util.Arrays;
import java.util.Comparator;

public class Scratch {

  void go(String[][] requests) {

    Arrays.sort(requests, Comparator.comparing(t -> toMinutes(t[1])));

    int[][] remember = new int[1440][2];
    remember[0][0] = 0;
    remember[0][1] = 0;

    int requestIndex = 0;

    for (int i = 1; i < 1440; i++) {
      remember[i][0] = remember[i - 1][0] + 1;
      remember[i][1] = remember[i - 1][1];

      while (toMinutes(requests[requestIndex][1]) == i) {
        int startMinute = toMinutes(requests[requestIndex][0]);
        if (remember[startMinute][0] < remember[i][0] ||
            (remember[startMinute][0] == remember[i][0] &&
                remember[startMinute][1] + 1 < remember[i][1])) {
          remember[i][0] = remember[startMinute][0];
          remember[i][1] = remember[startMinute][1] + 1;
        }

        requestIndex++;
      }
    }


  }

  int toMinutes(String s) {
    String[] split = s.split(":");
    return Integer.parseInt(split[0]) * 60 + Integer.parseInt(split[1]);
  }
}
