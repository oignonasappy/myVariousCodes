import java.util.ArrayList;
import java.util.Scanner;

class DeleteSpace {
    public static void main(String[] args) throws Exception {
        // Ultra Simple delete all blank
        Scanner sc = new Scanner(System.in, "Shift-JIS");
        var strList = new ArrayList<String>();
        System.out.println("↓Type some text here↓");
        while (sc.hasNextLine()) {
            strList.add(sc.nextLine());
        }
        sc.close();
        
        StringBuilder resultString = new StringBuilder();
        while (!strList.isEmpty()) {
            for (String str : strList.removeFirst().split(" ")) {
                resultString.append(str);
            }
        }

        System.out.println(resultString);
    }
}
