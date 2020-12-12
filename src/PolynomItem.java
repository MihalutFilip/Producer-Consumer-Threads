public class PolynomItem {
    private int rank;
    private int coefficient;

    public PolynomItem(int rank, int coefficient) {
        this.rank = rank;
        this.coefficient = coefficient;
    }

    public synchronized int getRank() {
        return rank;
    }

    public synchronized int getCoefficient() {
        return coefficient;
    }

    public synchronized void sumCoefficient(int coefficient) {
        this.coefficient += coefficient;
    }
}
