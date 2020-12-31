package com.company;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

//fix resize window fuckups?
//Add zooming feature (difficult)
//Color range proportional to ratio of size decrease
//Consistent color change for all children, may need multiple color objects

class Fractals extends JFrame {
    private double ratio;
    private String name, type;

    JPanel panel = new JPanel();

    public Fractals() {}

    public Fractals(double r, String n, String t) {
        ratio = r;
        name = n;
        type = t;

        setTitle(name);
        setSize(720, 720);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel.setBackground(Color.BLACK);
        add(panel);
    }

    public Fractals(String t) {
        this(0.9, "Fractals", t);
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (this.type.contains("Circle")) {
            Ellipse c = new Ellipse(getSize().width/2, getSize().height/2, 600, 0.9);
            if (this.type.contains("Repeat")) {
                c.draw(g, 1, getSize().width/2, getSize().height/2, c.getRadius(), 1);
            } else if (this.type.contains("Double")) {
                c.draw(g, 2, getSize().width/2, getSize().height/2, c.getRadius(), 1);
            } else if (this.type.contains("Quad")) {
                c.draw(g, 4, getSize().width/2, getSize().height/2, c.getRadius(), 1);
            } else if (this.type.contains("Glitch1")) {
                c.draw(g, 3, getSize().width/2, getSize().height/2, c.getRadius(), 1);
            } else {
                c.draw(g);
            }
        } else if (this.type.contains("Square")) {
            Square s = new Square(getSize().width / 2, getSize().height / 2, 600, 600, 0.9);
            if (this.type.contains("Repeat")) {
                s.draw(g, true);
            } else {
                s.draw(g);
            }
        }
//        else if (this.type.contains("Line")) {
//            Line l = new Line(getSize().width/2, getSize().height/4, 600, 0.9);
//            if (this.type.contains("Repeat")) {
//                l.draw(g, 1, getSize().width / 2, getSize().height / 4, l.getLength(), 1);
//            } else if (this.type.contains("Cantor")) {
//                l.draw(g, 2, getSize().width / 2, getSize().height / 4, l.getLength(), 1);
//            }
//        }
    }

    @Override
    public String getName() {
        return name;
    }

    public double getRatio() {
        return ratio;
    }

    public String getFractalType() {
        return type;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setRatio(int iterations) {
        this.ratio = ratio;
    }

    public void setFractalType(String type) {
        this.type = type;
    }

    public static void mandelbrotSet() throws Exception {
        int width = 1920, height = 1080, max = 1000, black = 0;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] colors = new int[max];
        for (int i = 0; i < max; i++) {
            colors[i] = Color.HSBtoRGB(i/256f, 1, i/(i+8f));
        }

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                double c_re = 4.0/width*(col - width/2);
                double c_im = 4.0/width*(row - height/2);
                double x = 0, y = 0;
                double r2;
                int iteration = 0;
                while (x*x+y*y < 4 && iteration < max) {
                    double x_new = x*x-y*y+c_re;
                    y = 2*x*y+c_im;
                    x = x_new;
                    iteration++;
                }
                if (iteration < max)
                    image.setRGB(col, row, colors[iteration]);
                else
                    image.setRGB(col, row, black);
            }
        }
        ImageIO.write(image, "png", new File("mandelbrot.png"));
    }

    public static void main(String[] args) {
        Fractals f = new Fractals(600, "Circles", "CircleDouble");
        f.repaint();
//        try {
//            mandelbrotSet();
//        } catch (Exception e) {
//            System.out.println("nope");
//        }
    }
}

class Ellipse extends Fractals {
    private int x, y, radius;
    private double ratio = 0.9;
    Colors rainbow = new Colors(255, 0, 0, ratio);

    public Ellipse(int x, int y, int rad, double r) {
        this.x = x;
        this.y = y;
        this.radius = rad;
        ratio = r;
    }

