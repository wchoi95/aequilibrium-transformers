package com.aequilibrium.Transformers.services;

import com.aequilibrium.Transformers.dynamo.TransformersTable;
import com.aequilibrium.Transformers.models.BattleRequest;
import com.aequilibrium.Transformers.models.BattleResponse;
import com.aequilibrium.Transformers.models.Transformer;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

/**
 * Functions used in battling transformers.
 */
public class BattleService {
    private static final String AUTOBOT_WIN = "Autobot Win!";
    private static final String DECEPTICON_WIN = "Decepticon Win!";
    private static final String TIE = "Tie";
    private static final String TOO_MUCH_POWER = "Power Overload!!!";
    private static final String OPTIMUS_PRIME = "Optimus Prime";
    private static final String PREDAKING = "Predaking";

    /**
     * Battle the autobots vs decepticons. The transformers that will be fight will be given as a list of IDs.
     *
     * @param request - Request containing the IDs of the transformers to battle
     * @return The number of battles that occured, the winner, and the surviving transformers
     */
    public static BattleResponse battleTransformers(BattleRequest request) {
        TransformersTable table = new TransformersTable();
        BattleResponse response = new BattleResponse();
        List<Transformer> autobots = new ArrayList<>();
        List<Transformer> decepticons = new ArrayList<>();
        List<String> survivingAutobots = new LinkedList<>();
        List<String> survivingDecepticons = new LinkedList<>();
        int numBattles = 0;
        int autobotScore = 0;
        int decepticonScore = 0;

        // add the autobots and decepticons to their respective teams as a list
        for (String id : request.getTransformerIds()) {
            Transformer transformer = table.getById(id);

            if (transformer.getAllegiance() == 'A') {
                autobots.add(transformer);
            } else {
                decepticons.add(transformer);
            }
        }

        // ensure there is at least one autobot
        if (autobots.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must select at least one Autobot to fight!");
        }

        // ensure there is at least one decepticon
        if (decepticons.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must select at least one Decepticon to fight!");
        }

        // sort the transformers by descending rank for the fight
        sortByRankDescending(autobots);
        sortByRankDescending(decepticons);

        int numAutobots = autobots.size();
        int numDecepticons = decepticons.size();

        System.out.println(numAutobots + " Autobots VS " + numDecepticons + " Decepticons");

        // the number of fights will be smaller list size
        int numFights = numAutobots > numDecepticons ? numDecepticons : numAutobots;

        // simulate the fights one by one ordered by rank
        for (int i = 0; i < numFights; i++) {
            numBattles++;

            System.out.println("Autobot " + autobots.get(i).getName() + " VS Decepticon " + decepticons.get(i).getName());

            String outcome = fight(autobots.get(i), decepticons.get(i));

            // two powerful forces faced each other ending the game. There were no survivors
            if (outcome.equals(TOO_MUCH_POWER)) {
                System.out.println("TOO MUCH POWER. There was no survivors...");
                response.setNumBattles(numBattles);
                response.setSurvivingAutobots(new LinkedList<>());
                response.setSurvivingDecepticons(new LinkedList<>());
                response.setWinningTeam("There was no survivors...");

                return response;
            } else if (outcome.equals(AUTOBOT_WIN)) {
                System.out.println(autobots.get(i).getName() + " wins!");
                autobotScore++;
                survivingAutobots.add(autobots.get(i).getName());
            } else if (outcome.equals(DECEPTICON_WIN)) {
                System.out.println(decepticons.get(i).getName() + " wins!");
                decepticonScore++;
                survivingDecepticons.add(decepticons.get(i).getName());
            } else if (outcome.equals(TIE)) {
                System.out.println("It was a tie, both bots were destroyed");
                autobotScore++;
                decepticonScore++;
            }
        }

        // add transformers who did not fight to surviving list
        for (int i = numFights; i < numAutobots; i++) {
            survivingAutobots.add(autobots.get(i).getName());
        }

        for (int i = numFights; i < numDecepticons; i++) {
            survivingDecepticons.add(decepticons.get(i).getName());
        }

        // determine the winning team
        if (autobotScore > decepticonScore) {
            System.out.println("Autobots win the battle!");
            response.setWinningTeam("Autobots win the battle!");
        } else if (decepticonScore > autobotScore) {
            System.out.println("Decepticons win the battle!");
            response.setWinningTeam("Decepticons win the battle!");
        } else {
            System.out.println("The battle ended in a tie!");
            response.setWinningTeam("The battle ended in a tie!");
        }

        response.setNumBattles(numBattles);
        response.setSurvivingAutobots(survivingAutobots);
        response.setSurvivingDecepticons(survivingDecepticons);

        return response;
    }

    /**
     * Simulate the fight between two transformers.
     *
     * @param autobot - The autobot that is fighting
     * @param decepticon - The decepticon that is fighting
     * @return The outcome of the fight
     */
    private static String fight(Transformer autobot, Transformer decepticon) {
        // cover the special cases when there is an Optimus Prime and/or Predaking in the fight
        boolean autobotAutoWin = autobot.getName().equals(OPTIMUS_PRIME) || autobot.getName().equals(PREDAKING);
        boolean decepticonAutoWin = decepticon.getName().equals(OPTIMUS_PRIME) || decepticon.getName().equals(PREDAKING);

        if (autobotAutoWin && decepticonAutoWin) {
            return TOO_MUCH_POWER;
        } else if (autobotAutoWin) {
            return AUTOBOT_WIN;
        } else if (decepticonAutoWin) {
            return DECEPTICON_WIN;
        }

        // check if one of the transformers is too chicken to fight. It is assumed that the fleeing bot is not a survivor of the battle
        if (autobot.getCourage() - decepticon.getCourage() >= 4 && autobot.getStrength() - decepticon.getStrength() >= 3) {
            return AUTOBOT_WIN;
        }

        if (decepticon.getCourage() - autobot.getCourage() >= 4 && decepticon.getStrength() - autobot.getStrength() >= 3) {
            return DECEPTICON_WIN;
        }

        // if one transformer is highly skilled compared to the other, they win
        if (autobot.getSkill() - decepticon.getSkill() >= 3) {
            return AUTOBOT_WIN;
        }

        if (decepticon.getSkill() - autobot.getSkill() >= 3) {
            return DECEPTICON_WIN;
        }

        // check the outcome of the fight with the overall rating
        if (autobot.getOverallRating() > decepticon.getOverallRating()) {
            return AUTOBOT_WIN;
        } else if (decepticon.getOverallRating() > autobot.getOverallRating()) {
            return DECEPTICON_WIN;
        } else {
            return TIE;
        }
    }

    /**
     * Sort a list of transformers by their rank in descending order.
     *
     * @param transformers The list of transformers to sort
     */
    private static void sortByRankDescending(List<Transformer> transformers) {
        Collections.sort(transformers, new Comparator<Transformer>() {
            @Override
            public int compare(Transformer o1, Transformer o2) {
                return o2.getRank() - o1.getRank();
            }
        });
    }
}
