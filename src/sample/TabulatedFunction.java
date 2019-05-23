package sample;

import java.util.ArrayList;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

public class TabulatedFunction {
    public ArrayList<Node> values = new ArrayList<>();
    public ArrayList<Node> allValues = new ArrayList<>();
    public ArrayList<Node> lagValues = new ArrayList<>();
    public ArrayList<Node> newtonValues = new ArrayList<>();
    public ArrayList<Node> linearSplineValues = new ArrayList<>();
    public ArrayList<Node> parabolSplineValues = new ArrayList<>();
    public ArrayList<Node> cubicSplineValues = new ArrayList<>();

    public ArrayList<Node> wList = new ArrayList<>();

    public ArrayList<Node> linearSplineError = new ArrayList<>();
    public ArrayList<Node> parabolSplineError = new ArrayList<>();
    public ArrayList<Node> cubicSplineError = new ArrayList<>();
    public ArrayList<Node> lagranjeError = new ArrayList<>();
    public ArrayList<Node> newtonError = new ArrayList<>();


    double[] linearCoef;
    double[] parabolCoef;
    double[] cubCoef;
    private Double step = 0.02;
    TabulatedFunction(Double a, Double b) {
        Double valueStep = (b-a)/3;
        for(int i = 0; i < 4; i++){
            values.add(new Node(a+i*valueStep, calcFunction(a+i*valueStep)));
        }
        for (Double i = a; i < b; i += step) {
            lagValues.add(new Node(i, lagrange(i)));
        }

        for (Double i = 0.0; i < 4.5; i += 0.01) {
            allValues.add(new Node(i, calcFunction(i)));
        }

        for (Double i = a; i < b; i += step) {
            newtonValues.add(new Node(i, newton(i)));
        }
        for (Node i : values) {
            System.out.println("x = " + i.x + "; y = " + i.y);
        }
        System.out.println("\n\n\nLAGRANGE:\n\n\n");
        Logger.logTextArea.appendText("\n\n\nLAGRANGE:\n\n\n");
        for (Node i : lagValues) {
            System.out.println("x = " + i.x + "; y = " + i.y);
            Logger.logTextArea.appendText("\nx = " + i.x + "; y = " + i.y );
        }
        System.out.println("\n\n\nNEWTON:\n\n\n");
        Logger.logTextArea.appendText("\n\n\nNEWTON:\n\n\n");
        for (Node i : newtonValues) {
            System.out.println("x = " + i.x + "; y = " + i.y);
            Logger.logTextArea.appendText("\nx = " + i.x + "; y = " + i.y );
        }

        LinearSpline lsp = new LinearSpline(values);
        linearCoef = getCf(lsp);
        System.out.println("\n\n\nLinear Spline coef:\n\n\n");
        Logger.logTextArea.appendText("\n\n\nLinear Spline coef:\n\n\n");
        for(double i : linearCoef){
            System.out.println(i);
            Logger.logTextArea.appendText("\n"+i);
        }

        ParabolSpline psp = new ParabolSpline(values);
        parabolCoef = getCf(psp);
        System.out.println("\n\n\nParabol Spline coef:\n\n\n");
        Logger.logTextArea.appendText("\n\n\nParabol Spline coef:\n\n\n");
        for(double i : parabolCoef){
            System.out.println(i);
            Logger.logTextArea.appendText("\n"+i);
        }

        CubSpline csp = new CubSpline(values);
        cubCoef = getCf(csp);
        System.out.println("\n\n\nCubic Spline coef: \n\n\n");
        Logger.logTextArea.appendText("\n\n\nCubic Spline coef:\n\n\n");
        for(double i : cubCoef){
            System.out.println(i);
            Logger.logTextArea.appendText("\n"+i);
        }

        for (Double i = a; i < b; i += step) {
            linearSplineValues.add(new Node(i, linearSplineCalc(i)));
            parabolSplineValues.add(new Node(i, parabolSplineCalc(i)));
            cubicSplineValues.add(new Node(i, cubSplineCalc(i)));
        }

        System.out.println("Linear Spline:");
        Logger.logTextArea.appendText("\n\n\nLinear Spline values:\n\n\n");
        for(Node i : linearSplineValues){
            System.out.println(i.y);
            Logger.logTextArea.appendText("\n" + i.y);
        }

        System.out.println("Parabol Spline:");
        Logger.logTextArea.appendText("\n\n\nParabol Spline values:\n\n\n");

        for(Node i : parabolSplineValues){
            System.out.println(i.y);
            Logger.logTextArea.appendText("\n" + i.y);

        }

        System.out.println("Cubic Spline:");
        Logger.logTextArea.appendText("\n\n\nCubic Spline values:\n\n\n");

        for(Node i : cubicSplineValues){
            System.out.println(i.y);
            Logger.logTextArea.appendText("\n" + i.y);

        }


        lagranjeError = compare(lagValues);
        newtonError = compare(newtonValues);
        linearSplineError = compare(linearSplineValues);
        parabolSplineError = compare(parabolSplineValues);
        cubicSplineError = compare(cubicSplineValues);

        for(int i = 0; i < 4; i++){
            allValues = derrivation(allValues);
        }

        wList = w(allValues);

    }

