package de.htwberlin.benutzer_manager.impl;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import de.htwberlin.benutzer_manager.api.BenutzerprofilService;
import de.htwberlin.benutzer_manager.api.domain.Benutzerprofil;
import de.htwberlin.benutzer_manager.api.domain.InvalidUsernameException;
import de.htwberlin.benutzer_manager.api.domain.UserAlreadyExistsException;
import de.htwberlin.benutzer_manager.api.domain.UserNotFoundException;
import jakarta.persistence.EntityTransaction;

/**
 * Unit test fuer Benutzerprofil_Manager.
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("Test: BenutzerprofilServiceImpl")
public class BenutzerprofilServiceTest {

	@InjectMocks
	private BenutzerprofilServiceImpl benutzerServiceImpl;
	@Mock
	private BenutzerprofilService benutzerService;
	@Mock
	private EntityTransaction entityTransaction;
	@Mock
	private BenutzerprofilDAO benutzerDAO;
	private static MockedStatic<BenutzerprofilDAO> ms;

	@BeforeAll
	public static void initialize() {
		ms = Mockito.mockStatic(BenutzerprofilDAO.class);
	}

	@BeforeEach
	void setUp() {
		ms.when(() -> BenutzerprofilDAO.getBenutzerprofilDAO()).thenReturn(benutzerDAO);
		lenient().when(benutzerDAO.getEntityTransaction()).thenReturn(entityTransaction);
		lenient().doNothing().when(entityTransaction).begin();
		lenient().doNothing().when(entityTransaction).commit();
	}
	
	@Test
	@DisplayName("01. Benutzer mit validem Benutzernamen anlegen")
	@Order(1)
	void erstelleBenutzerprofil_MitValidnameTest() throws InvalidUsernameException, UserAlreadyExistsException {
		// Arrange
		String benutzername = "Max Mustermann";
		Benutzerprofil benutzer = new Benutzerprofil(benutzername);

		// Stubbing
		lenient().when(benutzerDAO.speichereBenutzer(benutzer)).thenReturn(benutzer);

		// Act
		Benutzerprofil benutzer2 = benutzerServiceImpl.erstelleBenutzerprofil(benutzername);
		
		// Assert
		assertNotNull(benutzer2);
		assertEquals(benutzername, benutzer2.getBenutzername());

		// Verifying
		verify(benutzerDAO, Mockito.times(1)).speichereBenutzer(benutzer2);
	}

	@Test
	@DisplayName("02. Benutzer mit Name 'null' anlegen und InvalidUsernameException werfen")
	@Order(2)
	void erstelleBenutzerprofil_MitNullNameTest() throws InvalidUsernameException, UserAlreadyExistsException {
		// Arrange
		String benutzername = null;

		// Stubbing
		lenient().doThrow(new InvalidUsernameException(benutzername)).when(benutzerDAO).speichereBenutzer(any(Benutzerprofil.class));

		// Act, Assert
		Assertions.assertThrows(InvalidUsernameException.class, () -> {
			benutzerServiceImpl.erstelleBenutzerprofil(benutzername);
		});

		// Verifying
		verify(benutzerDAO, times(0)).speichereBenutzer(any(Benutzerprofil.class));
	}

	@Test
	@DisplayName("03. Benutzer mit leeren Namen anlegen und InvalidUsernameException werfen")
	@Order(3)
	void erstelleBenutzerprofil_MitleeremNameTest() throws InvalidUsernameException, UserAlreadyExistsException {
		// Arrange
		String benutzername = "";

		// Stubbing
		lenient().doThrow(new InvalidUsernameException(benutzername)).when(benutzerDAO).speichereBenutzer(any(Benutzerprofil.class));

		// Act, Assert
		assertThrows(InvalidUsernameException.class, () -> benutzerServiceImpl.erstelleBenutzerprofil(benutzername));

		// Verifying
		verify(benutzerDAO, times(0)).speichereBenutzer(any(Benutzerprofil.class));
	}

	@Test
	@DisplayName("04. Existierenden Benutzer aus der Datenbank löschen")
	@Order(4)
	void loescheVorhandeneNutzerTest() throws UserNotFoundException {
		// Arrange
		Benutzerprofil benutzer = new Benutzerprofil("Max Mustermann");

		// Stubbing
		when(benutzerDAO.findeBenutzerById(benutzer.getId())).thenReturn(benutzer);

		// Act
		boolean ergebnis = benutzerServiceImpl.loescheBenutzerprofil(benutzer);
		
		// Assert
		assertEquals(true, ergebnis);

		// Verifying
		verify(benutzerDAO, times(1)).findeBenutzerById(benutzer.getId());
		verify(benutzerDAO, times(1)).loescheBenutzer(benutzer);
	}

	@Test
	@DisplayName("05 Nicht existierenden Benutzer aus der Datenbank löschen und UserNotFoundException werfen")
	@Order(5)
	void loescheNichtVorhandeneNutzerTest() throws UserNotFoundException {
		// Arrange
		Benutzerprofil benutzer = new Benutzerprofil("Oliver Wulff");

		// Stubbing
		when(benutzerDAO.findeBenutzerById(benutzer.getId())).thenReturn(null);

		// Act, Assert
		Assertions.assertThrows(UserNotFoundException.class, () -> benutzerServiceImpl.loescheBenutzerprofil(benutzer));

		// Verifying
		verify(benutzerDAO, times(1)).findeBenutzerById(benutzer.getId());
		verify(benutzerDAO, times(0)).loescheBenutzer(benutzer);
	}

	@Test
	@DisplayName("06. Existierenden Benutzer anhand Benutzername holen")
	@Order(6)
	void findeVorhandeneBenutzerTest() throws UserNotFoundException {
		// Arrange
		Benutzerprofil benutzer = new Benutzerprofil("Olaf Scholz");

		// Stubbing
		when(benutzerDAO.findeBenutzerByName(benutzer.getBenutzername())).thenReturn(benutzer);

		// Act
		Benutzerprofil benutzerAusDB = benutzerServiceImpl.findeBenutzerprofil(benutzer.getBenutzername());
		
		// Assert
		assertNotNull(benutzerAusDB);
		assertEquals(benutzer, benutzerAusDB);

		// Verifying
		verify(benutzerDAO, times(1)).findeBenutzerByName(benutzer.getBenutzername());
	}

	@Test
	@DisplayName("07. Nicht existierenden Benutzer holen und UserNotFoundException werfen")
	@Order(7)
	void findeNichtVorhandeneBenutzerTest() throws UserNotFoundException {
		// Arrange
		String benutzername = "Otto Müller";
		
		// Stubbing
		when(benutzerDAO.findeBenutzerByName(benutzername)).thenThrow(new UserNotFoundException(benutzername, null));
		
		// Act, Assert
		assertThrows(UserNotFoundException.class, () -> benutzerServiceImpl.findeBenutzerprofil(benutzername));
		
		// Verifying
		verify(benutzerDAO, times(1)).findeBenutzerByName(benutzername);
	}

	@Test
	@DisplayName("08. Alle Benutzer holen")
	@Order(8)
	void findeAlleBenutzerTest() throws UserNotFoundException {
		// Arrange
		Benutzerprofil benutzer1 = new Benutzerprofil("Danielle Motio");
		Benutzerprofil benutzer2 = new Benutzerprofil("Ha Pham");
		Benutzerprofil benutzer3 = new Benutzerprofil("Eggi Eggi");
		List<Benutzerprofil> benutzerListe = Arrays.asList(benutzer1, benutzer2, benutzer3);
		
		// Stubbing
		when(benutzerDAO.findeAlleBenutzer()).thenReturn(benutzerListe);

		// Act
	    List<Benutzerprofil> benutzerListeAusDB = benutzerServiceImpl.findeAllBenutzer();
	    
	    // Assert
		assertNotNull(benutzerListe);
		assertEquals(benutzerListe.size(), benutzerListeAusDB.size());
		assertTrue(benutzerListeAusDB.contains(benutzer1));
		assertTrue(benutzerListeAusDB.contains(benutzer2));
		assertTrue(benutzerListeAusDB.contains(benutzer3));

		// Verifying
		verify(benutzerDAO, times(1)).findeAlleBenutzer();
	}

	@Test
	@DisplayName("09. Benutzer mit bereits existierenden Benutzernamen erstellen und_ serAlreadyExistsException werfen")
	@Order(9)
	void speichereSchonVorhandeBenutzerTest() throws UserAlreadyExistsException, InvalidUsernameException {
		// Arrange
		String benutzername = "Max Mustermann";
		
		
		
		// Stubbing
		when(benutzerDAO.speichereBenutzer(any(Benutzerprofil.class))).thenThrow(new UserAlreadyExistsException(benutzername, null));
		
		// Act, Assert
		Assertions.assertThrows(UserAlreadyExistsException.class, () -> benutzerServiceImpl.erstelleBenutzerprofil(benutzername));
		
		// Verifying
		verify(benutzerDAO, Mockito.times(1)).speichereBenutzer(any(Benutzerprofil.class));
	}
}
