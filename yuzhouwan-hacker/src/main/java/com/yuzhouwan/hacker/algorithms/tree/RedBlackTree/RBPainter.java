package com.yuzhouwan.hacker.algorithms.tree.RedBlackTree;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function: RBPainter
 *
 * @author Benedict Jin
 * @since 2016/8/23
 */
public final class RBPainter implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

    // pre-defined colors
    static final Color BLACK = Color.BLACK;
    static final Color BLUE = Color.BLUE;
    static final Color CYAN = Color.CYAN;
    static final Color DARK_GRAY = Color.DARK_GRAY;
    static final Color GRAY = Color.GRAY;
    static final Color GREEN = Color.GREEN;
    static final Color LIGHT_GRAY = Color.LIGHT_GRAY;
    static final Color MAGENTA = Color.MAGENTA;
    static final Color ORANGE = Color.ORANGE;
    static final Color PINK = Color.PINK;
    static final Color RED = Color.RED;
    static final Color WHITE = Color.WHITE;
    static final Color YELLOW = Color.YELLOW;

    // default colors
    private static final Color DEFAULT_PEN_COLOR = BLACK;
    private static final Color DEFAULT_CLEAR_COLOR = WHITE;
    // default canvas size is SIZE-by-SIZE
    private static final int DEFAULT_SIZE = 512;
    // default pen radius
    private static final double DEFAULT_PEN_RADIUS = 0.002;
    // boundary of drawing canvas, 5% border
    private static final double BORDER = 0.05;
    private static final double DEFAULT_X_MIN = 0.0;
    private static final double DEFAULT_X_MAX = 1.0;
    private static final double DEFAULT_Y_MIN = 0.0;
    private static final double DEFAULT_Y_MAX = 1.0;
    // default font
    private static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 16);
    // current pen color
    private static Color penColor;
    private static int width = DEFAULT_SIZE;
    private static int height = DEFAULT_SIZE;
    // current pen radius
    private static double penRadius;
    // show we draw immediately or wait until next show?
    private static boolean defer = false;
    private static double xmin, ymin, xmax, ymax;
    // current font
    private static Font font;

    // double buffered graphics
    private static BufferedImage offScreenImage;
    private static Graphics2D offScreen, onscreen;

    // singleton for callbacks: avoids generation of extra .class files
    private static RBPainter std = new RBPainter();

    // the frame for drawing to the screen
    private static JFrame frame;

    // mouse state
    private static boolean mousePressed = false;
    private static double mouseX = 0;
    private static double mouseY = 0;

    // keyboard state
    private static Character lastKeyTyped = null;

    // static initializer
    static {
        init();
    }


    // not instantiable
    private RBPainter() {
    }

    /**
     * Set the window size to w-by-h pixels.
     *
     * @param w the width as a number of pixels
     * @param h the height as a number of pixels
     *          RunTimeException if the width or height is 0 or negative.
     */
    public static void setCanvasSize(int w, int h) {
        if (w < 1 || h < 1) throw new RuntimeException("width and height must be positive");
        width = w;
        height = h;
        init();
    }

    // init
    private static void init() {
        if (frame != null) frame.setVisible(false);
        frame = new JFrame();
        offScreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage onscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        offScreen = offScreenImage.createGraphics();
        onscreen = onscreenImage.createGraphics();
        setXscale();
        setYscale();
        offScreen.setColor(DEFAULT_CLEAR_COLOR);
        offScreen.fillRect(0, 0, width, height);
        setPenColor();
        setPenRadius();
        setFont();
        clear();

        // add antialiasing
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        offScreen.addRenderingHints(hints);

        // frame stuff
        ImageIcon icon = new ImageIcon(onscreenImage);
        JLabel draw = new JLabel(icon);

        draw.addMouseListener(std);
        draw.addMouseMotionListener(std);

        frame.setContentPane(draw);
        frame.addKeyListener(std);    // JLabel cannot get keyboard focus
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);      // closes all windows
        // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);     // closes only current window
        frame.setTitle("Standard Draw");
        frame.setJMenuBar(createMenuBar());
        frame.pack();
        frame.setVisible(true);
    }

    // create the menu bar (changed to private)
    private static JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        JMenuItem menuItem1 = new JMenuItem(" Save...   ");
        menuItem1.addActionListener(std);
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menu.add(menuItem1);
        return menuBar;
    }


    /*************************************************************************
     *  User and screen coordinate systems
     *************************************************************************/

    /**
     * Set the X scale to be the default.
     */
    private static void setXscale() {
        setXscale(DEFAULT_X_MIN, DEFAULT_X_MAX);
    }

    /**
     * Set the Y scale to be the default.
     */
    private static void setYscale() {
        setYscale(DEFAULT_Y_MIN, DEFAULT_Y_MAX);
    }

    /**
     * Set the X scale (a border is added to the values).
     *
     * @param min the minimum value of the X scale
     * @param max the maximum value of the X scale
     */
    private static void setXscale(double min, double max) {
        double size = max - min;
        xmin = min - BORDER * size;
        xmax = max + BORDER * size;
    }

    /**
     * Set the Y scale (a border is added to the values).
     *
     * @param min the minimum value of the Y scale
     * @param max the maximum value of the Y scale
     */
    public static void setYscale(double min, double max) {
        double size = max - min;
        ymin = min - BORDER * size;
        ymax = max + BORDER * size;
    }

    // helper functions that scale from user coordinates to screen coordinates and back
    private static double scaleX(double x) {
        return width * (x - xmin) / (xmax - xmin);
    }

    private static double scaleY(double y) {
        return height * (ymax - y) / (ymax - ymin);
    }

    private static double factorX(double w) {
        return w * width / Math.abs(xmax - xmin);
    }

    private static double factorY(double h) {
        return h * height / Math.abs(ymax - ymin);
    }

    private static double userX(double x) {
        return xmin + x * (xmax - xmin) / width;
    }

    private static double userY(double y) {
        return ymax - y * (ymax - ymin) / height;
    }


    /**
     * Clear the screen with the default color, white.
     */
    private static void clear() {
        clear(DEFAULT_CLEAR_COLOR);
    }

    /**
     * Clear the screen with the given color. Calls show().
     *
     * @param color the Color to make the background
     */
    static void clear(Color color) {
        offScreen.setColor(color);
        offScreen.fillRect(0, 0, width, height);
        offScreen.setColor(penColor);
        show();
    }

    /**
     * Set the pen size to the default.
     */
    static void setPenRadius() {
        setPenRadius(DEFAULT_PEN_RADIUS);
    }

    /**
     * Set the pen size to the given size.
     *
     * @param r the radius of the pen
     * @throws RuntimeException if r is negative
     */
    static void setPenRadius(double r) {
        if (r < 0) throw new RuntimeException("pen radius must be positive");
        penRadius = r * DEFAULT_SIZE;
        BasicStroke stroke = new BasicStroke((float) penRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        offScreen.setStroke(stroke);
    }

    /**
     * Set the pen color to the default which is BLACK.
     */
    static void setPenColor() {
        setPenColor(DEFAULT_PEN_COLOR);
    }

    /**
     * Set the pen color to the given color. The available pen colors are
     * BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, MAGENTA,
     * ORANGE, PINK, RED, WHITE, and YELLOW.
     *
     * @param color the Color to make the pen
     */
    static void setPenColor(Color color) {
        penColor = color;
        offScreen.setColor(penColor);
    }

    /**
     * Set the font to be the default for all string writing.
     */
    static void setFont() {
        setFont(DEFAULT_FONT);
    }

    /**
     * Set the font as given for all string writing.
     *
     * @param f the font to make text
     */
    static void setFont(Font f) {
        font = f;
    }


    /*************************************************************************
     *  Drawing geometric shapes.
     *************************************************************************/

    /**
     * Draw a line from (x0, y0) to (x1, y1).
     *
     * @param x0 the x co-ord of the starting point
     * @param y0 the y co-ord of the starting point
     * @param x1 the x co-ord of the destination point
     * @param y1 the y co-ord of the destination point
     */
    static void line(double x0, double y0, double x1, double y1) {
        offScreen.draw(new Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1), scaleY(y1)));
        show();
    }

    /**
     * Draw one pixel at (x, y).
     *
     * @param x the x co-ord of the pixel
     * @param y the y co-ord of the pixel
     */
    private static void pixel(double x, double y) {
        offScreen.fillRect((int) Math.round(scaleX(x)), (int) Math.round(scaleY(y)), 1, 1);
    }

    /**
     * Draw a point at (x, y).
     *
     * @param x the x co-ord of the point
     * @param y the y co-ord of the point
     */
    public static void point(double x, double y) {
        double xs = scaleX(x);
        double ys = scaleY(y);
        double r = penRadius;
        // double ws = factorX(2*r);
        // double hs = factorY(2*r);
        // if (ws <= 1 && hs <= 1) pixel(x, y);
        if (r <= 1) pixel(x, y);
        else offScreen.fill(new Ellipse2D.Double(xs - r / 2, ys - r / 2, r, r));
        show();
    }

    /**
     * Draw circle of radius r, centered on (x, y); degenerate to pixel if small.
     *
     * @param x the x co-ord of the center of the circle
     * @param y the y co-ord of the center of the circle
     * @param r the radius of the circle
     * @throws RuntimeException if the radius of the circle is negative
     */
    static void circle(double x, double y, double r) {
        if (r < 0) throw new RuntimeException("circle radius can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2 * r);
        double hs = factorY(2 * r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offScreen.draw(new Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws, hs));
        show();
    }

    /**
     * Draw filled circle of radius r, centered on (x, y); degenerate to pixel if small.
     *
     * @param x the x co-ord of the center of the circle
     * @param y the y co-ord of the center of the circle
     * @param r the radius of the circle
     * @throws RuntimeException if the radius of the circle is negative
     */
    static void filledCircle(double x, double y, double r) {
        if (r < 0) throw new RuntimeException("circle radius can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2 * r);
        double hs = factorY(2 * r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offScreen.fill(new Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws, hs));
        show();
    }

    /**
     * Draw an arc of radius r, centered on (x, y), from angle1 to angle2 (in degrees).
     *
     * @param x      the x co-ord of the center of the circle
     * @param y      the y co-ord of the center of the circle
     * @param r      the radius of the circle
     * @param angle1 the starting angle. 0 would mean an arc beginning at 3 o'clock.
     * @param angle2 the angle at the end of the arc. For example, if
     *               you want a 90 degree arc, then angle2 should be angle1 + 90.
     * @throws RuntimeException if the radius of the circle is negative
     */
    static void arc(double x, double y, double r, double angle1, double angle2) {
        if (r < 0) throw new RuntimeException("arc radius can't be negative");
        while (angle2 < angle1) angle2 += 360;
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2 * r);
        double hs = factorY(2 * r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offScreen.draw(new Arc2D.Double(xs - ws / 2, ys - hs / 2, ws, hs, angle1, angle2 - angle1, Arc2D.OPEN));
        show();
    }

    /**
     * Draw squared of side length 2r, centered on (x, y); degenerate to pixel if small.
     *
     * @param x the x co-ord of the center of the square
     * @param y the y co-ord of the center of the square
     * @param r radius is half the length of any side of the square
     * @throws RuntimeException if r is negative
     */
    static void square(double x, double y, double r) {
        if (r < 0) throw new RuntimeException("square side length can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2 * r);
        double hs = factorY(2 * r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offScreen.draw(new Rectangle2D.Double(xs - ws / 2, ys - hs / 2, ws, hs));
        show();
    }

    /**
     * Draw a filled square of side length 2r, centered on (x, y); degenerate to pixel if small.
     *
     * @param x the x co-ord of the center of the square
     * @param y the y co-ord of the center of the square
     * @param r radius is half the length of any side of the square
     * @throws RuntimeException if r is negative
     */
    static void filledSquare(double x, double y, double r) {
        if (r < 0) throw new RuntimeException("square side length can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2 * r);
        double hs = factorY(2 * r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offScreen.fill(new Rectangle2D.Double(xs - ws / 2, ys - hs / 2, ws, hs));
        show();
    }

    /**
     * Draw a polygon with the given (x[i], y[i]) coordinates.
     *
     * @param x an array of all the x co-ordindates of the polygon
     * @param y an array of all the y co-ordindates of the polygon
     */
    public static void polygon(double[] x, double[] y) {
        int N = x.length;
        GeneralPath path = new GeneralPath();
        path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
        for (int i = 0; i < N; i++)
            path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
        path.closePath();
        offScreen.draw(path);
        show();
    }

    /**
     * Draw a filled polygon with the given (x[i], y[i]) coordinates.
     *
     * @param x an array of all the x co-ordindates of the polygon
     * @param y an array of all the y co-ordindates of the polygon
     */
    static void filledPolygon(double[] x, double[] y) {
        int N = x.length;
        GeneralPath path = new GeneralPath();
        path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
        for (int i = 0; i < N; i++)
            path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
        path.closePath();
        offScreen.fill(path);
        show();
    }


    /*************************************************************************
     * Drawing images.
     *************************************************************************/

    /**
     * Get an image from the given filename.
     */
    private static Image getImage(String filename) {

        // to read from file
        ImageIcon icon = new ImageIcon(filename);

        // try to read from URL
        if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            try {
                URL url = new URL(filename);
                icon = new ImageIcon(url);
            } catch (Exception e) { /* not a url */ }
        }

        // in case file is inside a .jar
        if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            URL url = RBPainter.class.getResource(filename);
            if (url == null) throw new RuntimeException("image " + filename + " not found");
            icon = new ImageIcon(url);
        }

        return icon.getImage();
    }

    /**
     * Draw picture (gif, jpg, or png) centered on (x, y). Calls show().
     *
     * @param x the center x co-ord of the image
     * @param y the center y co-ord of the image
     * @param s the name of the image/picture, i.e. "ball.gif"
     * @throws RuntimeException if the image's width or height are negative
     */
    public static void picture(double x, double y, String s) {
        Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = image.getWidth(null);
        int hs = image.getHeight(null);
        if (ws < 0 || hs < 0) throw new RuntimeException("image " + s + " is corrupt");

        offScreen.drawImage(image, (int) Math.round(xs - ws / 2.0), (int) Math.round(ys - hs / 2.0), null);
        show();
    }

    /**
     * Draw picture (gif, jpg, or png) centered on (x, y).
     * Rescaled to w-by-h. Degenerates to pixel if small. Calls show().
     *
     * @param x the center x co-ord of the image
     * @param y the center y co-ord of the image
     * @param s the name of the image/picture, i.e. "ball.gif"
     * @param w the width of the image
     * @param h the height of the image
     */
    public static void picture(double x, double y, String s, double w, double h) {
        Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(w);
        double hs = factorY(h);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else {
            offScreen.drawImage(image, (int) Math.round(xs - ws / 2.0),
                    (int) Math.round(ys - hs / 2.0),
                    (int) Math.round(ws),
                    (int) Math.round(hs), null);
        }
        show();
    }


    /*************************************************************************
     *  Drawing text.
     *************************************************************************/

    /**
     * Write the given text string in the current font, center on (x, y).
     * Calls show().
     *
     * @param x the center x co-ord of the text
     * @param y the center y co-ord of the text
     * @param s the text
     */
    static void text(double x, double y, String s) {
        offScreen.setFont(font);
        FontMetrics metrics = offScreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = metrics.stringWidth(s);
        int hs = metrics.getDescent();
        offScreen.drawString(s, (float) (xs - ws / 2.0), (float) (ys + hs));
        show();
    }

    /**
     * Display on screen and pause for t milliseconds.
     * Calling this method means that the screen will NOT be redrawn
     * after each line(), circle(), or square(). This is useful when there
     * are many methods to call to draw a complete picture.
     *
     * @param t number of milliseconds
     */
    public static void show(int t) {
        defer = true;
        onscreen.drawImage(offScreenImage, 0, 0, null);
        frame.repaint();
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping");
        }
    }


    /**
     * Display on-screen;
     * calling this method means that the screen WILL be redrawn
     * after each line(), circle(), or square(). This is the default.
     */
    public static void show() {
        if (!defer) onscreen.drawImage(offScreenImage, 0, 0, null);
        if (!defer) frame.repaint();
    }


    /*************************************************************************
     *  Save drawing to a file.
     *************************************************************************/

    /**
     * Save to file - suffix must be png, jpg, or gif.
     *
     * @param filename the name of the file with one of the required suffixes
     */
    public static void save(String filename) {
        File file = new File(filename);
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);

        // png files
        if (suffix.toLowerCase().equals("png"))
            try {
                ImageIO.write(offScreenImage, suffix, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // need to change from ARGB to RGB for jpeg
            // reference: http://archives.java.sun.com/cgi-bin/wa?A2=ind0404&L=java2d-interest&D=0&P=2727
        else if (suffix.toLowerCase().equals("jpg")) {
            WritableRaster raster = offScreenImage.getRaster();
            WritableRaster newRaster;
            newRaster = raster.createWritableChild(0, 0, width, height, 0, 0, new int[]{0, 1, 2});
            DirectColorModel cm = (DirectColorModel) offScreenImage.getColorModel();
            DirectColorModel newCM = new DirectColorModel(cm.getPixelSize(),
                    cm.getRedMask(),
                    cm.getGreenMask(),
                    cm.getBlueMask());
            BufferedImage rgbBuffer = new BufferedImage(newCM, newRaster, false, null);
            try {
                ImageIO.write(rgbBuffer, suffix, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid image file type: " + suffix);
        }
    }

    /**
     * Is the mouse being pressed?
     *
     * @return true or false
     */
    public static boolean mousePressed() {
        return mousePressed;
    }


    /*************************************************************************
     *  Mouse interactions.
     *************************************************************************/

    /**
     * Where is the mouse?
     *
     * @return the value of the X co-ord of the mouse
     */
    public static double mouseX() {
        return mouseX;
    }

    /**
     * Where is the mouse?
     *
     * @return the value of the Y co-ord of the mouse
     */
    public static double mouseY() {
        return mouseY;
    }

    /**
     * Has the user typed a key?
     *
     * @return true if the user has typed a key, false otherwise
     */
    public static boolean hasNextKeyTyped() {
        return lastKeyTyped != null;
    }

    /**
     * What is the next key that was typed by the user?
     *
     * @return the next key typed
     */
    public static char nextKeyTyped() {
        char c = lastKeyTyped;
        lastKeyTyped = null;
        return c;
    }

    public static void main(String[] args) {
        RBPainter.square(.2, .8, .1);
        RBPainter.filledSquare(.8, .8, .2);
        RBPainter.circle(.8, .2, .2);

        RBPainter.setPenColor(RBPainter.MAGENTA);
        RBPainter.setPenRadius(.02);
        RBPainter.arc(.8, .2, .1, 200, 45);

        // draw a blue diamond
        RBPainter.setPenRadius();
        RBPainter.setPenColor(RBPainter.BLUE);
        double[] x = {.1, .2, .3, .2};
        double[] y = {.2, .3, .2, .1};
        RBPainter.filledPolygon(x, y);

        // text
        RBPainter.setPenColor(RBPainter.BLACK);
        RBPainter.text(0.2, 0.5, "black text");
        RBPainter.setPenColor(RBPainter.WHITE);
        RBPainter.text(0.7, 0.8, "white text");
    }

    /**
     * Open a save dialog when the user selects "Save As" from the menu.
     */
    public void actionPerformed(ActionEvent e) {
        FileDialog chooser = new FileDialog(RBPainter.frame, "Use a .png or .jpg extension", FileDialog.SAVE);
        chooser.setVisible(true);
        String filename = chooser.getFile();
        if (filename != null) {
            RBPainter.save(chooser.getDirectory() + File.separator + chooser.getFile());
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        mouseX = RBPainter.userX(e.getX());
        mouseY = RBPainter.userY(e.getY());
        mousePressed = true;
    }


    /*************************************************************************
     * Keyboard interactions.
     *************************************************************************/

    public void mouseReleased(MouseEvent e) {
        mousePressed = false;
    }

    public void mouseDragged(MouseEvent e) {
        mouseX = RBPainter.userX(e.getX());
        mouseY = RBPainter.userY(e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = RBPainter.userX(e.getX());
        mouseY = RBPainter.userY(e.getY());
    }

    public void keyTyped(KeyEvent e) {
        lastKeyTyped = e.getKeyChar();
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
