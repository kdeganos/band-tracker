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
}
