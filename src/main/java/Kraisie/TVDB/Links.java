package Kraisie.TVDB;

class Links {

	private int first;
	private int last;
	private int next;
	private int previous;

	Links(int first, int last, int next, int previous) {
		this.first = first;
		this.last = last;
		this.next = next;
		this.previous = previous;
	}

	int getFirst() {
		return first;
	}

	int getLast() {
		return last;
	}

	int getNext() {
		return next;
	}

	int getPrevious() {
		return previous;
	}
}
