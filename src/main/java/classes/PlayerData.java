/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoe.controllers;
public class PlayerData {
    private static PlayerData instance;
    private String playerName;
    private String difficulty = "Medium";
    private boolean recordMoves = false;
     
     public boolean isRecordMoves() {
        
        return recordMoves;
    }
    
    public void setRecordMoves(boolean recordMoves) {
        
        this.recordMoves = recordMoves;
    }
           
    private PlayerData() {}
    
    public static PlayerData getInstance() {
        if (instance == null) {
            instance = new PlayerData();
        }
        return instance;
    }
    
    public void setPlayerName(String name) {
        this.playerName = name;
    }
    
    public String getPlayerName() {
        if (playerName == null || playerName.trim().isEmpty()) {
            return "Player 1";
        }
        return playerName.trim();
    }
    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
}