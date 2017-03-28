import org.sql2o.*;
import java.util.List;
import java.sql.Timestamp;
import java.sql.Date;

public class Patient {
  private String name;
  private int id;
  private Timestamp dob;
  private int doctorId;

  public Patient(String name, String dob, int doctorId) {
    this.name = name;
    this.dob = new Timestamp(Date.valueOf(dob).getTime());
    this.doctorId = doctorId;
  }

  public String getName() {
    return name;
  }

  public Timestamp getDOB(){
    return dob;
  }

  public int getId() {
    return id;
  }

  public int getDoctorId() {
    return doctorId;
  }

  @Override
  public boolean equals(Object otherPatient) {
    if (!(otherPatient instanceof Patient)) {
      return false;
    } else {
      Patient newPatient = (Patient) otherPatient;
      return this.getName().equals(newPatient.getName()) &&
             this.getDOB().equals(newPatient.getDOB()) &&
             this.getId() == newPatient.getId() &&
             this.getDoctorId() == newPatient.getDoctorId();
    }
  }

  public static List<Patient> all(){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM patients";
      return con.createQuery(sql).executeAndFetch(Patient.class);
    }
  }

  public void save() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO patients (name, dob, doctorId) VALUES (:name, :dob, :doctorId)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("dob", this.dob)
        .addParameter("doctorId", this.doctorId)
        .executeUpdate()
        .getKey();
    }
  }

  public static Patient find(int id) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM patients WHERE id = :id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Patient.class);
    }
  }

}
