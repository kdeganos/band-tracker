import java.util.Map;
import java.util.HashMap;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/list-bands", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("bands", Band.all());
      model.put("template", "templates/list-bands.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/add-band", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/add-band.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/add-band/error", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();      model.put("formError", true);
      model.put("template", "templates/add-band.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/add-band", (request, response) -> {
      String name = request.queryParams("name");

      if(name.equals("")) {
        response.redirect("/add-band/error");
      } else {
        Band newBand = new Band(name);
        newBand.save();

        response.redirect("/band/" + newBand.getId());
      }
      return null;
    });

    get("/band/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int bandId = Integer.parseInt(request.params(":id"));
      Band band = Band.find(bandId);
      model.put("band", band);
      model.put("template", "templates/band.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/list-venues", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("venues", Venue.all());
      model.put("template", "templates/list-venues.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/add-venue", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/add-venue.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/add-venue/error", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("formError", true);
      model.put("template", "templates/add-venue.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/add-venue", (request, response) -> {
      String name = request.queryParams("name");

      if(name.equals("")) {
        response.redirect("/add-venue/error");
      } else {
        Venue newVenue = new Venue(name);
        newVenue.save();

        response.redirect("/venue/" + newVenue.getId());
      }
      return null;
    });

    get("/venue/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int venueId = Integer.parseInt(request.params(":id"));
      Venue venue = Venue.find(venueId);
      model.put("venue", venue);
      model.put("template", "templates/venue.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
