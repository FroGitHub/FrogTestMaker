package com.example.demo.testFinderSite;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswersRepository extends JpaRepository<Answers, Long> {
    void deleteById(Long id);

    Answers getAnswerById(Long id);
}
