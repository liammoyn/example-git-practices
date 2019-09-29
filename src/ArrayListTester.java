import java.util.ArrayList;

public class ArrayListTester {

  public static void main(String[] args) {
    ArrayList<Integer> list;

    for (int i = 0; i < 25; i++) {
      list = new ArrayList<>(10);
      long totalTime = 0;
      long maxTime = 0;

      int count = (int) Math.pow(2, i);
      for (int j = 0; j < count; j++) {
        long beforeTime = System.nanoTime();
        list.add(j);
        long afterTime = System.nanoTime();
        long timeTaken = afterTime - beforeTime;
        totalTime += timeTaken;
        if (timeTaken > maxTime) {
          maxTime = timeTaken;
        }
      }

      long averageTime = totalTime / count;
      System.out.println(String.format("%d: avg: %d ns; maxTime: %d ns", count, averageTime, maxTime));
    }
  }
}
