import java.util.LinkedList;
import java.util.Scanner;

class PrimeFactorization {

    private static volatile boolean calculating = true;

    public static void main(String[] args) {

        // A timer to let know a calculation is in progress
        Thread wait = new Thread(() -> {
            while (calculating) {
                try {
                    Thread.sleep(1000);
                    if (calculating) {
                        System.out.print(".");
                    }
                } catch (InterruptedException e) {
                    System.out.println("Thread Interrupted");
                    break;
                }
            }
        });

        try (Scanner sc = new Scanner(System.in);) {
            System.out.print("Type a Natural number\n> "); // Max:long 9_223_372_036_854_775_807

            long num = sc.nextLong();
            wait.start();
            var factors = pFact(num);
            calculating = false;

            System.out.print("\n");
            for (PrimeFactor factor : factors) {
                System.out.println(factor.prime() + " ^ " + factor.power());
            }

            wait.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PrimeFactor[] pFact(long num) {
        var factorList = new LinkedList<PrimeFactor>();

        for (long i = 2; i * i < num; i++) {
            if (num % i != 0) {
                continue;
            }
            long temp = 0;
            while (num % i == 0) {
                temp++;
                num /= i;
            }
            factorList.add(new PrimeFactor(i, temp));
        }
        if (num != 1) {
            factorList.add(new PrimeFactor(num, 1));
        }

        return factorList.toArray(new PrimeFactor[] {});
    }
}

record PrimeFactor(long prime, long power) {
    public PrimeFactor(long prime, long power) {
        if (prime <= 1) {
            throw new java.lang.IllegalArgumentException("Prime factor is 1 or less");
        }
        if (power <= 0) {
            throw new java.lang.IllegalArgumentException("Powers of prime factor is negative or 0");
        }

        this.prime = prime;
        this.power = power;
    }
}
