package com.hatand.dadatagateway.business.suggestaddress.storage;

import com.hatand.dadatagateway.business.suggestaddress.dto.SuggestionDataDto;

import java.util.List;

public interface SuggestionService {

    List<SuggestionDataDto> fetchSuggestions(String searchQuery);
}
