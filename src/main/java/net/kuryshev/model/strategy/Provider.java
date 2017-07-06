package net.kuryshev.model.strategy;

import net.kuryshev.model.entity.Vacancy;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static net.kuryshev.Utils.ClassUtils.getClassName;

public class Provider {
    private int numThreads = 10;
    private static Logger logger = Logger.getLogger(getClassName());

    private Strategy strategy;

    public Provider(Strategy strategy, int numThreads) {
        this.numThreads = numThreads;
        this.strategy = strategy;
    }

    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancies = new CopyOnWriteArrayList<>();
        Thread[] threads = new Thread[numThreads];
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < numThreads; i++) {
            int initialPage = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Vacancy> result = strategy.getVacancies(searchString, initialPage, numThreads);
                    vacancies.addAll(result);
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                logger.warn("Exception during multithread parsing", e);
            }
        }
        long finishTime = System.currentTimeMillis();
        logger.info(vacancies.size() + " vacancies parsed with " + numThreads + " threads in " + (finishTime - startTime) + " ms");
        return vacancies;
    }

    public Strategy getStrategy() {
        return strategy;
    }
}
