package edu.uno.csci2830;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link EmployeeManager} and {@link Employee}.
 *
 * <p>Uses JUnit 5. Add the following dependency to your project
 * (Maven example):
 *
 * <pre>
 * &lt;dependency&gt;
 *     &lt;groupId&gt;org.junit.jupiter&lt;/groupId&gt;
 *     &lt;artifactId&gt;junit-jupiter&lt;/artifactId&gt;
 *     &lt;version&gt;5.10.2&lt;/version&gt;
 *     &lt;scope&gt;test&lt;/scope&gt;
 * &lt;/dependency&gt;
 * </pre>
 *
 * If you are using IntelliJ without Maven/Gradle, add the JUnit 5
 * JAR via File → Project Structure → Libraries.
 *
 * @author Generated for Shelby Wells
 * @version 1.0
 */
@DisplayName("Employee Manager Tests")
class EmployeeManagerTest {

    private EmployeeManager manager;

    // Fixed test dates
    private static final LocalDate PAST_DATE    = LocalDate.of(2020, 1, 15);
    private static final LocalDate TODAY        = LocalDate.now();
    private static final LocalDate FUTURE_DATE  = LocalDate.now().plusYears(1);

    // Shared CSV file name — must match EmployeeManager.CSV_FILE
    private static final String CSV_FILE = "employees.csv";

    // ─────────────────────────────────────────────────────────────────────────
    //  SETUP / TEARDOWN
    // ─────────────────────────────────────────────────────────────────────────

    @BeforeEach
    void setUp() {
        // Fresh manager before every test — no CSV loaded
        manager = new EmployeeManager();
        // Remove any leftover CSV so tests are isolated
        new File(CSV_FILE).delete();
    }

