package com.example.tetris;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Block {
    public int blockType;
    public int [][] squareArrangement= new int[2][4];

    Block(int blockType){
        this.blockType=blockType;
        Arrays.fill(squareArrangement[0],0);
        Arrays.fill(squareArrangement[1],0);

        switch(blockType){
            case Utils.I:{
                Arrays.fill(squareArrangement[1],1);
            }
            break;
            case Utils.J:
            {
                Arrays.fill(squareArrangement[0],1);

                squareArrangement[0][3]=0;
                squareArrangement[1][2]=1;
            }
            break;
            case Utils.L:
            {
                Arrays.fill(squareArrangement[0],1);

                squareArrangement[0][0]=0;
                squareArrangement[1][1]=1;
            }
            break;
            case Utils.O:
            {

                squareArrangement[0][1]=1;
                squareArrangement[0][2]=1;
                squareArrangement[1][1]=1;
                squareArrangement[1][2]=1;
            }
            break;
            case Utils.T:
            {
                squareArrangement[1][0]=1;
                squareArrangement[1][1]=1;
                squareArrangement[1][2]=1;
                squareArrangement[0][1]=1;
            }
            break;
            case Utils.S:
            {
                squareArrangement[0][2]=1;
                squareArrangement[0][3]=1;
                squareArrangement[1][1]=1;
                squareArrangement[1][2]=1;
            }
            break;
            case Utils.Z:
            {
                squareArrangement[0][0]=1;
                squareArrangement[0][1]=1;
                squareArrangement[1][1]=1;
                squareArrangement[1][2]=1;
            }
            break;

        }
    }
}
