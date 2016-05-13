import java.util.Map;
import java.util.HashMap;
import java.util.List;

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

    post("/add-band", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name").trim();

      if(name.equals("")) {
        model.put("formError", true);
        model.put("template", "templates/add-band.vtl");
      } else {
        Band newBand = new Band(name);
        newBand.save();

        model.put("band", newBand);
        model.put("new", true);
        model.put("template", "templates/band.vtl");
      }
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/band/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int bandId = Integer.parseInt(request.params(":id"));
      Band band = Band.find(bandId);
      model.put("band", band);
      model.put("venues", band.getVenues());
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

    post("/add-venue", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name").trim();

      if(name.equals("")) {
        model.put("formError", true);
        model.put("template", "templates/add-venue.vtl");
      } else {
        Venue newVenue = new Venue(name);
        newVenue.save();

        model.put("venue", newVenue);
        model.put("new", true);
        model.put("template", "templates/venue.vtl");
      }
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/venue/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int venueId = Integer.parseInt(request.params(":id"));
      Venue venue = Venue.find(venueId);
      model.put("venue", venue);
      model.put("bands", venue.getBands());
      model.put("template", "templates/venue.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/associate/venues/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Band band = Band.find(Integer.parseInt(request.params(":id")));

      model.put("band", band);
      model.put("venues", Venue.all());
      model.put("template", "templates/associate-venues.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/associate/venues/:id", (request, response) -> {
      int bandId = Integer.parseInt(request.params(":id"));
      Band band = Band.find(bandId);

      band.removeAllVenues();
      String[] venues = request.queryParamsValues("venues");
      if (venues != null){
        for (String venue : venues) {
          band.addVenue(Venue.find(Integer.parseInt(venue)));
        }
      }
      response.redirect("/band/" + bandId);
      return null;
    });

    get("/band/:id/edit", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int bandId = Integer.parseInt(request.params(":id"));
      Band band = Band.find(bandId);
      model.put("band", band);
      model.put("template", "templates/edit-band.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/band/:id/edit", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int bandId = Integer.parseInt(request.params(":id"));
      Band band = Band.find(bandId);
      String name = request.queryParams("name").trim();

      if(name.equals("")) {
        model.put("formError", true);
        model.put("band", band);
        model.put("template", "templates/edit-band.vtl");
      } else {
        band.update(name);
        band = Band.find(bandId);
        model.put("band", band);
        model.put("venues", band.getVenues());
        model.put("update", true);
        model.put("template", "templates/band.vtl");
      }
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/venue/:id/edit", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int venueId = Integer.parseInt(request.params(":id"));
      Venue venue = Venue.find(venueId);
      model.put("venue", venue);
      model.put("template", "templates/edit-venue.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/venue/:id/edit", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int venueId = Integer.parseInt(request.params(":id"));
      Venue venue = Venue.find(venueId);
      String name = request.queryParams("name").trim();

      if(name.equals("")) {
        model.put("venue", venue);
        model.put("formError", true);
        model.put("template", "templates/edit-venue.vtl");
      } else {
        venue.update(name);
        venue = Venue.find(venueId);
        model.put("venue", venue);
        model.put("bands", venue.getBands());
        model.put("update", true);
        model.put("template", "templates/venue.vtl");
      }
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/band/:id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();

      int bandId = Integer.parseInt(request.params(":id"));
      Band band = Band.find(bandId);

      band.delete();

      model.put("bands", Band.all());
      model.put("deleted", true);
      model.put("template", "templates/list-bands.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/venue/:id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int venueId = Integer.parseInt(request.params(":id"));
      Venue venue = Venue.find(venueId);

      venue.delete();

      model.put("venues", Venue.all());
      model.put("deleted", true);
      model.put("template", "templates/list-venues.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
