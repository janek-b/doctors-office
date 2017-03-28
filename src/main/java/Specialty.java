import org.sql2o.*;
import java.util.List;

public class Specialty {
  private String name;
  private int id;

  public Specialty(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public static List<Specialty> all(){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM specialties";
      return con.createQuery(sql).executeAndFetch(Specialty.class);
    }
  }

  @Override
  public boolean equals(Object otherSpecialty) {
    if (!(otherSpecialty instanceof Specialty)) {
      return false;
    } else {
      Specialty newSpecialty = (Specialty) otherSpecialty;
      return this.getName().equals(newSpecialty.getName()) &&
             this.getId() == newSpecialty.getId();
    }
  }

  public void save() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO specialties (name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Specialty find(int id){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM specialties WHERE id = :id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Specialty.class);
    }
  }

  public List<Doctor> getDoctors() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM doctors WHERE specialtyId = :id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Doctor.class);
    }
  }
}
