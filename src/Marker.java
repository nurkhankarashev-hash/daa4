import java.util.*;

/**
 * Missions 3 and 4. Search for DNA words with KMP and Rabin-Karp.
 */
public class Marker {

    public static int[] prefixFunction(String p) {
        int[] pi = new int[p.length()];
        for (int i = 1; i < p.length(); i++) {
            int j = pi[i - 1];
            while (j > 0 && p.charAt(i) != p.charAt(j)) {
                j = pi[j - 1];
            }
            if (p.charAt(i) == p.charAt(j)) j++;
            pi[i] = j;
        }
        return pi;
    }

    public static int[] findAll(String text, String p) {
        if (p.length() == 0) return new int[0];
        int[] pi = prefixFunction(p);
        ArrayList<Integer> positions = new ArrayList<>();
        int j = 0;

        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != p.charAt(j)) {
                j = pi[j - 1];
            }
            if (text.charAt(i) == p.charAt(j)) j++;
            if (j == p.length()) {
                positions.add(i - p.length() + 1);
                j = pi[j - 1];
            }
        }

        int[] result = new int[positions.size()];
        for (int i = 0; i < result.length; i++) result[i] = positions.get(i);
        return result;
    }

    public static int sharedWords(String a, String b, int k) {
        if (k <= 0 || a.length() < k || b.length() < k) return 0;

        long mod = 1000000007L;
        long base = 257;
        long power = 1;
        for (int i = 1; i < k; i++) power = power * base % mod;

        HashMap<Long, ArrayList<Integer>> windows = new HashMap<>();
        long hashB = 0;
        for (int i = 0; i < k; i++) hashB = (hashB * base + b.charAt(i)) % mod;
        windows.computeIfAbsent(hashB, x -> new ArrayList<>()).add(0);

        for (int i = 1; i + k <= b.length(); i++) {
            hashB = (hashB - b.charAt(i - 1) * power % mod + mod) % mod;
            hashB = (hashB * base + b.charAt(i + k - 1)) % mod;
            windows.computeIfAbsent(hashB, x -> new ArrayList<>()).add(i);
        }

        long hashA = 0;
        for (int i = 0; i < k; i++) hashA = (hashA * base + a.charAt(i)) % mod;
        int count = 0;

        for (int i = 0; i + k <= a.length(); i++) {
            if (i > 0) {
                hashA = (hashA - a.charAt(i - 1) * power % mod + mod) % mod;
                hashA = (hashA * base + a.charAt(i + k - 1)) % mod;
            }
            ArrayList<Integer> matches = windows.get(hashA);
            if (matches == null) continue;

            for (int start : matches) {
                int j = 0;
                while (j < k && a.charAt(i + j) == b.charAt(start + j)) j++;
                if (j == k) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }
}
