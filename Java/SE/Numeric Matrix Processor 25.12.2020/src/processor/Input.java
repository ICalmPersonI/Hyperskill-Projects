package processor;

import java.util.Scanner;


class Input {
    private static Scanner scanner = new Scanner(System.in);

    private static int sizeMatrixA;
    private static int sizeMatrixB;
    private static double[][] addMatrixFirst;
    private static double[][] addMatrixSecond;
    private static double[][] multiplyMatrixByAConstant;
    private static double constant;
    private static double[][] multiplyMatrixFirst;
    private static double[][] multiplyMatrixSecond;
    private static double[][] transposeMatrix;
    private static double[][] calculateADeterminantMatrix;
    private static double[][] inverseMatrix;

    public static void executeSelectOptions() {
        selectOptions();
    }

    private static void selectOptions() {
        MatrixProcessor matrixProcessor = new MatrixOperation();
        int inputSelectNumber;

        while (true) {
            do {
                System.out.println();
                System.out.print("1. Add matrices\n" +
                        "2. Multiply matrix by a constant\n" +
                        "3. Multiply matrices\n" +
                        "4. Transpose matrix\n" +
                        "5. Calculate a determinant\n" +
                        "6. Inverse matrix\n" +
                        "0. Exit\n");
                System.out.print("Your choice:\n");
                inputSelectNumber = scanner.nextInt();
            } while (inputSelectNumber >= 7);

            switch (inputSelectNumber) {
                case 1:
                    if (inputAddMatrix()) {
                        matrixProcessor.matrixAddition(addMatrixFirst, addMatrixSecond, sizeMatrixA, sizeMatrixB);
                        Output.executeOuput();
                        System.out.print("\n");
                        break;
                    }
                    break;
                case 2:
                    multiplyMatrixByAConstantInput();
                    matrixProcessor.matrixMultiplicationByConstant(multiplyMatrixByAConstant, constant, sizeMatrixA, sizeMatrixB);
                    Output.executeOuput();
                    System.out.print("\n");
                    break;
                case 3:
                    if (multiplyMatricesInput()) {
                        matrixProcessor.matrixMultiply(multiplyMatrixFirst, multiplyMatrixSecond);
                        Output.executeOuput();
                        System.out.print("\n");
                        break;
                    }
                    break;
                case 4:
                    transposeMatrix();
                    break;
                case 5:
                    if (calculateADeterminantInput()) {
                        System.out.print("The result is:\n");
                        System.out.print(matrixProcessor.calculateADeterminant(calculateADeterminantMatrix));
                        break;
                    }
                    break;
                case 6:
                    inverseMatrixInput();
                    matrixProcessor.inverseMatrix(inverseMatrix);
                    System.out.print("The result is:\n");
                    Output.executeOuput();
                    break;
                case 0:
                    System.exit(0);
            }
        }
    }

    private static void transposeMatrix() {
        MatrixProcessor matrixProcessor = new MatrixOperation();
        int inputSelectNumber;
        System.out.print("1. Main diagonal\n" +
                "2. Side diagonal\n" +
                "3. Vertical line\n" +
                "4. Horizontal line\n");
        System.out.print("Your choice:");
        inputSelectNumber = scanner.nextInt();
        switch (inputSelectNumber) {
            case 1:
                inputTransposeMatrix();
                matrixProcessor.transpositionAlongTheMainDiagonal(transposeMatrix);
                Output.executeOuput();
                break;
            case 2:
                inputTransposeMatrix();
                matrixProcessor.transpositionAlongTheSideDiagonal(transposeMatrix);
                Output.executeOuput();
                break;
            case 3:
                inputTransposeMatrix();
                matrixProcessor.transpositionAlongTheVerticalLine(transposeMatrix);
                Output.executeOuput();
                break;
            case 4:
                inputTransposeMatrix();
                matrixProcessor.transpositionAlongTheHorizontalLine(transposeMatrix);
                Output.executeOuput();
                break;
        }
    }

    private static void inputTransposeMatrix() {
        System.out.println("Enter size of first matrix:");
        int lineA = scanner.nextInt();
        int columnA = scanner.nextInt();
        System.out.println("Enter matrix:");
        transposeMatrix = fillMatrix(lineA, columnA);
    }

    private static void inverseMatrixInput() {
        System.out.println("Enter matrix size:");
        int lineA = scanner.nextInt();
        int columnA = scanner.nextInt();
        System.out.println("Enter matrix:");
        inverseMatrix = fillMatrix(lineA, columnA);
    }

    private static boolean calculateADeterminantInput() {
        boolean verdict;
        System.out.println("Enter matrix size:");
        int lineA = scanner.nextInt();
        int columnA = scanner.nextInt();
        System.out.println("Enter matrix:");
        calculateADeterminantMatrix = fillMatrix(lineA, columnA);
        if (lineA == columnA) {
            verdict = true;
        } else {
            System.out.println("The operation cannot be performed.");
            verdict = false;
        }
        return verdict;
    }

    private static boolean inputAddMatrix() {
        boolean verdict;

        System.out.println("Enter size of first matrix:");
        int lineA = scanner.nextInt();
        int columnA = scanner.nextInt();
        System.out.println("Enter first matrix:");
        addMatrixFirst = fillMatrix(lineA, columnA);
        System.out.println("Enter size of second matrix:");
        int lineB = scanner.nextInt();
        int columnB = scanner.nextInt();
        System.out.println("Enter second matrix:");
        addMatrixSecond = fillMatrix(lineB, columnB);
        if (lineA == columnB && columnA == columnB) {
            sizeMatrixA = lineA;
            sizeMatrixB = columnA;
            verdict = true;
        } else {
            System.out.println("The operation cannot be performed.");
            verdict = false;
        }
        return verdict;

    }

    private static void multiplyMatrixByAConstantInput() {
        System.out.println("Enter size of first matrix:");
        int line = scanner.nextInt();
        int column = scanner.nextInt();
        System.out.println("Enter matrix:");
        multiplyMatrixByAConstant = fillMatrix(line, column);
        System.out.println("Enter constant:");
        constant = scanner.nextFloat();
        sizeMatrixA = line;
        sizeMatrixB = column;
    }

    private static boolean multiplyMatricesInput() {
        boolean verdict;

        System.out.println("Enter size of first matrix:");
        int lineA = scanner.nextInt();
        int columnA = scanner.nextInt();
        System.out.println("Enter first matrix:");
        multiplyMatrixFirst = fillMatrix(lineA, columnA);
        System.out.println("Enter size of second matrix:");
        int lineB = scanner.nextInt();
        int columnB = scanner.nextInt();
        System.out.println("Enter second matrix:");
        multiplyMatrixSecond = fillMatrix(lineB, columnB);
        if (columnA == lineB) {
            verdict = true;
        } else {
            System.out.println("The operation cannot be performed.");
            verdict = false;
        }
        return verdict;

    }

    private static double[][] fillMatrix(int line, int column) {
        double[][] matrix = new double[line][column];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = scanner.nextFloat();
            }
        }
        return matrix;
    }


}
