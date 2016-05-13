import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Band {

  private int id;
  private String name;

  public Band(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public int getId() {
    return this.id;
  }

  public static List<Band> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM bands;";
      return con.createQuery(sql)
      .executeAndFetch(Band.class);
    }
  }

  @Override
  public boolean equals(Object otherBand) {
    if(!(otherBand instanceof Band)) {
      return false;
    } else {
      Band newBand = (Band) otherBand;
      return newBand.getName().equals(this.name) &&
        newBand.getId() == this.id;
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO bands (name) VALUES (:name);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Band find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM bands WHERE id=:id;";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Band.class);
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteBand = "DELETE FROM bands WHERE id=:id;";
      con.createQuery(deleteBand)
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
      String sql = "UPDATE bands SET name=:new_name WHERE id=:id;";
      con.createQuery(sql)
        .addParameter("new_name", new_name)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void addVenue(Venue newVenue) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO bands_venues (band_id, venue_id) VALUES (:band_id, :venue_id);";
      con.createQuery(sql)
        .addParameter("band_id", this.id)
        .addParameter("venue_id", newVenue.getId())
        .executeUpdate();
    }
  }

  public List<Venue> getVenues() {
    try(Connection con = DB.sql2o.open()) {
      String joinQuery = "SELECT venues.* FROM bands JOIN bands_venues ON (bands.id = bands_venues.band_id) JOIN venues ON (bands_venues.venue_id = venues.id) WHERE bands.id = :id;";
      return con.createQuery(joinQuery)
        .addParameter("id" , this.id)
        .executeAndFetch(Venue.class);
    }
  }

  // public void removeVenue(int venueId) {
  //   try(Connection con = DB.sql2o.open()) {
  //     String removeQuery = "DELETE FROM bands_venues WHERE venue_id=:venue_id AND band_id=:band_id;";
  //     con.createQuery(removeQuery)
  //       .addParameter("venue_id", venueId)
  //       .addParameter("band_id", this.id)
  //       .executeUpdate();
  //   }
  // }
  // 
  // public List<Venue> listAvailableVenues() {
  //   try(Connection con = DB.sql2o.open()) {
  //     String joinQuery = "SELECT venues.* FROM bands JOIN bands_venues ON (bands.id = bands_venues.band_id) JOIN venues ON (bands_venues.venue_id != venues.id) WHERE bands.id = :id;";
  //     return con.createQuery(joinQuery)
  //       .addParameter("id", this.id)
  //       .executeAndFetch(Venue.class);
  //   }
  // }
  //
  public static List<Band> search(String searchQuery) {
    try(Connection con = DB.sql2o.open()) {
      String search = "SELECT * FROM bands WHERE lower(band_name) LIKE :searchQuery;";
      return con.createQuery(search)
        .addParameter("searchQuery", "%" + searchQuery.toLowerCase() + "%")
        .executeAndFetch(Band.class);
    }
  }
}
