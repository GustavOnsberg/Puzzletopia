package main;

public class Generator {

    public static void generate(int n, int m, int cuts, float var, Puzzle puzzle){

        puzzle.puzzleWidth = n;
        puzzle.puzzleHeight = m;

        int N = n * (cuts + 1) + 1;
        int M = m * (cuts + 1) + 1;

        System.out.println("N: "+N+ ", M: "+M);

        float[][][] dots = new float[N][M][2];

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < 2; k++) {
                    if ((i == 0 || i == M - 1) && k == 1){
                        dots[j][i][k] = 0;
                    }
                    else if ((j == 0 || j == N - 1) && k == 0){
                        dots[j][i][k] = 0;
                    }
                    else{
                        dots[j][i][k] = (float) Math.random() * var * 2 - var;
                    }
                }
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                puzzle.pieces.add(new Piece());
                int pIndex = puzzle.pieces.size() - 1;

                int firstCornerIndexN = j * (cuts + 1);
                int firstCornerIndexM = i * (cuts + 1);

                System.out.println(firstCornerIndexN+", "+firstCornerIndexM);

                float x = 0;
                float y = 0;
                int xIndex = 0;
                int yIndex = 0;

                if (i != 0){
                    for (int k = 0; k <= cuts; k++) {
                        xIndex = firstCornerIndexN + k;
                        yIndex = firstCornerIndexM;
                        x = -0.5f + k * (1 / ((float) cuts + 1.0f));
                        y = -0.5f;

                        x += dots[xIndex][yIndex][0];
                        y += dots[xIndex][yIndex][1];
                        puzzle.pieces.get(pIndex).corners.add(new FPoint(x, y));
                    }
                }
                else {
                    xIndex = firstCornerIndexN;
                    yIndex = firstCornerIndexM;
                    x = -0.5f;
                    y = -0.5f;

                    x += dots[xIndex][yIndex][0];
                    y += dots[xIndex][yIndex][1];
                    puzzle.pieces.get(pIndex).corners.add(new FPoint(x, y));
                }

                if (j != n - 1){
                    for (int k = 0; k <= cuts; k++) {
                        xIndex = firstCornerIndexN + cuts + 1;
                        yIndex = firstCornerIndexM + k;
                        x = 0.5f;
                        y = -0.5f + k * (1 / ((float) cuts + 1.0f));

                        x += dots[xIndex][yIndex][0];
                        y += dots[xIndex][yIndex][1];
                        puzzle.pieces.get(pIndex).corners.add(new FPoint(x, y));
                    }
                }
                else {
                    xIndex = firstCornerIndexN + cuts + 1;
                    yIndex = firstCornerIndexM;
                    x = 0.5f;
                    y = -0.5f;

                    x += dots[xIndex][yIndex][0];
                    y += dots[xIndex][yIndex][1];
                    puzzle.pieces.get(pIndex).corners.add(new FPoint(x, y));
                }

                if (i != m - 1){
                    for (int k = 0; k <= cuts; k++) {
                        xIndex = firstCornerIndexN + cuts + 1 - k;
                        yIndex = firstCornerIndexM + cuts + 1;
                        x = 0.5f - k * (1 / ((float) cuts + 1.0f));
                        y = 0.5f;

                        x += dots[xIndex][yIndex][0];
                        y += dots[xIndex][yIndex][1];
                        puzzle.pieces.get(pIndex).corners.add(new FPoint(x, y));
                    }
                }
                else{
                    xIndex = firstCornerIndexN + cuts + 1;
                    yIndex = firstCornerIndexM + cuts + 1;
                    x = 0.5f;
                    y = 0.5f;

                    x += dots[xIndex][yIndex][0];
                    y += dots[xIndex][yIndex][1];
                    puzzle.pieces.get(pIndex).corners.add(new FPoint(x, y));
                }

                if (j != 0){
                    for (int k = 0; k <= cuts; k++) {
                        xIndex = firstCornerIndexN;
                        yIndex = firstCornerIndexM + cuts + 1 - k;
                        x = -0.5f;
                        y = 0.5f - k * (1 / ((float) cuts + 1.0f));

                        x += dots[xIndex][yIndex][0];
                        y += dots[xIndex][yIndex][1];
                        puzzle.pieces.get(pIndex).corners.add(new FPoint(x, y));
                    }
                }
                else{
                    xIndex = firstCornerIndexN;
                    yIndex = firstCornerIndexM + cuts + 1;
                    x = -0.5f;
                    y = 0.5f;

                    x += dots[xIndex][yIndex][0];
                    y += dots[xIndex][yIndex][1];
                    puzzle.pieces.get(pIndex).corners.add(new FPoint(x, y));
                }
            }
        }
    }
}
