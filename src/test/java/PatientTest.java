import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class PatientTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/doctors_office_test", null, null);
  }

  @After
  public void tearDown() {
    try (Connection con = DB.sql2o.open()) {
      String deleteDoctors = "DELETE FROM doctors *;";
      String deletePatients = "DELETE FROM patients *;";
      con.createQuery(deleteDoctors).executeUpdate();
      con.createQuery(deletePatients).executeUpdate();
    }
  }

  @Test
  public void patient_instantiatesCorrectly() {
    Patient testPatient = new Patient("testPatient", "2001-01-01", 1);
    assertTrue(testPatient instanceof Patient);
  }

  @Test
  public void getName_returnsNameCorrectly() {
    Patient testPatient = new Patient("testPatient", "2001-01-01", 1);
    assertEquals("testPatient", testPatient.getName());
  }

  @Test
  public void getDoctorId_returnsDoctorCorrectly() {
    Patient testPatient = new Patient("testPatient", "2001-01-01", 1);
    assertEquals(1, testPatient.getDoctorId());
  }

  @Test
  public void all_returnsAllFromDatabase() {
    Patient testPatient1 = new Patient("testPatient1", "2001-01-01", 1);
    testPatient1.save();
    Patient testPatient2 = new Patient("testPatient2", "2001-01-01", 1);
    testPatient2.save();
    assertTrue(Patient.all().get(0).equals(testPatient1));
    assertTrue(Patient.all().get(1).equals(testPatient2));
  }

  @Test
  public void savesEntriesToDatabase(){
    Patient testPatient = new Patient("testPatient", "2001-01-01", 1);
    testPatient.save();
    assertTrue(Patient.all().get(0).equals(testPatient));
  }

  @Test
  public void equals_ComparesEntriesByName(){
    Patient testPatient1 = new Patient("testPatient", "2001-01-01", 1);
    Patient testPatient2 = new Patient("testPatient", "2001-01-01", 1);
    assertTrue(testPatient1.equals(testPatient2));
  }

  @Test
  public void save_assignsIdToObject() {
    Patient testPatient = new Patient("testPatient", "2001-01-01", 1);
    testPatient.save();
    Patient savedPatient = Patient.all().get(0);
    assertEquals(testPatient.getId(), savedPatient.getId());
  }

  @Test
  public void find_returnsPatientWithSameID(){
    Patient firstPatient = new Patient("testPatient", "2001-01-01", 1);
    firstPatient.save();
    assertEquals(Patient.find(firstPatient.getId()), firstPatient);
  }

}
