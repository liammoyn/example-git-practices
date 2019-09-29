

public class AlgoFinalPractice {

  public static void main(String[] args) {
    int[] payoffA = {};
    int[] payoffB = {};
    int[] destA = {};
    int[] destB = {};

    dp(payoffA, payoffB, destA, destB);
  }

  private static boolean[] dp(int[] payoffA, int[] payoffB, int[] destA, int[] destB) {
    Integer[] maxScore = new Integer[payoffA.length];
    boolean[] usedA = new boolean[payoffA.length];
    recur(payoffA, payoffB, destA, destB, 0, maxScore, usedA);

    for(int i = 0; i < payoffA.length; i++) {
      int aScore = 0;
      int bScore = 0;
      for (int j = 0; j < i; j++) {
        if (destA[j] == i) {
          aScore = Math.max(aScore, maxScore[j] + payoffA[j]);
        }
        if (destB[j] == i) {
          bScore = Math.max(bScore, maxScore[j] + payoffB[j]);
        }
      }

      if (aScore > bScore) {
        usedA[i] = true;
        maxScore[i] = aScore;
      } else {
        usedA[i] = false;
        maxScore[i] = bScore;
      }
    }

    return usedA;
  }


  private static int recur(int[] payoffA, int[] payoffB, int[] destA, int[] destB, int index, Integer[] memo, boolean[] usedA) {
    if (index >= payoffA.length) {
      return 0;
    }

    if (memo[index] != null) {
      return memo[index];
    }

    int aPath = payoffA[index];
    int bPath = payoffB[index];

    aPath += recur(payoffA, payoffB, destA, destB, destA[index], memo, usedA);
    bPath += recur(payoffA, payoffB, destA, destB, destB[index], memo, usedA);

    if (aPath > bPath) {
      usedA[index] = true;
      memo[index] = aPath;
    } else {
      usedA[index] = false;
      memo[index] = bPath;
    }

    return memo[index];
  }
}
