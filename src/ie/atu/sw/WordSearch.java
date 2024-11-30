package ie.atu.sw;

import java.util.*;
import java.util.stream.*;

public class WordSearch {
    public List<String> findClosestWordsInClusters(List<Cluster> clusters, String searchWord, int topN) {
        for (Cluster cluster : clusters) {
            Optional<WordEmbedding> match = cluster.getPoints().stream()
                    .filter(e -> e.getWord().equals(searchWord))
                    .findFirst();

            if (match.isPresent()) {
                return findClosestWords(cluster.getPoints(), searchWord, topN);
            }
        }
        System.out.println("Word not found in any cluster.");
        return Collections.emptyList();
    }

    List<String> findClosestWords(List<WordEmbedding> embeddings, String searchWord, int topN) {
        WordEmbedding searchEmbedding = getEmbeddingByWord(embeddings, searchWord);

        if (searchEmbedding == null) {
            return Collections.emptyList();
        }

        List<WordSimilarity> similarities = new ArrayList<>();

        for (WordEmbedding embedding : embeddings) {
            if (!embedding.getWord().equals(searchWord)) {
                double similarity = cosineSimilarity(searchEmbedding.getVector(), embedding.getVector());
                similarities.add(new WordSimilarity(embedding.getWord(), similarity));
            }
        }

        return similarities.stream()
                .sorted((s1, s2) -> Double.compare(s2.getSimilarity(), s1.getSimilarity()))
                .limit(topN)
                .map(WordSimilarity::getWord)
                .collect(Collectors.toList());
    }

    private WordEmbedding getEmbeddingByWord(List<WordEmbedding> embeddings, String word) {
        return embeddings.stream()
                .filter(e -> e.getWord().equals(word))
                .findFirst()
                .orElse(null);
    }

    private double cosineSimilarity(double[] vector1, double[] vector2) {
        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;

        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            magnitude1 += vector1[i] * vector1[i];
            magnitude2 += vector2[i] * vector2[i];
        }

        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);

        if (magnitude1 == 0 || magnitude2 == 0) {
            return 0.0;
        }

        return dotProduct / (magnitude1 * magnitude2);
    }
}
