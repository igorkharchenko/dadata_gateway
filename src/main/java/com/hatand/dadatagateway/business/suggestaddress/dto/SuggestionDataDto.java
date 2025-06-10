package com.hatand.dadatagateway.business.suggestaddress.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuggestionDataDto implements Serializable {
    @JsonProperty("unrestricted_value")
    String unrestrictedValue;
    String region;
    String city;
    String settlement;
    String street;
    String house;
    String flat;
    @JsonIgnore
    List<String> metroStations = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @JsonProperty("data")
    private void unpackData(Map<String, Object> data) {
        region = Objects.requireNonNullElse(data.get("region"), "").toString();
        city = Objects.requireNonNullElse(data.get("city"), "").toString();
        settlement = Objects.requireNonNullElse(data.get("settlement"), "").toString();
        street = Objects.requireNonNullElse(data.get("street"), "").toString();
        house = Objects.requireNonNullElse(data.get("house"), "").toString();
        flat = Objects.requireNonNullElse(data.get("flat"), "").toString();

        Map<String, Object> stations = ((Map<String, Object>) data.get("metro"));
        if (null != stations) {
            metroStations = stations.values()
                    .stream()
                    .map(entry -> ((Map<String, String>) entry).get("name"))
                    .toList();
        }
    }
}
