package hadeel.com.popularmovies1;

import java.io.Serializable;

public class Movie implements Serializable {

    private String title;
    private String poster;
    private String releaseDate;
    private double voteAverage;
    private String plotSynopsis;
    private String review;
    private String id;

    public Movie(String title, String poster, String releaseDate, double voteAverage, String plotSynopsis, String id) {
        this.title = title;
        this.poster = poster;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        String url = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/";
        return url + poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }
}
