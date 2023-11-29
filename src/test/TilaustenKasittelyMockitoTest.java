import com.example.junitandmockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class TilaustenKasittelyMockitoTest {

    @Mock
    IHinnoittelija hinnoittelijaMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @CsvSource({
            "100.0, 30.0, 20.0",
            "150.0, 130.0, 42.0",
            "80.0, 500.0, 100.0",
            "100.0, 100.0, 100.0",
    })
    public void testaaKasittelijaWithMockitoHinnoittelija(float alkuSaldo, float listahinta, float alennus) {
        //Arrange
        float loppuSaldo = alkuSaldo - (listahinta * (1 - (alennus / 100)));

        Asiakas asiakas = new Asiakas(alkuSaldo);
        Tuote tuote = new Tuote("TDD in Action", listahinta);
        // Record
        when(hinnoittelijaMock.getAlennusProsentti(asiakas, tuote)).thenReturn(alennus);

        doNothing().when(hinnoittelijaMock).setAlennusProsentti(asiakas, alennus + 5);

        //Act
        TilaustenKäsittely käsittelijä = new TilaustenKäsittely();
        käsittelijä.setHinnoittelija(hinnoittelijaMock);
        käsittelijä.käsittele(new Tilaus(asiakas, tuote));

        //Assert
        assertEquals(loppuSaldo, asiakas.getSaldo(), 0.01);
        verify(hinnoittelijaMock, times(2)).getAlennusProsentti(asiakas, tuote);
        if (tuote.getHinta() >= 100) {
            verify(hinnoittelijaMock, times(1)).setAlennusProsentti(asiakas, alennus + 5);
        } else {
            verify(hinnoittelijaMock, times(0)).setAlennusProsentti(any (Asiakas.class), any (Float.class));
        }
    }
}
