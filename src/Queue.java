

public class Queue {
    private PolynomItem[] list;
    private int size;

    // private constructor restricted to this class itself
    public Queue()
    {
        list = new PolynomItem[11];
        size = 0;
    }

    public synchronized void addInQueue(PolynomItem item) throws InterruptedException {
        while(size == 10)
            wait();

        list[++size] = item;

        notify();
    }

    public synchronized PolynomItem extractFromQueue() throws InterruptedException {
        while(size == 0)
            wait();

        PolynomItem firstElement = list[1];

        for(int i = 1; i < size;i++)
            list[i] = list[i + 1];

        size--;

        notify();
        return firstElement;
    }
}
