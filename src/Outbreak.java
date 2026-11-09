/**
 * Mission 5. Dynamic programming on the infection tree.
 */
public class Outbreak {

    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        if (n == 0) return new boolean[0];
        int[] yes = new int[n];
        int[] no = new int[n];
        countCosts(0, -1, nb, cost, yes, no);

        boolean[] tested = new boolean[n];
        restore(0, -1, yes[0] <= no[0], nb, yes, no, tested);
        return tested;
    }

    private static void countCosts(int v, int parent, int[][] nb,
                                   int[] cost, int[] yes, int[] no) {
        yes[v] = cost[v];
        for (int child : nb[v]) {
            if (child == parent) continue;
            countCosts(child, v, nb, cost, yes, no);
            yes[v] += Math.min(yes[child], no[child]);
            no[v] += yes[child];
        }
    }

    private static void restore(int v, int parent, boolean take,
                                int[][] nb, int[] yes, int[] no,
                                boolean[] tested) {
        tested[v] = take;
        for (int child : nb[v]) {
            if (child == parent) continue;
            boolean takeChild = !take || yes[child] <= no[child];
            restore(child, v, takeChild, nb, yes, no, tested);
        }
    }

    public static int longestChain(int n, int[][] nb) {
        if (n == 0) return 0;
        int[] best = new int[1];
        height(0, -1, nb, best);
        return best[0];
    }

    private static int height(int v, int parent, int[][] nb, int[] best) {
        int longest = 0;
        int second = 0;
        for (int child : nb[v]) {
            if (child == parent) continue;
            int down = height(child, v, nb, best) + 1;
            if (down > longest) {
                second = longest;
                longest = down;
            } else if (down > second) {
                second = down;
            }
        }
        best[0] = Math.max(best[0], longest + second);
        return longest;
    }
}
