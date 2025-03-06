/*
 * This work is a derivative fan creation based on Pictured as Perfect.
 * The copyrights of the original work belong to Frums.
 */

// WIP

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

class Perfect {

    static char CELL_MAGENTA = '田';
    // '田', '■', '2',
    static char CELL_WHITE = '口';
    // '口', '□', '1',
    static char CELL_SPACE = '　';
    // '(full space)', ' ', '0',
    static char CELL_ERROR = '＊';
    // '＊', '*'

    public static void main(String[] args) throws Exception {

        // Window setting
        JFrame f = new JFrame("Pictured as Perfect");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(640, 640);
        f.setMinimumSize(new Dimension(520, 400));
        f.setResizable(true);
        f.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        controlPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Window elements
        JLabel title = new JLabel("<<< Pictured as Perfect Cipher >>>", JLabel.CENTER);
        title.setFont(new Font(null, Font.BOLD, 24));
        title.setForeground(Color.MAGENTA);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.insets = new Insets(40, 5, 5, 5);
        controlPanel.add(title, gbc);

        JLabel label1 = new JLabel("Enter text > ", JLabel.RIGHT);
        label1.setFont(new Font(null, 0, 16));
        label1.setForeground(Color.white);
        JTextArea textArea1 = new JTextArea("perfect", 3, 16);
        textArea1.setFont(new Font(null, 0, 20));
        textArea1.setForeground(Color.black);
        textArea1.setBackground(Color.LIGHT_GRAY);
        textArea1.setMargin(new Insets(8, 8, 8, 8));
        textArea1.setBorder(new LineBorder(Color.white, 4, true));
        textArea1.setLineWrap(true);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 0, 10, 0);
        controlPanel.add(label1, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        controlPanel.add(textArea1, gbc);

        JRadioButton radioEncode = new JRadioButton("Encode", true);
        radioEncode.setFont(new Font(null, 0, 16));
        radioEncode.setForeground(Color.white);
        radioEncode.setBackground(Color.black);
        JRadioButton radioDecode = new JRadioButton("Decode", false);
        radioDecode.setFont(new Font(null, 0, 16));
        radioDecode.setForeground(Color.white);
        radioDecode.setBackground(Color.black);
        ButtonGroup bGroup1 = new ButtonGroup();
        bGroup1.add(radioEncode);
        bGroup1.add(radioDecode);
        JButton button1 = new JButton("Encode");
        button1.setFont(new Font(null, Font.BOLD, 16));
        button1.setForeground(Color.magenta);
        button1.setBackground(Color.darkGray);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 5, 0, 5);
        controlPanel.add(radioEncode, gbc);
        gbc.gridy = 4;
        controlPanel.add(radioDecode, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridheight = 2;
        controlPanel.add(button1, gbc);

        JTextArea textArea2 = new JTextArea();
        textArea2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        textArea2.setForeground(Color.magenta);
        textArea2.setBackground(Color.darkGray);
        textArea2.setMargin(new Insets(8, 8, 8, 8));
        textArea2.setBorder(new LineBorder(Color.white, 4, true));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 5, 5, 5);
        controlPanel.add(textArea2, gbc);

