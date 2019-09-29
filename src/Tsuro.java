import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Tsuro {
  public static void main(String[] args) {
    PortSetRep psr = new PortSetRep();
    Set<PortSetRep.Tile> t = psr.generateAll();
    System.out.println(String.format("There are %d unique tiles", t.size()));
    for (PortSetRep.Tile o : t) {
      System.out.println(o.toString());
    }
  }


  /*
   *      1   2
   *    +-------+
   *  8 |       | 3
   *  7 |       | 4
   *    +-------+
   *      6   5
   *
   * [ 1,-1, 1,-1, 1,-1, 1,-1]
   * [ 2, 2,-2,-2, 1,-1, 1,-1] => [ 1,-1, 2, 2,-2,-2, 1,-1] => [ 1,-1, 1,-1, 2, 2,-2,-2] => [-2,-2, 1,-1, 1,-1, 2, 2]
   * [ 3, 1,-1,-3, 1,-1, 1,-1]
   * [ 4, 1,-1, 2,-4,-2, 1,-1]
   *
   *
   *
   * [(1,2),(3,4),(5,6),(7,8)]
   * [(1,3),(2,4),(5,6),(7,8)] => [(1,2),(3,5),(4,6),(7,8)] => [(1,2),(3,4),(5,7),(6,8)] => [(1,7),(2,8),(3,4),(5,6)]
   * [(1,4),(2,3),(5,6),(7,8)]
   * [(1,5),(2,3),(4,6),(7,8)]
   *
   *
   * 35 total
   */

  static class PortSetRep {
    int[][] tile = {{0,1},{2,3},{4,5},{6,7}};
    PortSetRep() {}

    class Tile {
      Set<Set<Integer>> connsSet;
      private int[][] conns;

      Tile(int[][] conns) {
        this.conns = standardize(conns);

        this.connsSet = new HashSet<>();
        for (int i = 0; i < 4; i++) {
          Set<Integer> con = new HashSet<>();
          for (int j = 0; j < 2; j++) {
            con.add(conns[i][j]);
          }
          this.connsSet.add(con);
        }
      }

      private int[][] standardize(int[][] tile) {
        int[][] newTile = new int[4][2];
        PriorityQueue<int[]> sort = new PriorityQueue<>(Comparator.comparing(a -> a[0]));
        for (int i = 0; i < 4; i++) {
          int[] pair = tile[i];
          if (pair[0] > pair[1]) {
            int temp = pair[0];
            pair[0] = pair[1];
            pair[1] = temp;
          }

          sort.add(pair);
        }

        for (int i = 0; i < 4; i++) {
          int[] pair = sort.poll();
          newTile[i] = pair;
        }
        return newTile;
      }

      private int[][] rotate90Degrees(int[][] tile) {
        int[][] newTile = new int[4][2];
        for (int i = 0; i < 4; i++) {
          for (int j = 0; j < 2; j++) {
            newTile[i][j] = (tile[i][j] + 2) % 8;
          }
        }
        return newTile;
      }

      @Override
      public boolean equals(Object o) {
        if (o instanceof Tile) {
          Tile that = (Tile) o;
//          int[][] st2 = that.conns;
//          if (arrayDeepEqual(this.conns, st2)) {
//            return true;
//          }
//
//          for (int i = 0; i < 3; i++) {
//            st2 = rotate90Degrees(st2);
//            if (arrayDeepEqual(this.conns, st2)) {
//              return true;
//            }
//          }
          return this.connsSet.equals(that.connsSet);
        }

        return false;
      }

      @Override
      public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("{");
        for (int i = 0; i < this.conns.length; i++) {
          s.append("{");
          for (int j = 0; j < this.conns[i].length; j++) {
            s.append(this.conns[i][j]);
            if (j < this.conns[i].length - 1) {
              s.append(",");
            }
          }
          s.append("}");
        }
        s.append("}");
        return s.toString();
      }
    }



    public Set<Tile> generateAll() {
      /*
      [
    1:  [[0,1],[0,2],[0,3],[0,4],[0,5],[0,6],[0,7]],
    2:  [[1,2],[1,3],[1,4],[1,5],[1,6],[1,7]],
        ..
    6:  [[5,6],[5,7]],
    7:  [[6,7]
      ]
       */
      int[][][] possibleConnection = new int[7][7][2];
      for (int i = 0; i < 7; i++) {
        for (int j = i + 1; j < 8; j++) {
          int[] conn = {i, j};
          possibleConnection[i][j - 1 - i] = conn;
        }
      }

      List<int[][]> uniquePairs = new ArrayList<>(105);
      boolean[] used = new boolean[8];
      int i1 = 0;
      for (int j1 = 0; j1 < (7 - i1); j1++) {
        int[] firstCon = possibleConnection[i1][j1];
        used[i1] = true;
        used[i1 + j1 + 1] = true;

        int i2 = 1;
        while (used[i2]) i2++;
        for (int j2 = 0; j2 < (7 - i2); j2++) {
          if (used[i2 + j2 + 1]) continue;
          int[] secondCon = possibleConnection[i2][j2];
          used[i2] = true;
          used[i2 + j2 + 1] = true;

          int i3 = 2;
          while (used[i3]) i3++;
          for (int j3 = 0; j3 < (7 - i3); i3++) {
            if (used[i3 + j3 + 1]) continue;
            int[] thirdCon = possibleConnection[i3][j3];
            used[i3] = true;
            used[i3 + j3 + 1] = true;

            int i4 = 3;
            while (used[i4]) i4++;
            for (int j4 = 0; j4 < (7 - i4); i4++) {
              if (used[i4 + j4 + 1]) continue;
              int[] fourthCon = possibleConnection[i4][j4];

              int[][] connection = {firstCon, secondCon, thirdCon, fourthCon};
              uniquePairs.add(connection);
            }

            used[i3] = false;
            used[i3 + j3 + 1] = false;
          }

          used[i2] = false;
          used[i2 + j2 + 1] = false;
        }

        used[i1] = false;
        used[i1 + j1 + 1] = false;
      }

      //uniquePairs.add(connection);

      Set<Tile> uniqueTiles = new HashSet<>(35);
      for (int[][] uniquePair : uniquePairs) {
        uniqueTiles.add(new Tile(uniquePair));
      }

      return uniqueTiles;
    }

    private boolean arrayDeepEqual(int[][] arr1, int[][] arr2) {
      if (arr1.length != arr2.length) {
        return false;
      }

      for (int i = 0; i < arr1.length; i++) {
        if (arr1[i].length != arr2[i].length) {
          return false;
        }

        for (int j = 0; j < arr1[i].length; j++) {
          if (arr1[i][j] != arr2[i][j]) {
            return false;
          }
        }
      }

      return true;
    }
  }




  class PortAheadRep {
    int[] rep = {1,7,2,2,6,6,1,7};

    public boolean twoRepEqual(int[] config1, int[] config2) {
      if (Arrays.equals(config1, config2)) {
        return true;
      }
      int[] comparer = config2;
      for (int i = 0; i < 3; i++) {
        comparer = rotateArray90Degrees(comparer);
        if (Arrays.equals(config1, comparer)) {
          return true;
        }
      }
      return false;
    }

    private int[] rotateArray90Degrees(int[] config) {
      int[] rotatedArray = new int[8];
      for (int i = 0; i < 8; i++) {
        rotatedArray[(i + 2) % 8] = config[i];
      }
      return rotatedArray;
    }
  }
}
