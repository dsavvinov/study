package sp;

/**
 * Created by dsavv on 05.04.2016.
 */
public class Track {

    private final String name;
    private final int rating;

    public Track(String name, int rating) {
        this.name = name;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }
}
