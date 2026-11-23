package fr.abes.thesesapirecherche.theses.service;

import fr.abes.thesesapirecherche.theses.data.DbRequests;
import fr.abes.thesesapirecherche.theses.dto.SignalerErreurDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
class MoviesServiceTest {

    @Mock
    Environment env;

    @Mock
    DbRequests dbRequests;

    @Mock
    CaptchaService captchaService;

    MoviesService moviesService;

    @BeforeEach
    public void setUp() {
        moviesService = new MoviesService(env, dbRequests, captchaService);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "1234567890", "123", "aaabbbccc", "123x56789"})
    void getMailAdressesFromMovies_should_fail_when_bad_ppn(String ppn) {
        SignalerErreurDto json = new SignalerErreurDto();
        json.setEtabPpn(ppn);
        Assertions.assertThatThrownBy(() -> moviesService.getMailAdressesFromMovies(json))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid query value ("+ppn+"). Must 9 digits long. Last digit can be an X");

    }


}