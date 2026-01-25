package com.example.Cinema3D.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packages = "com.example.Cinema3D")
public class ArchitectureTest {

    @ArchTest
    static final ArchRule rest_controllers_should_not_depend_on_entities =
            ArchRuleDefinition.noClasses()
                    .that()
                    .areAnnotatedWith(RestController.class)
                    .and()
                    .doNotHaveSimpleName("SeatController")
                    .and()
                    .doNotHaveSimpleName("ScreeningSeatController")
                    .should()
                    .dependOnClassesThat()
                    .areAnnotatedWith(Entity.class);


    @ArchTest
    static final ArchRule services_should_be_in_service_package =
            ArchRuleDefinition.classes()
                    .that()
                    .areAnnotatedWith(Service.class)
                    .should()
                    .resideInAPackage("..service..")
                    .because("Service classes must be located in the service package");
}
