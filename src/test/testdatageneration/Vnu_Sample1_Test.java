package test.testdatageneration;

import com.fit.config.IFunctionConfig;
import com.fit.config.Paths;
import com.fit.testdatagen.htmlreport.FuntionTestReportGUI;
import org.junit.Assert;
import org.junit.Test;

import javax.sound.sampled.LineUnavailableException;

public class Vnu_Sample1_Test extends AbstractJUnitTest {
    @Test
    public void test1() throws LineUnavailableException {

        Assert.assertEquals(true,
                generateTestdata(Paths.SAMPLE01, "StackLinkedList::push(Node*)", null, IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test2() throws LineUnavailableException {

        Assert.assertEquals(true,
                generateTestdata(Paths.SAMPLE01, "StackLinkedList::pop1()", null, IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test3() throws LineUnavailableException {

        Assert.assertEquals(true,
                generateTestdata(Paths.SAMPLE01, "StackLinkedList::pop2()", null, IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test4() throws LineUnavailableException {

        Assert.assertEquals(true, generateTestdata(Paths.SAMPLE01, "StackLinkedList::destroyList()", null,
                IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test5() throws LineUnavailableException {

        Assert.assertEquals(true,
                generateTestdata(Paths.SAMPLE01, "disp(Node*)", null, IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }
}
