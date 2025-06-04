package com.hatand.dadatagateway.controller.suggestaddress;

import com.hatand.dadatagateway.business.suggestaddress.entity.AddressSuggestion;
import com.hatand.dadatagateway.business.suggestaddress.service.AddressSuggestionService;
import com.hatand.dadatagateway.controller.suggestaddress.mapper.AddressSuggestionReqRespMapper;
import com.hatand.dadatagateway.controller.suggestaddress.reqresp.AddressSuggestionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/v1")
@Tag(name = "Подсказки по адресам", description = "Подсказки по адресам")
public class SuggestAddressController {
    private final AddressSuggestionService addressSuggestionService;
    private final AddressSuggestionReqRespMapper reqRespMapper;

    @GetMapping("/suggest/address")
    @Operation(summary = "Возвращает найденные адреса по любой части адреса от региона до квартиры.",
            description = """
                    Ищет адреса по любой части адреса от региона до квартиры
                     («самара авроры 7 12» → «443017, Самарская обл, г Самара, ул Авроры, д 7, кв 12»).
                     Также ищет по почтовому индексу («105568» → «г Москва, ул Магнитогорская»).
                    """)
    public AddressSuggestionResponse getAddressSuggestions(
            @RequestParam
            @NotBlank(message = "queryString не должен быть пустым.")
            @Size(min = 2, max = 128, message = "Строка поиска должна быть от 2 до 128 символов.")
            @Pattern(regexp = "[А-яЁёA-z0-9.,()\\s/\\-]+", message = """
                    Строка поиска может содержать только русские/латинские символы, пробелы, а также символы: .,-\\/""")
            String queryString) {
        List<AddressSuggestion> suggestions = addressSuggestionService.getSuggestions(queryString);

        return new AddressSuggestionResponse()
                .setSuggestions(suggestions.stream().map(reqRespMapper::map).toList());
    }
}
