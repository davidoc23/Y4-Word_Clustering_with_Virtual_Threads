package ie.atu.sw;

import java.util.List;

public class Cluster {
    private WordEmbedding centroid;
    private List<WordEmbedding> points;

    public Cluster(WordEmbedding centroid, List<WordEmbedding> points) {
        this.centroid = centroid;
        this.points = points;
    }

    public WordEmbedding getCentroid() {
        return centroid;
    }

    public List<WordEmbedding> getPoints() {
        return points;
    }

    public void setCentroid(WordEmbedding centroid) {
        this.centroid = centroid;
    }

    public void setPoints(List<WordEmbedding> points) {
        this.points = points;
    }
    
    
}
