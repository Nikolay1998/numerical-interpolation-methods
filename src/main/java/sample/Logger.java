package sample;

import javafx.scene.control.TextArea;

public class Logger {
    static TextArea logTextArea;
    public static void initialization(TextArea textArea){
        logTextArea = textArea;
    }
}
