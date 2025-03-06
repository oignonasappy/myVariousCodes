import java.awt.*;
import javax.swing.*;
import java.util.concurrent.Executors;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ビルトイングラフィックスライブラリSwingを使用したグラフ描画
 * <p>
 * stepの変化に応じてグラフを描画していく
 * <p>
 * Plotを継承したクラスを用いてプロットする方法を選択
 */
class Graph extends JPanel {
    Plot[] plots;
    int step;

    // 画面サイズ
    static final int WINDOW_SIZE = 800;
    // グラフの表示範囲の目安
    static final int AREA_SIZE = 640;

    // グラフのステップ(x軸のイメージ)
    static final int START_STEP = -314;
    static final int END_STEP = 314;
    // ステップに対しての倍率
    static final double STEP_SCALE = 0.01;

    // 1フレームの遅延 小さいほど早い
    static final int DELAYMS = 10;

    public static void main(String[] args) throws Exception {
        // ウィンドウの初期設定
        JFrame f = new JFrame("plot");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(WINDOW_SIZE, WINDOW_SIZE);
        f.setResizable(false);
        f.setLayout(null);
        f.setBackground(new Color(0, 0, 0));

        // メインのグラフ要素を貼っつける
        Graph p = new Graph();
        f.add(p);

        f.setVisible(true);

        p.play();
    }

    /**
     * Graphの初期設定など
     */
    private void play() {
        setBackground(null);
        setSize(getParent().getWidth(), getParent().getHeight());

        step = START_STEP;
        /* それぞれのグラフ要素Plotを記述 */
        plots = new Plot[] {
                new Point(3, Color.RED),
                new Line(Color.BLUE),
                new Area(Color.GREEN, "letsparty")
        };

        // 別スレッドでループを実行
        Executors.newSingleThreadExecutor().execute(() -> schedule());
    }

    /**
     * ループ
     */
    private void schedule() {
        while (true) {
            next();

            try {
                Thread.sleep(DELAYMS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            step++;
            if (step > END_STEP) {
                break;
            }
        }
    }

    /**
     * フレームの処理
     */
    private void next() {
        // Swingの画面処理は異なるスレッドで実行することは好ましくないため、
        // 画面処理の部分のみSwing内部の安定したスレッドに処理を依頼する
        SwingUtilities.invokeLater(() -> {
            /* それぞれのPlotの関数を記述 */
            // 主に{ -(AREA_SIZE / 2) <= x,y <= (AREA_SIZE / 2) }の範囲になるよう
            plots[0].updateCoordinate(
                    (int) (Math.sin(step * STEP_SCALE) * (AREA_SIZE / 2)),
                    (int) (Math.acos(Math.cos(step * STEP_SCALE)) * (AREA_SIZE / Math.PI)) - AREA_SIZE / 2);
            plots[1].updateCoordinate(
                    (int) (Math.acos(Math.cos(step * STEP_SCALE)) * (AREA_SIZE / Math.PI)) - AREA_SIZE / 2,
                    (int) (Math.sin(step * STEP_SCALE) * (AREA_SIZE / 2)));
            plots[2].updateCoordinate(
                    (int) (Math.sin(step * STEP_SCALE) * (AREA_SIZE / 3)),
                    (int) (Math.cos(step * STEP_SCALE) * (AREA_SIZE / 3)));

            // 画面更新
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // 以下があると以前の画面がクリアされる(上書きされない)
        // super.paintComponent(g);

        // 全てプロット
        if (plots != null) {
            for (Plot p : plots) {
                p.plot(g);
            }
        }

    }

    /**
     * 基底クラス
     * 
     * @param x
     * @param y
     * @param c {@code Color}
     */
    private abstract class Plot {
        // mainが同じファイルに書いてあるので・privateなのでprotectedである意味がないですが一応
        protected int x;
        protected int y;
        protected Color c;
        protected boolean partyMode = false;

        public Plot(Color c) {
            this.c = c;
        }

        public Plot(Color c, String aikotoba) {
            this(c);
            // 謎な実装
            StringBuffer sb = new StringBuffer();
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] cipher = md.digest(aikotoba.getBytes());
                for (int i = 0; i < cipher.length; i++) {
                    sb.append(String.format("%02x", cipher[i] & 0xff));
                }
            } catch (NoSuchAlgorithmException ex1) {
                System.err.println("no such hash algorithm");
            }
            if ("a88130a83a6e9bc144c54817919d5fa348b16493e426805780ea07c4a3e381e0".equals(sb.toString())) {
                this.partyMode = true;
            }
        }

        public void updateCoordinate(int x, int y) {
            this.x = x;
            this.y = y;
            // 不穏な実装
            if (partyMode) {
                c = Color.getHSBColor((float) Math.random(), 1.0f, 1.0f);
            }
        }

        // 原点(0,0)を画面の中心とするように
        // Graphicsの参照を引数にとり、それに描画していく
        public abstract void plot(Graphics g);
    }

    /**
     * {@code Plot}の継承
     * <p>
     * 点です。というか円です。
     * 
     * @param size
     */
    private class Point extends Plot {
        protected int size;

        /**
         * @param size is must be at least {@code 2}
         */
        public Point(int size, Color c) {
            super(c);
            this.size = size;
        }

        public Point(int size, Color c, String aikotoba) {
            super(c, aikotoba);
            this.size = size;
        }

        @Override
        public void plot(Graphics g) {
            g.setColor(c);
            g.fillOval((WINDOW_SIZE / 2) + x - (size / 2),
                    (WINDOW_SIZE / 2) + y - (size / 2),
                    size, size);
        }
    }

    /**
     * {@code Plot}の継承
     * <p>
     * 一つ前の座標と線をつなぎます
     */
    private class Line extends Plot {
        protected int prevX;
        protected int prevY;
        protected boolean initFlg = false;

        public Line(Color c) {
            super(c);
        }

        public Line(Color c, String aikotoba) {
            super(c, aikotoba);
        }

        @Override
        public void updateCoordinate(int x, int y) {
            prevX = this.x;
            prevY = this.y;
            if (!initFlg) {
                initFlg = true;
                prevX = x;
                prevY = y;
            }
            super.updateCoordinate(x, y);
        }

        @Override
        public void plot(Graphics g) {
            if (initFlg) {
                g.setColor(c);
                g.drawLine((WINDOW_SIZE / 2) + prevX, (WINDOW_SIZE / 2) + prevY,
                        (WINDOW_SIZE / 2) + x, (WINDOW_SIZE / 2) + y);
            }
        }
    }

    /**
     * {@code Plot}の継承
     * <p>
     * 原点、一つ前の座標、現在の座標で囲まれた三角形の範囲を塗りつぶします
     */
    private class Area extends Plot {
        protected int prevX;
        protected int prevY;
        protected boolean initFlg = false;

        public Area(Color c) {
            super(c);
        }

        public Area(Color c, String aikotoba) {
            super(c, aikotoba);
        }

        @Override
        public void updateCoordinate(int x, int y) {
            prevX = this.x;
            prevY = this.y;
            if (!initFlg) {
                initFlg = true;
                prevX = x;
                prevY = y;
            }
            super.updateCoordinate(x, y);
        }

        @Override
        public void plot(Graphics g) {
            if (initFlg) {
                g.setColor(c);
                g.fillPolygon(new int[] { (WINDOW_SIZE / 2), (WINDOW_SIZE / 2) + prevX, (WINDOW_SIZE / 2) + x },
                        new int[] { (WINDOW_SIZE / 2), (WINDOW_SIZE / 2) + prevY, (WINDOW_SIZE / 2) + y }, 3);
            }
        }
    }
}
