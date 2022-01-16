/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Map;

public final class BaseballElimination {

    private final int size;
    private final int[] w, l, r;
    private final int[][] g;
    private final boolean[] eliminated;
    // id -> string
    private final String[] teams;
    // string -> id
    private final Map<String, Integer> map;
    private final Bag<String>[] bags;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("file not found");
        }
        In in = new In(filename);
        this.size = in.readInt();
        w = new int[size];
        l = new int[size];
        r = new int[size];
        g = new int[size][size];
        eliminated = new boolean[size];
        teams = new String[size];
        map = new HashMap<>();
        bags = (Bag<String>[]) new Bag[size];
        handInput(in);

        // trivial elimination
        int maxIndex = 0;
        for (int i = 0; i < size; i++) {
            if (w[i] > w[maxIndex]) {
                maxIndex = i;
            }
        }
        for (int i = 0; i < size; i++) {
            if (w[i] + r[i] < w[maxIndex]) {
                eliminated[i] = true;
                bags[i] = new Bag<>();
                bags[i].add(teams[maxIndex]);
            }
        }

        // Nontrivial elimination
        for (int x = 0; x < size; x++) {
            if (eliminated[x]) continue;
            int vertices = size + 2;
            for (int i = 0; i < size; i++) {
                if (i == x) continue;
                for (int j = i + 1; j < size; j++) {
                    if (j == x) continue;
                    if (g[i][j] != 0) {
                        vertices++;
                    }
                }
            }

            int s = vertices - 2, t = vertices - 1;
            FlowNetwork flowNetwork = new FlowNetwork(vertices);
            // connect s to each game vertex i-j
            int game = size;
            int sum = 0;
            for (int i = 0; i < size; i++) {
                if (i == x) continue;
                flowNetwork.addEdge(new FlowEdge(i, t, w[x] + r[x] - w[i]));
                for (int j = i + 1; j < size; j++) {
                    if (j == x) continue;
                    if (g[i][j] != 0) {
                        flowNetwork.addEdge(new FlowEdge(game, i, Double.POSITIVE_INFINITY));
                        flowNetwork.addEdge(new FlowEdge(game, j, Double.POSITIVE_INFINITY));
                        flowNetwork.addEdge(new FlowEdge(s, game++, g[i][j]));
                        sum += g[i][j];
                    }
                }
            }
            FordFulkerson ff = new FordFulkerson(flowNetwork, s, t);
            if (ff.value() == sum) continue;
            else eliminated[x] = true;
            Bag<String> bag = new Bag<>();
            for (int i = 0; i < size; i++) {
                if (i != x && ff.inCut(i)) {
                    bag.add(teams[i]);
                }
            }
            bags[x] = bag;
        }
    }

    private void handInput(In in) {
        for (int i = 0; i < size; i++) {
            teams[i] = in.readString();
            map.put(teams[i], i);
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < size; j++) {
                g[i][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return size;
    }

    // all teams
    public Iterable<String> teams() {
        return map.keySet();
    }

    private void checkValidTeam(String team) {
        if (team == null || !map.containsKey(team)) {
            throw new IllegalArgumentException("team is not valid");
        }
    }

    // number of wins for given team
    public int wins(String team) {
        checkValidTeam(team);
        return w[map.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        checkValidTeam(team);
        return l[map.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        checkValidTeam(team);
        return r[map.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        checkValidTeam(team1);
        checkValidTeam(team2);
        int i = map.get(team1);
        int j = map.get(team2);
        return g[i][j];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        checkValidTeam(team);
        return eliminated[map.get(team)];
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        checkValidTeam(team);
        return bags[map.get(team)];
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
