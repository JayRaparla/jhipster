package com.jay.statements.cucumber.stepdefs;

import com.jay.statements.MyappApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = MyappApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
