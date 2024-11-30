package ie.atu.sw;

import java.util.*;

public class SearchEngine {
    public List<String> searchClosestWords(String searchTerm, List<List<WordEmbedding>> clusters) {
        List<String> results = new ArrayList<>();
        for (List<WordEmbedding> cluster : clusters) {
            WordEmbedding closest = null;
            double minDistance = Double.MAX_VALUE;

            for (WordEmbedding wordEmbedding : cluster) {
                double distance = calculateDistance(searchTerm, wordEmbedding);
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = wordEmbedding;
                }
            }

            if (closest != null) {
                results.add(closest.getWord());
            }
        }
        return results;
    }

    private double calculateDistance(String searchTerm, WordEmbedding wordEmbedding) {
        // Placeholder: Use a simple cosine or Euclidean distance
        // Implement a simplified placeholder comparison (this could be expanded)
        return Math.random(); // Randomized for example purposes
    }
}
