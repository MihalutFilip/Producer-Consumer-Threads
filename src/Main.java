import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class Main {
    public static void main(String[] args)
            throws InterruptedException, IOException {
        int nrOfPolynomial = Integer.parseInt(args[0]);

        long startTime = System.nanoTime();

        if(args.length == 1)
            secventialMethod(nrOfPolynomial);
        else
            paralelMethod(nrOfPolynomial, Integer.parseInt(args[1]), Integer.parseInt(args[2]));

        long endTime = System.nanoTime();
        System.out.println((double) (endTime - startTime) / 1E6);
    }

    public static void secventialMethod(int nrOfPolynomial) throws IOException {
        CustomLinkedList list = new CustomLinkedList();

        for(int i = 1;i <= nrOfPolynomial;i++) {
            String versionNumber = nrOfPolynomial == 10 ? "1" : "2";
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
                        list.add(new PolynomItem(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1])));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        writeToFile(list.getHead());
    }

    public static void paralelMethod(int nrOfPolynomial, int nrThreads, int nrOfReaderThreads) throws InterruptedException, IOException {
        Worker[] threads = new Worker[nrThreads];
        CustomLinkedList list = new CustomLinkedList();
        Queue queue = new Queue();
        CyclicBarrier barrier = new CyclicBarrier(nrOfReaderThreads);

        for(int i = 0;i < nrThreads;i++) {
            threads[i] = new Worker(i, nrOfPolynomial, nrThreads, list, queue, nrOfReaderThreads, barrier);
            threads[i].start();
        }

        for(int i = 0;i < nrThreads;i++)
            threads[i].join();

        writeToFile(list.getHead());
    }

    public static void generateNumbers() {
        int maximItems = 100, maximGradAndCoefficient = 10000;
        Random rand = new Random();
        for(int i = 0;i < maximItems;i++) {
            int grad = rand.nextInt(maximGradAndCoefficient);
            int coefficient = rand.nextInt(maximGradAndCoefficient);
            System.out.println(grad + "," + coefficient);
        }
    }

    public static void writeToFile(Node head) throws IOException {
        FileWriter myWriter = new FileWriter("results.txt");
        while(head.getNext() != null) {
            if(head.getValue() != null && head.getValue().getCoefficient() != 0)
            {
                String output = head.getValue().getRank() + "," + head.getValue().getCoefficient() + '\n';
                myWriter.write(output);
            }

            head = head.getNext();
        }
        myWriter.close();
    }
} 