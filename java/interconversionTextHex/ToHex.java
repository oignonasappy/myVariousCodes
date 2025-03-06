class ToHex {
    public static void main(String[] args) throws Exception {
        String[] ss = {
                "hello",
                "world"
        };
        for (String s : ss) {
            for (char c : s.toCharArray()) {
                System.out.print(String.format("%04x", (short) c).toUpperCase());
            }
            System.out.print('\n');
        }
    }
}
