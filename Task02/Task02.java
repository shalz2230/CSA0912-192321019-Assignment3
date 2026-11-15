
import java.io.IOException;

class ExamException extends Exception {
    ExamException(String message) {
        super(message);
    }

    ExamException(String message, Throwable cause) {
        super(message, cause);
    }
}

class ExamClosedException extends ExamException {
    ExamClosedException(String message) {
        super(message);
    }
}

class CourseNotRegisteredException extends ExamException {
    CourseNotRegisteredException(String message) {
        super(message);
    }
}

class SubmissionStorageException extends ExamException {
    SubmissionStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

class SubmissionStorage {

    void save(String fileName, int sizeKb)
            throws SubmissionStorageException {

        try {
            if (sizeKb > 500) {
                throw new IOException("Disk quota exceeded");
            }

            System.out.println("Saved " + fileName);

        } catch (IOException e) {
            throw new SubmissionStorageException(
                "Could not store " + fileName, e
            );
        }
    }
}

class ExamService {

    SubmissionStorage storage = new SubmissionStorage();

    void submit(String fileName, int sizeKb,
                boolean examOpen, boolean registered)
                throws ExamException {

        if (!examOpen) {
            throw new ExamClosedException(
                "Exam submission is closed"
            );
        }

        if (!registered) {
            throw new CourseNotRegisteredException(
                "Student is not registered for this course"
            );
        }

        storage.save(fileName, sizeKb);
    }
}

class ExamController {

    ExamService service = new ExamService();

    void submitAnswer(String fileName, int sizeKb,
                      boolean examOpen, boolean registered) {
        try {
            service.submit(fileName, sizeKb, examOpen, registered);

        } catch (ExamException e) {
            System.out.println("Error: " + e.getMessage());

            if (e.getCause() != null) {
                System.out.println("Cause: " +
                    e.getCause().getMessage());
            }
        }
    }
}

public class Task02 {

    public static void main(String[] args) {

        ExamController controller = new ExamController();

        System.out.println("ONLINE EXAM PORTAL");
        System.out.println("==================");

        System.out.println("\nTest 1: Valid Submission");
        controller.submitAnswer(
            "answer01.pdf", 300, true, true
        );

        System.out.println("\nTest 2: Large Answer File");
        controller.submitAnswer(
            "answer02.pdf", 600, true, true
        );

        System.out.println("\nTest 3: Exam Closed");
        controller.submitAnswer(
            "answer03.pdf", 200, false, true
        );

        System.out.println("\nTest 4: Course Not Registered");
        controller.submitAnswer(
            "answer04.pdf", 200, true, false
        );

        System.out.println("\nTESTING COMPLETED");
    }
}
