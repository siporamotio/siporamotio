package de.htwberlin.spiel_manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import de.htwberlin.benutzer_manager.api.domain.Benutzerprofil;
import de.htwberlin.spiel_manager.api.domain.Benutzerantwort;
import de.htwberlin.spiel_manager.api.domain.NotEnoughPlayerException;
import de.htwberlin.spiel_manager.api.domain.Spiel;
import de.htwberlin.vokabel_manager.api.VokabelService;
import de.htwberlin.vokabel_manager.api.domain.Datei;
import de.htwberlin.vokabel_manager.api.domain.Quizfrage;
import jakarta.persistence.EntityTransaction;

/**
 * Unit test fuer Spiel_Manager.
 */
@ExtendWith(MockitoExtension.class)
public class SpielServiceTest {

	@InjectMocks
	private SpielServiceImpl spielServiceImpl;
	@Mock
	private VokabelService vokabelService;
	@Mock
	private EntityTransaction entityTransaction;
	@Mock
	private SpielDAO spielDao;
	private static MockedStatic<SpielDAO> spielDaoStatic;

	@BeforeAll
	public static void initialize() {
		spielDaoStatic = Mockito.mockStatic(SpielDAO.class);
	}

	@BeforeEach
	void setUp() {
		
		spielDaoStatic.when(() -> SpielDAO.getSpielDAO()).thenReturn(spielDao);
		Mockito.lenient().when(spielDao.getEntityTransaction()).thenReturn(entityTransaction);
		Mockito.lenient().doNothing().when(entityTransaction).begin();
		Mockito.lenient().doNothing().when(entityTransaction).commit();
	}

	@Test
	@DisplayName("gültiger Test für erstelleSpiel()")
	void erstelleSpielValidTest() throws NotEnoughPlayerException {
		// Arrange
		Benutzerprofil spieler1 = new Benutzerprofil("Max");
		Benutzerprofil spieler2 = new Benutzerprofil("Moritz");
		List<Benutzerprofil> spielerListe = new ArrayList<Benutzerprofil>();
		spielerListe.add(spieler1);
		spielerListe.add(spieler2);
		Datei datei = new Datei("Testeinheit", "Sprache1", "Sprache2", "Testkategorie", null);
		List<String> falscheAntworten = new ArrayList<String>();
		falscheAntworten.add("gehen");
		falscheAntworten.add("fliegen");
		falscheAntworten.add("trinken");
		Quizfrage vokabelfrage1 = new Quizfrage("eat", "essen", falscheAntworten);
		List<Quizfrage> fragenListe = new ArrayList<Quizfrage>();
		fragenListe.add(vokabelfrage1);
		// stubbing
		Mockito.when(vokabelService.generiereFragen(datei, 5)).thenReturn(fragenListe);
		Mockito.lenient().doNothing().when(spielDao).speichereSpiel(null);
		// Act
		Spiel spiel = spielServiceImpl.erstelleSpiel(spielerListe, datei);
		// Assert
		Assertions.assertNotNull(spiel);
		Assertions.assertEquals(spieler1, spiel.getSpieler().get(0));
		Assertions.assertEquals(spieler2, spiel.getSpieler().get(1));
		Assertions.assertEquals(datei, spiel.getDatei());
		Assertions.assertEquals(fragenListe, spiel.getQuizfragen());
		// Verify
		Mockito.verify(vokabelService, Mockito.times(1)).generiereFragen(datei, 5);
		Mockito.verify(spielDao, Mockito.times(1)).speichereSpiel(spiel);
	
	}

	@Test
	@DisplayName("Test für duellErstellen() mit zu wenig Spielern")
	void erstelleSpielValidSpielerTest() throws NotEnoughPlayerException {
		// Arrange
		Benutzerprofil spieler1 = new Benutzerprofil("Max");
		ArrayList<Benutzerprofil> spielerListe1 = new ArrayList<Benutzerprofil>(); // ArrayList ohne Spieler
		ArrayList<Benutzerprofil> spielerListe2 = new ArrayList<Benutzerprofil>(); // ArrayList mit nur einem Spieler
		spielerListe2.add(spieler1);
		Datei einheit = new Datei("Testeinheit", "Sprache1", "Sprache2", "Testkategorie", null);
		// Assert
		Assertions.assertThrows(NotEnoughPlayerException.class, () -> {
			// Act
			spielServiceImpl.erstelleSpiel(spielerListe1, einheit);
		});
		Assertions.assertThrows(NotEnoughPlayerException.class, () -> {
			// Act
			spielServiceImpl.erstelleSpiel(spielerListe2, einheit);
		});
	}

