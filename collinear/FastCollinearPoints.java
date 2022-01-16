/* *****************************************************************************
 *  Name: lincanshu
 *  Date: 2020/7/18
 *  Description: Given a point p, the following method determines whether p
 *  participates in a set of 4 or more collinear sortedPoints.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {

    private final LineSegment[] lineSegments;


    // finds all line segments containing 4 or more sortedPoints
    public FastCollinearPoints(Point[] points) {
        checkNull(points);
        Point[] sortedPoints = points.clone();
        checkDuplicate(sortedPoints);
        int n = sortedPoints.length;
        List<LineSegment> list = new LinkedList<>();
        Point[] copy = new Point[n];
        for (Point p : sortedPoints) {
            // copy the original reference
            for (int i = 0; i < n; ++i) {
                copy[i] = sortedPoints[i];
            }
            Arrays.sort(copy, p.slopeOrder());
            // from i to j is the equal slopes with respect to p
            for (int i = 1; i < n - 1;) {
                double slope = p.slopeTo(copy[i]);
                int j = i + 1;
                // keep the p is the most smallest
                while (j < n && slope == p.slopeTo(copy[j])) ++j;
                if (j - i >= 3 && p.compareTo(copy[i]) < 0) {
                    list.add(new LineSegment(p, copy[j - 1]));
                }
                i = j;
            }
        }
        lineSegments = list.toArray(new LineSegment[0]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    private void checkNull(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException(
                    "the argument to the constructor is null");
        }
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException(
                        "any point in the array is null");
            }
        }
    }

    private void checkDuplicate(Point[] points) {
        Arrays.sort(points);
        int n = points.length;
        for (int i = 1; i < n; ++i) {
            if (points[i].compareTo(points[i - 1]) == 0)
                throw new IllegalArgumentException(
                        "the argument to the constructor contains a repeat point");
        }
    }

    public static void main(String[] args) {

        // read the n sortedPoints from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; ++i) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the sortedPoints
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }
}
