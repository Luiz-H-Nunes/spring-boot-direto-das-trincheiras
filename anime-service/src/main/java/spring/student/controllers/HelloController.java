package spring.student.controllers;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/grettings")
public class HelloController {

    @PostMapping
    public String save(@RequestBody String name)
    {
        return name + " Is saved";
    }


}
