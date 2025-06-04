package com.hatand.dadatagateway.infrastructure.dadata.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.hatand.dadatagateway.business.suggestaddress.dto.SuggestionDataDto;
import com.hatand.dadatagateway.business.suggestaddress.dto.SuggestionResultDto;
import com.hatand.dadatagateway.business.suggestaddress.storage.SuggestionService;
import com.hatand.dadatagateway.crosscut.exception.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DaDataSuggestionsServiceImpl implements SuggestionService {
    @Value("${dadata.base-url}")
    private String dadataBaseUrl;
    @Value("${dadata.auth-token}")
    private String dadataAuthToken;
    @Value("${dadata.endpoints.suggest-address}")
    private String suggestAddressEndpointUrl;

    private final OkHttpClient client;

    @Override
    public List<SuggestionDataDto> fetchSuggestions(String searchQuery) {
        String daDataAnswer = getDaDataResponse(searchQuery);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader reader = objectMapper.readerFor(SuggestionResultDto.class);

        SuggestionResultDto suggestionResultDto;
        try {
            suggestionResultDto = reader.readValue(daDataAnswer);
        } catch (Exception e) {
            throw new ExternalServiceException(
                    "Exception during convert dadata suggestions into object: " + e.getMessage(), e);
        }

        return suggestionResultDto.getSuggestions();
    }

    private String getDaDataResponse(String queryParam) {
        final MediaType JSON = MediaType.get("application/json");

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject()
                    .put("query", queryParam);
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }

        try {
            RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
            final String url = dadataBaseUrl + suggestAddressEndpointUrl;
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .header("Authorization", "Token " + dadataAuthToken)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (null == response.body()) {
                    throw new ExternalServiceException("response body is null");
                }

                return response.body().string();
            }
        } catch (Exception e) {
            throw new ExternalServiceException("Error during get dadata response: " + e.getMessage(), e);
        }

    }
}
