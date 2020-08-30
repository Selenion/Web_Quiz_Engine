package engine.Services;

import engine.Entities.Quiz;
import engine.Repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class QuizService {

    @Autowired
    QuizRepository quizRepository;

    public Page<Quiz> getAllQuiz(Integer page){
        Pageable paging = PageRequest.of(page, 10);
        Page<Quiz> pageresult = quizRepository.findAll(paging);
            return pageresult;
    }
}
