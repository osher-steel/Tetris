import java.util.Random;

public class Grid {
    public Vector2[][] cells;
    private Block nextBlock;
    private PlayerStats playerStats;

    private boolean gameOver;
    public boolean blockStopped;

    private int currentBlockType;
    private int rotation;
    private int linesCleared;

    Grid(PlayerStats playerStats){
        this.playerStats = playerStats;

        cells= new Vector2[22][10];
        for(int i=0; i<22 ; i++){
            for(int j=0; j<10; j++){
                cells[i][j]=new Vector2(0,0);
            }
        }
        gameOver=false;

        rotation=1;
        blockStopped=false;
        linesCleared=0;
        nextBlock=randomizeBlock();
    }

    //-------------------ADD BLOCK------------------//
    void addBlock(){
        currentBlockType=nextBlock.blockType;
        rotation=1;
        blockStopped=false;


        cells[1][3]= new Vector2(nextBlock.squareArrangement[0][0],nextBlock.blockType);
        cells[1][4]= new Vector2(nextBlock.squareArrangement[0][1],nextBlock.blockType);
        cells[1][5]= new Vector2(nextBlock.squareArrangement[0][2],nextBlock.blockType);
        cells[1][6]= new Vector2(nextBlock.squareArrangement[0][3],nextBlock.blockType);

        cells[2][3]= new Vector2(nextBlock.squareArrangement[1][0],nextBlock.blockType);
        cells[2][4]= new Vector2(nextBlock.squareArrangement[1][1],nextBlock.blockType);
        cells[2][5]= new Vector2(nextBlock.squareArrangement[1][2],nextBlock.blockType);
        cells[2][6]= new Vector2(nextBlock.squareArrangement[1][3],nextBlock.blockType);


        randomizeRotation();
        nextBlock=randomizeBlock();

    }
    private Block randomizeBlock(){
        Random rand= new Random();
        int num=rand.nextInt(7);
        return new Block(num);
    }
    private void randomizeRotation() {
        Random rand= new Random();
        int numOfRotations=(rand.nextInt(4));

        for(int i=0; i<numOfRotations;i++){
            rotateBlock();
        }
    }

    //-------------------MOVEMENT------------------//
    public void moveDown(){
        if(!bottomCollision()){
            for (int i=21; i>=0; i--) {
                for (int j = 0; j < 10; j++) {
                    if (cells[i][j].x == 1) {
                        cells[i + 1][j].x = cells[i][j].x;
                        cells[i + 1][j].y = cells[i][j].y;
                        cells[i][j].x= 0;
                        cells[i][j].y= 0;
                    }
                }
            }
        }
        else{
            blockStopped=true;
        }
    }
    public void moveRight(){
        if(noSideCollision(1)){
            for (int i=0; i<22; i++) {
                for (int j=9; j >=0; j--) {
                    if (cells[i][j].x == 1){
                        cells[i][j+1].x=cells[i][j].x;
                        cells[i][j+1].y=cells[i][j].y;
                        cells[i][j].x=0;
                        cells[i][j].y=0;
                    }
                }

            }
        }
    }
    public void moveLeft() {
        if(noSideCollision(-1)){
            for (int i = 0; i < 22; i++) {
                for (int j = 0; j <10; j++) {
                    if (cells[i][j].x == 1){
                        cells[i][j-1].x=cells[i][j].x;
                        cells[i][j-1].y=cells[i][j].y;
                        cells[i][j].x=0;
                        cells[i][j].y=0;
                    }
                }

            }
        }

    }

