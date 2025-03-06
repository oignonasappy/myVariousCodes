import java.awt.*;
import javax.swing.*;

class LinerIncreaseBPM {
    public static void main(String[] args) {
        int iteration = 1000;

        // double[] anss = new double[iteration];
        // double sumDifference = 0;
        // int stop = iteration;
        for (int i = 1; i <= iteration; i++) {
            // System.out.println(i+":");
            // calculateTime(∞, 120, 240, 4)でln(n)となります
            double ans = calculateTime(i, 120, 240, 4);
            System.out.println("[" + i + "] " + ans + " 差 " + (ans - Math.log(4)));
            if (ans == 0.0) {
                // stop = i;
                break;
            }
            // sumDifference += Math.abs(ans - Math.log(4));
            // anss[i - 1] = ans;
        }
        /*
         * double sum = 0;
         * for (int i = 0; i < stop; i++) {
         * sum += anss[i];
         * }
         */
        // System.out.println("avg " + (sum / stop));
        // System.out.println("" + sumDifference);

        System.out.println("ln(4) = " + Math.log(4));
    }

    // 線形に増加するBPMの経過時間
    // 細かく分割して疑似的に積分する
    // 引数(細かさ, 初期BPM, 終了BPM, 1小節毎の四分音符の数(拍子, x/4))
    static double calculateTime(int detail, int sBPM, int eBPM, int length) {
        int startBPM = sBPM;
        int endBPM = eBPM;
        if (sBPM > eBPM) {
            startBPM = eBPM;
            endBPM = sBPM;
        }
        double sum = 0;
        double bpm = startBPM;
        for (int i = 1; i <= detail; i++) {
            sum += (60 * length) / (double) detail / bpm;
            // System.out.print("BPM" + bpm + " ");
            // BPMの寄りを(ほぼ)中央に合わせる
            bpm = startBPM
                    + (((endBPM - startBPM) * i / (double) detail) + ((endBPM - startBPM) * (i + 1) / (double) detail))
                            / 2;
        }

        return sum;
    }
}
