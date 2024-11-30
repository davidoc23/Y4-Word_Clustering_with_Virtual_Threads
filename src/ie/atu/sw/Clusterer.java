package ie.atu.sw;

import java.util.*;
import java.util.concurrent.*;

public class Clusterer {
    private static final double EPSILON = 1e-6;

    public List<Cluster> kMeans(List<WordEmbedding> embeddings, int k) {
        Random rand = new Random();
        List<WordEmbedding> centroids = new ArrayList<>();
        List<Cluster> clusters = new ArrayList<>();

        // Initialize centroids randomly
        for (int i = 0; i < k; i++) {
            centroids.add(embeddings.get(rand.nextInt(embeddings.size())));
            clusters.add(new Cluster(centroids.get(i), new ArrayList<>()));
        }

        boolean converged;
        do {
            converged = true;

            // Clear the points in each cluster for reassignment
            clusters.forEach(cluster -> cluster.getPoints().clear());

            // Assign each embedding to the nearest centroid using virtual threads
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (WordEmbedding embedding : embeddings) {
                    executor.submit(() -> {
                        int nearestCentroid = findNearestCentroid(embedding, centroids);
                        synchronized (clusters.get(nearestCentroid)) {
                            clusters.get(nearestCentroid).getPoints().add(embedding);
                        }
                    });
                }
            }

            // Recalculate centroids
            for (int i = 0; i < k; i++) {
                WordEmbedding newCentroid = calculateCentroid(clusters.get(i).getPoints());
                if (!isConverged(centroids.get(i), newCentroid)) {
                    converged = false;
                }
                centroids.set(i, newCentroid);
                clusters.get(i).setCentroid(newCentroid);
            }
        } while (!converged);

        return clusters;
    }

    private int findNearestCentroid(WordEmbedding embedding, List<WordEmbedding> centroids) {
        double minDistance = Double.MAX_VALUE;
        int nearestCentroid = 0;
        for (int i = 0; i < centroids.size(); i++) {
            double distance = calculateDistance(embedding.getVector(), centroids.get(i).getVector());
            if (distance < minDistance) {
                minDistance = distance;
                nearestCentroid = i;
            }
        }
        return nearestCentroid;
    }

    private double calculateDistance(double[] vector1, double[] vector2) {
        double sum = 0;
        for (int i = 0; i < vector1.length; i++) {
            sum += Math.pow(vector1[i] - vector2[i], 2);
        }
        return Math.sqrt(sum);
    }

    private WordEmbedding calculateCentroid(List<WordEmbedding> cluster) {
        int vectorLength = cluster.get(0).getVector().length;
        double[] centroidVector = new double[vectorLength];

        for (WordEmbedding embedding : cluster) {
            for (int i = 0; i < vectorLength; i++) {
                centroidVector[i] += embedding.getVector()[i];
            }
        }

        for (int i = 0; i < vectorLength; i++) {
            centroidVector[i] /= cluster.size();
        }

        return new WordEmbedding("centroid", centroidVector);
    }

    private boolean isConverged(WordEmbedding oldCentroid, WordEmbedding newCentroid) {
        return calculateDistance(oldCentroid.getVector(), newCentroid.getVector()) < EPSILON;
    }
}
