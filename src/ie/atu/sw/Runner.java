package ie.atu.sw;

import java.util.*;

public class Runner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<WordEmbedding> embeddings = null;
        WordSearch wordSearch = new WordSearch();

        boolean running = true;
        
        while (running) {
            // Display the main menu
            System.out.println("\n--- Word Embedding Search Menu ---");
            System.out.println("1. Load Embeddings");
            System.out.println("2. Set Number of Clusters");
            System.out.println("3. Search for a Word");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    // Input and load the embeddings
                    System.out.print("Enter the path to embeddings.txt: ");
                    String filePath = scanner.nextLine().trim();
                    embeddings = DataLoader.loadEmbeddings(filePath);
                    
                    if (embeddings != null && !embeddings.isEmpty()) {
                        System.out.println("Embeddings loaded successfully.");
                    } else {
                        System.out.println("Failed to load embeddings. Please check the file path.");
                    }
                    break;
                    
                case "2":
                    // Input the number of clusters
                    if (embeddings == null) {
                        System.out.println("Please load embeddings first.");
                        break;
                    }
                    
                    System.out.print("Enter the number of clusters: ");
                    try {
                        int numClusters = Integer.parseInt(scanner.nextLine().trim());
                        Clusterer clusterer = new Clusterer();
                        clusterer.kMeans(embeddings, numClusters); // Perform clustering without storing the result
                        System.out.println("Clustering completed with " + numClusters + " clusters.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format. Please enter a valid integer.");
                    }
                    break;
                    
                case "3":
                    // Search for a word
                    if (embeddings == null) {
                        System.out.println("Please load embeddings first.");
                        break;
                    }
                    
                    System.out.print("Enter a word to search for: ");
                    String searchWord = scanner.nextLine().trim();
                    List<String> closestWords = wordSearch.findClosestWords(embeddings, searchWord, 5);
                    
                    if (closestWords.isEmpty()) {
                        System.out.println("Word not found in embeddings.");
                    } else {
                        System.out.println("Closest words:");
                        closestWords.forEach(System.out::println);
                    }
                    break;
                    
                case "4":
                    // Exit the program
                    running = false;
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                    
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
        
        scanner.close();
    }
}
