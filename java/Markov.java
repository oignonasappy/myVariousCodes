import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Markov {
    public static void main(String[] args) throws Exception {
        Random rd = new Random();
        Scanner sc = new Scanner(System.in);

        System.out.print("Markov chain\n> ");
        String text = sc.nextLine();
        if (text.isEmpty()) {
            text = "Hello, World!";
        }

        char[] chrArr = text.toCharArray();
        for (char c : chrArr) {
            System.out.print((int) c + " ");
        }
        List<Character> chrSet = new ArrayList<>();
        List<Integer> chrCnt = new ArrayList<>();
        for (char c : chrArr) {
            Character chr = Character.valueOf(c);
            int idx = chrSet.indexOf(chr);
            if (idx == -1) {
                chrSet.add(chr);
                chrCnt.add(1);
            } else {
                chrCnt.set(idx, chrCnt.get(idx) + 1);
            }
        }
        // テスト表示
        for (char c : chrArr) {
            System.out.print(c + " ");
        }
        System.out.println("\n" + chrSet);
        System.out.println(chrCnt);

        // [in][out(末尾は終了)]
        int[][] MarkovTbl = new int[chrSet.size()][chrSet.size() + 1];
        for (int i = 0; i < chrArr.length - 1; i++) {
            int idx1 = chrSet.indexOf(chrArr[i]);
            int idx2 = chrSet.indexOf(chrArr[i + 1]);
            MarkovTbl[idx1][idx2] += 1;
        }
        // 終了を設定
        int idx1 = chrSet.indexOf(chrArr[chrArr.length - 1]);
        MarkovTbl[idx1][chrSet.size()] += 1;
        // 表示
        for (int[] row : MarkovTbl) {
            for (int n : row) {
                System.out.print(n + " ");
            }
            System.out.println();
        }

        System.out.print("Exec count\n> ");
        int loopCount = sc.nextInt();
        boolean equalMode = false;
        if (loopCount == 0) {
            loopCount = 1000;
            equalMode = true;
        }
        for (int j = 0; j < loopCount; j++) {

            char temp = chrSet.get(0);
            int idx = chrSet.indexOf(temp);
            String result = "";
            int cnt = 0;
            while (cnt < 1000) {
                if (idx == -1) {
                    System.out.println("not found");
                    break;
                } else if (idx == chrSet.size()) {
                    // System.out.println("end");
                    break;
                } else {
                    temp = chrSet.get(idx);
                    result += String.valueOf(temp);

                    int r = rd.nextInt(chrCnt.get(idx));
                    int i = -1;
                    while (r >= 0) {
                        i++;
                        r -= MarkovTbl[idx][i];
                    }
                    idx = i;
                }
                cnt++;
            }

            System.out.println(result);
            if (equalMode) {
                if (result.equals(text)) {
                    break;
                }
            }

        }

        sc.close();

    }
}
