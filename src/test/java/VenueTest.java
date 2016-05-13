import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.List;

public class VenueTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Venue_initializesCorrectly_true() {
    Venue testVenue = new Venue("Name 1");
    assertTrue(testVenue instanceof Venue);
  }

  @Test public void getName_returnsName_String() {
    Venue testVenue = new Venue("Name 1");
    assertEquals("Name 1", testVenue.getName());
  }

  @Test
  public void all_initializeAsEmpty_0() {
    assertEquals(0, Venue.all().size());
  }

  @Test
  public void equals_returnsTrueIfBothInstancesSame_true() {
    Venue testVenue = new Venue("Name 1");
    Venue testVenue2 = new Venue("Name 1");
    assertTrue(testVenue.equals(testVenue2));
  }

  @Test
  public void save_assignsIdToInstance_int() {
    Venue testVenue = new Venue("Name 1");
    testVenue.save();
    Venue savedVenue = Venue.all().get(0);
  }

  @Test
  public void find_findsInstanceById_Venue() {
    Venue testVenue = new Venue("Name 1");
    testVenue.save();
    Venue foundVenue = Venue.find(testVenue.getId());
    assertTrue(foundVenue.equals(testVenue));
  }

  @Test
  public void delete_deletesInstanceFromTable() {
    Venue testVenue = new Venue("Name 1");
    testVenue.save();
    testVenue.delete();
    assertEquals(0, Venue.all().size());
  }

  @Test
  public void update_updatesInstance_String() {
    Venue testVenue = new Venue("Name 1");
    testVenue.save();
    testVenue.update("Name 2");
    Venue updatedVenue = Venue.find(testVenue.getId());
    assertEquals("Name 2", updatedVenue.getName());
  }

  @Test
  public void addBand_joinsBandAndBand_true() {
    Venue testVenue = new Venue("Name 1");
    testVenue.save();
    Band testBand = new Band("Name 1");
    testBand.save();
    testVenue.addBand(testBand);
    Band joinedBand = testVenue.getBands().get(0);
    assertTrue(testBand.equals(joinedBand));
  }

  @Test
  public void getBands_returnsAllAssociatedBands_int() {
    Venue testVenue = new Venue("Name 1");
    testVenue.save();
    Band testBand = new Band("Name 2");
    testBand.save();
    testVenue.addBand(testBand);
    Band testBand2 = new Band("Name 3");
    testBand2.save();
    testVenue.addBand(testBand2);
    Band joinedBand = testVenue.getBands().get(0);
    assertTrue(testBand.equals(joinedBand));
    assertEquals(2, testVenue.getBands().size());
  }

  @Test
  public void removeBand_removesBandAssociation_true() {
    Venue testVenue = new Venue("Name 1");
    testVenue.save();
    Band testBand = new Band("Name 2");
    testBand.save();
    testVenue.addBand(testBand);
    testVenue.removeBand(testBand.getId());
    assertEquals(0, testVenue.getBands().size());
  }
}
