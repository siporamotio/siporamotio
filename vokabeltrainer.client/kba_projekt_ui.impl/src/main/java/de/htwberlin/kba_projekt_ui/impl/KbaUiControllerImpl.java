package de.htwberlin.kba_projekt_ui.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import de.htwberlin.benutzer_manager.api.BenutzerprofilService;
import de.htwberlin.benutzer_manager.api.domain.Benutzerprofil;
import de.htwberlin.benutzer_manager.api.domain.UserNotFoundException;
import de.htwberlin.kba_projekt_ui.api.KbaUiController;
import de.htwberlin.spiel_manager.api.SpielService;
import de.htwberlin.spiel_manager.api.domain.NotEnoughPlayerException;
import de.htwberlin.spiel_manager.api.domain.Spiel;
import de.htwberlin.vokabel_manager.api.VokabelService;
import de.htwberlin.vokabel_manager.api.domain.Datei;
import de.htwberlin.vokabel_manager.api.domain.Kategorie;
import de.htwberlin.vokabel_manager.api.domain.Quizfrage;

@Controller
public class KbaUiControllerImpl implements KbaUiController {

	private KbaUiView vokabelView;
	private SpielService spielService;
	private VokabelService vokabelService;
	private BenutzerprofilService benutzerService;


	@Autowired
	public KbaUiControllerImpl(KbaUiView vokabelView, SpielService vokabelDuellService,
			VokabelService vokabelVerwaltungService,
			BenutzerprofilService vokabelNutzerverwaltungService) {
		super();
		this.vokabelView = vokabelView;
		this.spielService = vokabelDuellService;
		this.vokabelService = vokabelVerwaltungService;
		this.benutzerService = vokabelNutzerverwaltungService;
	}

	@Override
	public void run() {
		
		vokabelView.Lade();
		initialSetUp();

		try {
			vokabelService.improtiereCSVDateien();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try (Scanner scanner = new Scanner(System.in)) {
			int input;
			do {
				vokabelView.Willkommen();
				vokabelView.Hauptmenue();
				while (!scanner.hasNextInt()) {
					vokabelView.falscheEingabe();
					scanner.next();
				}
				input = scanner.nextInt();
				if (input == 1) {
					List<Benutzerprofil> spieler = new ArrayList<Benutzerprofil>();
					Datei e = null;

					do {
						vokabelView.NutzerAuswahlmenue(spieler);
						while (!scanner.hasNextInt()) {
							vokabelView.falscheEingabe();
							scanner.next();
						}
						input = scanner.nextInt();
						switch (input) {
						case 1:
							try {
								vokabelView.NutzerAnlegen();
								String username = scanner.next();
								spieler.add(benutzerService.erstelleBenutzerprofil(username));
								vokabelView.NutzerAngelegt(username);
							} catch (Exception e1) {
								e1.printStackTrace();
							}

							break;
						case 2:
							vokabelView.SpielernameEingeben();
							String spielername = scanner.next();
							try {
								Benutzerprofil nutzer = benutzerService.findeBenutzerprofil(spielername);
								spieler.add(nutzer);
							} catch (UserNotFoundException e1) {
								vokabelView.Nutzernichtfinden();
								e1.printStackTrace();
							}
							
							break;
						case 3:
							if (spieler.size() < 2) {
								vokabelView.nichtgenugNutzer();
								break;
							}
							List<Kategorie> kategorien = vokabelService.ladeKategorien();
							do {
								vokabelView.AuswahlKategorie(kategorien);
								while (!scanner.hasNextInt()) {
									vokabelView.falscheEingabe();
									scanner.next();
								}
								input = scanner.nextInt();
								input--;
								while(input>=kategorien.size()) {
									vokabelView.falscheEingabe();
									input = scanner.nextInt();
									input--;
								}		
						
								Kategorie k = kategorien.get(input);
								List<Datei> einheiten = vokabelService.ladeDateiByKategorie(k);
								do {
									vokabelView.AuswahlDatei(einheiten);
									while (!scanner.hasNextInt()) {
										vokabelView.falscheEingabe();
										scanner.next();
									}
									input = scanner.nextInt();
									input--;
									while(input>=einheiten.size()) {
										vokabelView.falscheEingabe();
										input = scanner.nextInt();
										input--;
									}		
									e = einheiten.get(input);
									try {
										Spiel d = spielService.erstelleSpiel(spieler, e);
										for(Benutzerprofil n :spieler) {
											vokabelView.AktuellerSpieler(n);
											do {
												Quizfrage f = spielService.naechsteQuizfrage(d,n);
												if(f==null) {
													vokabelView.SpielerFertig(n);
													break;
												}
												vokabelView.Frage(f);
												while (!scanner.hasNextInt()) {
													vokabelView.falscheEingabe();
													scanner.next();
												}
												input = scanner.nextInt();
												input--;
												while(input>=f.getAllAntwortenAsList().size()) {
													vokabelView.falscheEingabe();
													input = scanner.nextInt();
													input--;
												}	
												spielService.benutzerantwortSpeichern(d, n, f, f.getAllAntwortenAsList().get(input));
												
											}while(input!=-1);
										}
										
										Map<Benutzerprofil, Integer> spielstand = spielService.spielBeenden(d);
										vokabelView.SpielEnde(spielstand);
										while (!scanner.hasNextInt()) {
											vokabelView.falscheEingabe();
											scanner.next();
										}
										input = scanner.nextInt();
										input--;
									} catch (NotEnoughPlayerException e1) {
										vokabelView.nichtgenugNutzer();
										e1.printStackTrace();
									} 
								} while (input!=-1);
							} while (input!=-1);
							


						}
					} while (input <= 3 && input > 0);
				}
			} while (input <= 1);
		}
	}


	
	/*
	 * Befüllen der DB mit einigen Benutzern
	 */
	private void initialSetUp() {
		try {
			benutzerService.erstelleBenutzerprofil("Danielle");
			benutzerService.erstelleBenutzerprofil("Markus");
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
