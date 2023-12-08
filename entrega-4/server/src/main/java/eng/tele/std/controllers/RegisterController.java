package eng.tele.std.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/register")
public class RegisterController {
  // print everything in the body of the request
  @PostMapping
  public ResponseEntity<String> register(@RequestParam int id) {
    System.out.println(id);

    return new ResponseEntity<String>("", HttpStatus.OK);
  }

}
