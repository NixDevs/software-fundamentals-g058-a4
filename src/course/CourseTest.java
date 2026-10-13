/**
 * 
 */
package course;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 */
class CourseTest {
	
	private Course course;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		course = new Course();
	}

	
	private boolean create(String name, String description, int points,
		         String type, boolean group, String groupName,
		         String pub, String due, String hide) {
		return course.createNewAssessment(name, description, points,
		            type, group, groupName, pub, due, hide);
	}
	
	@Test
	void validAssessmentIsCreated() {
		assertTrue(create("Essay", "Write a short essay", 20, "pdf",
	            false, null, "01/03/2026 09:00", "10/03/2026 17:00", "11/03/2026 17:00"));
	}
	@Test
	   void blankNameRejected() {
	      assertFalse(create("", "Write a short essay", 20, "pdf",
	            false, null, "01/03/2026 09:00", "10/03/2026 17:00", "11/03/2026 17:00"));
	   }

	   @Test
	   void duplicateNameRejected() {
	      create("Essay", "Write a short essay", 20, "pdf",
	            false, null, "01/03/2026 09:00", "10/03/2026 17:00", "11/03/2026 17:00");
	      assertFalse(create("Essay", "Another description here", 10, "zip",
	            false, null, "01/03/2026 09:00", "10/03/2026 17:00", "11/03/2026 17:00"));
	   }
}
