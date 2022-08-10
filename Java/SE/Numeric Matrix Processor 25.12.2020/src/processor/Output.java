package processor;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Output {

    public static void executeOuput() {
        output();
    }

    private static void output() {
        NumberFormat formatter = new DecimalFormat("#0.00");
        MatrixProcessor matrixProcessor = new MatrixOperation();
        double[][] resultOutput = matrixProcessor.getResultMatrixProcessor();
        System.out.print("The result is:");
        for (double[] a : resultOutput) {
            System.out.println();
            for (double s : a) {
                System.out.print(formatter.format(s) + " ");
            }
        }



    }
}
