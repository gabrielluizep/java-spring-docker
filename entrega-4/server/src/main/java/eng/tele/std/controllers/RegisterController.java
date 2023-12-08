package eng.tele.std.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/register", consumes = "application/json")
public class RegisterController {
  @PostMapping
  public ResponseEntity<String> register(@RequestBody Map<String, String> params) {
    System.out.println(params);

    return new ResponseEntity<String>("response text", HttpStatus.OK);
  }
}
