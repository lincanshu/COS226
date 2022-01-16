/* *****************************************************************************
 *  Name: Lincanshu
 *  Date: 2020/8/3
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;
    private Node root;
    private int size;

    // This saves the 8-byte inner class object overhead.
    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to the node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top substree

        Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    // construct an empty set of points
    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    /**
     * Search and insert.
     * The algorithms for search and insert are similar to those for BSTs, but at the root we use
     * the x-coordinate (if the point to be inserted has a smaller x-coordinate than
     * the point at the root, go left; otherwise go right); then at the next level, we use the
     * y-coordinate (if the point to be inserted has a smaller y-coordinate than the point in the
     * node, go left; otherwise go right); then at the next level the x-coordinate, and so forth.
     *
     * @param p
     */
    public void insert(Point2D p) {
        checkNull(p);
        root = insert(root, null, p, VERTICAL, true);
    }

    // helper method for insert()
    private Node insert(Node x, Node prev, Point2D p, boolean flag, boolean less) {
        if (x == null) {
            size++;
            if (prev == null) {
                return new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0));
            }
            if (flag == HORIZONTAL && less) {
                return new Node(p, new RectHV(prev.rect.xmin(), prev.rect.ymin(),
                                              prev.p.x(), prev.rect.ymax()));
            }
            if (flag == HORIZONTAL && !less) {
                return new Node(p, new RectHV(prev.p.x(), prev.rect.ymin(),
                                              prev.rect.xmax(), prev.rect.ymax()));
            }
            if (flag == VERTICAL && less) {
                return new Node(p, new RectHV(prev.rect.xmin(), prev.rect.ymin(),
                                              prev.rect.xmax(), prev.p.y()));
            }
            if (flag == VERTICAL && !less) {
                return new Node(p, new RectHV(prev.rect.xmin(), prev.p.y(),
                                              prev.rect.xmax(), prev.rect.ymax()));
            }
        }
        else {
            if (p.equals(x.p)) return x;

            if (flag == VERTICAL) {
                if (p.x() < x.p.x()) {
                    x.lb = insert(x.lb, x, p, HORIZONTAL, true);
                }
                else {
                    x.rt = insert(x.rt, x, p, HORIZONTAL, false);
                }
            }
            if (flag == HORIZONTAL) {
                if (p.y() < x.p.y()) {
                    x.lb = insert(x.lb, x, p, VERTICAL, true);
                }
                else {
                    x.rt = insert(x.rt, x, p, VERTICAL, false);
                }
            }
        }
        return x;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkNull(p);
        return contains(root, p, VERTICAL);
    }

    // helper method for contains()
    private boolean contains(Node x, Point2D p, boolean flag) {
        if (x == null) return false;
        if (p.equals(x.p)) return true;

        if (flag == VERTICAL) {
            if (p.x() < x.p.x()) {
                return contains(x.lb, p, HORIZONTAL);
            }
            else {
                return contains(x.rt, p, HORIZONTAL);
            }
        }
        else {
            if (p.y() < x.p.y()) {
                return contains(x.lb, p, VERTICAL);
            }
            else {
                return contains(x.rt, p, VERTICAL);
            }
        }

    }

    /**
     * Draw.
     * A 2d-tree divides the unit square in a simple way:
     * all the points to the left of the root go in the left subtree;
     * all those to the right go in the right subtree;
     * and so forth, recursively.
     * Your draw() method should draw all of the points to standard draw in black and the
     * subdivisions in red (for vertical splits) and blue (for horizontal splits).
     * This method need not be efficient—it is primarily for debugging.
     */
    public void draw() {
        draw(root, true);
    }

    // helper function for draw()
    private void draw(Node x, boolean flag) {
        if (x == null) return;

        StdDraw.setPenRadius(0.004);
        // draw subdivisions
        if (flag == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }

        // draw point
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        x.p.draw();

        draw(x.lb, !flag);
        draw(x.rt, !flag);
    }

    /**
     * Range search.
     * To find all points contained in a given query rectangle, start at the root and recursively
     * search for points in both subtrees using the following pruning rule:
     * if the query rectangle does not intersect the rectangle corresponding to a node,
     * there is no need to explore that node (or its subtrees).
     * A subtree is searched only if it might contain a point contained in the query rectangle.
     *
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        Queue<Point2D> queue = new Queue<>();
        range(queue, root, rect);
        return queue;
    }

    // helper function for range
    private void range(Queue<Point2D> queue, Node x, RectHV rect) {
        if (x == null) return;
        if (!rect.intersects(x.rect)) return;
        if (rect.contains(x.p)) queue.enqueue(x.p);
        range(queue, x.lb, rect);
        range(queue, x.rt, rect);
    }

    /**
     * Nearest-neighbor search.
     * To find a closest point to a given query point, start at the root and recursively search in
     * both subtrees using the following pruning rule:
     * <p>
     * if the closest point discovered so far is closer than the distance between the query point
     * and the rectangle corresponding to a node, there is no need to explore that node (or its
     * subtrees). That is, search a node only only if it might contain a point that is closer than
     * the best one found so far. The effectiveness of the pruning rule depends on quickly finding a
     * nearby point.
     * <p>
     * To do this, organize the recursive method so that when there are two possible subtrees to go
     * down, you always choose the subtree that is on the same side of the splitting line as the
     * query point as the first subtree to explore—the closest point found while exploring the
     * first subtree may enable pruning of the second subtree.
     *
     * @param p
     * @return Point2D
     */
    public Point2D nearest(Point2D p) {
        checkNull(p);
        if (root == null) return null;
        return nearest(root, root.p, p);
    }

    // helper function for nearest
    private Point2D nearest(Node x, Point2D nearest, Point2D target) {
        if (x == null || nearest.distanceSquaredTo(target) < x.rect.distanceSquaredTo(target)) {
            return nearest;
        }
        if (target.distanceSquaredTo(x.p) < target.distanceSquaredTo(nearest)) {
            nearest = x.p;
        }
        /**
         *  two ways:
         *  1. first left then right
         *  2. first right then left
         */
        if (x.lb != null && x.rt != null) {
            if (x.lb.rect.distanceSquaredTo(target) < x.rt.rect.distanceSquaredTo(target)) {
                nearest = nearest(x.lb, nearest, target);
                if (nearest.distanceSquaredTo(target) > x.rt.rect.distanceSquaredTo(target)) {
                    nearest = nearest(x.rt, nearest, target);
                }
            }
            else {
                nearest = nearest(x.rt, nearest, target);
                if (nearest.distanceSquaredTo(target) > x.lb.rect.distanceSquaredTo(target)) {
                    nearest = nearest(x.lb, nearest, target);
                }
            }
        }
        else if (x.lb != null) {
            nearest = nearest(x.lb, nearest, target);
        }
        else if (x.rt != null) {
            nearest = nearest(x.rt, nearest, target);
        }
        return nearest;

    }

    // check if any argument is null
    private void checkNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("argument is null");
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        /**
         * test insert and contains API
         */
        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();

        StdDraw.enableDoubleBuffering();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            // System.out.println(kdtree.contains(p));

            StdDraw.clear();
            kdtree.draw();
            StdDraw.show();
            StdDraw.pause(40);
        }
        kdtree.nearest(new Point2D(0.5, 0.75));
    }
}
