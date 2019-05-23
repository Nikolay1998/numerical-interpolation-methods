package sample;

import java.util.ArrayList;

public class LinearSpline extends Spline{
    LinearSpline(ArrayList <Node> nodes){
        h = nodes.get(1).x - nodes.get(0).x;
        matrix = new double[][]{
                {1, 0, 0, 0, 0,  0},
                {0, 1, 0, 0, 0,  0},
                {0, 0, 1, 0, 0,  0},
                {1, 0, 0, h, 0,  0},
                {0, 1, 0, 0, h,  0},
                {0, 0, 1, 0, 0,  h}
        };
        rightPart = new double[]{nodes.get(0).y, nodes.get(1).y, nodes.get(2).y, nodes.get(1).y, nodes.get(2).y, nodes.get(3).y};
    }
}
