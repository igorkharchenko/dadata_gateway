package com.hatand.dadatagateway.controller.suggestaddress.mapper;

import com.hatand.dadatagateway.business.suggestaddress.entity.AddressSuggestion;
import com.hatand.dadatagateway.controller.suggestaddress.reqresp.AddressSuggestionDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AddressSuggestionReqRespMapper {
    AddressSuggestionDto map(AddressSuggestion suggestion);
}
