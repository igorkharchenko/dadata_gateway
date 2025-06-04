package com.hatand.dadatagateway.business.suggestaddress.service;

import com.hatand.dadatagateway.business.suggestaddress.dto.SuggestionDataDto;
import com.hatand.dadatagateway.business.suggestaddress.entity.AddressSuggestion;
import com.hatand.dadatagateway.business.suggestaddress.mapper.BusinessSuggestionMapper;
import com.hatand.dadatagateway.business.suggestaddress.storage.SuggestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressSuggestionService {
    private final SuggestionService suggestionService;
    private final BusinessSuggestionMapper addressSuggestionMapper;

    public List<AddressSuggestion> getSuggestions(String searchQuery) {
        List<SuggestionDataDto> suggestionsResult = suggestionService.fetchSuggestions(searchQuery);

        return suggestionsResult
                .stream()
                .map(dataDto -> addressSuggestionMapper.map(dataDto)
                            .setUnrestrictedValue(dataDto.getUnrestrictedValue()))
                .toList();
    }
}
