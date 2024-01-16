package de.htwberlin.vokabel_manager.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.naming.InvalidNameException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import de.htwberlin.vokabel_manager.api.VokabelService;
import de.htwberlin.vokabel_manager.api.domain.Bedeutungen;
import de.htwberlin.vokabel_manager.api.domain.Datei;
import de.htwberlin.vokabel_manager.api.domain.Kategorie;
import de.htwberlin.vokabel_manager.api.domain.Quizfrage;
import de.htwberlin.vokabel_manager.api.domain.Vokabel;

@Service
public class VokabelServiceImpl implements VokabelService {

	@Override
	public Datei ladeDatei(String name) throws InvalidNameException {
		Datei e = DateiDAO.getDateiDao().getDateiByName(name);
		if (e != null) {
			return e;
		} else {
			throw new InvalidNameException("Einheit mit dem Namen " + name + " kann nicht geladen werden");
		}
	}

	@Override
	public boolean loescheDatei(Datei datei) throws IOException, SQLException {
		DateiDAO.getDateiDao().deleteEinheit(datei);
		return DateiDAO.getDateiDao().getById(datei.getId()) == null;
	}

	@Override
	public List<Kategorie> ladeKategorien() {
		return KategorieDAO.getKategorieDao().getAll();
	}

	@Override
	public Kategorie ladeKategorie(String name) throws InvalidNameException {

		Kategorie k = KategorieDAO.getKategorieDao().getKategorieByName(name);
		if (k != null) {
			return k;
		} else {
			throw new InvalidNameException("Kategorie mit dem Namen " + name + " kann nicht geladen werden");
		}

	}

	@Override
	public List<Quizfrage> generiereFragen(Datei datei, int anzahl) {
		if (datei == null || anzahl <= 0) {
			throw new IllegalArgumentException("Ein Parameter hat einen ungültigen Wert");
		}
		List<Quizfrage> fragen = new ArrayList<Quizfrage>(anzahl);
		List<Vokabel> vokabeln = datei.getVokablen();
		int max = vokabeln.size() - 1;
		List<Integer> index = generateUniqueRandomNumbers(0, max, anzahl, -1);
		for (int i = 0; i < anzahl; i++) {
			vokabeln.get(index.get(i));
			String frage = vokabeln.get(index.get(i)).getBedeutungen_fremdsprache().toString();
			String richtigeAntwort = vokabeln.get(index.get(i)).getBedeutungen_uebersetzungssprache().toString();
			List<String> falscheAntworten = new ArrayList<String>();
			List<Integer> indexFalscheAntworten = generateUniqueRandomNumbers(0, max, 3, index.get(i));
			for (Integer in : indexFalscheAntworten) {
				falscheAntworten.add(vokabeln.get(in).getBedeutungen_uebersetzungssprache().toString());
			}
			fragen.add(new Quizfrage(frage, richtigeAntwort, falscheAntworten));
		}
		return fragen;
	}

	public List<Integer> generateUniqueRandomNumbers(int min, int max, int count, int excludedNumber) {
		if (max - min + 1 < count) {
			throw new IllegalArgumentException("Der Bereich ist kleiner als die angeforderte Anzahl von Zahlen.");
		}

		List<Integer> randomNumbers = new ArrayList<>();
		Random random = new Random();

		while (randomNumbers.size() < count) {
			int randomNumber = random.nextInt(max - min + 1) + min;

			if (randomNumber != excludedNumber && !randomNumbers.contains(randomNumber)) {
				randomNumbers.add(randomNumber);
			}
		}

		return randomNumbers;
	}

