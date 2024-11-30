package ie.atu.sw;

import java.util.Map;
import java.util.concurrent.*;

public class VirtualThreadProcessor {
    public void processInParallel(Map<String, double[]> embeddings, double[] searchVector) {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        try {
            embeddings.entrySet().parallelStream().forEach(entry -> {
                executor.submit(() -> {
                    String word = entry.getKey();
                    double[] vector = entry.getValue();
                    double distance = cosineSimilarity(searchVector, vector);
                    System.out.println("Word: " + word + ", Distance: " + distance);
                });
            });
        } finally {
            executor.shutdown();
        }
    }

    private double cosineSimilarity(double[] v1, double[] v2) {
        double dotProduct = 0, normA = 0, normB = 0;
        for (int i = 0; i < v1.length; i++) {
            dotProduct += v1[i] * v2[i];
            normA += Math.pow(v1[i], 2);
            normB += Math.pow(v2[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
