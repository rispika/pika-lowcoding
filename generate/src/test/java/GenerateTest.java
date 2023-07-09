import com.pika.generate.GenerateApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

@SpringBootTest(classes = GenerateApplication.class)
public class GenerateTest {

    @Test
    public void run() throws IOException {
        ClassPathResource resource = new ClassPathResource("/templates");
        System.out.println(resource.getPath());
        File file = resource.getFile();
        System.out.println(file.getAbsolutePath());
    }

}
