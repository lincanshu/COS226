/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.util.Arrays;

public class SeamCarver {

    private Picture picture;
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("in SeamCarve, picture is null");
        }
        this.picture = picture;
        int height = picture.height();
        int width = picture.width();
        energy = new double[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                energy[i][j] = energy(j, i);
            }
        }
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    private int thetaX2(int x, int y) {
        int rgb1 = picture.getRGB(x - 1, y);
        int rgb2 = picture.getRGB(x + 1, y);
        int rx = ((rgb1 >> 16) & 0xFF) - ((rgb2 >> 16) & 0xFF);
        int gx = ((rgb1 >> 8) & 0xFF) - ((rgb2 >> 8) & 0xFF);
        int bx = (rgb1 & 0xFF) - (rgb2 & 0xFF);
        return rx * rx + gx * gx + bx * bx;
    }

    private int thetaY2(int x, int y) {
        int rgb1 = picture.getRGB(x, y - 1);
        int rgb2 = picture.getRGB(x, y + 1);
        int ry = ((rgb1 >> 16) & 0xFF) - ((rgb2 >> 16) & 0xFF);
        int gy = ((rgb1 >> 8) & 0xFF) - ((rgb2 >> 8) & 0xFF);
        int by = (rgb1 & 0xFF) - (rgb2 & 0xFF);
        return ry * ry + gy * gy + by * by;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IllegalArgumentException("x or y is outside its prescribed range");
        }
        // the pixel at the border of the image
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
            return 1000.0;
        }
        int thethaX2 = thetaX2(x, y);
        int thethaY2 = thetaY2(x, y);
        return Math.sqrt(thethaX2 + thethaY2);
    }

    private double[][] copyEnergy() {
        int height = picture.height();
        int width = picture.width();
        double[][] copy = new double[height][width];
        for (int i = 0; i < height; i++) {
            copy[i] = Arrays.copyOf(energy[i], width);
        }
        return copy;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int height = picture.height();
        int width = picture.width();

        double[][] dp = copyEnergy();
        int[][] edgeTo = new int[height][width];
        for (int j = 1; j < width; j++) {
            for (int i = 0; i < height; i++) {
                int k = i;
                if (i - 1 >= 0 && dp[i - 1][j - 1] < dp[k][j - 1]) {
                    k = i - 1;
                }
                if (i + 1 < height && dp[i + 1][j - 1] < dp[k][j - 1]) {
                    k = i + 1;
                }
                dp[i][j] += dp[k][j - 1];
                edgeTo[i][j] = k;
            }
        }
        // find min
        int minIndex = 0;
        for (int i = 0; i < height; i++) {
            if (dp[i][width - 1] < dp[minIndex][width - 1]) {
                minIndex = i;
            }
        }
        // count the seam
        int[] seam = new int[width];
        for (int j = width - 1; j >= 0; j--) {
            seam[j] = minIndex;
            minIndex = edgeTo[minIndex][j];
        }
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int height = picture.height();
        int width = picture.width();

        double[][] dp = copyEnergy();
        int[][] edgeTo = new int[height][width];
        for (int i = 1; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int k = j;
                if (j - 1 >= 0 && dp[i - 1][j - 1] < dp[i - 1][k]) {
                    k = j - 1;
                }
                if (j + 1 < width && dp[i - 1][j + 1] < dp[i - 1][k]) {
                    k = j + 1;
                }
                dp[i][j] += dp[i - 1][k];
                edgeTo[i][j] = k;
            }
        }
        // find min
        int minIndex = 0;
        for (int j = 0; j < width; j++) {
            if (dp[height - 1][j] < dp[height - 1][minIndex]) {
                minIndex = j;
            }
        }
        int[] seam = new int[height];
        for (int i = height - 1; i >= 0; i--) {
            seam[i] = minIndex;
            minIndex = edgeTo[i][minIndex];
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("seam is null");
        }
        if (picture.height() <= 1) {
            throw new IllegalArgumentException("the height is less than or equal to 1");
        }
        if (seam.length != picture.width()) {
            throw new IllegalArgumentException("seam is not enough");
        }

        int height = picture.height();
        int width = picture.width();

        // if the array is not a vaild seam
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= picture.height()) {
                throw new IllegalArgumentException("seam is not valid");
            }
            if (i - 1 >= 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("the diff is not valid");
            }
            if (i + 1 < seam.length && Math.abs(seam[i + 1] - seam[i]) > 1) {
                throw new IllegalArgumentException("the diff is not valid");
            }
        }

        // update the picture
        Picture res = new Picture(width, height - 1);
        for (int j = 0; j < width; j++) {
            for (int i = 0; i < res.height(); i++) {
                if (i < seam[j]) {
                    res.setRGB(j, i, picture.getRGB(j, i));
                }
                else {
                    res.setRGB(j, i, picture.getRGB(j, i + 1));
                }
            }
        }
        this.picture = res;

        // updateEnergy
        double[][] mat = new double[height - 1][width];
        for (int j = 0; j < width; j++) {
            for (int i = 0; i < mat.length; i++) {
                if (i < seam[j]) {
                    mat[i][j] = energy[i][j];
                }
                else {
                    mat[i][j] = energy[i + 1][j];
                }
            }
        }
        for (int j = 0; j < width; j++) {
            int i = seam[j];
            if (i - 1 >= 0) {
                mat[i - 1][j] = energy(j, i - 1);
            }
            if (i < picture.height()) {
                mat[i][j] = energy(j, i);
            }
        }
        this.energy = mat;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("seam is null");
        }
        if (picture.width() <= 1) {
            throw new IllegalArgumentException("the width is less than or equal to 1");
        }
        if (seam.length != picture.height()) {
            throw new IllegalArgumentException("seam is not enough");
        }
        int height = picture.height();
        int width = picture.width();

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width) {
                throw new IllegalArgumentException("seam is not valid");
            }
            if (i - 1 >= 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("the diff is not valid");
            }
            if (i + 1 < seam.length && Math.abs(seam[i + 1] - seam[i]) > 1) {
                throw new IllegalArgumentException("the diff is not valid");
            }
        }

        // update the picture
        Picture res = new Picture(width - 1, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < res.width(); j++) {
                if (j < seam[i]) {
                    res.setRGB(j, i, picture.getRGB(j, i));
                }
                else {
                    res.setRGB(j, i, picture.getRGB(j + 1, i));
                }
            }
        }
        this.picture = res;

        // updateEnergy
        for (int i = 0; i < height; i++) {
            double[] copy = new double[energy[i].length - 1];
            for (int j = 0; j < copy.length; j++) {
                if (j < seam[i]) {
                    copy[j] = energy[i][j];
                }
                else {
                    copy[j] = energy[i][j + 1];
                }
            }
            energy[i] = copy;
        }
        for (int i = 0; i < height; i++) {
            int j = seam[i];
            if (j - 1 >= 0) {
                energy[i][j - 1] = energy(j - 1, i);
            }
            if (j < picture.width()) {
                energy[i][j] = energy(j, i);
            }
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(picture);
        // int[] verticalSeam = sc.findVerticalSeam();
        // sc.removeVerticalSeam(verticalSeam);
        sc.picture.show();
        // sc.findVerticalSeam();
        // sc.findHorizontalSeam();
        // System.out.println(sc.energy(1, 2));
        // System.out.println(sc.energy(1, 1));
    }
}
