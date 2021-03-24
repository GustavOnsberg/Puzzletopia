package main;

import java.awt.*;

public class Generator {

    int nums[][] = {
            {5, 1, 3, 4, 1, 2, 6, 5, 5, 2, 3, 2, 7}, //13
            {5, 0, 7, 6, 6, 5, 1, 7, 7, 1, 8, 1, 7, 0, 1, 3, 4}, //17
            {0, 4, 2, 7, 7, 2, 3, 6, 1, 4, 1, 8, 6, 3, 7, 4, 4, 6, 1}, // 19
            {2, 3, 6, 4, 7, 4, 1, 2, 3, 3, 7, 1, 3, 5, 5, 6, 6, 3, 6, 3, 8, 3, 5}, //23
            {0, 4, 3, 5, 8, 6, 1, 0, 1, 2, 1, 7, 5, 2, 7, 0, 4, 2, 3, 1, 2, 7, 2, 3, 3, 8, 3, 1, 0}, //29
            {1, 3, 0, 5, 3, 6, 3, 7, 3, 7, 5, 1, 5, 5, 4, 7, 4, 2, 5, 3, 4, 5, 1, 1, 3, 0, 7, 6, 3, 8, 5}, //31
            {7, 1, 2, 3, 3, 7, 1, 6, 6, 3, 2, 6, 7, 2, 1, 0, 6, 1, 7, 8, 4, 6, 7, 4, 3, 3, 3, 5, 6, 1, 1, 7, 4, 5, 2, 8, 3}, //37
            {3, 4, 6, 6, 4, 1, 6, 0, 4, 7, 5, 4, 1, 0, 6, 7, 8, 6, 4, 6, 4, 8, 6, 5, 2, 7, 4, 2, 4, 7, 2, 6, 5, 4, 5, 5, 5, 3, 5, 2, 7}, //41
            {7, 5, 5, 7, 3, 1, 5, 1, 2, 6, 4, 4, 1, 2, 5, 2, 1, 3, 0, 5, 6, 1, 0, 1, 2, 3, 8, 0, 8, 2, 4, 3, 2, 8, 5, 5, 7, 2, 6, 2, 7, 8, 3}, //43
            {4, 5, 7, 4, 5, 5, 2, 6, 7, 3, 7, 0, 4, 3, 1, 4, 7, 5, 7, 3, 2, 6, 0, 5, 4, 0, 2, 5, 3, 7, 1, 4, 4, 7, 3, 4, 6, 8, 1, 2, 1, 7, 7, 2, 3, 5, 0}, //47
    };
    int numsIndex[] = {0,0,0,0,0,0,0,0,0,0};


    public void generate(PuzzleInfo target){
        target.pointCross.clear();
        target.pointHorizontal.clear();
        target.pointVertical.clear();

        for (int i = 0; i < (target.n-1)*(target.m-1); i++)
            target.getPointCross().add(new Point(getRandomNum(),getRandomNum()));

        for (int i = 0; i < (target.m-1)*target.n*2; i++)
            target.getPointHorizontal().add(new Point(getRandomNum(),getRandomNum()));

        for (int i = 0; i < (target.n-1)*target.m*2; i++)
            target.getPointVertical().add(new Point(getRandomNum(),getRandomNum()));
    }

    private int getRandomNum(){
        int out = 0;

        for (int i = 0; i < nums.length; i++){
            out += nums[i][numsIndex[i]];
            numsIndex[i]++;
            if (numsIndex[i] >= nums[i].length) {
                numsIndex[i] = 0;
            }
        }

        return (out % 9) - 4;
    }
}
