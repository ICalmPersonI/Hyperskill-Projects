package processor;

interface MatrixProcessor {
    void matrixAddition(double[][] matrixFirst, double[][] matrixSecond, int sizeMatrixA, int sizeMatrixB);

    void matrixMultiplicationByConstant(double[][] matrix, double constant, int sizeMatrixA, int sizeMatrixB);

    void matrixMultiply(double[][] matrixFirst, double[][] matrixSecond);

    void transpositionAlongTheMainDiagonal(double[][] matrix);

    void transpositionAlongTheSideDiagonal(double[][] matrix);

    void transpositionAlongTheVerticalLine(double[][] matrix);

    void transpositionAlongTheHorizontalLine(double[][] matrix);

    void inverseMatrix(double[][] matrix);

    double calculateADeterminant(double[][] matrix);

    double[][] getResultMatrixProcessor();


}

class MatrixOperation implements MatrixProcessor {

    private static double[][] resultMatrix;


    public double[][] getResultMatrixProcessor() {
        return resultMatrix;
    }


    public void transpositionAlongTheMainDiagonal(double[][] matrix) {
     resultMatrix = new double[matrix.length][matrix[0].length];
     for (int i = 0; i < resultMatrix.length; i++) {
         for (int s = 0; s < resultMatrix[0].length; s++) {
             resultMatrix[i][s] = matrix[s][i];
         }
     }
    }

    public void inverseMatrix(double[][] matrix) {
        resultMatrix = invert(matrix);
    }
    public static double[][] invert(double a[][])
    {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i=0; i<n; ++i)
            b[i][i] = 1;

        // Transform the matrix into an upper triangle
        gaussian(a, index);

        // Update the matrix b[i][j] with the ratios stored
        for (int i=0; i<n-1; ++i)
            for (int j=i+1; j<n; ++j)
                for (int k=0; k<n; ++k)
                    b[index[j]][k]
                            -= a[index[j]][i]*b[index[i]][k];

