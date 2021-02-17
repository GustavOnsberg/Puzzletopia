package main;

public class PuzzleInfo {
    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public int[] getPointCross() {
        return pointCross;
    }

    public int[] getPointHorizontal() {
        return pointHorizontal;
    }

    public int[] getPointVertical() {
        return pointVertical;
    }



    public void setPointCross(int[] pointCross) {
        this.pointCross = pointCross;
    }

    public void setPointHorizontal(int[] pointHorizontal) {
        this.pointHorizontal = pointHorizontal;
    }

    public void setPointVertical(int[] pointVertical) {
        this.pointVertical = pointVertical;
    }

    int n;
    int m;
    int[] pointCross;
    int[] pointHorizontal;
    int[] pointVertical;

    public PuzzleInfo(int n, int m){
        this.n = n;
        this.m = m;

        pointCross = new int[(n-1)*(m-1)];
        pointHorizontal = new int[(m-1)*n*2];
        pointVertical = new int[(n-1)*m*2];
    }
}
