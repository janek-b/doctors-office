import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class DoctorTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/doctors_office_test", null, null);
  }

  @After
  public void tearDown() {
    try (Connection con = DB.sql2o.open()) {
      String deleteSpecialties = "DELETE FROM specialties *;";
      String deleteDoctors = "DELETE FROM doctors *;";
      String deletePatients = "DELETE FROM patients *;";
      con.createQuery(deleteSpecialties).executeUpdate();
      con.createQuery(deleteDoctors).executeUpdate();
      con.createQuery(deletePatients).executeUpdate();
    }
  }

  @Test
  public void doctor_instantiatesCorrectly() {
    Doctor testDoctor = new Doctor("testDoctor", 1);
    assertTrue(testDoctor instanceof Doctor);
  }

  @Test
  public void getName_returnsNameCorrectly() {
    Doctor testDoctor = new Doctor("testDoctor", 1);
    assertEquals("testDoctor", testDoctor.getName());
  }

  @Test
  public void getSpecialtyId_returnsSpecialtyCorrectly() {
    Doctor testDoctor = new Doctor("testDoctor", 1);
    assertEquals(1, testDoctor.getSpecialtyId());
  }

  @Test
  public void all_returnsAllFromDatabase() {
    Doctor testDoctor1 = new Doctor("testDoctor1", 1);
    testDoctor1.save();
    Doctor testDoctor2 = new Doctor("testDoctor2", 1);
    testDoctor2.save();
    assertTrue(Doctor.all().get(0).equals(testDoctor1));
    assertTrue(Doctor.all().get(1).equals(testDoctor2));
  }

  @Test
  public void savesEntriesToDatabase(){
    Doctor testDoctor = new Doctor("testDoctor", 1);
    testDoctor.save();
    assertTrue(Doctor.all().get(0).equals(testDoctor));
  }

  @Test
  public void equals_ComparesEntriesByName(){
    Doctor testDoctor1 = new Doctor("testDoctor", 1);
    Doctor testDoctor2 = new Doctor("testDoctor", 1);
    assertTrue(testDoctor1.equals(testDoctor2));
  }

  @Test
  public void getId_specialtyInstantiatesWithAnId() {
    Doctor testDoctor = new Doctor("testDoctor", 1);
    testDoctor.save();
    assertTrue(testDoctor.getId() > 0);
  }

  @Test
  public void save_assignsIdToObject() {
    Doctor testDoctor = new Doctor("testDoctor", 1);
    testDoctor.save();
    Doctor savedDoctor = Doctor.all().get(0);
    assertEquals(testDoctor.getId(), savedDoctor.getId());
  }

  @Test
  public void find_returnsDoctorWithSameID(){
    Doctor firstDoctor = new Doctor("testDoctor", 1);
    firstDoctor.save();
    assertEquals(Doctor.find(firstDoctor.getId()), firstDoctor);
  }

  @Test
  public void save_savesSpecialtyIdIntoDB() {
    Specialty specialty = new Specialty("Cardiac");
    specialty.save();
    Doctor testDoctor = new Doctor("testDoctor", specialty.getId());
    testDoctor.save();
    Doctor savedDoctor = Doctor.find(testDoctor.getId());
    assertEquals(savedDoctor.getSpecialtyId(), specialty.getId());
  }

  @Test
  public void getPatients_retrievesAllPatientsFromDB() {
    Doctor doctor = new Doctor("testDoctor", 1);
    doctor.save();
    Patient testPatient1 = new Patient("testPatient1", "2001-01-01", doctor.getId());
    testPatient1.save();
    Patient testPatient2 = new Patient("testPatient2", "2001-01-01", doctor.getId());
    testPatient2.save();
    Patient[] patients = new Patient[] {testPatient1, testPatient2};
    assertTrue(doctor.getPatients().containsAll(Arrays.asList(patients)));
  }

  @Test
  public void delete_deletesDoctor_true() {
    Doctor myDoctor = new Doctor("testDoctor", 1);
    myDoctor.save();
    int myDoctorId = myDoctor.getId();
    myDoctor.delete();
    assertEquals(null, Doctor.find(myDoctorId));
  }

}