        // Perform backward substitutions
        for (int i=0; i<n; ++i)
        {
            x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
            for (int j=n-2; j>=0; --j)
            {
                x[j][i] = b[index[j]][i];
                for (int k=j+1; k<n; ++k)
                {
                    x[j][i] -= a[index[j]][k]*x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }

// Method to carry out the partial-pivoting Gaussian
// elimination.  Here index[] stores pivoting order.

    public static void gaussian(double a[][], int index[])
    {
        int n = index.length;
        double c[] = new double[n];

        // Initialize the index
        for (int i=0; i<n; ++i)
            index[i] = i;

        // Find the rescaling factors, one from each row
        for (int i=0; i<n; ++i)
        {
            double c1 = 0;
            for (int j=0; j<n; ++j)
            {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }

        // Search the pivoting element from each column
        int k = 0;
        for (int j=0; j<n-1; ++j)
        {
            double pi1 = 0;
            for (int i=j; i<n; ++i)
            {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1)
                {
                    pi1 = pi0;
                    k = i;
                }
            }

            // Interchange rows according to the pivoting order
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i=j+1; i<n; ++i)
            {
                double pj = a[index[i]][j]/a[index[j]][j];

                // Record pivoting ratios below the diagonal
                a[index[i]][j] = pj;

                // Modify other elements accordingly
                for (int l=j+1; l<n; ++l)
                    a[index[i]][l] -= pj*a[index[j]][l];
            }
        }
    }

    public double calculateADeterminant(double[][] matrix) {
        double num1, num2, det = 1, total = 1;
        int index;
        int n = matrix.length;

        double[] temp = new double[n + 1];

        for (int i = 0; i < n; i++) {
            index = i;
            while (matrix[index][i] == 0 && index < n) {
                index++;
            }
            if (index == n) {
                continue;
            }
            if (index != i) {
                for (int j = 0; j < n; j++) {
                    swap(matrix, index, j, i, j);
                }
                det = (int)(det * Math.pow(-1, index - i));
            }
            for (int j = 0; j < n; j++) {
                temp[j] = matrix[i][j];
            }
            for (int j = i + 1; j < n; j++) {
                num1 = temp[i];
                num2 = matrix[j][i];
                for (int k = 0; k < n; k++) {
                    matrix[j][k] = (num1 * matrix[j][k]) - (num2 * temp[k]);
                }
                total = total * num1;
            }
        }
        for (int i = 0; i < n; i++) {
            det = det * matrix[i][i];
        }
        return (det / total);

    }
    private static double[][] swap(double[][] arr, int i1, int j1, int i2, int j2) {
        double temp = arr[i1][j1];
        arr[i1][j1] = arr[i2][j2];
        arr[i2][j2] = temp;
        return arr;
    }

    public void transpositionAlongTheSideDiagonal(double[][] matrix) {
        resultMatrix = new double[matrix.length][matrix[0].length];
        for (int i = 0, ii = matrix.length - 1; i < resultMatrix.length; i++, ii--) {
            for (int s = 0, ss = matrix[0].length - 1; s < resultMatrix[0].length; s++, ss--) {
                resultMatrix[i][s] = matrix[ss][ii];
            }
        }
    }

    public void transpositionAlongTheVerticalLine(double[][] matrix) {
        resultMatrix = new double[matrix.length][matrix[0].length];
        int resultLine = 0;
        int resultColumn = matrix[0].length - 1;
        int matrixLine = 0;
        int matrixColumn = 0;
        while (resultLine < matrix.length) {
            while (resultColumn != -1) {
                resultMatrix[resultLine][resultColumn] = matrix[matrixLine][matrixColumn];
                 resultColumn--;
                 matrixColumn++;

            }
            resultColumn = matrix[0].length - 1;
            matrixColumn = 0;
            resultLine++;
            matrixLine++;
        }
    }

    public void transpositionAlongTheHorizontalLine(double[][] matrix) {
        resultMatrix = new double[matrix.length][matrix[0].length];
        int resultLine = matrix.length - 1;
        int resultColumn = 0;
        int matrixLine = 0;
        int matrixColumn = 0;
        while (resultLine != -1) {
            while (resultColumn < matrix[0].length) {
                resultMatrix[resultLine][resultColumn] = matrix[matrixLine][matrixColumn];
                resultColumn++;
                matrixColumn++;
            }
            resultColumn = 0;
            matrixColumn = 0;
            resultLine--;
            matrixLine++;
        }
    }

    @Override
    public void matrixMultiply(double[][] matrixFirst, double[][] matrixSecond) {
        resultMatrix = new double[matrixFirst.length][matrixSecond[0].length];
        int resultLine = 0;
        int resultColumn = 0;
        int matrixFirstLine = 0;
        int matrixFirstColumn = 0;
        int matrixSecondLine = 0;
        int matrixSecondColumn = 0;
        while (resultColumn < matrixSecond[0].length) {
            while (resultLine < matrixFirst.length) {
                while (matrixFirstColumn < matrixFirst[0].length && matrixSecondColumn < matrixSecond[0].length) {
                    resultMatrix[resultLine][resultColumn] += matrixFirst[matrixFirstLine][matrixFirstColumn] *
                            matrixSecond[matrixSecondLine][matrixSecondColumn];
                    matrixFirstColumn++;
                    matrixSecondLine++;
                }
                matrixFirstColumn = 0;
                matrixSecondLine = 0;
                resultLine++;
                matrixFirstLine++;
            }
            resultColumn++;
            matrixSecondColumn++;
            matrixFirstColumn = 0;
            matrixSecondLine = 0;
            resultLine = 0;
            matrixFirstLine = 0;
        }
    }

    @Override
    public void matrixMultiplicationByConstant(double[][] matrix, double constant, int sizeMatrixA, int sizeMatrixB) {
        resultMatrix = new double[sizeMatrixA][sizeMatrixB];
        for (int i = 0; i < resultMatrix.length; i++) {
            for (int s = 0; s < resultMatrix[i].length; s++) {
                resultMatrix[i][s] = matrix[i][s] * constant;
            }
        }
    }

    @Override
    public void matrixAddition(double[][] matrixFirst, double[][] matrixSecond, int sizeMatrixA, int sizeMatrixB) {
        resultMatrix = new double[sizeMatrixA][sizeMatrixB];
        for (int i = 0; i < resultMatrix.length; i++) {
            for (int s = 0; s < resultMatrix[i].length; s++) {
                resultMatrix[i][s] = matrixFirst[i][s] + matrixSecond[i][s];
            }
        }
    }

}