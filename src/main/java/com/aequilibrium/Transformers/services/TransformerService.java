package com.aequilibrium.Transformers.services;

import com.aequilibrium.Transformers.dynamo.TransformersTable;
import com.aequilibrium.Transformers.models.CreateTransformerRequest;
import com.aequilibrium.Transformers.models.Transformer;
import com.aequilibrium.Transformers.models.UpdateTransformerRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * Functions pertaining to creating, editing, viewing, and deleting transformers.
 */
public class TransformerService {
    /**
     * Create a new transformer with the given information and save it to the database.
     *
     * @param request - Request containing name, stats, and allegiance of transformer to create
     * @return The newly created transformer
     */
    public static Transformer createTransformer(CreateTransformerRequest request) {
        Transformer transformer = new Transformer();
        transformer.setId(generateUUID());
        transformer.setName(request.getName());
        transformer.setStrength(request.getStrength());
        transformer.setIntelligence(request.getIntelligence());
        transformer.setSpeed(request.getSpeed());
        transformer.setEndurance(request.getEndurance());
        transformer.setRank(request.getRank());
        transformer.setCourage(request.getCourage());
        transformer.setFirepower(request.getFirepower());
        transformer.setSkill(request.getSkill());
        transformer.setAllegiance(Character.toUpperCase(request.getAllegiance()));

        new TransformersTable().save(transformer);

        return transformer;
    }

    /**
     * Get all the transformers that currently exist in the database.
     *
     * @return the list of all transformers found in the database
     */
    public static List<Transformer> getAllTransformers() {
        return new TransformersTable().scan();
    }

    /**
     * Delete a transformer in the database by ID.
     *
     * @param id - the ID of the transformer to delete
     * @return the transformer that was deleted
     */
    public static Transformer deleteTransformerById(String id) {
        TransformersTable table = new TransformersTable();
        Transformer transformer = table.getById(id);

        // ensure there is a transformer to delete
        if (transformer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transformer with the ID " + id + " does not exist");
        }

        table.delete(transformer);

        return transformer;
    }

    /**
     * Update a transformer in the database by its ID and details to update.
     *
     * @param request - Request containing the ID of the transformer to update and details to update
     * @return the transformer that was updated with its new details
     */
    public static Transformer updateTransformer(UpdateTransformerRequest request) {
        TransformersTable table = new TransformersTable();
        Transformer transformer = table.getById(request.getId());

        // for each value, if it exists update it
        if (request.getName() != null) {
            transformer.setName(request.getName());
        }

        if (request.getStrength() != null) {
            transformer.setStrength(request.getStrength());
        }

        if (request.getIntelligence() != null) {
            transformer.setIntelligence(request.getIntelligence());
        }

        if (request.getSpeed() != null) {
            transformer.setSpeed(request.getSpeed());
        }

        if (request.getEndurance() != null) {
            transformer.setEndurance(request.getEndurance());
        }

        if (request.getRank() != null) {
            transformer.setRank(request.getRank());
        }

        if (request.getCourage() != null) {
            transformer.setCourage(request.getCourage());
        }

        if (request.getFirepower() != null) {
            transformer.setFirepower(request.getFirepower());
        }

        if (request.getSkill() != null) {
            transformer.setSkill(request.getSkill());
        }

        if (request.getAllegiance() != null) {
            transformer.setAllegiance(Character.toUpperCase(request.getAllegiance()));
        }

        table.save(transformer);

        return transformer;
    }

    /**
     * Generate a random UUID.
     *
     * @return a random UUID as a string
     */
    private static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
