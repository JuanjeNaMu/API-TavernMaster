package dam.saruman.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String index(){
        return "index.html";
    }
}
//PERMITE ACCEDER AL ARCHIVO INDEX.HTML ACCEDIENDO SOLO AL PUERTO localhost:8080 // logalhost:8080/index.html