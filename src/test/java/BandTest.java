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

  @Test public void getBandName_returnsBandName_String() {
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
}