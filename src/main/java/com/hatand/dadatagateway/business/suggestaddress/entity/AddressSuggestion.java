package com.hatand.dadatagateway.business.suggestaddress.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressSuggestion {
    String unrestrictedValue;
    String region;
    String city;
    String settlement;
    String street;
    String house;
    String flat;
    List<String> metroStations;
}
