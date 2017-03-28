import org.sql2o.*;
import java.util.List;

public class Doctor {
  private String name;
  private int id;
  private int specialtyId;

  public Doctor(String name, int specialtyId) {
    this.name = name;
    this.specialtyId = specialtyId;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public int getSpecialtyId() {
    return specialtyId;
  }

  public static List<Doctor> all(){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM doctors";
      return con.createQuery(sql).executeAndFetch(Doctor.class);
    }
  }

  @Override
  public boolean equals(Object otherDoctor) {
    if (!(otherDoctor instanceof Doctor)) {
      return false;
    } else {
      Doctor newDoctor = (Doctor) otherDoctor;
      return this.getName().equals(newDoctor.getName()) &&
             this.getSpecialtyId() == newDoctor.getSpecialtyId() &&
             this.getId() == newDoctor.getId();
    }
  }

  public void save() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO doctors (name, specialtyId) VALUES (:name, :specialtyId)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("specialtyId", this.specialtyId)
        .executeUpdate()
        .getKey();
    }
  }

  public static Doctor find(int id) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM doctors WHERE id = :id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Doctor.class);
    }
  }

  public List<Patient> getPatients() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM patients WHERE doctorId = :id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Patient.class);
    }
  }

  public void delete() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM doctors WHERE id = :id";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

}
