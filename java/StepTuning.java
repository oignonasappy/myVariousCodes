import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

class StepTuning {

    static final int STANDARD_Hz = 440;
    static int targetHz = 432; // 目標のHz
    static int stepRange = 64; // チューニングの0からの最大設定範囲(ステップ)
    static int maxCentRange = 75; // チューニングの最大設定幅(cent)

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.print("キーボードのチューニングの際Hzを直接入力できない、ステップ数で設定しなければいけないときのための、設定すべきステップ数\n\n");
            System.out.print("設定したいチューニング {A=xxxHz}\n> ");
            targetHz = sc.nextInt();
            if (targetHz <= 0) {
                sc.close();
                throw new InputMismatchException("入力は1以上である必要がある");
            }
            System.out.print("チューニングの0からの最大設定範囲(ステップ) {-xxx ~ 0 ~ xxx}\n> ");
            stepRange = sc.nextInt();
            if (stepRange <= 0) {
                sc.close();
                throw new InputMismatchException("入力は1以上である必要がある");
            }
            System.out.print("チューニングの最大設定幅(cent (100cent=半音)) {xxxcent}\n> ");
            maxCentRange = sc.nextInt();
            if (maxCentRange <= 0) {
                sc.close();
                throw new InputMismatchException("入力は1以上である必要がある");
            }

            sc.close();
        } catch (InputMismatchException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.print("\n");

        DecimalFormat dFormat = new DecimalFormat("+#.####;-#.####");

        double targetCent = 1200 * (Math.log((double) targetHz / STANDARD_Hz) / Math.log(2));
        if (Math.abs(targetCent) >= 100) {
            int transpose = (int) (targetCent / 100);
            System.out.printf(" Transpose : %s\n", dFormat.format(transpose));
            targetCent -= (int) (targetCent / 100) * 100;
        }

        double targetStep = targetCent / ((double) maxCentRange / stepRange);
        System.out.printf(" Step : %sstep [%s]\n(Cent : %scent [%s])\n",
                dFormat.format(Math.round(targetStep)), dFormat.format(targetStep),
                dFormat.format(Math.round(targetCent)), dFormat.format(targetCent));
    }
}
