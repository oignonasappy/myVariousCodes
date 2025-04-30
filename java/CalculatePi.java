import java.util.function.UnaryOperator;
import java.util.function.Function;

class CalculatePi {
    public static void main(String[] args) {
        // 自然数の階乗を計算（むりやりラムダ式）
        UnaryOperator<Integer> fact = new UnaryOperator<>() {
            public Integer apply(Integer n) {
                return ((Function<UnaryOperator<Integer>, UnaryOperator<Integer>>) self -> x ->
                        (x <= 1) ? 1 : x * self.apply(x - 1)).apply(this).apply(n);
            }
        };

        // チェドノフスキー級数でのπの計算
        // しかし！！doubleの範囲内だけで計算しているので、一撃で情報落ち！！！！アバー！！！！！！
        Function<Integer, Double> chudnovsky = (n) -> {
            double summa = 0.0;
            for (int i = 0; i < n; i++) {
                summa += (Math.pow(-1, i) * fact.apply(6 * i)) / (fact.apply(3 * i) * Math.pow(fact.apply(n), 3)) *
                        ((13591409.0 + 545140134.0 * i) / Math.pow(Math.pow(640320, 3), i + 0.5));
            }
            return 1 / (12 * summa);
        };

        System.out.println(chudnovsky.apply(1) + "\n" + Math.PI);
        // 3.1415926535897345
        // 3.141592653589793
        System.out.println(chudnovsky.apply(2) + "\n" + Math.PI);
        // 25.13274122871835 は？
        // 3.141592653589793
    }
}
