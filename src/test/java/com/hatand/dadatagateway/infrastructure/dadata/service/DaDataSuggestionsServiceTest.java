package com.hatand.dadatagateway.infrastructure.dadata.service;

import com.hatand.dadatagateway.business.suggestaddress.dto.SuggestionDataDto;
import com.hatand.dadatagateway.crosscut.exception.ExternalServiceException;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DaDataSuggestionsServiceTest {
    @Mock
    OkHttpClient okHttpClient;
    @Mock
    Call okHttpCall;
    @Mock
    Request okHttpRequest;
    @Mock
    Response okHttpResponse;
    @Mock
    ResponseBody okHttpResponseBody;
    @InjectMocks
    DaDataSuggestionsServiceImpl service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "dadataBaseUrl", "https://api.dadata.ru/v1");
        ReflectionTestUtils.setField(service, "dadataAuthToken", "auth-token");
        ReflectionTestUtils.setField(service, "suggestAddressEndpointUrl", "/suggest/address");
    }

    @Test
    void whenResponseIsEmpty_thenThrowExternalServiceException() throws IOException {
        // Arrange
        final String queryParam = "Москва ул. Ленина д. 1";

        when(okHttpClient.newCall(any(Request.class))).thenReturn(okHttpCall);
        when(okHttpCall.execute()).thenReturn(okHttpResponse);
        when(okHttpResponse.body()).thenReturn(null);

        // Act
        assertThatThrownBy(() -> service.fetchSuggestions(queryParam))
                .isInstanceOf(ExternalServiceException.class)
                .hasMessageContaining("response body is null");
    }

    @Test
    void whenSendOkHttpRequest_andResponseInvalid_thenThrowExternalServiceException() throws IOException {
        // Arrange
        final String queryParam = "Москва, ул. Ленина, д 15, кв, 24";

        when(okHttpClient.newCall(any(Request.class))).thenReturn(okHttpCall);
        when(okHttpCall.execute()).thenReturn(okHttpResponse);
        when(okHttpResponse.body()).thenReturn(okHttpResponseBody);
        when(okHttpResponseBody.string()).thenReturn("{дичь:");

        // Act, Assert
        assertThatThrownBy(() -> service.fetchSuggestions(queryParam))
                .isInstanceOf(ExternalServiceException.class)
                .hasMessageContaining("Exception during convert dadata suggestions into object");
    }

    @Test
    void whenSendOkHttpRequest_thenReturnSuggestions() throws IOException {
        // Arrange
        final String queryParam = "Москва, ул. Ленина, д 15, кв, 24";
        SuggestionDataDto expected = new SuggestionDataDto()
                .setUnrestrictedValue("108816, г Москва, Новомосковский округ, поселок дск Мичуринец, ул Ленина, д 15, кв 24")
                .setRegion("Москва")
                .setCity("Москва")
                .setSettlement("дск Мичуринец")
                .setStreet("Ленина")
                .setHouse("15")
                .setFlat("24")
                .setMetroStations(List.of());

        when(okHttpClient.newCall(any(Request.class))).thenReturn(okHttpCall);
        when(okHttpCall.execute()).thenReturn(okHttpResponse);
        when(okHttpResponse.body()).thenReturn(okHttpResponseBody);
        when(okHttpResponseBody.string()).thenReturn(getDaDataResponse());

        // Act
        List<SuggestionDataDto> actual = service.fetchSuggestions(queryParam);

        // Assert
        assertFalse(actual.isEmpty());
        assertEquals(expected, actual.get(0));
    }

    @Test
    void whenSendOkHttpRequest_thenVerifyRequestParametersSet() throws IOException {
        // Arrange
        final String queryParam = "Москва, ул. Ленина, д 15, кв, 24";
        final String expectedRequestUrl = "https://api.dadata.ru/v1/suggest/address";

        when(okHttpClient.newCall(any(Request.class))).thenReturn(okHttpCall);
        when(okHttpCall.execute()).thenReturn(okHttpResponse);
        when(okHttpResponse.body()).thenReturn(okHttpResponseBody);
        when(okHttpResponseBody.string()).thenReturn(getDaDataResponse());
        ArgumentCaptor<Request> okHttpRequestCaptor = ArgumentCaptor.forClass(Request.class);

        // Act
        service.fetchSuggestions(queryParam);

        // Assert
        verify(okHttpClient).newCall(okHttpRequestCaptor.capture());
        Request actualRequest = okHttpRequestCaptor.getValue();

        HttpUrl actualRequestUrl = actualRequest.url();
        assertEquals(expectedRequestUrl, actualRequestUrl.toString());

        RequestBody actualBody = actualRequest.body();
        assertNotNull(actualBody);

        Headers actualHeaders = actualRequest.headers();
        assertEquals("Token auth-token", actualHeaders.get("Authorization"));
    }

    private String getDaDataResponse() throws IOException {
        ClassPathResource resource = new ClassPathResource("dadata/response.json");
        return new String(resource.getInputStream().readAllBytes());
    }
}
