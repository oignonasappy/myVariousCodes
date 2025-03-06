import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;

class MarkovApp {
    public static void main(String[] args) throws Exception {
        Frame f = new Frame("Markov Chain");
        f.setSize(800, 640);
        f.setResizable(false);
        f.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 画面の要素
        Label label1 = new Label("マルコフしたい文字列");
        TextField textField1 = new TextField(50);
        gbc.gridx = 0;
        gbc.gridy = 0;
        f.add(label1, gbc);
        gbc.gridx = 1;
        f.add(textField1, gbc);

        Label label2 = new Label("試行回数 (0:完全一致モード)");
        TextField numField2 = new TextField(5);
        gbc.gridx = 0;
        gbc.gridy = 1;
        f.add(label2, gbc);
        gbc.gridx = 1;
        f.add(numField2, gbc);

        Button StartButton = new Button("実行");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        f.add(StartButton, gbc);

        TextArea textArea = new TextArea(24, 100);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        f.add(textArea, gbc);

        Label resultLabel1 = new Label("");
        Label resultLabel2 = new Label("");
        Label resultLabel3 = new Label("");
        gbc.gridx = 0;
        gbc.gridy = 4;
        f.add(resultLabel1, gbc);
        gbc.gridy = 5;
        f.add(resultLabel2, gbc);
        gbc.gridy = 6;
        f.add(resultLabel3, gbc);

        StartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("clicked");

                try {
                    if (numField2.getText().isEmpty()) {
                        String[] returnArr = markov(textField1.getText(), 0);
                        textArea.setText(returnArr[0]);
                        resultLabel1.setText(returnArr[1]);
                        resultLabel2.setText(returnArr[2]);
                        resultLabel3.setText(returnArr[3]);
                    } else if (Integer.parseInt(numField2.getText()) < 0) {
                        showDialog(f, "正の整数を入力してください");
                    } else {
                        String[] returnArr = markov(textField1.getText(), Integer.parseInt(numField2.getText()));
                        textArea.setText(returnArr[0]);
                        resultLabel1.setText(returnArr[1]);
                        resultLabel2.setText(returnArr[2]);
                        resultLabel3.setText(returnArr[3]);
                    }
                } catch (NumberFormatException ex) {
                    showDialog(f, "［試行回数］には半角数字を入力してください");
                }

            }
        });

        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        f.setVisible(true);

    }

    private static void showDialog(Frame f, String message) {
        Dialog dialog = new Dialog(f, "Error", true);
        dialog.setLayout(new BorderLayout());
        Label label = new Label(message, Label.CENTER);
        Button okButton = new Button("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });

        dialog.add(label, BorderLayout.CENTER);
        dialog.add(okButton, BorderLayout.SOUTH);
        dialog.setSize(300, 100);
        dialog.setVisible(true);
    }

    // return: result, 一致回数||一致までの回数, 最短文字列, 最長文字列
    public static String[] markov(String text, int loopCount) {
        Random rd = new Random();

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

        boolean equalMode = false;
        if (loopCount == 0) {
            loopCount = 1000;
            equalMode = true;
        }
        String[] returnArr = new String[4];
        String returnText = "";
        // equalModeにより用途が変化
        int equalCnt = 0;
        String minText = text;
        String maxText = "";

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
            returnText += result + "\n";

            if(result.length() < minText.length()){
                minText = result;
            }
            if(result.length() > maxText.length()){
                maxText = result;
            }

            if (result.equals(text)) {
                if (equalMode) {
                    equalCnt = j + 1;
                    break;
                } else {
                    equalCnt++;
                }
            }

        }

        returnArr[0] = returnText;
        if (equalMode) {
            returnArr[1] = "一致までの回数 : " + equalCnt;
        } else {
            returnArr[1] = "一致回数 : " + equalCnt;
        }
        returnArr[2] = "最短文字列 : " + minText;
        returnArr[3] = "最長文字列 : " + maxText;
        return returnArr;
    }
}
