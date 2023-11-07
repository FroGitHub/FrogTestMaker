package com.example.demo.testFinderSite;


import com.example.demo.API.GPTAPP;
import com.example.demo.API.OCRAPP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping
public class pageController {

    private static final Logger logger = LoggerFactory.getLogger(pageController.class);
    private OCRAPP ocrapp = new OCRAPP();
    private GPTAPP gptapp = new GPTAPP();
    private AnswerService answerService;

    @Autowired
    public pageController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping("/home")
    public String homePage(Model model){
        List<Answers> answersList = answerService.getAnswers();
        model.addAttribute("answers", answersList);
        return "home";
    }

    @GetMapping("/add")
    public String showAddPage(Model model){
        model.addAttribute("ans", new Answers());
        return "add";
    }

    @PostMapping("/save")
    public String saveAns(@ModelAttribute Answers answer, @RequestParam("imageFile")MultipartFile imgFile){

        if (!imgFile.isEmpty()){
            try {
                String nameFile = UUID.randomUUID().toString() + "_" + imgFile.getOriginalFilename();
                Path pathFile = Paths.get("src/main/resources/static/img/" + nameFile);

                Files.write(pathFile, imgFile.getBytes());                           // add img in the folder img

                try {
                    String questionFromIMG = ocrapp.getText(nameFile);               // get text from img

                    logger.warn("An question "+ questionFromIMG);                    // Log question

                    String answerFromGPT = gptapp.getAnswerFromGPT(questionFromIMG); //Answer -> GPT

                    logger.info("GPT answers " + answerFromGPT);                     // Log answer

                    answer.setAnswer(answerFromGPT);                                 // save answer in answer`s obj
                    answer.setImg("/img/" + nameFile);                               // save img in answer`s obj
                    answerService.saveAns(answer);                                   // save answer obj
                } catch (Exception e){
                   Files.delete(pathFile);                                // delete img from folder if GPT can`t answer
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "redirect:/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteAnAnswer(@PathVariable("id") Long id){
        Answers answer = answerService.getAnswerById(id);
        try {
            logger.info("deleted answer on id: " + answer.getId().toString());               // Log to delete
            Files.delete(Paths.get("src/main/resources/static" + answer.getImg()));     // deleting img from dir.
            answerService.deleteById(id);                                                   // deleting answer
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/home";
    }


}
