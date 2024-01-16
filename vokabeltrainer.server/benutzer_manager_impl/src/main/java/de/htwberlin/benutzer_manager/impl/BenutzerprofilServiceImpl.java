package de.htwberlin.benutzer_manager.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import de.htwberlin.benutzer_manager.api.BenutzerprofilService;
import de.htwberlin.benutzer_manager.api.domain.Benutzerprofil;
import de.htwberlin.benutzer_manager.api.domain.InvalidUsernameException;
import de.htwberlin.benutzer_manager.api.domain.UserAlreadyExistsException;
import de.htwberlin.benutzer_manager.api.domain.UserNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class BenutzerprofilServiceImpl implements BenutzerprofilService {

	@Override
	@Transactional(rollbackOn = { InvalidUsernameException.class, UserAlreadyExistsException.class })
	public Benutzerprofil erstelleBenutzerprofil(String benutzername/*, String passwort, String email*/)
			throws InvalidUsernameException, UserAlreadyExistsException {
		
		 if (benutzername == null || benutzername.isEmpty()) {
	            throw new InvalidUsernameException(benutzername);
	        } else {
	            Benutzerprofil nutzer = new Benutzerprofil(benutzername);
	            BenutzerprofilDAO.getBenutzerprofilDAO().speichereBenutzer(nutzer);
	            return nutzer;
	        }

	}
;
	@Override
	@Transactional(rollbackOn = { UserNotFoundException.class })
	public boolean loescheBenutzerprofil(Benutzerprofil benutzer) throws UserNotFoundException {
		
		 Benutzerprofil findUser = BenutzerprofilDAO.getBenutzerprofilDAO().findeBenutzerById(benutzer.getId());

	        if (findUser == null) {
	            throw new UserNotFoundException("NULL", null);
	        } else {
	            BenutzerprofilDAO.getBenutzerprofilDAO().loescheBenutzer(benutzer);
	        }

	        return true;

	}

	@Override
	
	public Benutzerprofil findeBenutzerprofil(String benutzername) throws UserNotFoundException {
		return BenutzerprofilDAO.getBenutzerprofilDAO().findeBenutzerByName(benutzername);
	}

	@Override
	public List<Benutzerprofil> findeAllBenutzer() throws UserNotFoundException {
		return BenutzerprofilDAO.getBenutzerprofilDAO().findeAlleBenutzer();
	}



	

}
