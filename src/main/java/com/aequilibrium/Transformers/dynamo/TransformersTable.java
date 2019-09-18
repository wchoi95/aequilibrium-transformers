package com.aequilibrium.Transformers.dynamo;

import com.aequilibrium.Transformers.models.Transformer;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.List;

/**
 * Transformers DynamoDB Table Class.
 */
public class TransformersTable {
    private AmazonDynamoDB client;
    private DynamoDBMapper mapper;

    /**
     * Constructor for the Transformers table. Accesses the DB with credentials and endpoint configuration.
     * Also sets the DynamoDBMapper to the table.
     */
    public TransformersTable() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials("mock_access_key_id", "mock_secret_key_id");
        client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8001", "us-west-2"))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        mapper = new DynamoDBMapper(client);
    }

    /**
     * Create the Transformers table for Dynamo if it doesn't exist.
     *
     * @throws Exception when there is an error connecting to DynamoDB
     */
    public void initialize() throws Exception {
        CreateTableRequest createTableRequest = mapper.generateCreateTableRequest(Transformer.class);
        createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(10L, 10L));

        // create the table if it doesn't exist or else do nothing
        try {
            System.out.println("Trying to create table ".concat(createTableRequest.getTableName()));
            CreateTableResult createTableResult = client.createTable(createTableRequest);

            String status = createTableResult.getTableDescription().getTableStatus();

            System.out.println("Waiting for table to become active...");

            // wait for table to be active
            while (!status.equals("ACTIVE")) {
                Thread.sleep(1000);
                status = client.describeTable(createTableRequest.getTableName()).getTable().getTableStatus();
            }

            System.out.println("Successfully created table");
        } catch (ResourceInUseException e) {
            System.out.println("Table is already created");
        }
    }

    /**
     * Get the transformer from the Transformers table with the matching ID.
     *
     * @param id - the ID of the transformer to find
     * @return The transformer with the given ID
     */
    public Transformer getById(String id) {
        return mapper.load(Transformer.class, id);
    }

    /**
     * Create or update the DynamoDB table with the transformer object given.
     *
     * @param transformer - the transformer to save
     */
    public void save(Transformer transformer) {
        mapper.save(transformer);
    }

    /**
     * Delete the transformer that matches the model given from DynamoDB.
     *
     * @param transformer - the transformer to delete
     */
    public void delete(Transformer transformer) {
        mapper.delete(transformer);
    }

    /**
     * Get a list of all transformers that exist in the Transformers table.
     *
     * @return the list of all transformers found in the Transformers table
     */
    public List<Transformer> scan() {
        return mapper.scan(Transformer.class, new DynamoDBScanExpression());
    }
}
