
package com.mycompany.connect4game;

public class PlayerPiece {
    private static final int EMPTY = 0;
    private static final int COMPUTER = 1;
    public  static final int PLAYER =  2;
    
    public PlayerPiece(){
        
    }
    
    public int getEMPTY()
    {
        return EMPTY;
    }
    
    public int getCOMPUTER()
    {
        return COMPUTER;
    }
    
    public int getPLAYER()
    {
        return PLAYER;
    }
}
