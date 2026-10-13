package course;



import java.util.Arrays;
import java.util.List;
// needed for condition 7
import java.util.ArrayList;

// libraries needed for conditions 10 and 11
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;


class Assessment {
   private String name;
   private String description;
   private int points;
   private String submissionType;
   private boolean group;
   private String groupingName;
   private String publishDateTime;
   private String dueDateTime;
   private String hideDateTime;

   public Assessment(String name, String description, int points,
         String submissionType, boolean group, String groupingName,
         String publishDateTime, String dueDateTime, String hideDateTime) {
      this.name = name;
      this.description = description;
      this.points = points;
      this.submissionType = submissionType;
      this.group = group;
      this.groupingName = groupingName;
      this.publishDateTime = publishDateTime;
      this.dueDateTime = dueDateTime;
      this.hideDateTime = hideDateTime;
   }
   // we have added only necessary getters to conserve encapsulation
   public String getName() {
      return name;
   }
   public int getPoints() {
      return points;
   }
   public String getGroupingName() {
      return groupingName;
   }
}

public class Course {
   private List<Assessment> assessments = new ArrayList<>();

   public boolean createNewAssessment(String name, String description, int points,
         String submissionType, boolean group, String groupingName,
         String publishDateTime, String dueDateTime, String hideDateTime) {

         // if the assessment parameters meet the given conditions,
         // the function should create a new Assessment object,
         // add it in the assessments list and then return true.
         // else
         // the function should only return false.

      // Note: the boilerplate valid variable was scrapped because its ineffecient and useless.
      
      // condition 2: The assessment name cannot be blank.
      if (name == null || name.isBlank()) {
         return false;
      }
      // Condition 1: The assessment name must be unique to the course -
      // the assessment list cannot have assessments with the same name.
      if (assessments.stream().anyMatch(assessment -> name.equals(assessment.getName()))) {
         return false;
      }

      // Sanity check
      if (description == null || description.isEmpty()) {
         return false;
      }
      
      // Condition 3: The assessment description must have at least 3 words.
      // the regex \s+ matches one or more consecutive whitespace characters
      // regex generator is a nice tool 
      String[] words = description.trim().split("\\s+");
      if (words.length < 3) {
         return false;
      }
      // Condition 4: The assessment points must be greater than 0.
      // AND
      // Condition 5: No single assessment can exceed 50 points.
      if (points <= 0 || points > 50) {
         return false;
      }

      // Condition 6: The total sum of points across all assessments cannot exceed 
      // 100 points.
      int total = 0; 
      for (Assessment assessment : assessments) {
         // For loop iterates thru all items
         // gets each assessment's points and adds to total
         total += assessment.getPoints(); 
      }
      if (total + points > 100) {
         return false; // existing total + new points cannot exceed 100
      }
      
      // Condition 7: Submission type must be one of ("pdf", "zip", "doc").
      if (!(Arrays.asList("pdf", "zip", "doc")).contains(submissionType)) {
         // make list -> check if contains -> true or false
         return false;
      }

      // Condition 8: Assessment are either individual or group. 
      if (group) {  
      // If it is a group assessment, the grouping name must be provided.
         if (groupingName == null || groupingName.isEmpty()) {
            return false;
         }
         // Condition 9: The grouping name must be unique for each assessment
         if (assessments.stream().anyMatch(assessment -> groupingName.equals(assessment.getGroupingName()))) {
            return false;
         }
      }

      

      // create the format we will use to verify inputs
      DateTimeFormatter format = DateTimeFormatter
            .ofPattern("dd/MM/uuuu HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);


      // initialize variables to hold parsed date-time values
      LocalDateTime pubDate, dueDate, hideDate;

      try {
         // run the parsers and catch for errors
         pubDate = LocalDateTime.parse(publishDateTime, format);
         dueDate = LocalDateTime.parse(dueDateTime, format);
         hideDate = LocalDateTime.parse(hideDateTime, format);
      }  
      catch (Exception e) {
         // return false because if error caught then pubDate, dueDate and hideDatr
         // are not initialised thus throws runtime error.
         return false;
      }


      if (hideDate.isBefore(dueDate)) {
         return false; // is hide before due?
      }


      Assessment newAssessment = new Assessment(name, description, points,
            submissionType, group, groupingName,
            publishDateTime, dueDateTime, hideDateTime);
      
      this.assessments.add(newAssessment);
      return true; // guarantee that it returns true if all conditions are met
   }
}