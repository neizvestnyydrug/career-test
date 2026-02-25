package ru.project.careertest.controller;

import ru.project.careertest.service.QRCodeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @Autowired
    private QRCodeService qrCodeService;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        String sessionId = session.getId();
        model.addAttribute("sessionId", sessionId);

        // Генерируем QR-код для главной страницы
        String qrCodeBase64 = qrCodeService.generateQRCodeBase64();
        model.addAttribute("qrCode", qrCodeBase64);

        return "index";
    }

    @GetMapping("/test/{userType}")
    public String test(@PathVariable String userType, Model model, HttpSession session) {
        model.addAttribute("userType", userType);
        model.addAttribute("sessionId", session.getId());

        String title = "";
        switch (userType) {
            case "schoolboy":
                title = "Тест для школьников и абитуриентов";
                break;
            case "student":
                title = "Тест для студентов";
                break;
            case "working":
                title = "Тест для работающих";
                break;
        }
        model.addAttribute("title", title);

        return "test";
    }

    @GetMapping("/result")
    public String result() {
        return "result";
    }
}
