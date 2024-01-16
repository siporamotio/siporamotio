package de.htwberlin.vokabel_manager.impl;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InvalidNameException;

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

import de.htwberlin.vokabel_manager.api.domain.Bedeutungen;
import de.htwberlin.vokabel_manager.api.domain.Datei;
import de.htwberlin.vokabel_manager.api.domain.Kategorie;
import de.htwberlin.vokabel_manager.api.domain.Quizfrage;
import de.htwberlin.vokabel_manager.api.domain.Vokabel;
import jakarta.persistence.EntityTransaction;
/**
 * Unit test fuer Vokabel_Manager.
 */
@ExtendWith(MockitoExtension.class)
public class VokabelServiceTest {
	

	@InjectMocks
	private static VokabelServiceImpl service;
	@Mock
	private static DateiDAO eDao;
	private static MockedStatic<DateiDAO> eDAOstatic;
	@Mock
	private KategorieDAO kDao;
	private static MockedStatic<KategorieDAO> kDAOstatic;
	@Mock
	private VokabelDAO vDao;
	private static MockedStatic<VokabelDAO> vDAOstatic;
	@Mock
	private EntityTransaction entityTransaction;
	private String EinheitValidName = "body";
	private String KategorieValidName = "access 3";
	

	@BeforeAll
	public static void initialize() {
		// wird einmalig vor allen Testfällen ausgeführt
		eDAOstatic = Mockito.mockStatic(DateiDAO.class);
		kDAOstatic = Mockito.mockStatic(KategorieDAO.class);
		vDAOstatic = Mockito.mockStatic(VokabelDAO.class);
		
	}

	@BeforeEach
	public void setUp() {
		// wird vor jedem Testfall ausgeführt
		eDAOstatic.when(() -> DateiDAO.getDateiDao()).thenReturn(eDao);
		Mockito.lenient().when(eDao.getEntityTransaction()).thenReturn(entityTransaction);
        Mockito.lenient().doNothing().when(entityTransaction).begin();
        Mockito.lenient().doNothing().when(entityTransaction).commit();
        
        kDAOstatic.when(() -> KategorieDAO.getKategorieDao()).thenReturn(kDao);
		Mockito.lenient().when(kDao.getEntityTransaction()).thenReturn(entityTransaction);
        Mockito.lenient().doNothing().when(entityTransaction).begin();
        Mockito.lenient().doNothing().when(entityTransaction).commit();
        
        vDAOstatic.when(() -> VokabelDAO.getVokabelDao()).thenReturn(vDao);
		Mockito.lenient().when(vDao.getEntityTransaction()).thenReturn(entityTransaction);
        Mockito.lenient().doNothing().when(entityTransaction).begin();
        Mockito.lenient().doNothing().when(entityTransaction).commit();

	}
	
    @Test
    @DisplayName("Test für improtiereCSVDatein")
    void testImportiereCSVDatein() throws SQLException, IOException, InvalidNameException {
    	Mockito.lenient().doNothing().when(eDao).saveEinheit(null);
    	Mockito.lenient().doReturn(null).when(eDao).getDateiByName(null);
    	Mockito.lenient().doNothing().when(kDao).saveKategorie(null);
    	Mockito.lenient().doReturn(null).when(kDao).getKategorieByName(null);
    	Mockito.lenient().doNothing().when(vDao).saveVokabel(null);
    	service.improtiereCSVDateien();
    }
    
    @Test
    @DisplayName("Test für improtiereCSVDatei")
    void testImportiereCSVDatei() throws SQLException, IOException, InvalidNameException {
    	String txtFile = "src/main/resources/body.txt";
    	Mockito.lenient().doNothing().when(eDao).saveEinheit(null);
    	Mockito.lenient().doReturn(null).when(eDao).getDateiByName(null);
    	Mockito.lenient().doNothing().when(kDao).saveKategorie(null);
    	Mockito.lenient().doReturn(null).when(kDao).getKategorieByName(null);
    	Mockito.lenient().doNothing().when(vDao).saveVokabel(null);
    	assertTrue(service.improtiereCSVDatei(txtFile));
    }
    
    @Test
    @DisplayName("Test für ladeEinheit() mit gültigem Namen")
    void testLadeEinheitWithValidName() throws InvalidNameException {
    	Datei e = new Datei();
    	e.setName(EinheitValidName);
    	Mockito.doReturn(e).when(eDao).getDateiByName(EinheitValidName);
    	Datei einheit = service.ladeDatei(EinheitValidName);
        assertNotNull(einheit);
        assertEquals(EinheitValidName, einheit.getName());
    }
    
    @Test
    @DisplayName("Test für ladeEinheit() mit ungültigem Namen")
    void testLadeEinheitWithInvalidName() throws InvalidNameException {
    	Mockito.doReturn(null).when(eDao).getDateiByName("Ungültiger Name");
        assertThrows(InvalidNameException.class, () -> {
            service.ladeDatei("Ungültiger Name");
        });
    }
    
