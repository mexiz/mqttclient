package vorlagen;

import java.util.ArrayList;

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

	public ArrayListMaxSize(int maxsize) {
		this.max = maxsize;

	}

	synchronized public boolean add(TopicNachrichten e) {
		ArrayList<TopicNachrichten> test = new ArrayList<TopicNachrichten>();
		if (super.size() == this.max) {
			for (int i = 0; i < max - 1; i++) {

				test.add(this.get(i + 1));
//				System.out.println("Testsize " + test.size() + " Arrraysize " + this.size()) ;

			}

			if (test.size() == this.size() - 1) {
//				System.out.println("ARRAY: " + test.size() + "  =  " + (this.size()-1) );
				this.clear();
			} else {

//				System.out.println("ArrayListProjekt fail 1");
				return true;
			}

			for (int i = 0; i < max - 1; i++) {

				this.add(test.get(i));
//				System.out.println("nach forschleife size " + this.size());

			}

		}
		return super.add(e);
	}

}
