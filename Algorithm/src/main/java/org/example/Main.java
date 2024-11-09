package org.example;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter costs");
        int[][] cost_initial = {
                {scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt()},
                {scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt()},
                {scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt()}
        };

        System.out.println("Enter supply 3 numbers");
        int[] supply_initial = {scanner.nextInt(), scanner.nextInt(), scanner.nextInt()};

        System.out.println("Enter demand 4 numbers");
        int[] demand_initial = {scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt()};

        System.out.println("North-West Corner Method: " + northWestCornerMethod(supply_initial, demand_initial, cost_initial));
        System.out.println("Vogel's Approximation Method: " + vogelApproximationMethod(supply_initial, demand_initial, cost_initial));
        System.out.println("Russel's Approximation Method: " + russellsApproximationMethod(supply_initial, demand_initial, cost_initial));

    }

    public static int northWestCornerMethod(int[] supply1, int[] demand1, int[][] cost) {
        List<Integer> supply = new ArrayList<>();
        for (int s : supply1) {
            supply.add(s);
        }

        List<Integer> demand = new ArrayList<>();
        for (int d : demand1) {
            demand.add(d);
        }

        int sum = 0;
        int counterforsupply = 0;
        int counterfordemand = 0;

        while (counterforsupply < supply.size() && counterfordemand < demand.size()) {
            if (supply.get(counterforsupply) < demand.get(counterfordemand)) {
                sum += cost[counterforsupply][counterfordemand] * supply.get(counterforsupply);
                demand.set(counterfordemand, demand.get(counterfordemand) - supply.get(counterforsupply));
                supply.set(counterforsupply, 0);
                counterforsupply += 1;
            } else {
                sum += cost[counterforsupply][counterfordemand] * demand.get(counterfordemand);
                supply.set(counterforsupply, supply.get(counterforsupply) - demand.get(counterfordemand));
                demand.set(counterfordemand, 0);
                counterfordemand += 1;
            }
        }

        return sum;
    }


    public static int vogelApproximationMethod(int[] supply1, int[] demand1, int[][] cost){
        List<Integer> supply = new ArrayList<>();
        for (int s : supply1) {
            supply.add(s);
        }

        List<Integer> demand = new ArrayList<>();
        for (int d : demand1) {
            demand.add(d);
        }

        List<int[]> costList = new ArrayList<>();
        for (int[] row : cost) {
            costList.add(row);
        }

        int sum = 0;

        while (supply.size() != 1 && demand.size() != 1) {
            List<Integer> all_dem_dif = new ArrayList<>();
            List<Integer> all_sup_dif = new ArrayList<>();
            for (int y = 0; y < demand.size(); y++) {
                List<Integer> num_sup = new ArrayList<>();
                for (int i = 0; i < supply.size(); i++) {
                    num_sup.add(costList.get(i)[y]);
                }
                num_sup.sort(Comparator.naturalOrder());
                all_sup_dif.add(num_sup.get(1) - num_sup.get(0));
            }

            for (int y = 0; y < supply.size(); y++) {
                List<Integer> num_dem = new ArrayList<>();
                for (int t = 0; t < demand.size(); t++) {
                    num_dem.add(costList.get(y)[t]);
                }
                num_dem.sort(Comparator.naturalOrder());
                all_dem_dif.add(num_dem.get(1) - num_dem.get(0));
            }


            int maxfordemand = 0;
            int demand_row = -1;
            for (int t = 0; t < all_sup_dif.size(); t++) {
                if (all_sup_dif.get(t) > maxfordemand){
                    maxfordemand = all_sup_dif.get(t);
                    demand_row = t;
                }
            }

            int maxforsupply = 0;
            int supply_row = -1;
            for (int t = 0; t < all_dem_dif.size(); t++) {
                if (all_dem_dif.get(t) > maxforsupply){
                    maxforsupply = all_dem_dif.get(t);
                    supply_row = t;
                }
            }

            if (maxforsupply > maxfordemand){
                int coord_demand = -1;
                int min_demand = Integer.MAX_VALUE;
                for (int j = 0; j < demand.size(); j++){
                    if (costList.get(supply_row)[j] < min_demand){
                        min_demand = costList.get(supply_row)[j];
                        coord_demand = j;
                    }
                }
                if (supply.get(supply_row) >= demand.get(coord_demand)){
                    sum += costList.get(supply_row)[coord_demand] * demand.get(coord_demand);
                    supply.set(supply_row, supply.get(supply_row) - demand.get(coord_demand));
                    for (int i = 0; i < costList.size(); i++) {
                        int[] originalArray = costList.get(i);
                        int[] newArray = new int[originalArray.length - 1];

                        int index = 0;
                        for (int j = 0; j < originalArray.length; j++) {
                            if (j != coord_demand) {
                                newArray[index++] = originalArray[j];
                            }
                        }
                        costList.set(i, newArray);
                    }
                    demand.remove(coord_demand);
                }
                else {
                    sum += costList.get(supply_row)[coord_demand] * supply.get(supply_row);
                    demand.set(coord_demand, demand.get(coord_demand) - supply.get(supply_row));
                    costList.remove(supply_row);
                    supply.remove(supply_row);
                }

            }
            else {
                int coord_supply = -1;
                int min_supply = Integer.MAX_VALUE;
                for (int j = 0; j < supply.size(); j++){
                    if (costList.get(j)[demand_row] < min_supply){
                        min_supply = costList.get(j)[demand_row];
                        coord_supply = j;
                    }
                }
                if (supply.get(coord_supply) >= demand.get(demand_row)){
                    sum += costList.get(coord_supply)[demand_row] * demand.get(demand_row);
                    supply.set(coord_supply, supply.get(coord_supply) - demand.get(demand_row));
                    for (int i = 0; i < costList.size(); i++) {
                        int[] originalArray = costList.get(i);
                        int[] newArray = new int[originalArray.length - 1];


                        int index = 0;
                        for (int j = 0; j < originalArray.length; j++) {
                            if (j != demand_row) {
                                newArray[index++] = originalArray[j];
                            }
                        }
                        costList.set(i, newArray);
                    }
                    demand.remove(demand_row);
                }
                else {
                    sum += costList.get(coord_supply)[demand_row] * supply.get(coord_supply);
                    demand.set(demand_row, demand.get(demand_row) - supply.get(coord_supply));
                    costList.remove(coord_supply);
                    supply.remove(coord_supply);
                }
            }
        }
        if (demand.size() == 1){
            for (int i = 0; i < supply.size(); i++) {
                sum += supply.get(i) * costList.get(i)[0];
            }
        }
        else {
            for (int i = 0; i < demand.size(); i++) {
                sum += demand.get(i) * costList.get(0)[i];
            }
        }
        return sum;
    }
    public static int russellsApproximationMethod(int[] supply1, int[] demand1, int[][] cost) {
        List<Integer> supply = new ArrayList<>();
        for (int s : supply1) {
            supply.add(s);
        }

        List<Integer> demand = new ArrayList<>();
        for (int d : demand1) {
            demand.add(d);
        }

        int[] u = new int[supply.size()];
        int[] v = new int[demand.size()];
        int sum = 0;

        Arrays.fill(u, Integer.MIN_VALUE);
        Arrays.fill(v, Integer.MIN_VALUE);

        for (int i = 0; i < supply.size(); i++) {
            for (int j = 0; j < demand.size(); j++) {
                u[i] = Math.max(u[i], cost[i][j]);
                v[j] = Math.max(v[j], cost[i][j]);
            }
        }

        while (supply.stream().mapToInt(Integer::intValue).sum() > 0 &&
                demand.stream().mapToInt(Integer::intValue).sum() > 0) {

            int maxEvaluation = Integer.MIN_VALUE;
            int selectedRow = -1, selectedCol = -1;

            for (int i = 0; i < supply.size(); i++) {
                for (int j = 0; j < demand.size(); j++) {
                    if (supply.get(i) > 0 && demand.get(j) > 0) {
                        int evaluation = u[i] + v[j] - cost[i][j];
                        if (evaluation > maxEvaluation) {
                            maxEvaluation = evaluation;
                            selectedRow = i;
                            selectedCol = j;
                        }
                    }
                }
            }

            int allocation = Math.min(supply.get(selectedRow), demand.get(selectedCol));
            sum += allocation * cost[selectedRow][selectedCol];
            supply.set(selectedRow, supply.get(selectedRow) - allocation);
            demand.set(selectedCol, demand.get(selectedCol) - allocation);

            if (supply.get(selectedRow) == 0) u[selectedRow] = Integer.MIN_VALUE;
            if (demand.get(selectedCol) == 0) v[selectedCol] = Integer.MIN_VALUE;
        }

        return sum;
    }

}
