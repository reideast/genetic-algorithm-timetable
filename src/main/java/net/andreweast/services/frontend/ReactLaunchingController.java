package net.andreweast.services.frontend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReactLaunchingController {
    @Value("${spring.application.name}")
    String appName;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("appName", appName);
        return "index"; // Loads "index.html": The return value of a RequestMapping method under a @Controller, and .html is the file prefix by default
    }
}