    public Double calcFunction(double x){
        return 4*Math.pow(x,2)*Math.sqrt(3*Math.PI-2*x);
    }

    public double lagrange(Double arg){
        double res = 0;
        for (int i = 0; i < values.size(); i++) {
            double p = 1;
            for (int j = 0; j < values.size(); j++) {
                if (i != j){
                    p *= (arg - values.get(j).x) / (values.get(i).x - values.get(j).x);
                }
            }
            res += p * values.get(i).y;
        }
        return res;
    }

    public ArrayList<Node> derrivation(ArrayList<Node> func){
        ArrayList<Node> der = new ArrayList<>();
        for(int i = 0; i < func.size()-1; i++){
            der.add(new Node(func.get(i).x, func.get(i).y - func.get(i+1).y));
        }
        return der;
    }

    public double newton(Double x){
        double res = lagValues.get(0).y;
        Double t = (x - lagValues.get(0).x)/step;
        ArrayList<Double> diff = new ArrayList<>();
        for(int i = 0; i < lagValues.size(); i++) {
            diff.add(lagValues.get(i).y);
        }

        for(int j = 1; j < lagValues.size(); j++) {
            for (int i = 0; i < diff.size() - 1; i++) {
                diff.set(i, diff.get(i + 1) - diff.get(i));
            }
            diff.remove(diff.size() - 1);
            Double temp = diff.get(0)/factorial(j);
            for(int n = 0; n < j; n++){
                temp*=(t-n);
            }
            res+=temp;
        }
        return res;
    }


    public long factorial(int number) {
        long result = 1;

        for (int factor = 2; factor <= number; factor++) {
            result *= factor;
        }

        return result;
    }

    private static double[] getCf(Spline m){
        RealMatrix matrix = new Array2DRowRealMatrix(m.matrix);
        QRDecomposition qrL = new QRDecomposition(matrix);
        return qrL.getSolver().solve(new ArrayRealVector(m.rightPart)).toArray();
    }

    private Double linearSplineCalc(Double x){
        if (values.get(0).x <= x && x <= values.get(1).x)
            return linearCoef[0] + linearCoef[3] * (x - values.get(0).x);
        if (values.get(1).x <= x && x <= values.get(2).x)
            return linearCoef[1] + linearCoef[4] * (x - values.get(1).x);
        if (values.get(2).x <= x && x <= values.get(3).x)
            return linearCoef[2] + linearCoef[5] * (x - values.get(2).x);
        return -1.0;
    }

    private Double parabolSplineCalc(Double x){
        if (values.get(0).x <= x && x <= values.get(1).x) {
            double t = x - values.get(0).x;
            return parabolCoef[0] + parabolCoef[3] * t + parabolCoef[6] * t * t;
        }
        if (values.get(1).x <= x && x <= values.get(2).x) {
            double t = x - values.get(1).x;
            return parabolCoef[1] + parabolCoef[4] * t + parabolCoef[7] * t * t;
        }
        if (values.get(2).x <= x && x <= values.get(3).x) {
            double t = x - values.get(2).x;
            return parabolCoef[2] + parabolCoef[5] * t + parabolCoef[8] * t * t;
        }
        return -1.0;
    }

    private Double cubSplineCalc(Double x){
        if (values.get(0).x <= x && x <= values.get(1).x) {
            double t = x - values.get(0).x;
            return cubCoef[0] + cubCoef[3] * t + cubCoef[6] * t * t + cubCoef[9] * t * t * t;
        }
        if (values.get(1).x <= x && x <= values.get(2).x) {
            double t = x - values.get(1).x;
            return cubCoef[1] + cubCoef[4] * t + cubCoef[7] * t * t + cubCoef[10] * t * t * t;
        }
        if (values.get(2).x <= x && x <= values.get(3).x) {
            double t = x - values.get(2).x;
            return cubCoef[2] + cubCoef[5] * t + cubCoef[8] * t * t + cubCoef[11] * t * t * t;
        }
        return -1.0;
    }


    private ArrayList<Node> compare(ArrayList<Node> interpol){
        ArrayList<Node> result = new ArrayList<>();
        System.out.println("Pogresh:");
        for(Node i : interpol){
            double t = calcFunction(i.x) - i.y;
            result.add(new Node(i.x ,t));
            System.out.println(t);
        }
        return result;
    }

    public ArrayList<Node> w(ArrayList<Node> derr){
        ArrayList<Node> result = new ArrayList<>();
        for(Node i : derr){
            Double temp = 1.0;
            for(int j = 0 ; j < 4; j++) {
                temp *= (i.x - values.get(j).x);
            }
            result.add(new Node(i.x, temp*i.y));
        }
        return result;
    }
}
