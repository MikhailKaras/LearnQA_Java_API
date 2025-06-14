import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class Ex10Test {
    String hello = "Hello, the beautiful world";
    @Test
    public void CheckLength()
    {
        assertTrue(hello.length() > 15, "Test passed unsuccessfully");
    }

}
