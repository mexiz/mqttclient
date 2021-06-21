package utils;

import java.util.ArrayList;

@SuppressWarnings("hiding")
public class ArrayListMaxSize<TopicNachrichten> extends ArrayList<TopicNachrichten> {

	/*
	 * 
	 * 
	 * Die Klasse übernimmt die Console. Wenn maxsize erreicht ist löscht sie den
	 * ersten Eintrag und übernimmt den nächsten, sodass ein neuer Platz frei wird.
	 * 
	 * 
	 */
	private static final long serialVersionUID = 6759253532538061232L;
	private static int max;

	@SuppressWarnings("static-access")
	public ArrayListMaxSize(int maxsize) {
		this.max = maxsize;

	}

	@SuppressWarnings("static-access")
	synchronized public boolean add(TopicNachrichten e) {
		//ein Array zum Zwischenspeichern wird erzeugt
		ArrayList<TopicNachrichten> test = new ArrayList<TopicNachrichten>();
		
		//Abfrage ob die Arraylist den maxsize Wert erreicht hat
		if (super.size() == this.max) {
			//Alle Werte (außer der 1. Wert) werden im Zwischenspeicher Arraylist gesetzt
			for (int i = 0; i < max - 1; i++) {
				test.add(this.get(i + 1));
			}
			
			//Sicherheitsabfrage (nicht wichtig)
			if (test.size() == this.size() - 1) {
				//die Arraylist wird cleared
				this.clear();
			} else {
				return true;
			}
			
			// Zurück geschrieben
			for (int i = 0; i < max - 1; i++) {
				this.add(test.get(i));

			}

		}
		//hier wird der neue Wert geadded
		return super.add(e);
	}

}
