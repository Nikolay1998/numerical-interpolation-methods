package sample;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    NumberAxis xAxis = new NumberAxis();
    @FXML
    NumberAxis yAxis = new NumberAxis();
    @FXML
    LineChart<Number,Number> funcChart =
            new LineChart<Number,Number>(xAxis,yAxis);
    @FXML
    NumberAxis xAxis1 = new NumberAxis();
    @FXML
    NumberAxis yAxis1 = new NumberAxis();
    @FXML
    LineChart<Number,Number> funcChart1 =
            new LineChart<Number,Number>(xAxis,yAxis);

    @FXML
    NumberAxis xAxis2 = new NumberAxis();
    @FXML
    NumberAxis yAxis2 = new NumberAxis();
    @FXML
    LineChart<Number,Number> funcChart2 =
            new LineChart<Number,Number>(xAxis,yAxis);

    @FXML
    NumberAxis xAxis21 = new NumberAxis();
    @FXML
    NumberAxis yAxis21 = new NumberAxis();
    @FXML
    LineChart<Number,Number> funcChart21 =
            new LineChart<Number,Number>(xAxis,yAxis);
    @FXML
    TextArea logTextArea;




    public void initialize(){
        Logger.initialization(logTextArea);


        TabulatedFunction myFunc = new TabulatedFunction((double) 0,0.5);



        funcChart.getData().clear();
        //MyFunction.chartCalculate(fromVal,toVal);
        XYChart.Series LineSplineErrorSeria = new XYChart.Series();
        XYChart.Series ParabolSplineErrorSeria = new XYChart.Series();
        XYChart.Series CubicSplineErrorSeria = new XYChart.Series();
        XYChart.Series lagranjeErrorSeria = new XYChart.Series();
        XYChart.Series newtonErrorSeria = new XYChart.Series();
        for( Node p : myFunc.linearSplineError){
            LineSplineErrorSeria.getData().add(new XYChart.Data(p.x, p.y));
        }
        for( Node p : myFunc.parabolSplineError){
            ParabolSplineErrorSeria.getData().add(new XYChart.Data(p.x, p.y));
        }
        for( Node p : myFunc.cubicSplineError){
            CubicSplineErrorSeria.getData().add(new XYChart.Data(p.x, p.y));
        }
        for( Node p : myFunc.lagranjeError){
            lagranjeErrorSeria.getData().add(new XYChart.Data(p.x, p.y));
        }
        for( Node p : myFunc.newtonError){
            newtonErrorSeria.getData().add(new XYChart.Data(p.x, p.y));
        }
        funcChart.getData().add(LineSplineErrorSeria);
        funcChart.getData().add(ParabolSplineErrorSeria);
        funcChart.getData().add(CubicSplineErrorSeria);
        funcChart1.getData().add(lagranjeErrorSeria);
        funcChart1.getData().add(newtonErrorSeria);


        funcChart.setCreateSymbols(false);
        funcChart1.setCreateSymbols(false);


        XYChart.Series DerrivationSeria = new XYChart.Series();

        for( Node p : myFunc.allValues){
            DerrivationSeria.getData().add(new XYChart.Data(p.x, p.y));
        }
        funcChart2.getData().add(DerrivationSeria);
        funcChart2.setCreateSymbols(false);


        XYChart.Series wDerrivationSeria = new XYChart.Series();

        for( Node p : myFunc.wList){
            wDerrivationSeria.getData().add(new XYChart.Data(p.x, p.y));
        }
        funcChart21.getData().add(wDerrivationSeria);
        funcChart21.setCreateSymbols(false);

        //funcChart.setLegendVisible(false);
        //graphSeria = new XYChart.Series();
    }
}
