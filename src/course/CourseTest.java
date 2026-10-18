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
	
	// ==========================================
	// Condition 1: Unique Name Requirement
	// ==========================================
	@Test
	void duplicateNameRejected() {
		// Precondition: Successfully added an assessment for the first time.
		boolean firstCreated = create("essay", "write a short essay", 20, "pdf",
		            false, null, "01/03/2026 09:00", "10/03/2026 17:00", "11/03/2026 17:00");
		assertTrue(firstCreated);
		assertEquals(1, course.getAssessments().size());

		// Test: Attempting to add an evaluation name that already exists (including cases with different letter casing) should be rejected.
		boolean duplicateCreated = create("ESSAY", "another description here", 10, "doc",
		            false, null, "01/03/2026 09:00", "10/03/2026 17:00", "11/03/2026 17:00");
		assertFalse(duplicateCreated);
			// The object was not added to the list after the assertion check failed (the list length remained 1).
		assertEquals(1, course.getAssessments().size());
	}
	// ==========================================
	// Condition 2: Non-blank Name Requirement
	// ==========================================
	@Test
	void blankNameRejected() {
		// Precondition: Ensure the list is initially empty.
		assertTrue(course.getAssessments().isEmpty());

		// Test: Pass a string consisting solely of spaces or an empty string as the name
		assertFalse(create("   ", "write a short essay", 20, "pdf",
	            false, null, "01/03/2026 09:00", "10/03/2026 17:00", "11/03/2026 17:00"));
		// The list remains empty after the assertion check fails.
		assertEquals(0, course.getAssessments().size());
	}
	// ==========================================
	// Condition 3: Description Word Count Requirement
	// ==========================================
	@Test
	void descriptionLessThanThreeWordsRejected() {
		// Precondition: Ensure the list is initially empty.
		assertTrue(course.getAssessments().isEmpty());

		// Test (Boundary test): Description contains only 2 words (fewer than the required 3 words)
		assertFalse(create("quiz 1", "final exam", 10, "pdf",
	            false, null, "01/03/2026 09:00", "10/03/2026 17:00", "11/03/2026 17:00"));
		// The list remains empty after the assertion check fails.
		assertEquals(0, course.getAssessments().size());
	}

	@Test
	void descriptionExactlyThreeWordsAccepted() {
		// Test (Boundary test): A description with exactly 3 words; should pass successfully.
		assertTrue(create("quiz 2", "this is valid", 10, "pdf",
	            false, null, "01/03/2026 09:00", "10/03/2026 17:00", "11/03/2026 17:00"));
		// Successfully verified and added to the list
		assertEquals(1, course.getAssessments().size());
	}
	
	// ==========================================
	// Condition 4: Points Greater Than 0 Requirement
	// ==========================================
	@Test
	void pointsZeroOrNegativeRejected() {

		// Precondition: Ensure the list is initially empty.
		assertTrue(course.getAssessments().isEmpty());

		// Boundary Test 1: Input score is 0 (invalid boundary; should be rejected)
		assertFalse(create("quiz 1", "write a short essay", 0, "pdf",
				false, null, "01/03/2026 09:00", "10/03/2026 17:00", "11/03/2026 17:00"));
		// The list remains empty after the assertion check fails.
		assertEquals(0, course.getAssessments().size());

		// Boundary Test 2: Input negative score of -5 (invalid value; should be rejected)
		assertFalse(create("quiz 2", "write a short essay", -5, "pdf",
				false, null, "01/03/2026 09:00", "10/03/2026 17:00", "11/03/2026 17:00"));
		assertEquals(0, course.getAssessments().size());
	}
	
	// ==========================================
	// Condition 5: No single assessment can exceed 50 points
	// ==========================================
	@Test
	void pointsGreaterThanFiftyRejected() {
		// Precondition: assessment list must be empty
		assertEquals(0, course.getAssessments().size());
		
		// Test (Boundary): Points equals 51 (1 point above 50)
		assertFalse(
			create("Assignment 1", "Final project report", 51, "pdf", false, "", 
			"01/04/2027 09:00", "15/04/2027 23:59", "20/04/2027 23:59")
			);
		// The list remains empty after the assertion check fails.
		assertEquals(0, course.getAssessments().size());
	}
	@Test 
	void pointsEqualsFiftyAccepted() {
		// Precondition: assessment list must be empty
		assertEquals(0, course.getAssessments().size());
		// Test (Boundary): Points equals 50 (Exactly maximum value permitted)
		assertTrue(
			create("Assignment 1", "Final project report", 50, "pdf", false, "", 
				"01/04/2027 09:00", "15/04/2027 23:59", "20/04/2027 23:59")
			);
		// The list increases by 1
		assertEquals(1, course.getAssessments().size());
	}
	
	// ==========================================
	// Condition 6: The total sum of points across 
	// all assessments cannot exceed 100 points
	// ==========================================
	@Test
	void AccumulatedPointsExceedsHundredRejected() {
		boolean dummy1 = course.createNewAssessment(
				"assignment", "a dummy value", 50, "doc", false, "", 
				"01/04/2027 09:00", "15/04/2027 23:59", "20/04/2027 23:59"
				);
		// Precondition: the list contains two valid assessments
		assertTrue(dummy1);
		boolean dummy2 = course.createNewAssessment(
				"quiz", "another dummy value", 49, "pdf", false, "", 
				"01/04/2027 09:00", "15/04/2027 23:59", "20/04/2027 23:59"
				);
		assertTrue(dummy2);
		assertEquals(2, course.getAssessments().size());
		// Precondition: total at this point should equal 99
		assertEquals(99, course.getTotalPoints());
		// Test (boundary) Points equals 101 (1 point above 100)
		assertFalse(
			course.createNewAssessment("assignment 2", "final project report", 2, "pdf", false, "", 
					"01/04/2027 09:00", "15/04/2027 23:59", "20/04/2027 23:59")
				);
		// The list remains size 2 after the assertion check fails.
		assertEquals(2, course.getAssessments().size());
	}
	
	@Test
	void AccumulatedPointsEqualsHundredAccepted() {
		boolean dummy1 = course.createNewAssessment(
				"assignment", "a dummy value", 50, "doc", false, "", 
				"01/04/2027 09:00", "15/04/2027 23:59", "20/04/2027 23:59"
				);
		// Precondition: the list contains two valid assessments
		assertTrue(dummy1);
		boolean dummy2 = course.createNewAssessment(
				"quiz", "another dummy value", 49, "pdf", false, "", 
				"01/04/2027 09:00", "15/04/2027 23:59", "20/04/2027 23:59"
				);
		assertTrue(dummy2);
		assertEquals(2, course.getAssessments().size());
		// Precondition: total at this point should equal 99
		assertEquals(99, course.getTotalPoints());
		// Test (boundary) total points equals 100
		assertTrue(
			course.createNewAssessment("assignment 3", "final project report", 1, "pdf", false, "", 
					"01/04/2027 09:00", "15/04/2027 23:59", "20/04/2027 23:59")
				);	
		// The list increases by 1
		assertEquals(3, course.getAssessments().size());
	}
	
	// ==========================================
	// Condition 7: Submission type must be one of "pdf", "zip" or "doc".
	// ==========================================	
	@Test
	void submissionTypeRejected() {
		// Precondition: assessment list must be empty
		assertEquals(0, course.getAssessments().size());
		// Test rejected file type "docx" (outside the allowed file types)
		assertFalse(
			course.createNewAssessment("assignment 1", "final project report", 20, "docx", false, "", 
				"01/04/2027 09:00", "15/04/2027 23:59", "20/04/2027 23:59")
			);	
		
	}
	// ==========================================
	// Condition 8: If an assessment is a group assessment, 
	// a grouping name must be provided. 
	// ==========================================	
	@Test 
	void missingGroupingNameRejected() {
		// Precondition: assessment list must be empty
		assertEquals(0, course.getAssessments().size());
		// Test group = true and groupingName is empty
		assertFalse(
			course.createNewAssessment("assignment 1", "final project report", 20, "doc", true, "", 
				"01/04/2027 09:00", "15/04/2027 23:59", "20/04/2027 23:59")
			);	
		// The list remains empty after the assertion check fails.
		assertEquals(0, course.getAssessments().size());
	}
	
	// ==========================================
	// Condition 9: Grouping name must be unique for each assessment. 
	// ==========================================
	@Test
	void duplicateGroupingNameRejected() {
		// Precondition: existing entry must exist
		boolean dummy = course.createNewAssessment(
			"assignment 1", "a dummy value", 20, "doc", true, "project teams", 
			"01/04/2027 09:00", "15/04/2027 23:59", "20/04/2027 23:59"
			);
		assertTrue(dummy);
		
		// Test duplicate grouping names
		assertFalse(
			course.createNewAssessment(
				"assignment 2", "final project report", 20, "pdf", true, "project teams", 
				"01/04/2027 09:00", "15/04/2027 23:59", "20/04/2027 23:59"
				)
			);
		// The list remains equal to 1 after the assertion check fails.
		assertEquals(1, course.getAssessments().size());
	}

	// ==========================================
	// Condition 10: Date times must use the 24-hour clock with STRICT validation (day and time boundary).
	// ==========================================
	@Test
	void invalidTimeDateTimeRejected() {
		// Precondition: assessment list must be empty
		assertEquals(0, course.getAssessments().size());
		// Test (boundary) 23:59 is the last valid time, so 24:00 is one minute past it.
		assertFalse(
			course.createNewAssessment("assignment 2", "final project report", 20, "doc", false, "", 
				"01/05/2027 09:00 AM", "15/05/2027 24:00", "20/05/2027 23:59")
			);	
		// The list remains empty after the assertion check fails.
		assertEquals(0, course.getAssessments().size());
	}
	@Test 
	void invalidDayDateTimeRejected() {
		// Precondition: assessment list must be empty
		assertEquals(0, course.getAssessments().size());
		// Test April has 30 days, so 31/04 is one day past the last valid day
		assertFalse(
			course.createNewAssessment("assignment 2", "final project report", 20, "doc", false, "", 
				"31/04/2027 09:00 AM", "15/05/2027 24:00", "20/05/2027 23:59")
			);
		// The list remains empty after the assertion check fails.
		assertEquals(0, course.getAssessments().size());
	}
	
	// ==========================================
	// Condition 11: Due date time must be after the publish date time, which must
	// be after the publish date time
	// ==========================================
	@Test
	void dueEqualToPublishRejected() {
		// Precondition: list is empty
		assertTrue(course.getAssessments().isEmpty());
		// Test (boundary): due date equals publish date, so it is not strictly after
		assertFalse(course.createNewAssessment("assignment 2", "final project report", 20, "pdf", false, "",
				"01/06/2027 09:00", "01/06/2027 09:00", "10/06/2027 23:59"));
		// The list remains empty and no "assignment 2" was added
		assertEquals(0, course.getAssessments().size());
	}
	
	@Test
	void hideBeforeDueRejected() {
		// Precondition: list is empty
		assertTrue(course.getAssessments().isEmpty());
		// Test (boundary): hide date is 1 minute before the due date
		assertFalse(course.createNewAssessment("assignment 2", "final project report", 20, "pdf", false, "",
				"01/06/2027 09:00", "05/06/2027 23:59", "05/06/2027 23:58"));
		// The list remains empty and no "assignment 2" was added
		assertEquals(0, course.getAssessments().size());
	}
	// ==========================================
	// Condition 12: All input valid
	// ==========================================
	@Test
	void validAssessmentCaseOne() {
		// Precondition: list is empty
		assertTrue(course.getAssessments().isEmpty());
		// Test (Boundary) maximum allowed points (50); hide is 1 minute after due, crossing midnight.
		assertTrue(course.createNewAssessment("quiz 1", "weekly quiz on java", 50, "pdf", false, "",
				"01/03/2027 09:00", "15/03/2027 23:59", "16/03/2027 00:00"));
		// List size increases by 1
		assertEquals(1, course.getAssessments().size());
	}
	@Test
	void validAssessmentCaseTwo() {
		// Precondition: list is empty
		assertTrue(course.getAssessments().isEmpty());
		// Test (Boundary) minimum allowed points (1); due date on a leap day (2028 is a leap year).
		assertTrue(course.createNewAssessment("group project", "build a web application", 1, "zip", true, "lab groups",
				"28/02/2028 09:00", "29/02/2028 17:00", "01/03/2028 17:00"));
		// List size increases by 1
		assertEquals(1, course.getAssessments().size());
	}
	@Test
	void validAssessmentCaseThree() {
		// Precondition: list is empty
		assertTrue(course.getAssessments().isEmpty());
		// Test (Boundary) publish, due and hide are each exactly 1 minute apart (smallest valid gaps).
		assertTrue(course.createNewAssessment("essay", "write a reflective essay", 30, "doc", false, "",
				"10/04/2027 12:00", "10/04/2027 12:01", "10/04/2027 12:02"));
		// List size increases by 1
		assertEquals(1, course.getAssessments().size());
	}
	
	
	
}
