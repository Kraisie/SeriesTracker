package Data.TVDB;

public class Links {

    private int first;
    private int last;
    private int next;
    private int previous;

    public Links(int first, int last, int next, int previous) {
        this.first = first;
        this.last = last;
        this.next = next;
        this.previous = previous;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getPrevious() {
        return previous;
    }

    public void setPrevious(int previous) {
        this.previous = previous;
    }
}
