import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    static Random rand = new Random();

    public static void main(String[] args) {
        visualize();
    }

    private static void visualize() {
        int n = 5;
        double[][] cups = new double[n][n];
        Scanner in = new Scanner(System.in);

        Timer t = new Timer();

        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                pour(cups, 0.25, 0, 0);
                draw(cups);
            }
        }, 0, 250L);

        int[] a;


    }

    private static void pour(double[][] cups, double amount, int r, int c) {
        if (c > r || r >= cups.length) {
            return;
        }

        double total = cups[r][c] + amount;
        if (total <= 1) {
            cups[r][c] = total;
        } else {
            cups[r][c] = 1.0;
            total = total - 1;
            pour(cups, total / 2, r + 1, c);
            pour(cups, total / 2 , r + 1, c + 1);
        }

    }

    private static void draw(double[][] cups) {
        StringBuilder sb = new StringBuilder();
        int n = cups.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - (i + 1); j++) {
                sb.append("   ");
            }
            for (int j = 0; j <= i; j++) {
                sb.append(String.format("%.2f", cups[i][j]));
                sb.append("   ");
            }
            sb.append("\n");
        }
        System.out.flush();
        System.out.println(sb.toString());
    }
}