	@Override
	public boolean improtiereCSVDateien() throws IOException, SQLException {

		try {
			String resourcesFolderPath = getClass().getClassLoader().getResource("").getPath();
			resourcesFolderPath = getClass().getClassLoader().getResource("").getPath();
			Stream<File> s = Arrays.stream(new File(resourcesFolderPath).listFiles());
			s.filter(f -> FilenameUtils.getExtension(f.getPath()).equals("txt")).forEach(f -> {
				try {

					if (improtiereCSVDatei(f.getAbsolutePath())) {
						// System.out.println("Import erfolgreich von Datei: "+ f.getAbsolutePath());
					} else {
						// System.out.println("Import fehlgeschlagen von Datei: "+ f.getAbsolutePath());
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean improtiereCSVDatei(String datei) throws IOException, SQLException {
		try (BufferedReader reader = new BufferedReader(new FileReader(datei))) {
			try {
				String line;
				Datei e = null;
				while ((line = reader.readLine()) != null) {
					if (!line.contains(":")) {
						// erste Zeile

						// Kategorie anlegen
						List<String> words = new ArrayList<>();
						Pattern pattern = Pattern.compile("\\{\\{\\{.*?\\}\\}\\}");
						Matcher matcher = pattern.matcher(line);
						while (matcher.find()) {
							String word = matcher.group().replaceAll("\\{|\\}", "").trim();
							words.add(word);
						}
						Kategorie k = new Kategorie();
						k.setName(words.get(3));
						if (!KategorieDAO.getKategorieDao().getEntityTransaction().isActive()) {
							KategorieDAO.getKategorieDao().getEntityTransaction().begin();
						}
						Kategorie k2 = KategorieDAO.getKategorieDao().getKategorieByName(k.getName());
						if (k2 == null) {
							KategorieDAO.getKategorieDao().saveKategorie(k);
						} else {
							k = k2;
						}
						KategorieDAO.getKategorieDao().getEntityTransaction().commit();

						// Einheit anlegen
						e = new Datei();
						e.setKategorie(k);
						e.setName(datei);
						e.setAnfangsSprache(words.get(1));
						e.setZielSprache(words.get(2));

						if (!DateiDAO.getDateiDao().getEntityTransaction().isActive()) {
							DateiDAO.getDateiDao().getEntityTransaction().begin();
						}
						Datei e2 = DateiDAO.getDateiDao().getDateiByName(e.getName());
						if (e2 == null) {
							DateiDAO.getDateiDao().saveEinheit(e);
						} else {
							e = e2;
						}
						DateiDAO.getDateiDao().getEntityTransaction().commit();

					} else {
						String[] data = line.split(":");
						if (data.length == 2) {
							// Vokabel anlegen

							Vokabel v = new Vokabel();
							v.setDatei(e);
							List<String> fremdsprache = new ArrayList<String>();
							List<Bedeutungen> uebersetzungen = new ArrayList<Bedeutungen>();

							Matcher matcher = Pattern.compile("\\{(.*?)\\}").matcher(data[0]);
							while (matcher.find()) {
								// Bedeutung fremdsprache anlegen

								fremdsprache.add(matcher.group(1));
							}
							v.setBedeutungen_fremdsprache(fremdsprache);

							// Hinweis zur Übersetzung entfernen
							if (data[1].contains("{{") && data[1].contains("}}")) {
								data[1] = data[1].substring(1, data[1].indexOf("{{"));
							}

							if (!data[1].contains(",")) {
								// Bedeutung Übersetzungssprache anlegen
								String uebersetzung = data[1].trim();
								uebersetzung = uebersetzung.substring(1, uebersetzung.length() - 1);
								Bedeutungen b = new Bedeutungen();
								b.addSynonym(uebersetzung);
								uebersetzungen.add(b);
								v.setBedeutungen_uebersetzungssprache(uebersetzungen);

							} else {

								Pattern pattern = Pattern.compile("\\{(.*?)\\}");
								matcher = pattern.matcher(data[1]);

								while (matcher.find()) {
									String group = matcher.group(1);
									String[] elements = group.split(",\\s*");

									Bedeutungen b = new Bedeutungen();

									for (String element : elements) {
										b.addSynonym(element);
									}
									uebersetzungen.add(b);
									v.setBedeutungen_uebersetzungssprache(uebersetzungen);

								}
							}

							if (!VokabelDAO.getVokabelDao().getEntityTransaction().isActive()) {
								VokabelDAO.getVokabelDao().getEntityTransaction().begin();
							}
							VokabelDAO.getVokabelDao().saveVokabel(v);
							VokabelDAO.getVokabelDao().getEntityTransaction().commit();
							e.getVokablen().add(v);
						}

					}
					if (!DateiDAO.getDateiDao().getEntityTransaction().isActive()) {
						DateiDAO.getDateiDao().getEntityTransaction().begin();
					}
					DateiDAO.getDateiDao().saveEinheit(e);
					DateiDAO.getDateiDao().getEntityTransaction().commit();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	@Override
	public void executeSQLFile(String file, String jdbcUrl, String username, String password)
			throws SQLException, IOException {
		// Verbindung zur Datenbank herstellen
		Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
		Statement statement = connection.createStatement();

		// SQL-Datei einlesen
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		StringBuilder sqlStatements = new StringBuilder();

		// SQL-Anweisungen aus der Datei lesen und ausführen
		while ((line = reader.readLine()) != null) {
			// Leere Zeilen und Kommentare überspringen
			if (line.trim().isEmpty() || line.trim().startsWith("--")) {
				continue;
			}

			// SQL-Anweisung zur Sammlung hinzufügen
			sqlStatements.append(line);

			// Prüfen, ob die Anweisung abgeschlossen ist
			if (line.trim().endsWith(";")) {
				// SQL-Anweisung ausführen
				String sql = sqlStatements.toString();
				statement.execute(sql);

				// SQL-Anweisung zurücksetzen
				sqlStatements.setLength(0);
			}
		}

		// Ressourcen freigeben
		reader.close();
		statement.close();
		connection.close();

	}

	@Override
	public List<Datei> ladeDateiByKategorie(Kategorie k) {
		DateiDAO dao = DateiDAO.getDateiDao();
		return dao.getByKategorie(k);
	}

}