    @Test
    @DisplayName("Test für ladeKategorien()")
    void testLadeKategorien() {
    	List<Kategorie> kategorien1 = new ArrayList<Kategorie>();
    	kategorien1.add(new Kategorie());
    	Mockito.doReturn(kategorien1).when(kDao).getAll();
    
        List<Kategorie> kategorien2 = service.ladeKategorien();
        assertNotNull(kategorien2);
        assertTrue(kategorien2.size() > 0);
    }
    
    @Test
    @DisplayName("Test für ladeKategorie() mit ungültigem Namen")
    void testLadeKategorieWithInvalidName() throws InvalidNameException {
    	Mockito.doReturn(null).when(kDao).getKategorieByName("Ungültiger Name");
        assertThrows(InvalidNameException.class, () -> {
            service.ladeKategorie("Ungültiger Name");
        });
    }
    
    @Test
    @DisplayName("Test für ladeKategorie() mit gültigem Namen")
    void testLadeKategorie() throws InvalidNameException {
    	Kategorie k = new Kategorie();
    	k.setName(KategorieValidName);
    	Mockito.doReturn(k).when(kDao).getKategorieByName(KategorieValidName);
    	
        Kategorie kategorie = service.ladeKategorie(KategorieValidName);
        assertNotNull(kategorie);
        assertEquals(kategorie.getName(),KategorieValidName);       
    }
    
    @Test
    @DisplayName("Test für generiereFragen()")
    void testGeneriereFragen() throws InvalidNameException {
    	
    	Datei e = new Datei();
    	e.setName(EinheitValidName);
    	List<Vokabel> vl = new ArrayList<Vokabel>();
    	Vokabel v = new Vokabel();
    	List<String> bf = new ArrayList<String>();
    	bf.add("word");
    	v.setBedeutungen_fremdsprache(bf);
    	List<Bedeutungen> ub = new ArrayList<Bedeutungen>();
    	Bedeutungen b = new Bedeutungen();
    	b.addSynonym("Synonym1");
    	b.addSynonym("Synonym2");
    	Bedeutungen b2 = new Bedeutungen();
    	b2.addSynonym("Synonym3");
    	b2.addSynonym("Synonym4");
    	ub.add(b);
    	ub.add(b2);
    	v.setBedeutungen_uebersetzungssprache(ub);
    	vl.add(v);
    	
    	v = new Vokabel();
        bf = new ArrayList<String>();
    	bf.add("word2");
    	v.setBedeutungen_fremdsprache(bf);
    	ub = new ArrayList<Bedeutungen>();
    	b = new Bedeutungen();
    	b.addSynonym("Synonym5");
    	b.addSynonym("Synonym6");
    	b2 = new Bedeutungen();
    	b2.addSynonym("Synonym7");
    	b2.addSynonym("Synonym8");
    	ub.add(b);
    	ub.add(b2);
    	v.setBedeutungen_uebersetzungssprache(ub);
    	vl.add(v);
    	
    	v = new Vokabel();
        bf = new ArrayList<String>();
    	bf.add("word3");
    	v.setBedeutungen_fremdsprache(bf);
    	ub = new ArrayList<Bedeutungen>();
    	b = new Bedeutungen();
    	b.addSynonym("Synonym9");
    	b.addSynonym("Synonym10");
    	b2 = new Bedeutungen();
    	b2.addSynonym("Synonym11");
    	b2.addSynonym("Synonym12");
    	ub.add(b);
    	ub.add(b2);
    	v.setBedeutungen_uebersetzungssprache(ub);
    	vl.add(v);
    	
    	v = new Vokabel();
        bf = new ArrayList<String>();
    	bf.add("word4");
    	v.setBedeutungen_fremdsprache(bf);
    	ub = new ArrayList<Bedeutungen>();
    	b = new Bedeutungen();
    	b.addSynonym("Synonym13");
    	b.addSynonym("Synonym14");
    	b2 = new Bedeutungen();
    	b2.addSynonym("Synonym15");
    	b2.addSynonym("Synonym16");
    	ub.add(b);
    	ub.add(b2);
    	v.setBedeutungen_uebersetzungssprache(ub);
    	vl.add(v);
    	
    	e.setVokablen(vl);
    	Mockito.doReturn(e).when(eDao).getDateiByName(EinheitValidName);
    	
        Datei einheit = service.ladeDatei(EinheitValidName);
        List<Quizfrage> fragen = service.generiereFragen(einheit, 1);
        assertNotNull(fragen);
        assertEquals(1, fragen.size());      
    }
    
    @Test
    @DisplayName("Test für generiereFragen() mit ungültiger Anzahl")
    void testGeneriereFragenInvalidCount() throws InvalidNameException {
    	Datei e = new Datei();
    	e.setName(EinheitValidName);
    	Mockito.doReturn(e).when(eDao).getDateiByName(EinheitValidName);
    	
        Datei einheit = service.ladeDatei(EinheitValidName);
        assertThrows(IllegalArgumentException.class, () -> {
        	service.generiereFragen(einheit, -1);
        });
    }


	
}
