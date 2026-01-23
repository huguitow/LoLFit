package com.lolfit.backend.skin;

import com.lolfit.backend.skin.dto.AddSkinRequest;
import com.lolfit.backend.user.User;
import com.lolfit.backend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test unitaire pour WardrobeService.
 *
 * Testons la méthode addSkinToWardrobe() qui contient la logique métier
 * suivante :
 * 1. Vérifier que l'utilisateur existe
 * 2. Vérifier que le skin existe
 * 3. Vérifier que le skin n'est pas déjà dans la garde-robe
 * 4. Sauvegarder l'entrée dans la garde-robe
 */
@ExtendWith(MockitoExtension.class)
class WardrobeServiceTest {

        @Mock
        private UserRepository userRepository;

        @Mock
        private SkinRepository skinRepository;

        @Mock
        private WardrobeRepository wardrobeRepository;

        @InjectMocks
        private WardrobeService wardrobeService;

        private User testUser;
        private Skin testSkin;
        private Champion testChampion;
        private AddSkinRequest validRequest;

        @BeforeEach
        void setUp() {
                testUser = User.builder()
                                .id(1L)
                                .email("test@example.com")
                                .username("testuser")
                                .build();

                testChampion = Champion.builder()
                                .id(1L)
                                .name("Ahri")
                                .riotId("Ahri")
                                .build();

                testSkin = Skin.builder()
                                .id(100L)
                                .name("Star Guardian Ahri")
                                .riotId("Ahri_1")
                                .champion(testChampion)
                                .build();

                validRequest = AddSkinRequest.builder()
                                .skinId(100L)
                                .acquisitionMethod("Achat")
                                .build();
        }

        @Test
        @DisplayName("Doit ajouter un skin à la garde-robe avec succès")
        void addSkinToWardrobe_Success() {
                when(userRepository.findByEmail("test@example.com"))
                                .thenReturn(Optional.of(testUser));

                when(skinRepository.findById(100L))
                                .thenReturn(Optional.of(testSkin));

                when(wardrobeRepository.existsByUserIdAndSkinId(1L, 100L))
                                .thenReturn(false);

                wardrobeService.addSkinToWardrobe("test@example.com", validRequest);

                verify(wardrobeRepository, times(1)).save(any(Wardrobe.class));

                ArgumentCaptor<Wardrobe> wardrobeCaptor = ArgumentCaptor.forClass(Wardrobe.class);
                verify(wardrobeRepository).save(wardrobeCaptor.capture());

                Wardrobe savedWardrobe = wardrobeCaptor.getValue();
                assertEquals(testUser, savedWardrobe.getUser(), "L'utilisateur doit correspondre");
                assertEquals(testSkin, savedWardrobe.getSkin(), "Le skin doit correspondre");
                assertEquals("Achat", savedWardrobe.getAcquisitionMethod(),
                                "La méthode d'acquisition doit correspondre");
                assertFalse(savedWardrobe.getIsFavorite(), "isFavorite doit être false par défaut");
                assertNotNull(savedWardrobe.getObtainedAt(), "obtainedAt ne doit pas être null");
        }

        @Test
        @DisplayName("Doit lever une exception si l'utilisateur n'existe pas")
        void addSkinToWardrobe_UserNotFound_ThrowsException() {
                when(userRepository.findByEmail("inconnu@example.com"))
                                .thenReturn(Optional.empty());

                RuntimeException exception = assertThrows(
                                RuntimeException.class,
                                () -> wardrobeService.addSkinToWardrobe("inconnu@example.com", validRequest));

                assertEquals("User not found", exception.getMessage());

                verify(wardrobeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Doit lever une exception si le skin n'existe pas")
        void addSkinToWardrobe_SkinNotFound_ThrowsException() {
                when(userRepository.findByEmail("test@example.com"))
                                .thenReturn(Optional.of(testUser));

                when(skinRepository.findById(999L))
                                .thenReturn(Optional.empty());

                AddSkinRequest requestWithInvalidSkin = AddSkinRequest.builder()
                                .skinId(999L)
                                .build();

                RuntimeException exception = assertThrows(
                                RuntimeException.class,
                                () -> wardrobeService.addSkinToWardrobe("test@example.com", requestWithInvalidSkin));

                assertEquals("Skin not found", exception.getMessage());
                verify(wardrobeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Doit lever une exception si le skin est déjà dans la garde-robe")
        void addSkinToWardrobe_SkinAlreadyExists_ThrowsException() {
                when(userRepository.findByEmail("test@example.com"))
                                .thenReturn(Optional.of(testUser));

                when(skinRepository.findById(100L))
                                .thenReturn(Optional.of(testSkin));

                when(wardrobeRepository.existsByUserIdAndSkinId(1L, 100L))
                                .thenReturn(true);

                RuntimeException exception = assertThrows(
                                RuntimeException.class,
                                () -> wardrobeService.addSkinToWardrobe("test@example.com", validRequest));

                assertEquals("Skin already in wardrobe", exception.getMessage());

                verify(wardrobeRepository, never()).save(any());
        }
}
