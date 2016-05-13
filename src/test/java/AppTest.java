import static org.fluentlenium.core.filter.FilterConstructor.*;
import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;


public class AppTest extends FluentTest{

  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Band Tracker");
  }

  @Test
  public void listBands() {
    Band testBand = new Band("Name 1");
    testBand.save();
    goTo("http://localhost:4567/");
    click("a", withText("View All Bands"));
    assertThat(pageSource()).contains("Name 1");
  }

  @Test
  public void addBand() {
    goTo("http://localhost:4567/");
    click("a", withText("Add A Band"));
    fill("#name").with("Name 1");
    submit("#submitBand");
    assertThat(pageSource()).contains("Name 1");
  }

  @Test
  public void listVenues() {
    Venue testVenue = new Venue("Name 1");
    testVenue.save();
    goTo("http://localhost:4567/");
    click("a", withText("View All Venues"));
    assertThat(pageSource()).contains("Name 1");
  }

  @Test
  public void addVenue() {
    goTo("http://localhost:4567/");
    click("a", withText("Add A Venue"));
    fill("#name").with("Name 1");
    submit("#submitVenue");
    assertThat(pageSource()).contains("Name 1");
  }

  @Test
  public void addBandFormError() {
    goTo("http://localhost:4567/");
    click("a", withText("Add A Band"));
    submit("#submitBand");
    assertThat(pageSource()).contains("Please be sure");
  }

  @Test
  public void addVenueFormError() {
    goTo("http://localhost:4567/");
    click("a", withText("Add A Venue"));
    submit("#submitVenue");
    assertThat(pageSource()).contains("Please be sure");
  }

  @Test
  public void editBand() {
    Band testBand = new Band ("Name 1");
    testBand.save();
    goTo("http://localhost:4567/band/" + testBand.getId() + "/edit");
    fill("#name").with("Name 2");
    submit(".btn-success");
    assertThat(pageSource()).contains("Name 2");
  }

  @Test
  public void editVenue() {
    Venue testVenue = new Venue ("Name 1");
    testVenue.save();
    goTo("http://localhost:4567/venue/" + testVenue.getId() + "/edit");
    fill("#name").with("Name 2");
    submit(".btn-success");
    assertThat(pageSource()).contains("Name 2");
  }

  @Test
  public void deleteBand() {
    Band testBand = new Band ("Name 1");
    testBand.save();
    goTo("http://localhost:4567/band/" + testBand.getId() + "/edit");
    click(".btn-danger");
    assertThat(pageSource()).doesNotContain("Name 1");
  }

  @Test
  public void deleteVenue() {
    Venue testVenue = new Venue ("Name 1");
    testVenue.save();
    goTo("http://localhost:4567/venue/" + testVenue.getId() + "/edit");
    click(".btn-danger");
    assertThat(pageSource()).doesNotContain("Name 1");
  }

  @Test public void search() {
    Band testBand = new Band ("Name 1");
    testBand.save();
    Venue testVenue = new Venue ("Name 1");
    testVenue.save();
    Venue testVenue2 = new Venue ("Name 2");
    testVenue2.save();
    goTo("http://localhost:4567/");
    fill("#search").with("e 1");
    submit(".btn-info");
    assertThat(pageSource()).doesNotContain("Name 2");
    assertThat(pageSource()).contains("Name 1");
  }
}
