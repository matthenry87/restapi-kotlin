package com.matthenry87.restapi.exception;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/test")
class TestController {

    private final Pojo mock;

    TestController(Pojo mock) {
        this.mock = mock;
    }

    @PostMapping
    public Object post(@RequestBody @Valid Pojo pojo) {

        mock.foo();

        return null;
    }

}

@Component
class Pojo {

    @NotEmpty
    private String foo;

    private Status status;

    public Object foo() { return null; }

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

enum Status {

    OPEN
}