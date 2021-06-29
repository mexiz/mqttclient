package utils;

import java.util.ArrayList;

@SuppressWarnings("hiding")
public class ArrayListMaxSize<Object> extends ArrayList<Object> {

	private static final long serialVersionUID = 6759253532538061232L;
	private static int max;

	@SuppressWarnings("static-access")
	public ArrayListMaxSize(int maxsize) {
		this.max = maxsize;

	}

	@SuppressWarnings("static-access")
	synchronized public boolean add(Object e) {
		ArrayList<Object> test = new ArrayList<Object>();
		
		if (super.size() == this.max) {
			for (int i = 0; i < max - 1; i++) {
				test.add(this.get(i + 1));
			}
			
			if (test.size() == this.size() - 1) {
				this.clear();
			} else {
				return true;
			}
			
			for (int i = 0; i < max - 1; i++) {
				this.add(test.get(i));

			}

		}
		return super.add(e);
	}

}
