package cmc.feelim;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    public String main1() throws Exception{
        return "hi";
    }

    @GetMapping("/hi2")
    public String main2() throws Exception{
        return "main";
    }

}