	@Test
	@DisplayName("gültiger Test für naechsteQuizfrage()")
	void naechsteQuizfrageTest() {
		// Arrange
		Benutzerprofil spieler1 = new Benutzerprofil("Max");
		Benutzerprofil spieler2 = new Benutzerprofil("Moritz");
		ArrayList<Benutzerprofil> spielerListe = new ArrayList<Benutzerprofil>();
		spielerListe.add(spieler1);
		spielerListe.add(spieler2);
		Datei einheit = new Datei("Testeinheit", "Sprache1", "Sprache2", "Testkategorie", null);
		List<String> listStrings = new ArrayList<String>();
		listStrings.add("String1");
		Quizfrage vokabelfrage1 = new Quizfrage("String1", "String2", listStrings);
		Quizfrage vokabelfrage2 = new Quizfrage("String3", "String4", listStrings);
		List<Quizfrage> listVokabelfrage = new ArrayList<Quizfrage>();
		listVokabelfrage.add(vokabelfrage1);
		listVokabelfrage.add(vokabelfrage2);
		Spiel spiel = new Spiel(spielerListe, einheit, listVokabelfrage);
		List<Benutzerantwort> benutzerantworten = new ArrayList<Benutzerantwort>();
		Benutzerantwort benutzerantwort = new Benutzerantwort("String1", spieler1, vokabelfrage1, spiel);
		benutzerantworten.add(benutzerantwort);
		// Stubbing
		Mockito.doReturn(benutzerantworten).when(spielDao).getBenutzerantwort(spiel, spieler1);
		// Act
		Quizfrage quizfrage = spielServiceImpl.naechsteQuizfrage(spiel, spieler1);
		// Assert
		Assertions.assertNotNull(quizfrage);
		// Verify
		Mockito.verify(spielDao, Mockito.times(1)).getBenutzerantwort(spiel, spieler1);
	}

	@Test
	@DisplayName("gültiger Test für benutzerantwortSpeichern()")
	void benutzerantwortSpeichernTest() {
		// Arrange
		String antwort = "richtige Antwort";
		Quizfrage quizfrage = new Quizfrage("Frage", antwort, new ArrayList<String>());
		Benutzerprofil spieler1 = new Benutzerprofil("Max");
		Benutzerprofil spieler2 = new Benutzerprofil("Moritz");
		ArrayList<Benutzerprofil> spielerListe = new ArrayList<Benutzerprofil>();
		spielerListe.add(spieler1);
		spielerListe.add(spieler2);
		Datei datei = new Datei("Testeinheit", "Sprache1", "Sprache2", "Testkategorie", null);
		Spiel spiel = new Spiel(spielerListe, datei, null);
		List<String> listStrings = new ArrayList<String>();
		listStrings.add("String1");
		Benutzerantwort benutzerantwort = new Benutzerantwort(antwort, spieler1, quizfrage, spiel);
		// Stubbing
		Mockito.lenient().doNothing().when(spielDao).saveBenutzerantwort(benutzerantwort);
		Mockito.lenient().doNothing().when(spielDao).speichereSpiel(spiel);
		// Act & Assert
		Assertions.assertTrue(spielServiceImpl.benutzerantwortSpeichern(spiel, spieler1, quizfrage, antwort));
		Assertions.assertEquals(spiel.getSpielstand().get(spieler1), 1);
		// Verify
		Mockito.verify(spielDao, Mockito.times(1)).speichereSpiel(spiel);
	}



	@Test
	@DisplayName("gültiger Test für duellBeenden()")
	void spielBeendenTest() throws NotEnoughPlayerException {
		// Arrange
		Benutzerprofil spieler1 = new Benutzerprofil("Max");
		Benutzerprofil spieler2 = new Benutzerprofil("Moritz");
		ArrayList<Benutzerprofil> spielerListe = new ArrayList<Benutzerprofil>();
		spielerListe.add(spieler1);
		spielerListe.add(spieler2);
		Datei einheit = new Datei("Testeinheit", "Sprache1", "Sprache2", "Testkategorie", null);
		Spiel spiel = spielServiceImpl.erstelleSpiel(spielerListe, einheit);
		// Stubbing
		Mockito.doNothing().when(spielDao).speichereSpiel(spiel);
		// Act
		Map<Benutzerprofil, Integer> spielstand = spielServiceImpl.spielBeenden(spiel);
		// Assert
		Assertions.assertNotNull(spielstand);

		// Verify
		Mockito.verify(spielDao, Mockito.times(2)).speichereSpiel(spiel);

	}
}

