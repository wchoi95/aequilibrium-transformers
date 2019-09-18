package com.aequilibrium.Transformers.validation;

import com.aequilibrium.Transformers.models.BattleRequest;
import com.aequilibrium.Transformers.models.CreateTransformerRequest;
import com.aequilibrium.Transformers.models.UpdateTransformerRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Functions that validate objects or requests.
 */
public class Validator {
    public static boolean stringNullOrEmpty(String string) {
        return string == null || string.equals("");
    }

    public static void validateCreateTransformerRequest(CreateTransformerRequest request) {
        if (stringNullOrEmpty(request.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is Required");
        }

        if (request.getStrength() < 1 || request.getStrength() > 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Strength must be between 1 and 10");
        }

        if (request.getIntelligence() < 1 || request.getIntelligence() > 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Intelligence must be between 1 and 10");
        }

        if (request.getSpeed() < 1 || request.getSpeed() > 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Speed must be between 1 and 10");
        }

        if (request.getEndurance() < 1 || request.getEndurance() > 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Endurance must be between 1 and 10");
        }

        if (request.getRank() < 1 || request.getRank() > 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rank must be between 1 and 10");
        }

        if (request.getCourage() < 1 || request.getCourage() > 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Courage must be between 1 and 10");
        }

        if (request.getFirepower() < 1 || request.getFirepower() > 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Firepower must be between 1 and 10");
        }

        if (request.getSkill() < 1 || request.getSkill() > 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Skill must be between 1 and 10");
        }

        char allegiance = Character.toUpperCase(request.getAllegiance());

        if (allegiance != 'A' && allegiance != 'D') {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Allegiance must be 'A' or 'D'");
        }
    }

    public static void validateDeleteTransformerRequest(String id) {
        if (stringNullOrEmpty(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is Required");
        }
    }

    public static void validateUpdateTransformerRequest(UpdateTransformerRequest request) {
        if (stringNullOrEmpty(request.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is Required");
        }

        if (request.getName() != null && request.getName().equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is Required");
        }

        if (request.getStrength() != null && (request.getStrength() < 1 || request.getStrength() > 10)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Strength must be between 1 and 10");
        }

        if (request.getIntelligence() != null && (request.getIntelligence() < 1 || request.getIntelligence() > 10)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Intelligence must be between 1 and 10");
        }

        if (request.getSpeed() != null && (request.getSpeed() < 1 || request.getSpeed() > 10)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Speed must be between 1 and 10");
        }

        if (request.getEndurance() != null && (request.getEndurance() < 1 || request.getEndurance() > 10)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Endurance must be between 1 and 10");
        }

        if (request.getRank() != null && (request.getRank() < 1 || request.getRank() > 10)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rank must be between 1 and 10");
        }

        if (request.getCourage() != null && (request.getCourage() < 1 || request.getCourage() > 10)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Courage must be between 1 and 10");
        }

        if (request.getFirepower() != null && (request.getFirepower() < 1 || request.getFirepower() > 10)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Firepower must be between 1 and 10");
        }

        if (request.getSkill() != null && (request.getSkill() < 1 || request.getSkill() > 10)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Skill must be between 1 and 10");
        }

        if (request.getAllegiance() != null) {
            char allegiance = Character.toUpperCase(request.getAllegiance());

            if (allegiance != 'A' && allegiance != 'D') {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Allegiance must be 'A' or 'D'");
            }
        }
    }

    public static void validateBattleRequest(BattleRequest request) {
        if (request.getTransformerIds() == null || request.getTransformerIds().size() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must battle at least 2 transformers");
        }
    }

}
