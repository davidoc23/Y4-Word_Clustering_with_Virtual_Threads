package ie.atu.sw;

import java.util.Arrays;

public class WordEmbedding {
    private String word;
    private double[] vector;

    public WordEmbedding(String word, double[] vector) {
        this.word = word;
        this.vector = vector;
    }

    public String getWord() {
        return word;
    }

    public double[] getVector() {
        return vector;
    }

    @Override
    public String toString() {
        return word + " " + Arrays.toString(vector);
    }
}
