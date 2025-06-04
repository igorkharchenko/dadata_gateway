package com.hatand.dadatagateway.controller.suggestaddress.reqresp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressSuggestionResponse {
    List<AddressSuggestionDto> suggestions = new ArrayList<>();
}
