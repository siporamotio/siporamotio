package de.htwberlin.vokabel_manager.api.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "BEDEUTUNG_IN_UEBERSETZUNGSSPRACHE")

public class Bedeutungen {
	
	@Id
	@Column(name = "BEDEUTUNG_IN_UEBERSETZUNGSSPRACHE_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@ElementCollection
	private List<String> Synonyme;

	public Bedeutungen() {

	}

	public Bedeutungen(List<String> synonyme) {
		super();
		Synonyme = synonyme;
	}

	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<String> getSynonyme() {
		return Synonyme;
	}

	public void addSynonym(String s) {
        // Überprüft, ob die Liste null ist, und initialisiert sie gegebenenfalls
        if (Synonyme == null) {
            Synonyme = new ArrayList<>();
        }

        Synonyme.add(s);
    }

	public void setSynonyme(List<String> synonyme) {
		Synonyme = synonyme;
	}

	@Override
	public String toString() {
		if (Synonyme.size() > 1) {
			StringBuilder sb = new StringBuilder();
			for (String synonym : Synonyme) {
				sb.append(synonym).append(", ");
			}
			sb.setLength(sb.length() - 2); // Entferne das letzte ", "
			return sb.toString();
		} else if (Synonyme.size() == 1) {
			return Synonyme.get(0).toString();
		} else {
			return "";
		}
//			String s = "";
//			if(Synonyme.size()>1) {
//				for(String sy : Synonyme) {
//					s += sy.toString() +", ";
//				}
//				return s;
//			}else{
//				return Synonyme.get(0).toString();
//			}
	}

}
