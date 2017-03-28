import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class SpecialtyTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/doctors_office_test", null, null);
  }

  @After
  public void tearDown() {
    try (Connection con = DB.sql2o.open()) {
      String deleteSpecialties = "DELETE FROM specialties *;";
      String deleteDoctors = "DELETE FROM doctors *;";
      con.createQuery(deleteSpecialties).executeUpdate();
      con.createQuery(deleteDoctors).executeUpdate();
    }
  }

  @Test
  public void specialty_instantiatesCorrectly() {
    Specialty testSpecialty = new Specialty("testSpecialty");
    assertTrue(testSpecialty instanceof Specialty);
  }

  @Test
  public void getName_returnsNameCorrectly() {
    Specialty testSpecialty = new Specialty("testSpecialty");
    assertEquals("testSpecialty", testSpecialty.getName());
  }

  @Test
  public void all_returnsAllFromDatabase() {
    Specialty testSpecialty1 = new Specialty("testSpecialty1");
    testSpecialty1.save();
    Specialty testSpecialty2 = new Specialty("testSpecialty2");
    testSpecialty2.save();
    assertTrue(Specialty.all().get(0).equals(testSpecialty1));
    assertTrue(Specialty.all().get(1).equals(testSpecialty2));
  }

  @Test
  public void savesEntriesToDatabase(){
    Specialty testSpecialty = new Specialty("testSpecialty");
    testSpecialty.save();
    assertTrue(Specialty.all().get(0).equals(testSpecialty));
  }

  @Test
  public void equals_ComparesEntriesByName(){
    Specialty testSpecialty1 = new Specialty("testSpecialty");
    Specialty testSpecialty2 = new Specialty("testSpecialty");
    assertTrue(testSpecialty1.equals(testSpecialty2));
  }

  @Test
  public void getId_specialtyInstantiatesWithAnId() {
    Specialty testSpecialty = new Specialty("testSpecialty");
    testSpecialty.save();
    assertTrue(testSpecialty.getId() > 0);
  }

  @Test
  public void save_assignsIdToObject() {
    Specialty testSpecialty = new Specialty("testSpecialty");
    testSpecialty.save();
    Specialty savedSpecialty = Specialty.all().get(0);
    assertEquals(testSpecialty.getId(), savedSpecialty.getId());
  }

  @Test
  public void find_returnsSpecialtyWithSameID(){
    Specialty firstSpecialty = new Specialty("Cardiac");
    firstSpecialty.save();
    assertEquals(Specialty.find(firstSpecialty.getId()), firstSpecialty);
  }

  @Test
  public void getDoctors_retrievesAllDoctorsFromDB() {
    Specialty specialty = new Specialty("Cardiac");
    specialty.save();
    Doctor testDoctor1 = new Doctor("testDoctor1", specialty.getId());
    testDoctor1.save();
    Doctor testDoctor2 = new Doctor("testDoctor2", specialty.getId());
    testDoctor2.save();
    Doctor[] doctors = new Doctor[] {testDoctor1, testDoctor2};
    assertTrue(specialty.getDoctors().containsAll(Arrays.asList(doctors)));
  }
}