    //-------------------ROTATION------------------//
    public void rotateBlock(){
        for(int i=0;i<22;i++){
            for(int j=0; j<10; j++){
                if(cells[i][j].x==1){
                    switch (currentBlockType){
                        case Utils.Z:{
                            rotateZ(i,j);
                        }
                        break;
                        case Utils.I:{
                            rotateI(i,j);
                        }
                        break;
                        case Utils.T:{
                            rotateT(i,j);
                        }
                        break;
                        case Utils.S:{
                            rotateS(i,j);
                        }
                        break;
                        case Utils.L:{
                            rotateL(i,j);
                        }
                        break;
                        case Utils.J:{
                            rotateJ(i,j);
                        }
                        break;
                    }
                    i=100;
                    j=100;
                }
            }
        }
        if(rotation==5)
            rotation=1;
    }
    private void rotateS(int i, int j) {
        if(rotation==1||rotation==3){
            if(!(cells[i-1][j].x==2||cells[i-1][j-1].x==2||cells[i][j-1].x==2)) {
                cells[i + 1][j - 1].x = 0;
                cells[i + 1][j].x = 0;

                cells[i - 1][j].x = 1;
                cells[i + 1][j + 1].x = 1;
                cells[i - 1][j].y = currentBlockType;
                cells[i + 1][j + 1].y = currentBlockType;

                rotation++;
            }
        }
        else {
            if (!(j == 0 || cells[i + 2][j - 1].x == 2 || cells[i + 2][j].x == 2 || cells[i][j+1].x == 2)) {
            cells[i][j].x = 0;
            cells[i + 2][j + 1].x = 0;

            cells[i + 2][j - 1].x = 1;
            cells[i + 2][j].x = 1;
            cells[i + 2][j - 1].y = currentBlockType;
            cells[i + 2][j].y = currentBlockType;

            rotation++;
        }
        }
    }
    private void rotateZ(int i, int j) {
        if(rotation==1|| rotation==3){
            if(!(cells[i-1][j].x==2||cells[i-1][j+1].x==2||cells[i-1][j+2].x==2)) {
                cells[i][j].x = 0;
                cells[i + 1][j + 2].x = 0;

                cells[i][j + 2].x = 1;
                cells[i - 1][j + 2].x = 1;
                cells[i][j + 2].y = currentBlockType;
                cells[i - 1][j + 2].y = currentBlockType;

                rotation++;
            }
        }
        else{
            if(!(j==1||cells[i+1][j-2].x==2||cells[i+2][j-2].x==2||cells[i+2][j].x==2)) {
                cells[i][j].x = 0;
                cells[i + 1][j].x = 0;

                cells[i + 2][j].x = 1;
                cells[i + 1][j - 2].x = 1;
                cells[i + 2][j].y = currentBlockType;
                cells[i + 1][j - 2].y = currentBlockType;

                rotation++;
            }
        }
    }
    private void rotateT(int i, int j) {
        if(rotation==1){
            if(!(i== cells.length-1 || cells[i+2][j].x==2 || cells[i+2][j+1].x==2)){
                cells[i+1][j-1].x=0;

                cells[i+2][j].x=1;
                cells[i+2][j].y=currentBlockType;

                rotation++;

            }
        }
        else if(rotation==2){
            if(!(j==0|| cells[i+1][j-1].x==2|| cells[i+2][j-1].x==2)){
                cells[i][j].x=0;

                cells[i+1][j-1].x=1;
                cells[i+1][j-1].y=currentBlockType;

                rotation++;

            }
        }
        else if(rotation==3){
            if(!(cells[i-1][j+1].x==2|| cells[i+1][j+1].x==2)){
                cells[i][j+2].x=0;

                cells[i-1][j+1].x=1;
                cells[i-1][j+1].y=currentBlockType;

                rotation++;
            }

        }
        else if(rotation==4){
            if(!(j==9 || cells[i+1][j+1].x==2|| cells[i+2][j-1].x==2)){
                cells[i+2][j].x=0;

                cells[i+1][j+1].x=1;
                cells[i+1][j+1].y=currentBlockType;

                rotation++;
            }

        }
    }
    private void rotateI(int i, int j) {
        if(rotation==1 || rotation==3) {
            if (i!=cells.length-2)  {
                if (!(cells[i+1][j].x == 2 ||cells[i+1][j+1].x == 2 || cells[i+2][j].x == 2 || cells[i-1][j].x == 2 || cells[i - 2][j].x == 2)){
                    cells[i][j].x = 0;
                    cells[i][j + 1].x = 0;
                    cells[i][j + 3].x = 0;

                    cells[i - 1][j + 2].x = 1;
                    cells[i + 1][j + 2].x = 1;
                    cells[i + 2][j + 2].x = 1;

                    cells[i - 1][j + 2].y = currentBlockType;
                    cells[i + 1][j + 2].y = currentBlockType;
                    cells[i + 2][j + 2].y = currentBlockType;

                    rotation++;
                }
            }
        }
        else if(!(j == 9 || j == 1 || j == 0)) {
            if(!(cells[i + 1][j + 1].x == 2 || cells[i + 1][j - 1].x == 2 || cells[i + 1][j - 2].x == 2||
                    cells[i+3][j+1].x == 2||cells[i+2][j+1].x == 2|| cells[i+3][j-1].x == 2||cells[i+2][j-1].x == 2)) {
                cells[i][j].x = 0;
                cells[i + 2][j].x = 0;
                cells[i + 3][j].x = 0;

                cells[i + 1][j - 2].x = 1;
                cells[i + 1][j - 1].x = 1;
                cells[i + 1][j + 1].x = 1;

                cells[i + 1][j - 2].y = currentBlockType;
                cells[i + 1][j - 1].y = currentBlockType;
                cells[i + 1][j + 1].y = currentBlockType;

                rotation++;
            }
        }
    }
    private void rotateJ(int i, int j) {
        if(rotation==1){
            if(!(cells[i+1][j].x==2||cells[i+1][j+1].x==2||cells[i-1][j].x==2)||cells[i-1][j+1].x==2) {
                cells[i][j].x = 0;
                cells[i][j + 2].x = 0;
                cells[i + 1][j + 2].x = 0;

                cells[i + 1][j].x = 1;
                cells[i + 1][j + 1].x = 1;
                cells[i - 1][j + 1].x = 1;
                cells[i + 1][j].y = currentBlockType;
                cells[i + 1][j + 1].y = currentBlockType;
                cells[i - 1][j + 1].y = currentBlockType;

                rotation++;
            }
        }
        else if(rotation==2){
            if(!(j==9||cells[i+1][j+1].x==2) ||cells[i][j+1].x==2||cells[i][j-1].x==2||cells[i+1][j-1].x==2) {
                cells[i][j].x = 0;
                cells[i + 2][j].x = 0;
                cells[i + 2][j - 1].x = 0;

                cells[i][j - 1].x = 1;
                cells[i + 1][j - 1].x = 1;
                cells[i + 1][j + 1].x = 1;
                cells[i][j - 1].y = currentBlockType;
                cells[i + 1][j - 1].y = currentBlockType;
                cells[i + 1][j + 1].y = currentBlockType;

                rotation++;
            }
        }
        else if(rotation==3){
            if(!(i==cells.length-2||cells[i+2][j+1].x==2||cells[i+2][j+2].x==2||cells[i][j+1].x==2||cells[i][j+2].x==2)) {
                cells[i][j].x = 0;
                cells[i + 1][j].x = 0;
                cells[i + 1][j + 2].x = 0;

                cells[i][j + 1].x = 1;
                cells[i][j + 2].x = 1;
                cells[i + 2][j + 1].x = 1;
                cells[i][j + 1].y = currentBlockType;
                cells[i][j + 2].y = currentBlockType;
                cells[i + 2][j + 1].y = currentBlockType;


                rotation++;
            }
        }
        else if(rotation==4){
            if(!(j==0||cells[i+1][j-1].x==2||cells[i+2][j-1].x==2||cells[i+1][j+1].x==2||cells[i+2][j+1].x==2)) {
                cells[i][j].x = 0;
                cells[i][j + 1].x = 0;
                cells[i + 2][j].x = 0;

                cells[i + 1][j - 1].x = 1;
                cells[i + 1][j + 1].x = 1;
                cells[i + 2][j + 1].x = 1;
                cells[i + 1][j - 1].y = currentBlockType;
                cells[i + 1][j + 1].y = currentBlockType;
                cells[i + 2][j + 1].y = currentBlockType;

                rotation++;
            }
        }
    }
    private void rotateL(int i, int j) {
        if(rotation==1){
            if(!(cells[i-1][j].x==2||cells[i-1][j+1].x==2||cells[i+1][j+1].x==2||cells[i+1][j+2].x==2)){
                cells[i][j].x=0;
                cells[i+1][j].x=0;
                cells[i][j+2].x=0;

                cells[i-1][j].x=1;
                cells[i-1][j+1].x=1;
                cells[i+1][j+1].x=1;
                cells[i-1][j].y=currentBlockType;
                cells[i-1][j+1].y=currentBlockType;
                cells[i+1][j+1].y=currentBlockType;

                rotation++;
            }
        }

        else if(rotation==2){
            if(!(cells[i][j+2].x==2||cells[i+1][j+2].x==2||cells[i+2][j+2].x==2||cells[i+2][j].x==2|| j==8)) {
                cells[i][j].x = 0;
                cells[i][j + 1].x = 0;
                cells[i + 2][j + 1].x = 0;

                cells[i][j + 2].x = 1;
                cells[i + 1][j + 2].x = 1;
                cells[i + 1][j].x = 1;
                cells[i][j + 2].y = currentBlockType;
                cells[i + 1][j + 2].y = currentBlockType;
                cells[i + 1][j].y = currentBlockType;
                rotation++;
            }
        }
        else if(rotation==3){
            if(!(cells[i][j-1].x==2||cells[i][j-2].x==2||cells[i+2][j].x==2||cells[i+2][j-1].x==2 || j==cells.length-1)) {
                cells[i][j].x = 0;
                cells[i + 1][j - 2].x = 0;
                cells[i + 1][j].x = 0;

                cells[i][j - 1].x = 1;
                cells[i + 2][j].x = 1;
                cells[i + 2][j - 1].x = 1;
                cells[i][j - 1].y = currentBlockType;
                cells[i + 2][j].y = currentBlockType;
                cells[i + 2][j - 1].y = currentBlockType;

                rotation++;
            }
        }
        else if(rotation==4){
            if(!(cells[i][j+1].x==2||cells[i+1][j+1].x==2||cells[i+1][j-1].x==2||cells[i+2][j-1].x==2|| j==0)) {
                cells[i][j].x = 0;
                cells[i + 2][j].x = 0;
                cells[i + 2][j + 1].x = 0;

                cells[i + 1][j + 1].x = 1;
                cells[i + 1][j - 1].x = 1;
                cells[i + 2][j - 1].x = 1;
                cells[i + 1][j + 1].y = currentBlockType;
                cells[i + 1][j - 1].y = currentBlockType;
                cells[i + 2][j - 1].y = currentBlockType;

                rotation++;
            }
        }
    }

