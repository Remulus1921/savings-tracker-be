package org.bekierz.savingstrackerbe.unit.saving.service;

import org.bekierz.savingstrackerbe.asset.model.dto.AssetValueDto;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.bekierz.savingstrackerbe.asset.model.entity.AssetType;
import org.bekierz.savingstrackerbe.datasource.service.AssetHandler;
import org.bekierz.savingstrackerbe.datasource.service.AssetHandlerRegistry;
import org.bekierz.savingstrackerbe.saving.model.entity.Saving;
import org.bekierz.savingstrackerbe.saving.repository.SavingRepository;
import org.bekierz.savingstrackerbe.saving.service.SavingService;
import org.bekierz.savingstrackerbe.user.model.CustomUserDetails;
import org.bekierz.savingstrackerbe.user.model.entity.User;
import org.bekierz.savingstrackerbe.utils.config.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SavingServiceTest {

    @Mock
    private SavingRepository savingRepository;
    @Mock
    private AssetHandlerRegistry handlerRegistry;
    @Mock
    private AssetHandler handler;
    @InjectMocks
    private SavingService savingService;

    private final static String EMAIL = "test@test.com";

    @BeforeEach
    public void setUp() {
        CustomUserDetails mockUserDetails = mock(CustomUserDetails.class);
        Authentication mockAuthentication = mock(UsernamePasswordAuthenticationToken.class);
        SecurityContext mockSecurityContext = mock(SecurityContext.class);

        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUsername()).thenReturn(EMAIL);

        SecurityContextHolder.setContext(mockSecurityContext);
    }

    @Test
    public void should_return_user_savings() {
        // given
        List<Saving> savings = List.of(
                Saving.builder()
                        .id(1L)
                        .amount(100.0)
                        .user(User.builder()
                                .email(EMAIL)
                                .build()
                        )
                        .asset(Asset.builder()
                                .id(1L)
                                .name("Bitcoin")
                                .code("BTC")
                                .assetType(AssetType.builder()
                                        .id(1L)
                                        .name("cryptocurrency")
                                        .build()
                                )
                                .build()
                        )
                        .build(),
                Saving.builder()
                        .id(2L)
                        .amount(200.0)
                        .user(User.builder()
                                .email(EMAIL)
                                .build()
                        )
                        .asset(Asset.builder()
                                .id(2L)
                                .name("Euro")
                                .code("EUR")
                                .assetType(AssetType.builder()
                                        .id(2L)
                                        .name("currency")
                                        .build()
                                )
                                .build()
                        )
                        .build()
        );

        // when
        when(savingRepository.findByUserEmail(EMAIL)).thenReturn(savings);
        when(handlerRegistry.getHandler("cryptocurrency")).thenReturn(handler);
        when(handlerRegistry.getHandler("currency")).thenReturn(handler);
        when(handler.getAssetValue("BTC")).thenReturn(new AssetValueDto("BTC", 2.0));
        when(handler.getAssetValue("EUR")).thenReturn(new AssetValueDto("EUR", 3.0));

        var result = savingService.getUserSavings();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(100.0, result.getFirst().amount());
        assertEquals("Bitcoin", result.getFirst().assetName());
        assertEquals("BTC", result.getFirst().assetCode());
        assertEquals(200.0, result.getFirst().value());
        assertEquals(200.0, result.get(1).amount());
        assertEquals("Euro", result.get(1).assetName());
        assertEquals("EUR", result.get(1).assetCode());
        assertEquals(600.0, result.get(1).value());
    }

    @Test
    public void should_return_saving_value() {
        // given
        String assetCode = "BTC";

        Saving saving = Saving.builder()
                .id(1L)
                .amount(100.0)
                .user(User.builder()
                        .email(EMAIL)
                        .build()
                )
                .asset(Asset.builder()
                        .id(1L)
                        .name("Bitcoin")
                        .code("BTC")
                        .assetType(AssetType.builder()
                                .id(1L)
                                .name("cryptocurrency")
                                .build()
                        )
                        .build()
                )
                .build();

        // when
        when(savingRepository.findSavingByUserEmailAndAssetCode(EMAIL, assetCode)).thenReturn(saving);
        when(handlerRegistry.getHandler("cryptocurrency")).thenReturn(handler);
        when(handler.getAssetValue("BTC")).thenReturn(new AssetValueDto("BTC", 2.0));

        var result = savingService.getSavingValue(assetCode);

        // then
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(100.0, result.get().amount());
        assertEquals("Bitcoin", result.get().assetName());
        assertEquals("BTC", result.get().assetCode());
        assertEquals(200.0, result.get().value());
        assertEquals(2.0, result.get().exchangeRate());
    }

    @Test
    public void should_return_empty_optional_when_saving_not_found() {
        // given
        String assetCode = "BTC";

        // when
        when(savingRepository.findSavingByUserEmailAndAssetCode(EMAIL, assetCode)).thenReturn(null);

        var result = savingService.getSavingValue(assetCode);

        // given
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
