package com.hatand.dadatagateway.business.suggestaddress.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuggestionResultDto {
    List<SuggestionDataDto> suggestions = new ArrayList<>();
}