    //-------------------COLLISION CHECK------------------//
    private boolean bottomCollision(){
        for(int i=0; i<22;i++){
            for(int j=0; j<10;j++){
                if(cells[i][j].x==1){
                    if(i==21 ||cells[i+1][j].x==2)
                        return true;
                }
            }
        }
        return false;
    }
    private boolean noSideCollision(int direction){
        for(int i=0;i<22;i++){
            for(int j=0; j<10; j++){
                if(cells[i][j].x==1){
                    if(j==9 && direction==1)
                        return false;
                    if(j==0 && direction==-1)
                        return false;
                    if(cells[i][j+direction].x==2)
                        return false;
                }
            }
        }
        return true;
    }

    //-------------------END OF BLOCK LIFE------------------//
    public void blockTouchedBottom(){
        checkGameOver();
        if(!gameOver)
        {
            switchToRestingBlock();
            clearLine(isLineCleared());
            clearLine(isLineCleared());
            clearLine(isLineCleared());
            clearLine(isLineCleared());

            playerStats.calculatePoints(linesCleared);
            linesCleared=0;
            addBlock();
        }
    }
    private int isLineCleared() {
        int blockCounter=0;

        for(int i=0; i<22;i++) {
            for(int j=0;j<10;j++){
                if(cells[i][j].x==2){
                    blockCounter++;
                    if(blockCounter==10){
                        return i;
                    }
                }
            }
            blockCounter=0;
        }

        return 100;
    }
    private void clearLine(int line) {
        if(line!=100){
            for(int j=0; j<10;j++){
                cells[line][j].x=0;
                cells[line][j].y=0;
            }

            for(int i=line; i>0;i--){
                for(int j=0; j<10; j++){
                    cells[i][j].x=cells[i-1][j].x;
                    cells[i][j].y=cells[i-1][j].y;
                }
            }
            linesCleared++;
        }
    }
    private void switchToRestingBlock() {
        for(int i=0; i<22;i++){
            for(int j=0; j<10;j++){
                if(cells[i][j].x==1){
                    cells[i][j].x=2;
                }
            }
        }
    }

    //-------------------GAME OVER------------------//
    private void checkGameOver()
    {
        for(int i=0; i<10; i++)
        {
            if (cells[1][i].x == 1 || cells[1][i].x == 2) {
                gameOver = true;
                break;
            }
        }
    }

    //-------------------ACESSOR------------------//
    public Block getNextBlock(){
        return nextBlock;
    }
    public boolean isGameOver(){
        return gameOver;
    }
}