    @AfterEach
    void tearDown() {
        new File(CSV_FILE).delete();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  ADD EMPLOYEE
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("addEmployee()")
    class AddEmployeeTests {

        @Test
        @DisplayName("Returns a non-null Employee")
        void addEmployee_returnsEmployee() {
            Employee emp = manager.addEmployee("Jane", "Doe", "Engineering", "Developer", PAST_DATE);
            assertNotNull(emp);
        }

        @Test
        @DisplayName("Employee has correct field values")
        void addEmployee_fieldsAreCorrect() {
            Employee emp = manager.addEmployee("Jane", "Doe", "Engineering", "Developer", PAST_DATE);
            assertAll(
                () -> assertEquals("Jane",        emp.getFirstName()),
                () -> assertEquals("Doe",         emp.getLastName()),
                () -> assertEquals("Engineering", emp.getDepartment()),
                () -> assertEquals("Developer",   emp.getTitle())
            );
        }

        @Test
        @DisplayName("IDs are unique and auto-incremented")
        void addEmployee_idsAreUnique() {
            Employee e1 = manager.addEmployee("Alice", "Smith", "HR",  "Manager",   PAST_DATE);
            Employee e2 = manager.addEmployee("Bob",   "Jones", "IT",  "Analyst",   PAST_DATE);
            Employee e3 = manager.addEmployee("Carol", "White", "Ops", "Coordinator", PAST_DATE);
            assertNotEquals(e1.getId(), e2.getId());
            assertNotEquals(e2.getId(), e3.getId());
            assertNotEquals(e1.getId(), e3.getId());
        }

        @Test
        @DisplayName("Employee appears in getAllEmployees() list")
        void addEmployee_appearsInList() {
            Employee emp = manager.addEmployee("Jane", "Doe", "Engineering", "Developer", PAST_DATE);
            assertTrue(manager.getAllEmployees().contains(emp));
        }

        @Test
        @DisplayName("Employee with past start date is active")
        void addEmployee_pastStartDate_isActive() {
            Employee emp = manager.addEmployee("Jane", "Doe", "Eng", "Dev", PAST_DATE);
            // Active status is readable via toCSV()
            assertTrue(emp.toCSV().contains("true"));
        }

        @Test
        @DisplayName("Employee with today's start date is active")
        void addEmployee_todayStartDate_isActive() {
            Employee emp = manager.addEmployee("Jane", "Doe", "Eng", "Dev", TODAY);
            assertTrue(emp.toCSV().contains("true"));
        }

        @Test
        @DisplayName("Employee with future start date is inactive")
        void addEmployee_futureStartDate_isInactive() {
            Employee emp = manager.addEmployee("Jane", "Doe", "Eng", "Dev", FUTURE_DATE);
            assertTrue(emp.toCSV().contains("false"));
        }

        @Test
        @DisplayName("Multiple employees all stored correctly")
        void addEmployee_multipleEmployees_allStored() {
            for (int i = 0; i < 5; i++) {
                manager.addEmployee("First" + i, "Last" + i, "Dept", "Title", PAST_DATE);
            }
            assertEquals(5, manager.getAllEmployees().size());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  FIND BY ID
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("findEmployeeById()")
    class FindByIdTests {

        @Test
        @DisplayName("Returns correct employee for valid ID")
        void findById_validId_returnsEmployee() {
            Employee added = manager.addEmployee("Jane", "Doe", "Eng", "Dev", PAST_DATE);
            Employee found = manager.findEmployeeById(added.getId());
            assertNotNull(found);
            assertEquals(added.getId(), found.getId());
        }

        @Test
        @DisplayName("Returns null for non-existent ID")
        void findById_invalidId_returnsNull() {
            assertNull(manager.findEmployeeById(9999));
        }

        @Test
        @DisplayName("Returns correct employee among multiple")
        void findById_multipleEmployees_returnsCorrectOne() {
            manager.addEmployee("Alice", "Smith", "HR",  "Manager", PAST_DATE);
            Employee target = manager.addEmployee("Bob", "Jones", "IT", "Analyst", PAST_DATE);
            manager.addEmployee("Carol", "White", "Ops", "Coordinator", PAST_DATE);

            Employee found = manager.findEmployeeById(target.getId());
            assertNotNull(found);
            assertEquals("Bob", found.getFirstName());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  FIND BY NAME
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("findEmployeeByName()")
    class FindByNameTests {

        @Test
        @DisplayName("Returns employee with exact name match")
        void findByName_exactMatch_returnsEmployee() {
            manager.addEmployee("Jane", "Doe", "Eng", "Dev", PAST_DATE);
            Employee found = manager.findEmployeeByName("Jane", "Doe");
            assertNotNull(found);
        }

        @Test
        @DisplayName("Search is case-insensitive")
        void findByName_caseInsensitive_returnsEmployee() {
            manager.addEmployee("Jane", "Doe", "Eng", "Dev", PAST_DATE);
            assertAll(
                () -> assertNotNull(manager.findEmployeeByName("jane", "doe")),
                () -> assertNotNull(manager.findEmployeeByName("JANE", "DOE")),
                () -> assertNotNull(manager.findEmployeeByName("Jane", "doe"))
            );
        }

        @Test
        @DisplayName("Returns null when name does not exist")
        void findByName_noMatch_returnsNull() {
            manager.addEmployee("Jane", "Doe", "Eng", "Dev", PAST_DATE);
            assertNull(manager.findEmployeeByName("John", "Smith"));
        }

        @Test
        @DisplayName("Returns null when only first name matches")
        void findByName_onlyFirstNameMatches_returnsNull() {
            manager.addEmployee("Jane", "Doe", "Eng", "Dev", PAST_DATE);
            assertNull(manager.findEmployeeByName("Jane", "Smith"));
        }

        @Test
        @DisplayName("Returns null when only last name matches")
        void findByName_onlyLastNameMatches_returnsNull() {
            manager.addEmployee("Jane", "Doe", "Eng", "Dev", PAST_DATE);
            assertNull(manager.findEmployeeByName("John", "Doe"));
        }

        @ParameterizedTest(name = "Name: {0} {1}")
        @CsvSource({
            "Alice, Smith",
            "Bob,   Jones",
            "Carol, White"
        })
        @DisplayName("Finds each employee by name in a populated list")
        void findByName_multipleEmployees_findsEach(String first, String last) {
            manager.addEmployee("Alice", "Smith", "HR",  "Manager",     PAST_DATE);
            manager.addEmployee("Bob",   "Jones", "IT",  "Analyst",     PAST_DATE);
            manager.addEmployee("Carol", "White", "Ops", "Coordinator", PAST_DATE);
            assertNotNull(manager.findEmployeeByName(first.trim(), last.trim()));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TERMINATE EMPLOYEE
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("terminateEmployee()")
    class TerminateTests {

        @Test
        @DisplayName("Terminated employee is no longer active")
        void terminate_setsInactive() {
            Employee emp = manager.addEmployee("Jane", "Doe", "Eng", "Dev", PAST_DATE);
            manager.terminateEmployee(emp.getId());
            assertTrue(emp.toCSV().contains("false"));
        }

        @Test
        @DisplayName("Terminated employee's CSV includes term date")
        void terminate_setsTermDate() {
            Employee emp = manager.addEmployee("Jane", "Doe", "Eng", "Dev", PAST_DATE);
            manager.terminateEmployee(emp.getId());
            // Term date column (index 6) should not be empty
            String termDate = emp.toCSV().split(",", -1)[6];
            assertFalse(termDate.isBlank());
        }

        @Test
        @DisplayName("Returns null for non-existent ID")
        void terminate_invalidId_returnsNull() {
            assertNull(manager.terminateEmployee(9999));
        }

        @Test
        @DisplayName("Terminating one employee does not affect others")
        void terminate_doesNotAffectOtherEmployees() {
            Employee e1 = manager.addEmployee("Alice", "Smith", "HR", "Manager", PAST_DATE);
            Employee e2 = manager.addEmployee("Bob",   "Jones", "IT", "Analyst", PAST_DATE);
            manager.terminateEmployee(e1.getId());
            assertTrue(e2.toCSV().contains("true"));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  EMPLOYEE SETTERS
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Employee setters")
    class SetterTests {

        private Employee emp;

        @BeforeEach
        void addOne() {
            emp = manager.addEmployee("Jane", "Doe", "Engineering", "Developer", PAST_DATE);
        }

        @Test
        @DisplayName("setFirstName() updates first name")
        void setFirstName_updatesValue() {
            emp.setFirstName("Janet");
            assertEquals("Janet", emp.getFirstName());
        }

        @Test
        @DisplayName("setLastName() updates last name")
        void setLastName_updatesValue() {
            emp.setLastName("Smith");
            assertEquals("Smith", emp.getLastName());
        }

        @Test
        @DisplayName("setTitle() updates title")
        void setTitle_updatesValue() {
            emp.setTitle("Senior Developer");
            assertEquals("Senior Developer", emp.getTitle());
        }

        @Test
        @DisplayName("setDepartment() updates department")
        void setDepartment_updatesValue() {
            emp.setDepartment("Product");
            assertEquals("Product", emp.getDepartment());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  CSV PERSISTENCE
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("CSV persistence (saveToCSV / loadFromCSV)")
    class CsvTests {

        @Test
        @DisplayName("saveToCSV() creates the CSV file")
        void save_createsFile() {
            manager.addEmployee("Jane", "Doe", "Eng", "Dev", PAST_DATE);
            manager.saveToCSV();
            assertTrue(new File(CSV_FILE).exists());
        }

        @Test
        @DisplayName("loadFromCSV() restores all employees")
        void saveAndLoad_restoresEmployees() {
            manager.addEmployee("Alice", "Smith", "HR",  "Manager", PAST_DATE);
            manager.addEmployee("Bob",   "Jones", "IT",  "Analyst", PAST_DATE);
            manager.saveToCSV();

            EmployeeManager loaded = new EmployeeManager();
            loaded.loadFromCSV();

            assertEquals(2, loaded.getAllEmployees().size());
        }

        @Test
        @DisplayName("loadFromCSV() restores correct field values")
        void saveAndLoad_fieldsMatch() {
            manager.addEmployee("Alice", "Smith", "HR", "Manager", PAST_DATE);
            manager.saveToCSV();

            EmployeeManager loaded = new EmployeeManager();
            loaded.loadFromCSV();

            Employee emp = loaded.getAllEmployees().get(0);
            assertAll(
                () -> assertEquals("Alice",   emp.getFirstName()),
                () -> assertEquals("Smith",   emp.getLastName()),
                () -> assertEquals("HR",      emp.getDepartment()),
                () -> assertEquals("Manager", emp.getTitle())
            );
        }

        @Test
        @DisplayName("loadFromCSV() restores terminated employee correctly")
        void saveAndLoad_preservesTermination() {
            Employee emp = manager.addEmployee("Jane", "Doe", "Eng", "Dev", PAST_DATE);
            manager.terminateEmployee(emp.getId());
            manager.saveToCSV();

            EmployeeManager loaded = new EmployeeManager();
            loaded.loadFromCSV();

            Employee restored = loaded.getAllEmployees().get(0);
            assertTrue(restored.toCSV().contains("false"));
        }

        @Test
        @DisplayName("loadFromCSV() does nothing when no file exists")
        void load_noFile_doesNotThrow() {
            new File(CSV_FILE).delete();
            assertDoesNotThrow(() -> manager.loadFromCSV());
            assertEquals(0, manager.getAllEmployees().size());
        }

        @Test
        @DisplayName("nextId continues from highest loaded ID")
        void saveAndLoad_nextIdContinuesFromMax() {
            manager.addEmployee("Alice", "Smith", "HR", "Manager", PAST_DATE);
            manager.addEmployee("Bob",   "Jones", "IT", "Analyst", PAST_DATE);
            manager.saveToCSV();

            EmployeeManager loaded = new EmployeeManager();
            loaded.loadFromCSV();

            // Adding a new employee should get an ID higher than any existing one
            int maxExisting = loaded.getAllEmployees().stream()
                .mapToInt(Employee::getId).max().orElse(0);
            Employee newEmp = loaded.addEmployee("Carol", "White", "Ops", "Coordinator", PAST_DATE);
            assertTrue(newEmp.getId() > maxExisting);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  GET ALL EMPLOYEES
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("getAllEmployees()")
    class GetAllTests {

        @Test
        @DisplayName("Returns empty list when no employees added")
        void getAll_empty_returnsEmptyList() {
            ArrayList<Employee> all = manager.getAllEmployees();
            assertNotNull(all);
            assertTrue(all.isEmpty());
        }

        @Test
        @DisplayName("Count matches number of employees added")
        void getAll_countMatchesAdded() {
            manager.addEmployee("A", "B", "Dept", "Title", PAST_DATE);
            manager.addEmployee("C", "D", "Dept", "Title", PAST_DATE);
            manager.addEmployee("E", "F", "Dept", "Title", PAST_DATE);
            assertEquals(3, manager.getAllEmployees().size());
        }
    }
}
