package com.hatand.dadatagateway.business.suggestaddress.service;

import com.hatand.dadatagateway.business.suggestaddress.dto.SuggestionDataDto;
import com.hatand.dadatagateway.business.suggestaddress.entity.AddressSuggestion;
import com.hatand.dadatagateway.business.suggestaddress.mapper.BusinessSuggestionMapper;
import com.hatand.dadatagateway.business.suggestaddress.storage.SuggestionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressSuggestionServiceTest {
    @Mock
    SuggestionService suggestionService;
    @Mock
    BusinessSuggestionMapper addressSuggestionMapper;
    @InjectMocks
    AddressSuggestionService service;

    @Test
    void whenGetSuggestions_thenReturnSuggestions() {
        // Arrange
        AddressSuggestion addressSuggestion = getAddressSuggestion();
        SuggestionDataDto suggestionDataDto = getSuggestionDataDto();
        final String searchQuery = "Москва ул. Ленина, д. 217, кв. 12";

        when(addressSuggestionMapper.map(any(SuggestionDataDto.class))).thenReturn(addressSuggestion);
        when(suggestionService.fetchSuggestions(searchQuery)).thenReturn(List.of(suggestionDataDto));

        // Act
        List<AddressSuggestion> actual = service.getSuggestions(searchQuery);

        // Assert
        assertFalse(actual.isEmpty());
        assertEquals(addressSuggestion, actual.get(0));
    }

    @Test
    void whenSuggestionsEmpty_thenReturnEmptyList() {
        // Arrange
        final String searchQuery = "Москва ул. Ленина, д. 217, кв. 12";

        when(suggestionService.fetchSuggestions(searchQuery)).thenReturn(List.of());

        // Act
        List<AddressSuggestion> actual = service.getSuggestions(searchQuery);

        // Assert
        assertTrue(actual.isEmpty());

        verify(addressSuggestionMapper, never()).map(any(SuggestionDataDto.class));
    }

    private AddressSuggestion getAddressSuggestion () {
        return new AddressSuggestion()
                .setRegion("Москва")
                .setCity("Москва")
                .setStreet("Ленина")
                .setHouse("27")
                .setFlat("1");
    }

    private SuggestionDataDto getSuggestionDataDto() {
        return new SuggestionDataDto()
                .setUnrestrictedValue("Москва г, ул Ленина, д 27, кв 1")
                .setRegion("Москва")
                .setCity("Москва")
                .setStreet("Ленина")
                .setHouse("27")
                .setFlat("1");
    }
}
