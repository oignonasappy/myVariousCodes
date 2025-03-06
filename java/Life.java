import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import javax.swing.*;
import javax.swing.event.*;

class Life extends JPanel {

    static int blockSize = 8;
    static final int TOP_MARGIN = 60;

    static boolean[][] lifeBoard;
    static int delayMs = 1000;

    public static void main(String[] args) throws Exception {
        JFrame f = new JFrame("Life Game");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setMinimumSize(new Dimension(480, 480));
        f.setSize(640, 640);
        f.setLayout(null);

        Life l = new Life();

        /* speed */
        JLabel speedLabel = new JLabel("Speed : 1FPS");
        speedLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        speedLabel.setHorizontalAlignment(JLabel.LEFT);
        speedLabel.setBounds(10, 5, 140, 20); // 0~160, 0~30
        f.add(speedLabel);
        JSlider speedSlider = new JSlider(0, 1000, 1);
        speedSlider.setBounds(15, 30, 130, 30); // 0~160, 30~60
        speedSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int sliderVal = speedSlider.getValue();
                if (sliderVal == 0) {
                    delayMs = 0;
                    speedLabel.setText("Speed : STOP");
                } else {
                    final double MINFPS = 1.0;
                    final double MAXFPS = 100.0;
                    double logMin = Math.log10(MINFPS);
                    double logMax = Math.log10(MAXFPS);
                    double logRange = logMax - logMin;
                    double fps = Math.pow(10, logMin + sliderVal * logRange / speedSlider.getMaximum());
                    delayMs = (int) (1000 / fps);
                    if (fps < 10) {
                        speedLabel.setText("Speed : " + (Math.round(fps * 10) / 10.0) + "FPS");
                    } else {
                        speedLabel.setText("Speed : " + Math.round(fps) + "FPS");
                    }

                }

            }
        });
        f.add(speedSlider);
        /* size */
        JLabel sizeLabel = new JLabel("Size : 8");
        sizeLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        sizeLabel.setHorizontalAlignment(JLabel.LEFT);
        sizeLabel.setBounds(195, 5, 90, 20); // 160~320, 0~30
        f.add(sizeLabel);
        JSlider sizeSlider = new JSlider(1, 16, 8);
        sizeSlider.setBounds(175, 30, 130, 30); // 160~320, 0~30
        sizeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                blockSize = sizeSlider.getValue();
                sizeLabel.setText("Size : " + blockSize);
            }
        });
        f.add(sizeSlider);
        /* randomButton */
        JButton randomButton = new JButton("RANDOM");
        randomButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        randomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < lifeBoard.length; i++) {
                    for (int j = 0; j < lifeBoard[i].length; j++) {
                        if (Math.random() > 0.8) {
                            lifeBoard[i][j] = true;
                        }
                    }
                }
                l.repaint();
            }
        });
        randomButton.setBounds(335, 10, 110, 40); // 320~440, 0~60
        f.add(randomButton);
        /* clearButton */
        JButton clearButton = new JButton("CLEAR");
        clearButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lifeBoard = new boolean[lifeBoard.length][lifeBoard[0].length];
                l.repaint();
            }
        });
        clearButton.setBounds(455, 10, 110, 40); // 440~560, 0~60
        f.add(clearButton);
        /* lifeGame */
        f.add(l);
        l.start();

        f.setVisible(true);
    }

    Life() {

    }

    private void start() {
        lifeBoard = new boolean[(640 - TOP_MARGIN) / blockSize + 1][640 / blockSize + 1];
        for (int i = 0; i < lifeBoard.length; i++) {
            for (int j = 0; j < lifeBoard[i].length; j++) {
                if (Math.random() > 0.5) {
                    lifeBoard[i][j] = true;
                }
            }
        }

        setBackground(new Color(240, 245, 240));
        Executors.newSingleThreadExecutor().execute(() -> schedule());
    }

    private void schedule() {
        while (true) {
            nextFrame();
            try {
                // System.out.println(delayMs);
                while (delayMs == 0) {
                    Thread.sleep(100);
                }
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void nextFrame() {
        SwingUtilities.invokeLater(() -> {
            setBounds(0, TOP_MARGIN, getParent().getWidth(), getParent().getHeight() - TOP_MARGIN);
            int currentWidth = getWidth();
            int currentHeight = getHeight();
            // System.out.println(getParent().getWidth() + " " + getParent().getHeight());
            // System.out.println(currentWidth + " " + currentHeight);
            if (currentHeight >= 160 && currentWidth >= 160) {
                lifeBoard = lifeGame(lifeBoard, currentWidth, currentHeight);
            }

            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GREEN);
        g.drawLine(0, 0, getWidth(), 0);

        g.setColor(Color.BLACK);
        if (blockSize >= 8) {
            for (int i = 0; i < lifeBoard.length; i++) {
                for (int j = 0; j < lifeBoard[i].length; j++) {
                    if (lifeBoard[i][j]) {
                        g.fillRect(j * blockSize + 1, i * blockSize + 1, blockSize - 2, blockSize - 2);
                    }
                }
            }
        } else if (blockSize >= 4) {
            for (int i = 0; i < lifeBoard.length; i++) {
                for (int j = 0; j < lifeBoard[i].length; j++) {
                    if (lifeBoard[i][j]) {
                        g.fillRect(j * blockSize, i * blockSize, blockSize - 1, blockSize - 1);
                    }
                }
            }
        } else {
            for (int i = 0; i < lifeBoard.length; i++) {
                for (int j = 0; j < lifeBoard[i].length; j++) {
                    if (lifeBoard[i][j]) {
                        g.fillRect(j * blockSize, i * blockSize, blockSize, blockSize);
                    }
                }
            }
        }

    }

    private boolean[][] lifeGame(boolean[][] inBoard, int width, int height) {
        boolean[][] outBoard = new boolean[height / blockSize + 1][width / blockSize + 1];

        int boundY = inBoard.length < outBoard.length ? inBoard.length : outBoard.length;
        int boundX = inBoard[0].length < outBoard[0].length ? inBoard[0].length : outBoard[0].length;

        for (int i = 0; i < boundY; i++) {
            for (int j = 0; j < boundX; j++) {
                outBoard[i][j] = searchArround(inBoard, i, j, boundY, boundX);
            }
        }

        return outBoard;
    }

    static final int[][] SURROUND = {
            { -1, -1 }, { -1, 0 }, { -1, 1 },
            { 0, -1 }, { 0, 1 },
            { 1, -1 }, { 1, 0 }, { 1, 1 }
    };

    private boolean searchArround(boolean[][] inBoard, int i, int j, int boundY, int boundX) {
        int cnt = 0;
        for (int[] pair : SURROUND) {
            int y = i + pair[0];
            int x = j + pair[1];

            if (y < 0) {
                y = boundY - 1;
            }
            if (x < 0) {
                x = boundX - 1;
            }
            if (inBoard[y % boundY][x % boundX]) {
                cnt++;
                if (cnt > 3) {
                    break;
                }
            }
        }
        if (inBoard[i][j]) {
            if (cnt == 2 || cnt == 3) {
                return true;
            }
        } else {
            if (cnt == 3) {
                return true;
            }
        }
        return false;
    }
}
