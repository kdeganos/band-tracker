import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Venue {

  private int id;
  private String name;

  public Venue(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public int getId() {
    return this.id;
  }

  public static List<Venue> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM venues;";
      return con.createQuery(sql)
      .executeAndFetch(Venue.class);
    }
  }

  @Override
  public boolean equals(Object otherVenue) {
    if(!(otherVenue instanceof Venue)) {
      return false;
    } else {
      Venue newVenue = (Venue) otherVenue;
      return newVenue.getName().equals(this.name) &&
        newVenue.getId() == this.id;
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO venues (name) VALUES (:name);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Venue find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM venues WHERE id=:id;";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Venue.class);
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteVenue = "DELETE FROM venues WHERE id=:id;";
      con.createQuery(deleteVenue)
        .addParameter("id", id)
        .executeUpdate();

      String deleteJoin = "DELETE FROM bands_venues WHERE id=:id;";
      con.createQuery(deleteJoin)
        .addParameter("id", id)
        .executeUpdate();
    }

  }

  public void update(String new_name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE venues SET name=:new_name WHERE id=:id;";
      con.createQuery(sql)
        .addParameter("new_name", new_name)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void addBand(Band newBand) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO bands_venues (band_id, venue_id) VALUES (:band_id, :venue_id);";
      con.createQuery(sql)
        .addParameter("venue_id", this.id)
        .addParameter("band_id", newBand.getId())
        .executeUpdate();
    }
  }

  public List<Band> getBands() {
    try(Connection con = DB.sql2o.open()) {
      String joinQuery = "SELECT bands.* FROM venues JOIN bands_venues ON (venues.id = bands_venues.venue_id) JOIN bands ON (bands_venues.band_id = bands.id) WHERE venues.id = :id;";
      return con.createQuery(joinQuery)
        .addParameter("id" , this.id)
        .executeAndFetch(Band.class);
    }
  }

  public void removeBand(int bandId) {
    try(Connection con = DB.sql2o.open()) {
      String removeQuery = "DELETE FROM bands_venues WHERE venue_id=:venue_id AND band_id=:band_id;";
      con.createQuery(removeQuery)
        .addParameter("band_id", bandId)
        .addParameter("venue_id", this.id)
        .executeUpdate();
    }
  }

  public static List<Venue> search(String searchQuery) {
    try(Connection con = DB.sql2o.open()) {
      String search = "SELECT * FROM venues WHERE lower(name) LIKE :searchQuery;";
      return con.createQuery(search)
        .addParameter("searchQuery", "%" + searchQuery.toLowerCase() + "%")
        .executeAndFetch(Venue.class);
    }
  }
}