    public void draw(Graphics g, int fractal, int x, int y, int radius, int dimension) {
        if (rainbow.getDimNum() < dimension) {
            rainbow.setRGB();
        }
        g.setColor(new Color(rainbow.dimensionColors.get(dimension - 1)[0], rainbow.dimensionColors.get(dimension - 1)[1], rainbow.dimensionColors.get(dimension - 1)[2]));
        g.drawOval(x - (radius/2), y - (radius/2), radius, radius);
        if (fractal == 1) {
            if (radius >= 1) {
                radius *= ratio;
                draw(g, 1, this.x, this.y, radius, ++dimension);
            }
        } else if (fractal == 2) {
            if (radius >= 1) {
                radius *= 0.5;
                draw(g, 2, x + radius, y, radius, ++dimension);
                draw(g, 2, x - radius, y, radius, dimension);
            }
        } else if (fractal == 3) {
            if (radius >= 1) {
                radius *= 0.5;
                draw(g, 3, x + radius, y + radius, radius, dimension);
                draw(g, 3, x - radius, y + radius, radius, dimension);
                draw(g, 3, x + radius, y - radius, radius, dimension);
                draw(g, 3, x - radius, y - radius, radius, dimension);
            }
        } else if (fractal == 4) {
            if (radius >= 20) {
                radius *= 0.5;
                draw(g, 4, x + radius, y, radius, dimension);
                draw(g, 4, x - radius, y, radius, dimension);
                draw(g, 4, x, y + radius, radius, dimension);
                draw(g, 4, x, y - radius, radius, dimension);
            }
        }
    }

    public void draw(Graphics g) {
        this.draw(g, 0, this.x, this.y, this.radius, 1);
    }

    public int getRadius() {
        return radius;
    }
}

class Square extends Fractals {
    private int x, y, lengthI, lengthJ;
    private double ratio = 0.9;
    Colors rainbow = new Colors(255, 0, 0, ratio);

    public Square(int x, int y, int rI, int rJ, double r) {
        this.x = x;
        this.y = y;
        lengthI = rI;
        lengthJ = rJ;
        ratio = r;
    }

    public void draw(Graphics g, boolean fractal) {
        g.setColor(new Color(rainbow.getR(), rainbow.getG(), rainbow.getB()));
        g.drawRect(x - (lengthI/2), y - (lengthJ/2), lengthI, lengthJ);
        if (fractal && lengthI >= 1 && lengthJ >= 1) {
            this.lengthI *= ratio;
            this.lengthJ *= ratio;
            draw(g, true);
        }
    }
    public void draw(Graphics g) {
        this.draw(g, false);
    }
}

//class Line extends Fractals {
//    private int x, y, length;
//    private double ratio = 0.9;
//    Colors rainbow = new Colors(255, 0, 0, ratio);
//
//    public Line(int x, int y, int l, double r) {
//        this.x = x;
//        this.y = y;
//        length = l;
//        ratio = r;
//    }
//
//    public void draw(Graphics g, int fractal, int x, int y, int length, int dimension) {
//        if (rainbow.getDimNum() < dimension) {
//            rainbow.setRGB();
//        }
//        g.setColor(new Color(rainbow.dimensionColors.get(dimension - 1)[0], rainbow.dimensionColors.get(dimension - 1)[1], rainbow.dimensionColors.get(dimension - 1)[2]));
//        g.drawLine(x - (length / 2), y, x + (length / 2), y);
//        if (fractal == 1) {
//            if (length >= 1) {
//                length *= ratio;
//                draw(g, 1, this.x, y + 10, length, ++dimension);
//            }
//        } else if (fractal == 2) {
//            if (length >= 1) {
//                length /= 3;
//                draw(g, 2, x - length, y + 20, length, ++dimension);
//                draw(g, 2, x + length, y + 20, length, dimension);
//            }
//        } else if (fractal == 3) /*Koch curve*/ {
//            if (length >= 1) {
//                length /= 3;
//                draw(, ++dimension);
//                draw(, dimension);
//                draw();
//            }
//        }
//    }
//
//    public void drawKoch(Graphics g, int x, int y, int length, int dimension) {
//        if (rainbow.getDimNum() < dimension) {
//            rainbow.setRGB();
//        }
//        g.setColor(new Color(rainbow.dimensionColors.get(dimension - 1)[0], rainbow.dimensionColors.get(dimension - 1)[1], rainbow.dimensionColors.get(dimension - 1)[2]));
//        g.drawLine(x - (length / 2), y, x + (length / 2), y);
//        if (length >= 1) {
//            length /= 3;
//            drawKoch(, ++dimension);
//            drawKoch(, dimension);
//            drawKoch();
//        }
//    }
//
//    public int getLength() {
//        return length;
//    }
//}

//public Triangle extends Fractals {
//        graphics.drawPolygon(new int[] {10, 20, 30}, new int[] {100, 20, 100}, 3);
//}