package com.aequilibrium.Transformers.controllers;

import com.aequilibrium.Transformers.models.*;
import com.aequilibrium.Transformers.services.BattleService;
import com.aequilibrium.Transformers.services.TransformerService;
import com.aequilibrium.Transformers.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Spring controller class for Transformer API.
 */
@RestController
public class TransformerController {
    /**
     * Create a new transformer.
     *
     * @param request - Request containing name, stats, and allegiance of transformer to create
     * @return Response containing the created transformer
     */
    @PostMapping("/create-transformer")
    public ResponseEntity<Transformer> createTransformer(@RequestBody CreateTransformerRequest request) {
        Validator.validateCreateTransformerRequest(request);
        return new ResponseEntity<Transformer>(TransformerService.createTransformer(request), HttpStatus.OK);
    }

    /**
     * List all of the transformers that currently exist in the database.
     *
     * @return A list of transformer
     */
    @GetMapping("/list-transformers")
    public ResponseEntity<List<Transformer>> listTransformers() {
        return new ResponseEntity<List<Transformer>>(TransformerService.getAllTransformers(), HttpStatus.OK);
    }

    /**
     * Delete a transformer from the database.
     *
     * @param id - The ID of the transformer to delete
     * @return The deleted transformer
     */
    @DeleteMapping("/delete-transformer-by-id")
    public ResponseEntity<Transformer> deleteTransformerById(@RequestParam(value="id") String id) {
        Validator.validateDeleteTransformerRequest(id);
        return new ResponseEntity<Transformer>(TransformerService.deleteTransformerById(id), HttpStatus.OK);
    }

    /**
     * Update a transformer's name, stats, and/or allegiance.
     *
     * @param request - Request containing the ID of the transformer to update and details to update
     * @return A response containing the updated transformer with its new details
     */
    @PutMapping("/update-transformer")
    public ResponseEntity<Transformer> updateTransformer(@RequestBody UpdateTransformerRequest request) {
        Validator.validateUpdateTransformerRequest(request);
        return new ResponseEntity<Transformer>(TransformerService.updateTransformer(request), HttpStatus.OK);
    }

    /**
     * Battle the autobots vs decepticons. The transformers that will be fight will be given as a list of IDs.
     *
     * @param request - Request containing the list of IDs of transformers to battle
     * @return The number of battles that occured, the winner, and the surviving transformers
     */
    @PostMapping("/battle")
    public ResponseEntity<BattleResponse> battle(@RequestBody BattleRequest request) {
        Validator.validateBattleRequest(request);
        return new ResponseEntity<BattleResponse>(BattleService.battleTransformers(request), HttpStatus.OK);
    }
}
