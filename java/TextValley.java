import java.util.Scanner;

class TextValley {
    public static void main(String[] args) throws Exception {
        // Scan command line
        Scanner sc = new Scanner(System.in, "Shift-JIS");
        String str1, str2;
        System.out.print("--Text Valley--\n\n");
        System.out.print("Text 1\n>");
        str1 = sc.nextLine();
        System.out.print("\nText 2\n>");
        str2 = sc.nextLine();
        sc.close();
        System.out.print("\n>Start<\n\n");
        str1 = str1.trim();
        str2 = str2.trim();

        // Fold to left or right or don't.
        if (str1.charAt(0) == str2.charAt(0)) {
            // < shape

            // Count a maximum match
            int matchCount = 0;
            for (int i = 0; i < Math.min(str1.length(), str2.length()); i++) {
                if (str1.charAt(i) != str2.charAt(i)) {
                    break;
                }
                if ((str1.charAt(i) == ' ' || str1.charAt(i) == '　') &&
                        (i + 1 >= Math.min(str1.length(), str2.length()) || str1.charAt(i + 1) != str2.charAt(i + 1))) {
                    break;
                }
                matchCount++;
            }

            for (int i = str1.length() - 1; i >= matchCount; i--) {
                // Skip a space
                if (str1.charAt(i) == ' ' || str1.charAt(i) == '　') {
                    continue;
                }
                System.out.println(str1.substring(0, i + 1));
            }
            for (int i = matchCount - 1; i < str2.length(); i++) {
                // Skip a space
                if (str2.charAt(i) == ' ' || str2.charAt(i) == '　') {
                    continue;
                }
                System.out.println(str2.substring(0, i + 1));
            }
        } else if (str1.charAt(str1.length() - 1) == str2.charAt(str2.length() - 1)) {
            // > shape

            int matchCount = 0;
            for (int i = 0; i < Math.min(str1.length(), str2.length()); i++) {
                if (str1.charAt(str1.length() - 1 - i) != str2.charAt(str2.length() - 1 - i)) {
                    break;
                }
                if ((str1.charAt(str1.length() - 1 - i) == ' ' || str1.charAt(str1.length() - 1 - i) == '　') &&
                        (i + 1 >= Math.min(str1.length(), str2.length())
                                || str1.charAt(str1.length() - i) != str2.charAt(str2.length() - i))) {
                    break;
                }
                matchCount++;
            }

            if (str1.length() < str2.length()) {
                StringBuilder blank = new StringBuilder();
                for (int i = 0; i < str2.length() - str1.length(); i++) {
                    blank.append(' ');
                }
                str1 = blank + str1;
            } else if (str1.length() > str2.length()) {
                StringBuilder blank = new StringBuilder();
                for (int i = 0; i < str1.length() - str2.length(); i++) {
                    blank.append(' ');
                }
                str2 = blank + str2;
            }

            // Fill in left side with blanks
            StringBuilder longBlank = new StringBuilder().repeat(' ', str1.length());
            for (int i = 0; i < str1.length() - matchCount; i++) {
                if (str1.charAt(i) == ' ' || str1.charAt(i) == '　') {
                    continue;
                }
                System.out.println(longBlank.substring(0, i) + str1.substring(i, str1.length()));
            }
            for (int i = str2.length() - matchCount; i >= 0; i--) {
                if (str2.charAt(i) == ' ' || str2.charAt(i) == '　') {
                    continue;
                }
                System.out.println(longBlank.substring(0, i) + str2.substring(i, str2.length()));
            }
        } else {
            System.err.println("Cannot create TextValley");
        }

        System.out.print("\n>End<\n");
    }
}
