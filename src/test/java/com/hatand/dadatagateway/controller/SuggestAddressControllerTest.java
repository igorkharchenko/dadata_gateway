package com.hatand.dadatagateway.controller;

import com.hatand.dadatagateway.business.suggestaddress.entity.AddressSuggestion;
import com.hatand.dadatagateway.business.suggestaddress.service.AddressSuggestionService;
import com.hatand.dadatagateway.controller.suggestaddress.SuggestAddressController;
import com.hatand.dadatagateway.controller.suggestaddress.mapper.AddressSuggestionReqRespMapper;
import com.hatand.dadatagateway.controller.suggestaddress.reqresp.AddressSuggestionDto;
import com.hatand.dadatagateway.controller.suggestaddress.reqresp.AddressSuggestionResponse;
import com.hatand.dadatagateway.framework.DaDataGatewayApplication;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = DaDataGatewayApplication.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class SuggestAddressControllerTest {
    @MockitoBean
    AddressSuggestionService addressSuggestionService;
    @MockitoBean
    AddressSuggestionReqRespMapper reqRespMapper;
    @Autowired
    MockMvc mvc;
    @Autowired
    SuggestAddressController controller;
    AutoCloseable mockito;

    @BeforeEach
    void setUp() {
        mockito = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockito.close();
    }

    @ParameterizedTest
    @MethodSource("getValidSuggestionRequests")
    void whenGetSuggestions_thenReturnSuggestions(final String searchQuery) {
        // Arrange
        List<AddressSuggestion> suggestions = List.of(
                getAddressSuggestion()
        );
        AddressSuggestionDto responseDto = getResponseDto();

        when(addressSuggestionService.getSuggestions(searchQuery)).thenReturn(suggestions);
        when(reqRespMapper.map(any(AddressSuggestion.class))).thenReturn(responseDto);

        // Act
        AddressSuggestionResponse actual = controller.getAddressSuggestions(searchQuery);

        // Assert
        assertFalse(actual.getSuggestions().isEmpty());
        assertEquals(responseDto, actual.getSuggestions().get(0));

        verify(addressSuggestionService).getSuggestions(searchQuery);
    }

    static Stream<Arguments> getValidSuggestionRequests() {
        return Stream.of(
                Arguments.of("aa"),
                Arguments.of("a".repeat(128)),
                Arguments.of("Москва"),
                Arguments.of("Москва "),
                Arguments.of("Москва ул"),
                Arguments.of("Москва ул."),
                Arguments.of("Москва ул. Ленина"),
                Arguments.of("Москва ул. Ленина д. 27 кв. 1"),
                Arguments.of("Москва ул. Ленина д. 27 кв. 1\\12"),
                Arguments.of("Москва, ул. Ленина д. 27, оф. 12//12-254"),
                Arguments.of("Moscow"),
                Arguments.of("Moscow "),
                Arguments.of("Moscow Lenin"),
                Arguments.of("Moscow Lenin st"),
                Arguments.of("Moscow Lenin st.")
        );
    }

    @ParameterizedTest
    @MethodSource("getInvalidSuggestionRequests")
    void whenGetSuggestions_andRequestIsInvalid_thenThrowValidationException(String queryString) {
        // Act
        assertThatThrownBy(() -> controller.getAddressSuggestions(queryString))
                .isInstanceOf(ValidationException.class);

        // Assert
        verify(addressSuggestionService, never()).getSuggestions(anyString());
    }

    static Stream<Arguments> getInvalidSuggestionRequests() {
        return Stream.of(
                Arguments.of((String)null),
                Arguments.of(""),
                Arguments.of(" "),
                Arguments.of("      "),
                Arguments.of("a"),
                Arguments.of("a".repeat(129)),
                Arguments.of("Москва?!@31")
        );
    }

    private AddressSuggestion getAddressSuggestion () {
        return new AddressSuggestion()
                .setRegion("Москва")
                .setCity("Москва")
                .setStreet("Ленина")
                .setHouse("27")
                .setFlat("1");
    }

    private AddressSuggestionDto getResponseDto() {
        return new AddressSuggestionDto()
                .setRegion("Москва")
                .setCity("Москва")
                .setStreet("Ленина")
                .setHouse("27")
                .setFlat("1");
    }
}
