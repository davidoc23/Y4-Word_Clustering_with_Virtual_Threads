package ie.atu.sw;

import java.util.*;
import java.util.concurrent.*;

public class Clustering {
    private int k; // Number of clusters
    private Map<String, double[]> embeddings; // Word embeddings

    public Clustering(int k, Map<String, double[]> embeddings) {
        this.k = k;
        this.embeddings = embeddings;
    }

    public Map<Integer, List<String>> kMeansClustering() {
        List<double[]> centroids = initializeCentroids();
        Map<Integer, List<String>> clusters = new HashMap<>();

        boolean centroidsChanged;
        do {
            clusters.clear();
            centroidsChanged = assignEmbeddingsToClusters(centroids, clusters);
            updateCentroids(centroids, clusters);
        } while (centroidsChanged);

        return clusters;
    }

    // Initialize k random centroids
    private List<double[]> initializeCentroids() {
        List<double[]> centroids = new ArrayList<>();
        List<double[]> values = new ArrayList<>(embeddings.values());
        Collections.shuffle(values);
        for (int i = 0; i < k; i++) centroids.add(values.get(i));
        return centroids;
    }

    // Assign each embedding to the nearest centroid
    private boolean assignEmbeddingsToClusters(List<double[]> centroids, Map<Integer, List<String>> clusters) {
        boolean changed = false;

        embeddings.forEach((word, vector) -> {
            int closest = findClosestCentroid(vector, centroids);
            clusters.computeIfAbsent(closest, c -> new ArrayList<>()).add(word);
        });

        return changed;
    }

    private int findClosestCentroid(double[] vector, List<double[]> centroids) {
        int closest = -1;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < centroids.size(); i++) {
            double distance = euclideanDistance(vector, centroids.get(i));
            if (distance < minDistance) {
                minDistance = distance;
                closest = i;
            }
        }
        return closest;
    }

    private double euclideanDistance(double[] v1, double[] v2) {
        double sum = 0;
        for (int i = 0; i < v1.length; i++) sum += Math.pow(v1[i] - v2[i], 2);
        return Math.sqrt(sum);
    }

    // Update centroids
    private void updateCentroids(List<double[]> centroids, Map<Integer, List<String>> clusters) {
        for (int i = 0; i < k; i++) {
            List<String> cluster = clusters.get(i);
            if (cluster == null) continue;
            double[] newCentroid = new double[centroids.get(0).length];

            for (String word : cluster) {
                double[] vector = embeddings.get(word);
                for (int j = 0; j < vector.length; j++) newCentroid[j] += vector[j];
            }

            for (int j = 0; j < newCentroid.length; j++) newCentroid[j] /= cluster.size();
            centroids.set(i, newCentroid);
        }
    }
}
