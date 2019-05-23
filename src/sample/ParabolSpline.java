package sample;

import java.util.ArrayList;

public class ParabolSpline extends Spline {
        private static final double Z = 0;
        ParabolSpline(ArrayList<Node> nodes){
            h = nodes.get(1).x - nodes.get(0).x;
            matrix = new double[][]{
                    {1, 0, 0, 0, 0,  0, 0,   0,   0},
                    {0, 1, 0, 0, 0,  0, 0,   0,   0},
                    {0, 0, 1, 0, 0,  0, 0,   0,   0},
                    {1, 0, 0, h, 0,  0, h*h, 0,   0},
                    {0, 1, 0, 0, h,  0, 0,   h*h, 0},
                    {0, 0, 1, 0, 0,  h, 0,   0,   h*h},
                    {0, 0, 0, 1, -1, 0, 2*h, 0,   0},
                    {0, 0, 0, 0, 1, -1, 0,   2*h, 0},
                    {0, 0, 0, Z, 0,  0, 2,   0,   0}

            };
            rightPart = new double[]{nodes.get(0).y, nodes.get(1).y, nodes.get(2).y, nodes.get(1).y, nodes.get(2).y, nodes.get(3).y, 0, 0, 0};
        }
}
