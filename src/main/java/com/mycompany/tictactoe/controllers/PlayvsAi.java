/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoe.controllers;

/**
 *
 * @author Sherry
 */
public class PlayvsAi {
    private static final char EMPTY = ' ';
    private char[][] board;
    private char humanSymbol;
    private char aiSymbol;
    private String difficulty;
    private int humanScore = 0;
    private int aiScore = 0;
    private int drawScore = 0;
    
    public PlayvsAi(String difficulty, char humanSymbol) {
        board = new char[3][3];
        this.difficulty = difficulty;
        this.humanSymbol = humanSymbol;
        this.aiSymbol = (humanSymbol == 'X') ? 'O' : 'X';
        initializeBoard();
    }
 
    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = EMPTY;
            }
        }
    }
    
    public boolean makeMove(int row, int col, char symbol) {
        if (isValidMove(row, col)) {
            board[row][col] = symbol;
            System.out.println(row + " " + col + " "+ symbol);
            return true;
        }
        return false;
    }
    
    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == EMPTY;
    }
    
    public int[] getAiMove() {
        switch (difficulty) {
            case "Hard":
                return getHardMove();
            case "Med":
                return getMediumMove();
            case "Easy":
            default:
                return getEasyMove();
        }
    }
    
    private int[] getEasyMove() {
        java.util.Random rand = new java.util.Random();
        int row, col;
        do {
            row = rand.nextInt(3);
            col = rand.nextInt(3);
        } while (board[row][col] != EMPTY);
        
        return new int[]{row, col};
    }
    
    private int[] getMediumMove() {
        int[] winningMove = findWinningMove(aiSymbol);
        if (winningMove != null) return winningMove;
        int[] blockMove = findWinningMove(humanSymbol);
        if (blockMove != null) return blockMove;
        if (board[1][1] == EMPTY) return new int[]{1, 1};
        int[][] corners = {{0,0}, {0,2}, {2,0}, {2,2}};
        for (int[] corner : corners) {
            if (board[corner[0]][corner[1]] == EMPTY) {
                return corner;
            }
        }
        return getEasyMove();
    }
    
    private int[] getHardMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = {-1, -1};
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = aiSymbol;
                    int score = minimax(0, false);
                    board[i][j] = EMPTY;
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        
        return bestMove;
    }
    
    private int minimax(int depth, boolean isMaximizing) {
        if (checkWin(aiSymbol)) return 10 - depth;
        if (checkWin(humanSymbol)) return depth - 10;
        if (isBoardFull()) return 0;
        
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = aiSymbol;
                        int score = minimax(depth + 1, false);
                        board[i][j] = EMPTY;
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = humanSymbol;
                        int score = minimax(depth + 1, true);
                        board[i][j] = EMPTY;
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }
    
    private int[] findWinningMove(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == EMPTY)
                return new int[]{i, 2};
            if (board[i][0] == player && board[i][2] == player && board[i][1] == EMPTY)
                return new int[]{i, 1};
            if (board[i][1] == player && board[i][2] == player && board[i][0] == EMPTY)
                return new int[]{i, 0};
        }
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == player && board[1][j] == player && board[2][j] == EMPTY)
                return new int[]{2, j};
            if (board[0][j] == player && board[2][j] == player && board[1][j] == EMPTY)
                return new int[]{1, j};
            if (board[1][j] == player && board[2][j] == player && board[0][j] == EMPTY)
                return new int[]{0, j};
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == EMPTY)
            return new int[]{2, 2};
        if (board[0][0] == player && board[2][2] == player && board[1][1] == EMPTY)
            return new int[]{1, 1};
        if (board[1][1] == player && board[2][2] == player && board[0][0] == EMPTY)
            return new int[]{0, 0};
        
        if (board[0][2] == player && board[1][1] == player && board[2][0] == EMPTY)
            return new int[]{2, 0};
        if (board[0][2] == player && board[2][0] == player && board[1][1] == EMPTY)
            return new int[]{1, 1};
        if (board[1][1] == player && board[2][0] == player && board[0][2] == EMPTY)
            return new int[]{0, 2};
        
        return null;
    }
    
    public boolean checkWin(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player)
                return true;
        }
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == player && board[1][j] == player && board[2][j] == player)
                return true;
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player)
            return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player)
            return true;
        
        return false;
    }
    
    public boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) return false;
            }
        }
        return true;
    }
    
    public char[][] getBoard() {
        return board;
    }
    
    public void resetBoard() {
        initializeBoard();
    }
    
    public void incrementHumanScore() {
        humanScore++;
    }
    
    public void incrementAiScore() {
        aiScore++;
    }
    
    public void incrementDrawScore() {
        drawScore++;
    }
    
    public int getHumanScore() {
        return humanScore;
    }
    
    public int getAiScore() {
        return aiScore;
    }
    
    public int getDrawScore() {
        return drawScore;
    }
    
    public void resetScores() {
        humanScore = 0;
        aiScore = 0;
        drawScore = 0;
    }
    
    public char getHumanSymbol() {
        return humanSymbol;
    }
    
    public char getAiSymbol() {
        return aiSymbol;
    }
}