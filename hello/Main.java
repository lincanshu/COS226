/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Scanner;

public class Main {

    private int cal(int number) {
        int bottoms = 0;
        while (number >= 3) {
            bottoms += number / 3;
            number = number % 3 + number / 3;
        }
        if (number == 2) {
            bottoms++;
        }
        return bottoms;
    }

    public Main() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        for (int i = 0; i < n; i++) {
            int number = in.nextInt();
            int result = cal(number);
            System.out.println(result);
        }
    }

    public static void main(String[] args) {
        Main solution = new Main();
    }
}
