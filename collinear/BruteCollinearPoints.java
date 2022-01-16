/* *****************************************************************************
 *  Name: lincanshu
 *  Date:2020/7/17
 *  Description: Examines 4 sortedPoints at a time and checks whether they all lie on
 *  the same line segment, returning all such line segments.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BruteCollinearPoints {

    private final LineSegment[] lineSegments;

    // finds all line segments containing 4 sortedPoints
    public BruteCollinearPoints(Point[] points) {
        checkNull(points);
        Point[] sortedPoints = points.clone();
        checkDuplicate(sortedPoints);
        List<LineSegment> list = new LinkedList<>();
        int n = sortedPoints.length;
        for (int p = 0; p < n; ++p) {
            for (int q = p + 1; q < n; ++q) {
                double slopePQ = sortedPoints[p].slopeTo(sortedPoints[q]);
                for (int r = q + 1; r < n; ++r) {
                    if (slopePQ != sortedPoints[p].slopeTo(sortedPoints[r])) continue;
                    for (int s = r + 1; s < n; ++s) {
                        if (slopePQ == sortedPoints[p].slopeTo(sortedPoints[s])) {
                            list.add(new LineSegment(sortedPoints[p], sortedPoints[s]));
                        }
                    }
                }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }
}
