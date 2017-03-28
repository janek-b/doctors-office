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
      model.put("doctors", Doctor.all());
      model.put("specialties", Specialty.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/specialties", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String url = String.format("/specialties/%d/doctors", Integer.parseInt(request.queryParams("specialtyId")));
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/specialties/:id/doctors", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Specialty specialty = Specialty.find(Integer.parseInt(request.params(":id")));
      model.put("specialty", specialty);
      model.put("specialtyDoctors", specialty.getDoctors());
      model.put("template", "templates/doctors.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    get("/doctors", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Doctor newDoctor = Doctor.find(Integer.parseInt(request.queryParams("doctorId")));
      String url = String.format("/specialties/%d/doctors/%d/patients", newDoctor.getSpecialtyId(), newDoctor.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/specialties/:id/doctors/:doctorId/patients", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Doctor newDoctor = Doctor.find(Integer.parseInt(request.params(":doctorId")));
      model.put("doctor", newDoctor);
      model.put("template", "templates/patients.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    get("/admin", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("doctors", Doctor.all());
      model.put("specialties", Specialty.all());
      model.put("template", "templates/admin.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/specialties", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String specialtyName = request.queryParams("specialtyName");
      Specialty newSpecialty = new Specialty(specialtyName);
      newSpecialty.save();
      response.redirect("/admin");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/patients", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String patientName = request.queryParams("name");
      String patientDOB = request.queryParams("dob");
      int patientDoctor = Integer.parseInt(request.queryParams("doctor"));
      Patient newPatient = new Patient(patientName, patientDOB, patientDoctor);
      newPatient.save();
      response.redirect("/admin");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/doctors", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String doctorName = request.queryParams("doctorName");
      int doctorSpecialty = Integer.parseInt(request.queryParams("specialtyId"));
      Doctor newDoctor = new Doctor(doctorName, doctorSpecialty);
      newDoctor.save();
      response.redirect("/admin");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/specialties/:id/doctors/:did/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Doctor doctor = Doctor.find(Integer.parseInt(request.params(":did")));
      doctor.delete();
      response.redirect("/admin");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/specialties/:id/doctors/:did", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Doctor doctor = Doctor.find(Integer.parseInt(request.params(":did")));
      model.put("doctor", doctor);
      model.put("template", "templates/doctor.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
