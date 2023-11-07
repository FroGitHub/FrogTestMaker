package com.example.demo.API;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class OCRAPP {
    private static Tesseract tesseract;
    private static File imgFile;

    public OCRAPP() {
    }

    public String getText(String nameFile) {

        String input = "src/main/resources/static/img/" + nameFile;       // path

        tesseract = new Tesseract();                                      // Create tesseract`s object
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata"); // path to folder of the languages

        imgFile = new File(input);

        tesseract.setLanguage("eng+ukr");                                 // set a language
        tesseract.setPageSegMode(6);                                      // number of a squares of scanner
        tesseract.setOcrEngineMode(1);                                    // idk

        try {

            String fullText = tesseract.doOCR(imgFile);                   // scanning
            return fullText;
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return "error OCR";
    }

}
