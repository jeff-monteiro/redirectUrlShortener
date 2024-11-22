package com.jeffmonteiro.redirectUrlShortener;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;
import java.util.Map;

public class Main implements RequestHandler<Map<String, String>, Map<String, String>> {

    private final S3Client s3Client = S3Client.builder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, String> handleRequest(Map<String, String> input, Context context) {
        String pathParameters = (String) input.get("rawPath");
        String shortUrlCode = pathParameters.replace("/", "");

        if(shortUrlCode == null || shortUrlCode.isEmpty()){
            throw new IllegalArgumentException("Invalid input: 'shortUrlCode' is required");
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket("url-bkt-shortener")
                .key(shortUrlCode + ".json")
                .build();

        InputStream s3ObjectStream;

        try {
            s3ObjectStream = s3Client.getObject(getObjectRequest);
        } catch (Exception exception){
            throw new RuntimeException("Error fetching data from S3 " + exception.getMessage(), exception);
        }

        UrlData urlData;

        try {
            urlData = objectMapper.readValue(s3ObjectStream, UrlData.class);
        } catch (Exception exception){
            throw new RuntimeException("Error on deserializing data" + exception.getMessage(), exception);
        }

        long currentTimeInSeconds = System.currentTimeMillis() / 1000;

        return null;
    }
}