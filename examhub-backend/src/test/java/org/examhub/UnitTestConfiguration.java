package org.examhub;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * Test configuration for unit tests which prevent Liquibase from executing database migration.
 *
 * @author Hieu Do
 */
@Configuration
@EnableAutoConfiguration(exclude = {LiquibaseAutoConfiguration.class})
@ComponentScan(excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ExamHubApplication.class)
})
public class UnitTestConfiguration {
}
