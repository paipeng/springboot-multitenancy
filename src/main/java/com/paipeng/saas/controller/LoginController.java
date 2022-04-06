package com.paipeng.saas.controller;
import com.paipeng.saas.tenant.model.User;
import com.paipeng.saas.tenant.service.UserService;
import com.sun.istack.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/")
public class LoginController {
    private final static Logger logger = LogManager.getLogger(LoginController.class.getSimpleName());
    @Autowired
    private UserService userService;

    @PostMapping(value = "/login2", produces = {"application/json;charset=UTF-8"})
    public User login(@NotNull @RequestBody User user) throws Exception {
        logger.trace("login" + user);
        return userService.login(user);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/logout2")
    public void logout() {
        logger.info("logout2");
        userService.logout();
        //response.setStatus(HttpStatus.NO_CONTENT.value());
        //return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping(value = "/register", produces = {"application/json;charset=UTF-8"})
    public User register(@NotNull @RequestBody User user) throws Exception {
        return userService.register(user);
    }

}
