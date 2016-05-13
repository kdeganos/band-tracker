import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.List;

public class BandTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Band_initializesCorrectly_true() {
    Band testBand = new Band("Name 1");
    assertTrue(testBand instanceof Band);
  }

  @Test public void getName_returnsName_String() {
    Band testBand = new Band("Name 1");
    assertEquals("Name 1", testBand.getName());
  }

  @Test
  public void all_initializeAsEmpty_0() {
    assertEquals(0, Band.all().size());
  }

  @Test
  public void equals_returnsTrueIfBothInstancesSame_true() {
    Band testBand = new Band("Name 1");
    Band testBand2 = new Band("Name 1");
    assertTrue(testBand.equals(testBand2));
  }

  @Test
  public void save_assignsIdToInstance_int() {
    Band testBand = new Band("Name 1");
    testBand.save();
    Band savedBand = Band.all().get(0);
  }

  @Test
  public void find_findsInstanceById_Band() {
    Band testBand = new Band("Name 1");
    testBand.save();
    Band foundBand = Band.find(testBand.getId());
    assertTrue(foundBand.equals(testBand));
  }

  @Test
  public void delete_deletesInstanceFromTable() {
    Band testBand = new Band("Name 1");
    testBand.save();
    testBand.delete();
    assertEquals(0, Band.all().size());
  }

  @Test
  public void update_updatesInstance_String() {
    Band testBand = new Band("Name 1");
    testBand.save();
    testBand.update("Name 2");
    Band updatedBand = Band.find(testBand.getId());
    assertEquals("Name 2", updatedBand.getName());
  }

  @Test
  public void addVenue_joinsBandAndVenue_true() {
    Band testBand = new Band("Name 1");
    testBand.save();
    Venue testVenue = new Venue("Name 1");
    testVenue.save();
    testBand.addVenue(testVenue);
    Venue joinedVenue = testBand.getVenues().get(0);
    assertTrue(testVenue.equals(joinedVenue));
  }

  @Test
  public void getVenues_returnsAllAssociatedVenues_int() {
    Band testBand = new Band("Name 1");
    testBand.save();
    Venue testVenue = new Venue("Name 2");
    testVenue.save();
    testBand.addVenue(testVenue);
    Venue testVenue2 = new Venue("Name 3");
    testVenue2.save();
    testBand.addVenue(testVenue2);
    Venue joinedVenue = testBand.getVenues().get(0);
    assertTrue(testVenue.equals(joinedVenue));
    // assertEquals(0, testBand.getVenues().size());
  }
}
