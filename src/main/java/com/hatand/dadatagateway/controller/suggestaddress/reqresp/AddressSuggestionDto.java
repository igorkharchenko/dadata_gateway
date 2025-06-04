package com.hatand.dadatagateway.controller.suggestaddress.reqresp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressSuggestionDto {

    @Schema(description = "Адрес одной строкой (полный, с индексом)", example = "г Москва, ул Хабаровская")
    String unrestrictedValue;
    @Schema(description = "Регион", example = "Москва")
    String region;
    @Schema(description = "Город", example = "Москва")
    String city;
    @Schema(description = "Населённый пункт", example = "Москва")
    String settlement;
    @Schema(description = "Улица", example = "Хабаровская")
    String street;
    @Schema(description = "Номер дома", example = "12")
    String house;
    @Schema(description = "Номер квартиры", example = "46")
    String flat;
    @Schema(description = "Станции метро рядом", example = "Станция метро ВДНХ")
    List<String> metroStations;
}
