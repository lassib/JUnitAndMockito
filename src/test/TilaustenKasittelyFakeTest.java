import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.junitandmockito.*;

public class TilaustenKasittelyFakeTest {

    @Test
    public void testaaKasittelijaWithFakeHinnoittelija() {
        //Arrange
        float alkuSaldo = 100.0f;
        float listahinta = 30.0f;
        float alennus = 20.0f;
        float loppuSaldo = alkuSaldo - (listahinta * (1 - (alennus / 100)));

        Asiakas asiakas = new Asiakas(alkuSaldo);
        Tuote tuote = new Tuote("TDD in Action", listahinta);
        IHinnoittelija hinnoittelija = new FakeHinnoittelija(alennus);

        //Act
        TilaustenKäsittely käsittelijä = new TilaustenKäsittely();
        käsittelijä.setHinnoittelija(hinnoittelija);
        käsittelijä.käsittele(new Tilaus(asiakas, tuote));

        //Assert
        assertEquals(loppuSaldo, asiakas.getSaldo());
    }
}
