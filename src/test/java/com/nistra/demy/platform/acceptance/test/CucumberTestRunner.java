package com.nistra.demy.platform.acceptance.test;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com/nistra/demy/platform/acceptance/test/steps",
                "com/nistra/demy/platform/institution/bdd/steps",
                "com/nistra/demy/platform/enrollment/bdd/steps"
        },
        plugin = {"pretty", "html:target/cucumber.html"}
)
public class CucumberTestRunner {
}
