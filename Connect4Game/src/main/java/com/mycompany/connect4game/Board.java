package com.mycompany.connect4game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Board {
    private static final int COLUMN_COUNT = 7;
    private static final int ROW_COUNT = 6;
    private static final int DEPTH = 3;
    private static int[][] board;
    private static final PlayerPiece piece = new PlayerPiece();
    private static final int pieceEMPTY = piece.getEMPTY();
    private static final int pieceCOMPUTER = piece.getCOMPUTER();
    private static final int piecePLAYER = piece.getPLAYER();
    
    

     public static void initializeBoard() {
        board = new int[ROW_COUNT][COLUMN_COUNT];
        int rows = board.length;
        int cols = board[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = pieceEMPTY;
            }
        }
    }

    private static int[][] tryDropPiece(int[][] B, int colAdd, int piece)
    {
        int rows = board.length;
        int cols = board[0].length;
        int[][] tempBoard = new int[rows][cols];
        for(int r = 0; r < rows; r++)
        {
            for(int c = 0; c < cols; c++)
            {
                tempBoard[r][c] = B[r][c];
            }
        }
        for(int i = rows - 1; i >= 0; i--)
        {
            if(tempBoard[i][colAdd] == pieceEMPTY)
            {
                tempBoard[i][colAdd] = piece;
                break;
            }
        }
        return tempBoard;
    }
    
    
    private static boolean checkWinningPosition(int[][] board, int piece)
    {
        //horizontal
        for(int r = 0; r < ROW_COUNT; r++)
        {
            for(int c = 0; c < COLUMN_COUNT - 3; c++)
            {
                if(board[r][c] == piece && board[r][c+1] == piece && board[r][c+2] == piece && board[r][c+3] == piece)
                    return true;
            }
        }
        
        //vertical
        for(int r = 0; r < ROW_COUNT - 3; r++)
        {
            for(int c = 0; c < COLUMN_COUNT; c++)
            {
                if(board[r][c] == piece && board[r+1][c] == piece && board[r+2][c] == piece && board[r+3][c] == piece)
                    return true;
            }
        }
        
        
        //positively diagonals
        for (int r = 0; r < ROW_COUNT - 3; r++)
            for (int c = 0; c < COLUMN_COUNT - 3; c++)
                if (board[r][c] == piece && board[r + 1][c + 1] == piece && board[r + 2][c + 2] == piece && board[r + 3][c + 3] == piece)
                    return true;
        // negatively diagonals
        for (int r = 3; r < ROW_COUNT; r++)
            for (int c = 0; c < COLUMN_COUNT - 3; c++)
                if (board[r][c] == piece && board[r - 1][c + 1] == piece && board[r - 2][c + 2] == piece && board[r - 3][c + 3] == piece)
                    return true;
        
        return false;
    }
    
    private static double scorePosition(int[][] board){
        int score = 0; 
        
        //Score center col
        int centerScore = 0;
        for(int i = 0; i < COLUMN_COUNT; i++)
        {
            if(board[0][3] == pieceCOMPUTER)
            {
                centerScore++;
            }
        }
        score += centerScore * 3;
        
        //Score horizontal
        for(int r = 0; r < ROW_COUNT; r++)
        {
            for(int c = 0; c < COLUMN_COUNT - 3; c++)
            {
                score += evaluateWindow(board, r, c, "HORIZONTAL");
            }
        }
        
        //Score vertical
        for(int r = 0; r < ROW_COUNT - 3; r++)
        {
            for(int c = 0; c < COLUMN_COUNT; c++)
            {
                score += evaluateWindow(board, r, c, "VERTICAL");
            }
        }
        
        //Score Diagonal
        for (int r = 0; r < ROW_COUNT - 3; r++) {
            for (int c = 0; c < COLUMN_COUNT - 3; c++) {
                score += evaluateWindow(board, r, c, "POSITIVE_DIAGONAL");
                score += evaluateWindow(board, r, c, "NEGATIVE_DIAGONAL");
            }
        }
        return score;
    }
    
    private static int evaluateWindow(int board[][], int row, int column, String lineType)
    {
        int windowScore = 0;
        int empty = 0;
        int pieceCount = 0;
        int oppPieceCount = 0;
        if(lineType == "HORIZONTAl")
        {
            for(int i = 0; i < 4; i++)
            {
                if(board[row][column + 1] == pieceCOMPUTER)
                {
                    pieceCount++;
                }else if(board[row][column + 1] == piecePLAYER)
                {
                    oppPieceCount++;
                }else{
                    empty++;
                }
            }
        }else if(lineType == "VERTICAL")
        {
            for (int i = 0; i < 4; i++) {
                if (board[row + i][column] == pieceCOMPUTER)
                    pieceCount++;
                else if (board[row + i][column] == piecePLAYER)
                    oppPieceCount++;
                else
                    empty++;
            }
        }else if (lineType == "POSITIVE_DIAGONAL") {
            for (int i = 0; i < 4; i++) {
                if (board[row + i][column + i] == pieceCOMPUTER)
                    pieceCount++;
                else if (board[row + i][column + i] == piecePLAYER)
                    oppPieceCount++;
                else
                    empty++;
            }
        } else if (lineType == "NEGATIVE_DIAGONAL") {
            for (int i = 0; i < 4; i++) {
                if (board[row + 3 - i][column + i] == pieceCOMPUTER)
                    pieceCount++;
                else if (board[row + 3 - i][column + i] == piecePLAYER)
                    oppPieceCount++;
                else
                    empty++;
            }
        }
        
        if (pieceCount == 4)
            windowScore += 100;
        else if (pieceCount == 3 && empty == 1)
            windowScore += 5;
        else if (pieceCount == 2 && empty == 2)
            windowScore += 2;

        if (oppPieceCount == 3 && empty == 1)
            windowScore -= 4;

        return windowScore;
    }
    
    public static void updateBoard(int column, int playerOrComputerMove) {
        int rows = board.length;
        for (int i = 0; i < rows; i++) {
            if (board[i][column] == pieceEMPTY) {
                board[i][column] = playerOrComputerMove;
                break;
            }
        }
    }
    
    
     public static int getAIMove() {
        double next = minimax(board, DEPTH, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true).get(1);
        updateBoard((int) next, pieceCOMPUTER);
        return (int) next;
    }
     
     private static List<Double> minimax(int[][] B, int depth, double alpha, double beta, boolean maximizing) {
        List<Double> ret = new ArrayList<>();
        if (isTerminal(B)) {
            if (checkWinningPosition(B, pieceCOMPUTER))
                ret.add(1000000.0);
            else if (checkWinningPosition(B, piecePLAYER))
                ret.add(-1000000.0);
            else
                ret.add(0.0);
        } else if (depth == 0) {
            double x = scorePosition(B);
            ret.add(x);
        } else {
            double value;
            int next_move = 0;
            List<Integer> valid_columns = getAvailableColumns(B);

            if (maximizing) {
                //maximizing
                value = Double.NEGATIVE_INFINITY;
                for (int c : valid_columns) {
                    int[][] temp_Board = tryDropPiece(B, c, pieceCOMPUTER);
                    double new_value = minimax(temp_Board, depth - 1, alpha, beta, false).get(0);
                    if (new_value > value) {
                        value = new_value;
                        next_move = c;
                    }
                    if (value > alpha)
                        alpha = value;
                    if (alpha >= beta)
                        break;
                }
            } else {
                //minimizing
                value = Double.POSITIVE_INFINITY;
                for (int c : valid_columns) {
                    int[][] temp_Board = tryDropPiece(B, c, piecePLAYER);
                    double new_value = minimax(temp_Board, depth - 1, alpha, beta, true).get(0);
                    if (new_value < value) {
                        value = new_value;
                        next_move = c;
                    }
                    if (value < beta)
                        beta = value;
                    if (alpha >= beta)
                        break;
                }
            }
            ret.add(value);
            ret.add((double) next_move);
        }
        return ret;
    }
     
     private static List<Integer> getAvailableColumns(int[][] B) {
        List<Integer> valid_cols = new ArrayList<>();
        int rows = B.length;
        int cols = B[0].length;
        for (int i = 0; i < cols; i++) {
            if (B[rows - 1][i] == pieceEMPTY)
                valid_cols.add(i);
        }
        return valid_cols;
    }


    private static boolean isTerminal(int[][] B) {
        return checkWinningPosition(B, piecePLAYER) || checkWinningPosition(B, pieceCOMPUTER) || getAvailableColumns(B).size() == 0;
    }
    
    
     public static void main(String[] args) {
        System.out.println("Please choose a play mode: \n 1. Human vs Human \n 2. Human vs AI");
        System.out.println("Enter a mode: ");
        Scanner mode = new Scanner(System.in);
	int ansMode = mode.nextInt();
        if(ansMode == 2){
        initializeBoard();
        while(true) {
                System.out.println("Please enter a move 1 - 7");
                Scanner input = new Scanner(System.in);

                int new_column = input.nextInt();
                updateBoard(new_column - 1, piecePLAYER);
                System.out.println(Arrays.deepToString(board).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
                if(isTerminal(board))
                         break;

                System.out.println();

                new_column = getAIMove();
//	        updateBoard(new_column, pieceCOMPUTER);
                System.out.println(Arrays.deepToString(board).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
                if(isTerminal(board))
                        break;
                }
                 System.out.println("Game Over !");
        }else if(ansMode == 1)
        {
            initializeBoard();
        while(true) {
            System.out.println("Player 2 enters a move 1 - 7");
                Scanner input = new Scanner(System.in);

                int new_column = input.nextInt();
                updateBoard(new_column - 1, piecePLAYER);
                System.out.println(Arrays.deepToString(board).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
                if(isTerminal(board))
                         break;

                System.out.println();
                System.out.println("Player 1 enters a move 1 - 7");
                int new_column2 = input.nextInt();
                updateBoard(new_column2 - 1, pieceCOMPUTER);
//	        updateBoard(new_column, pieceCOMPUTER);
                System.out.println(Arrays.deepToString(board).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
                if(isTerminal(board))
                        break;
                }
                 System.out.println("Game Over !");
        }
    }
}