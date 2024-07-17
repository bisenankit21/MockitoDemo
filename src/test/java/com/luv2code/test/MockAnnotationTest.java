package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.dao.ApplicationDao;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import com.luv2code.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class MockAnnotationTest {
    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent studentOne;

    @Autowired
    StudentGrades studentGrades;
    // @Mock
    @MockBean
    private ApplicationDao applicationDao;

  //  @InjectMocks   //will only inject dependency annotated with @Mock or @Spy
    @Autowired
    private ApplicationService applicationService;

    @BeforeEach
    public void beforeEach(){
        studentOne.setFirstname("Ankit");
        studentOne.setLastname("Bisen");
        studentOne.setEmailAddress("anna@test.com");
        studentOne.setStudentGrades(studentGrades);
    }

    @DisplayName("When and Verify")
    @Test
    public void assertEqualsTestAddGrades(){
        when(applicationDao.addGradeResultsForSingleClass(
                studentGrades.getMathGradeResults())).thenReturn(100.00);

        assertEquals(100.00,applicationDao.addGradeResultsForSingleClass(
                studentGrades.getMathGradeResults()));

        verify(applicationDao).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());  //verify the DAC method was called
        verify(applicationDao , times(1)).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());
    }

    @DisplayName("Find GPA")
    @Test
    public void assertEqualsTestFindGpa(){
        when(applicationDao.findGradePointAverage(studentGrades.getMathGradeResults()))
                .thenReturn(88.31);
        assertEquals(88.31,applicationService.findGradePointAverage(
                studentOne.getStudentGrades().getMathGradeResults()));

    }
    @DisplayName("not null")
    @Test
    public void testAssertNotNull(){
        when(applicationDao.checkNull(studentGrades.getMathGradeResults())).thenReturn(true);
        assertNotNull(applicationService.checkNull(studentOne.getStudentGrades().getMathGradeResults()),"Object should not be null");
    }

    @DisplayName("Throw run time execption")
    @Test
    public void throRunTimeError(){
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");
        doThrow(new RuntimeException()).when(applicationDao).checkNull(nullStudent);

        assertThrows(RuntimeException.class,()->{
            applicationService.checkNull(nullStudent);
        });

        verify(applicationDao,times(1)).checkNull(nullStudent);
    }


    @DisplayName("Multiple stubbing")
    @Test
    public void stubbingConsectutiveCalls(){
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");
        when(applicationDao.checkNull(nullStudent))
                .thenThrow(new RuntimeException())
                .thenReturn("Do not throw execption second time");


        assertThrows(RuntimeException.class,()->{applicationService.checkNull(nullStudent);
        });
        assertEquals("Do not throw execption second time", applicationService.checkNull(nullStudent));

        verify(applicationDao,times(2)).checkNull(nullStudent);
    }


}
