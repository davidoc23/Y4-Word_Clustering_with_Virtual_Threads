package ie.atu.sw;

import java.io.*;
import java.util.*;

public class DataLoader {
    public static List<WordEmbedding> loadEmbeddings(String filePath) {
        List<WordEmbedding> embeddings = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                String word = tokens[0].trim();
                double[] vector = new double[tokens.length - 1];

                for (int i = 1; i < tokens.length; i++) {
                    try {
                        vector[i - 1] = Double.parseDouble(tokens[i].trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid value: " + tokens[i]);
                    }
                }

                embeddings.add(new WordEmbedding(word, vector));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return embeddings;
    }
}
