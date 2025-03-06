class ToText {
    public static void main(String[] args) {
        String[] ss = {
                "00680065006C006C006F",
                "0077006F0072006C0064"
        };
        for (String s : ss) {
            for (int i = 0; i < s.length(); i += 4) {
                System.out.print(Character.toChars(Integer.parseInt(s.substring(i, i + 4), 16)));
            }
            System.out.print('\n');
        }
    }
}
