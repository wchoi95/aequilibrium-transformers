package com.aequilibrium.Transformers.models;

import java.util.List;

public class BattleResponse {
    private int numBattles;
    private String winningTeam;
    private List<String> survivingAutobots;
    private List<String> survivingDecepticons;

    public int getNumBattles() {
        return numBattles;
    }

    public void setNumBattles(int numBattles) {
        this.numBattles = numBattles;
    }

    public String getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(String winningTeam) {
        this.winningTeam = winningTeam;
    }

    public List<String> getSurvivingAutobots() {
        return survivingAutobots;
    }

    public void setSurvivingAutobots(List<String> survivingAutobots) {
        this.survivingAutobots = survivingAutobots;
    }

    public List<String> getSurvivingDecepticons() {
        return survivingDecepticons;
    }

    public void setSurvivingDecepticons(List<String> survivingDecepticons) {
        this.survivingDecepticons = survivingDecepticons;
    }
}