        // Event
        radioEncode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                button1.setText("Encode");
            }
        });
        radioDecode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                button1.setText("Decode");
            }
        });
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (radioEncode.isSelected()) {
                    System.out.println("Encode");
                    textArea2.setText(encode(textArea1.getText()));
                } else {
                    System.out.println("Decode");
                    textArea2.setText(decode(textArea1.getText()));
                }
            }
        });

        // Paint
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Frame design
                g.setColor(Color.WHITE);
                g.fillRect(10, 10, getWidth() - 20, getHeight() - 20);
                g.setColor(Color.MAGENTA);
                g.fillRect(20, 20, getWidth() - 40, getHeight() - 40);
                g.setColor(Color.BLACK);
                g.fillRect(30, 30, getWidth() - 60, getHeight() - 60);

                g.setColor(Color.MAGENTA);
                g.fillRect(30, 30, 20, 20);
                g.fillRect(getWidth() - 50, 30, 20, 20);
                g.fillRect(30, getHeight() - 50, 20, 20);
                g.fillRect(getWidth() - 50, getHeight() - 50, 20, 20);
                g.setColor(Color.WHITE);
                g.fillRect(20, 20, 20, 20);
                g.fillRect(getWidth() - 40, 20, 20, 20);
                g.fillRect(20, getHeight() - 40, 20, 20);
                g.fillRect(getWidth() - 40, getHeight() - 40, 20, 20);
                g.setColor(Color.MAGENTA);
                g.fillRect(20, 20, 10, 10);
                g.fillRect(getWidth() - 30, 20, 10, 10);
                g.fillRect(20, getHeight() - 30, 10, 10);
                g.fillRect(getWidth() - 30, getHeight() - 30, 10, 10);
            }
        };
        mainPanel.setBackground(Color.BLACK);
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        f.add(mainPanel, BorderLayout.CENTER);

        f.setVisible(true);

        System.out.println("lower -> [97~122]");
        System.out.println("upper -> [65~90]");
        System.out.println("a -> " + (int) 'a');
        System.out.println("z -> " + (int) 'z');
        System.out.println("A -> " + (int) 'A');
        System.out.println("Z -> " + (int) 'Z');
        System.out.println("  -> " + (int) ' ');
    }

    static String encode(String text) {
        String[] texts = text.split("\n");
        int maxSentence = 0;
        for (int i = 0; i < texts.length; i++) {
            if (texts[i].length() > maxSentence) {
                maxSentence = texts[i].length();
            }
        }

        int[][] resultMatrix = new int[texts.length * 4 - 1][maxSentence];

        for (int i = 0; i < texts.length; i++) {
            char[] lineArr = texts[i].toCharArray();

            for (int j = 0; j < lineArr.length; j++) {

                if (97 <= lineArr[j] && lineArr[j] <= 122) {
                    int alp = lineArr[j] - 96;
                    resultMatrix[i * 4][lineArr.length - j - 1] = alp / 9;
                    resultMatrix[i * 4 + 1][lineArr.length - j - 1] = (alp % 9) / 3;
                    resultMatrix[i * 4 + 2][lineArr.length - j - 1] = alp % 3;

                } else if (65 <= lineArr[j] && lineArr[j] <= 90) {
                    int alp = lineArr[j] - 64;
                    resultMatrix[i * 4][lineArr.length - j - 1] = alp / 9;
                    resultMatrix[i * 4 + 1][lineArr.length - j - 1] = (alp % 9) / 3;
                    resultMatrix[i * 4 + 2][lineArr.length - j - 1] = alp % 3;

                } else if (lineArr[j] == 32) {
                    for (int j2 = 0; j2 < 3; j2++) {
                        resultMatrix[i * 4 + 2 - j2][lineArr.length - j - 1] = 0;
                    }
                } else {
                    for (int j2 = 0; j2 < 3; j2++) {
                        resultMatrix[i * 4 + 2 - j2][lineArr.length - j - 1] = -1;
                    }
                }
            }
        }

        String resultText = "";
        for (int i = 0; i < resultMatrix.length; i++) {
            String line = "";
            for (int j = 0; j < resultMatrix[i].length; j++) {
                if (resultMatrix[i][j] == 0) {
                    line += String.valueOf(CELL_SPACE);
                } else if (resultMatrix[i][j] == 1) {
                    line += String.valueOf(CELL_WHITE);
                } else if (resultMatrix[i][j] == 2) {
                    line += String.valueOf(CELL_MAGENTA);
                } else {
                    line += "＊";
                }
            }
            resultText += line;
            if (i != resultMatrix.length - 1) {
                resultText += "\n";
            }
        }

        return resultText;
    }

    static String decode(String text) {
        return null;
    }
}
