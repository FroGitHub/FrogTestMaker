package com.example.demo.testFinderSite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {

    private final AnswersRepository answersRepository;

    @Autowired
    public AnswerService(AnswersRepository answersRepository) {
        this.answersRepository = answersRepository;
    }

    public List<Answers> getAnswers() {
        return answersRepository.findAll();
    }

    public void saveAns(Answers answer) {
        answersRepository.save(answer);
    }

    public void deleteById(Long id) {
        answersRepository.deleteById(id);
    }

    public Answers getAnswerById(Long id) {
        return answersRepository.getAnswerById(id);
    }
}
