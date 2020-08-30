package engine.Controllers;

import engine.Entities.Users;
import engine.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.HashMap;


@RestController
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/api/register", consumes = "application/json")
    public ResponseEntity addUser(@Valid @RequestBody HashMap<String,String> map){
        System.out.println(map.get("email")+" ; "+map.get("password"));
        if(!userRepository.existsById(map.get("email").toLowerCase())
                && map.get("email").matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")
                && map.get("password").length()>=5){
            userRepository.save(new Users(map.get("email").toLowerCase(),new BCryptPasswordEncoder().encode(map.get("password"))));
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
