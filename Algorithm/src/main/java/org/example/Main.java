package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static int[][] cost = {
            {8, 6, 10, 9},
            {9, 12, 13, 7},
            {14, 9, 16, 5}
    };

    public static int[] supply_initial = {20, 30, 50};
    public static int[] demand_initial = {30, 20, 40, 10};

    public static void main(String[] args) {
        System.out.println("North-West Corner Method: " + northWestCornerMethod(supply_initial, demand_initial));

//        System.out.println("Vogel's Approximation Method: " + vogelsApproximationMethod(supply, demand, cost));

        System.out.println("Russell's Approximation Method: " + russellsApproximationMethod(supply_initial, demand_initial));
    }

    public static int northWestCornerMethod(int[] supply1, int[] demand1) {
        int[] supply = Arrays.copyOf(supply1, supply1.length);
        int[] demand = Arrays.copyOf(demand1, demand1.length);
        int sum = 0;
        int counterforsupply = 0;
        int counterfordemand = 0;
        while (supply[2] != demand[3]) {
            if (supply[counterforsupply] < demand[counterfordemand]) {
                sum += cost[counterforsupply][counterfordemand] * supply[counterforsupply];
                demand[counterfordemand] -= supply[counterforsupply];
                supply[counterforsupply] = 0;
                counterforsupply += 1;
            } else {
                sum += cost[counterforsupply][counterfordemand] * demand[counterfordemand];
                supply[counterforsupply] -= demand[counterfordemand];
                demand[counterfordemand] = 0;
                counterfordemand += 1;
            }
        }
        sum += supply[2] * cost[2][3];
        return sum;
    }


//    public static int vogelsApproximationMethod(int[] supply, int[] demand, int[][] cost) {
//        int sum = 0;
//
//        return sum;
//    }


    public static int russellsApproximationMethod(int[] supply1, int[] demand1) {
        int[] supply = Arrays.copyOf(supply1, supply1.length);
        int[] demand = Arrays.copyOf(demand1, demand1.length);

        int sum = 0;
        int counterforsupply = 0;
        int counterfordemand = 0;
        List<Integer> all_dem_dif = new ArrayList<>();
        List<Integer> all_sup_dif = new ArrayList<>();

        while (supply.length != 1 || demand.length != 1) {
            for (int y = 0; y < 4; y++) {
                List<Integer> num_sup = new ArrayList<>();
                for (int i = 0; i < supply.length; i++) {
                    num_sup.add(cost[i][counterfordemand]);
                }
                num_sup.sort(Comparator.naturalOrder());
                all_sup_dif.add(num_sup.get(1) - num_sup.get(0));
                counterfordemand += 1;
            }

            for (int y = 0; y < 3; y++) {
                List<Integer> num_dem = new ArrayList<>();
                for (int t = 0; t < demand.length; t++) {
                    num_dem.add(cost[counterforsupply][t]);
                }
                num_dem.sort(Comparator.naturalOrder());

                all_dem_dif.add(num_dem.get(1) - num_dem.get(0));
                counterforsupply += 1;
            }

            int maxforsupply = 0;
            int demand_row = -1;
            for (int t = 0; t < all_sup_dif.size(); t++) {
                if (all_sup_dif.get(t) > maxforsupply){
                    maxforsupply = all_sup_dif.get(t);
                    demand_row = t;
                }
            }

            int minfordemand = Integer.MAX_VALUE;
            int supply_row = -1;
            for (int t = 0; t < all_dem_dif.size(); t++) {
                if (all_dem_dif.get(t) < minfordemand){
                    minfordemand = all_dem_dif.get(t);
                    supply_row = t;
                }
            }
            sum += cost[supply_row][demand_row] * Math.min(supply[supply_row], demand[demand_row]);

            if (supply[supply_row] > demand[demand_row]){
                for (int h = 0; h < demand.length; h++) {
                    cost[h][demand_row] = Integer.MAX_VALUE;
                }
            }
            else {
                for (int h = 0; h < supply.length; h++) {
                    cost[supply_row][h] = Integer.MAX_VALUE;
                }
            }
        }

        return sum;
    }
}