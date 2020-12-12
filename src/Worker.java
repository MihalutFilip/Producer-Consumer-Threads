import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Worker extends Thread {
    private int numberOfPolynomial;
    private int numberOfThreads;
    private CustomLinkedList list;
    private int threadNumber;
    private Queue queue;
    private int nrOfReaderThreads;
    private CyclicBarrier barrier;

    public Worker(int threadNumber, int numberOfPolynomial, int numberOfThreads, CustomLinkedList list, Queue queue, int nrOfReaderThreads, CyclicBarrier barrier) {
        this.numberOfPolynomial = numberOfPolynomial;
        this.numberOfThreads = numberOfThreads;
        this.list = list;
        this.threadNumber = threadNumber;
        this.queue = queue;
        this.nrOfReaderThreads = nrOfReaderThreads;
        this.barrier = barrier;
    }

    public void run() {
        if(nrOfReaderThreads > threadNumber) {
	    // Producer - Read from file and add in queue
            int chunk = numberOfPolynomial / nrOfReaderThreads;
            int start = 1 + threadNumber * chunk;
            int end = nrOfReaderThreads - 1 == threadNumber ? numberOfPolynomial : (threadNumber + 1) * chunk;
            for(int i = start;i <= end;i++) {
                String versionNumber = numberOfPolynomial == 10 ? "1" : "2";
                String fileName = "polinom" + versionNumber + "_" + i + ".txt";
                String line;

                try {
                    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                        while(true)
                        {

                            line = br.readLine();

                            if(line == null)
                                break;
                            String[] numbers = line.split(",");
                            queue.addInQueue(new PolynomItem(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1])));
                        }
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            if(threadNumber == 0) {
                for(int i = 0;i < numberOfThreads - nrOfReaderThreads; i++) {
                    try {
                        queue.addInQueue(null);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


        }
        else {
	    // Cosumer - Extract from queue and add in the linked list
            try {
                while(true) {
                    PolynomItem item = queue.extractFromQueue();
                    if(item == null)
                        break;
                    list.add(item);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
