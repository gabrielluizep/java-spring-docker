package eng.tele.std;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@SpringBootApplication
public class StdApplication {

	public static void main(String[] args) {
		SpringApplication.run(StdApplication.class, args);
	}

	@PostMapping("/register")
	public ResponseEntity<String> register() {
		System.err.println("cu");
		return new ResponseEntity<>("Registered", HttpStatus.OK);
	}
}
