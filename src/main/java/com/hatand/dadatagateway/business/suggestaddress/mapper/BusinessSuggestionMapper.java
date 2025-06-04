package com.hatand.dadatagateway.business.suggestaddress.mapper;

import com.hatand.dadatagateway.business.suggestaddress.dto.SuggestionDataDto;
import com.hatand.dadatagateway.business.suggestaddress.entity.AddressSuggestion;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface BusinessSuggestionMapper {
    AddressSuggestion map(SuggestionDataDto suggestionDataDto);
}
