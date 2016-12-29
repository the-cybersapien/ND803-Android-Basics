import java.util.Arrays;

/**
 * ReportCard class for Udacity Android for beginners ND Project-5
 * This ReportCard class follows the CBSE-CCE/Indian System of education, which deals with education upto the 10th grade.
 * there are 5 main subjects,
 * First Language (English)
 * Second Language(region/choice dependent)
 * Mathematics
 * Science (equal weightage to Physics/Chemistry/Biology)
 * Social Science(equal weightage to Economics, Civil Science, History and Geography)
 *
 * The Grade system is as follows:
 * Marks Range | Grade | Grade Point
 * >90         | A1    | 10
 * 80-90       | A2    | 9
 * 70-80       | B1    | 8
 * 60-70       | B2    | 7
 * 50-60       | C1    | 6
 * 40-50       | C2    | 5
 * 33-40       | D     | 4
 * <33         | E     | -
 *
 * The marks of 5 different subjects shall be stored in variables:
 * marksEnglish, marksLanguage2, marksMathematics, marksScience, marksSocialScience
 * Sequence of gradePoints and grades in 2 Arrays:
 * {English, secondLanguage, Mathematics, Science, Social Science}
 */
public class ReportCard
{
    private String studentName;
    final private int NUMBER_OF_SUBJECTS = 5;
    /*5 subjects to store marks for*/
    private float marksMathematics;
    private float marksEnglish;
    private float marksLanguage2;
    private float marksScience;
    private float marksSocialScience;

    private int gradePointArray[] = new int[NUMBER_OF_SUBJECTS];
    private String gradeArray[] = new String[NUMBER_OF_SUBJECTS];

    /* The Final CGPA of the student*/
    private float CGPA;

    /**
     * Constructor for the ReportCard class
     * The constructor Initializes marks as 0 for all subjects until the marks are added by a teacher
     * @param studentName The name of the student for whom to create the report
     */
    public ReportCard(String studentName)
    {
        this.studentName = studentName;
        setEnglishMarks(0);
        setMathematicsMarks(0);
        setScienceMarks(0);
        setSecondLanguageMarks(0);
        setSocialScienceMarks(0);
    }

    /**
     * Sets the marks for English
     * @param marks for English subject
     */
    public void setEnglishMarks(float marks){
        this.marksEnglish =marks;
        gradePointArray[0] = getGradePoint(marks);
        gradeArray[0] = getGrade(gradePointArray[0]);
    }

    /**
     * Sets the marks for the Second Language
     * @param marks for the second Language
     */
    public void setSecondLanguageMarks(float marks){
        this.marksLanguage2 = marks;
        gradePointArray[1] = getGradePoint(marks);
        gradeArray[1] = getGrade(gradePointArray[1]);
    }

    /**
     * Sets the marks for Mathematics
     * @param marks for Mathematics
     */
    public void setMathematicsMarks(float marks){
        this.marksMathematics = marks;
        gradePointArray[2] = getGradePoint(marks);
        gradeArray[2] = getGrade(gradePointArray[2]);
    }

    /**
     * Sets the marks for Science
     * @param marks for Science
     */
    public void setScienceMarks(float marks){
        this.marksScience = marks;
        gradePointArray[3] = getGradePoint(marks);
        gradeArray[3] = getGrade(gradePointArray[3]);
    }

    /**
     * Sets the marks for Social Science
     * @param marks for Social Science
     */
    public void setSocialScienceMarks(float marks){
        this.marksSocialScience = marks;
        gradePointArray[4] = getGradePoint(marks);
        gradeArray[4] = getGrade(gradePointArray[4]);
    }

    /**
     * Gets the marks for Mathematics
     */
    public float getMarksMathematics() {
        return marksMathematics;
    }

    /**
     * Gets the marks for English Language
     */
    public float getMarksEnglish() {
        return marksEnglish;
    }

    /**
     * Gets the marks for Second Language
     */
    public float getMarksLanguage2() {
        return marksLanguage2;
    }

    /**
     * Gets the marks for Science
     */
    public float getMarksScience() {
        return marksScience;
    }

    /**
     * Gets the marks for Social Science
     */
    public float getMarksSocialScience() {
        return marksSocialScience;
    }

    /**
     * Return the gradePoint for given parameters
     * @param marks checks these marks
     * @return grade of the subject
     */
    private int getGradePoint(float marks){
        if(marks>90){
            return 10;
        } else if(marks > 80){
            return 9;
        } else if(marks > 70){
            return 8;
        } else if(marks > 60){
            return 7;
        } else if(marks > 50){
            return 6;
        } else if(marks > 40){
            return 5;
        } else if(marks > 33){
            return 4;
        } else {
            return 0;
        }
    }

    /**
     * Return the grade for given parameters
     * @param gradePoint checks these marks
     * @return grade of the subject
     */
    private String getGrade(int gradePoint){
        switch (gradePoint){
            case 10:
                return "A1";
            case 9:
                return "A2";
            case 8:
                return "B1";
            case 7:
                return "B2";
            case 6:
                return "C1";
            case 5:
                return "C2";
            case 4:
                return "D";
            default:
                return "E1";
        }
    }

    /**
     * Calculates the marks for the student
     */
    public void calculateCGPA(){
        float cgpa = 0;
        for (int gradePoint : gradePointArray) {
            cgpa += gradePoint;
        }
        cgpa /= 5;
        this.CGPA = cgpa;
    }

    /**
     * Returns the CGPA of the student
     */
    public float getCGPA(){
        return CGPA;
    }

    /**
     * Returns the complete details of the class as a String
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder("ReportCard{");
        sb.append("Student\'s Name='").append(studentName).append('\'');
        sb.append("\n NUMBER OF SUBJECTS=").append(NUMBER_OF_SUBJECTS);
        sb.append("\n marks in English=").append(marksEnglish).append(" Grade Point: ").append(gradePointArray[0]).append(" Grade: ").append(gradeArray[0]);
        sb.append("\n marks in Language2=").append(marksLanguage2).append(" Grade Point: ").append(gradePointArray[1]).append(" Grade: ").append(gradeArray[1]);
        sb.append("\n marks in Mathematics=").append(marksMathematics).append(" Grade Point: ").append(gradePointArray[2]).append(" Grade: ").append(gradeArray[2]);
        sb.append("\n marks in Science=").append(marksScience).append(" Grade Point: ").append(gradePointArray[3]).append(" Grade: ").append(gradeArray[3]);
        sb.append("\n marks in SocialScience=").append(marksSocialScience).append(" Grade Point: ").append(gradePointArray[4]).append(" Grade: ").append(gradeArray[4]);
        sb.append("\n CGPA=").append(CGPA);
        sb.append('}');
        return sb.toString();
    }
}
