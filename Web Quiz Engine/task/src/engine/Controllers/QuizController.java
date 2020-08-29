package engine.Controllers;

import engine.Entities.Quiz;

import engine.Repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
public class QuizController {

    @Autowired
    QuizRepository quizRepository;
    @Autowired
    QuizService quizService;

    @GetMapping(path = "/api/quizzes/{id}", produces = "application/json")
    public Object getApiQuiz(@PathVariable Integer id){
        if(quizRepository.existsById(id)){
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow();
            Map<String, Object> outmap = new TreeMap<>();
            outmap.put("id", id);
            outmap.put("title", quiz.getTitle());
            outmap.put("text", quiz.getText());
            outmap.put("options", quiz.getOptions());
            return outmap;
        }else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/api/quizzes", consumes = "application/json")
    public Map<String,Object> addQuizzies (@Valid @RequestBody Quiz quiz) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        quiz.setEmail(email);
        System.out.println(email);
        quiz = quizRepository.save(quiz);

        TreeMap<String,Object> outmap = new TreeMap<>();
        outmap.put("id", quiz.getId());
        outmap.put("title", quiz.getTitle());;
        outmap.put("text", quiz.getText());
        outmap.put("options", quiz.getOptions());
        return outmap;
    }

    @GetMapping(path = "/api/quizzes", produces = "application/json")
    public Object getAllQuiz(@RequestParam(defaultValue = "0") Integer page){

        Page<Quiz> quizzes = quizService.getAllQuiz(page);
        if(quizzes.hasContent()){
                return quizzes;
        }else{
            return new ResponseEntity(quizzes, HttpStatus.OK);
        }
    }


    @PostMapping(path = "/api/quizzes/{id}/solve", consumes = "application/json")
    public Object solveQuiz(@PathVariable Integer id, @RequestBody Map<String, ArrayList<Integer>> answer){
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow();
        if (quiz!=null){
            HashMap <String,Object> ans = new HashMap<>();
            if(quiz.solveQuiz(answer.get("answer"))){
                ans.put("success",true);
                ans.put("feedback", "Congratulations, you're right!");
            }else{
                ans.put("success",false);
                ans.put("feedback", "Wrong answer! Please, try again.");
            }
            return ans;

        }else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/api/quizzes/{id}")
    public ResponseEntity deleteQuiz(@PathVariable Integer id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userMail = authentication.getName();

        if (quizRepository.existsById(id)){
            if(quizRepository.findById(id).get().checkEmail().equalsIgnoreCase(userMail)) {
                quizRepository.deleteById(id);
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }else{
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
