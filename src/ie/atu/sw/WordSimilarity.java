package ie.atu.sw;

public class WordSimilarity {
    private String word;
    private double similarity;

    public WordSimilarity(String word, double similarity) {
        this.word = word;
        this.similarity = similarity;
    }

    public String getWord() {
        return word;
    }

    public double getSimilarity() {
        return similarity;
    }
}

