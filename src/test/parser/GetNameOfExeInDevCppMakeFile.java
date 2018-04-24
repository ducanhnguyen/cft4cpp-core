package test.parser;

import com.fit.config.Paths;
import com.fit.utils.Utils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Lấy tên tệp exe định nghĩa trong makefile
 */
public class GetNameOfExeInDevCppMakeFile {

    @Test
    public void test1() {
        String makefileName = Utils
                .getNameOfExeInDevCppMakefile(Paths.GET_NAME_OF_EXE_IN_DEVCPP_MAKEFILE + "Makefile1.win");

        String EO = "Project1.exe";
        Assert.assertEquals(EO, makefileName);
    }

    @Test
    public void test2() {
        String makefileName = Utils
                .getNameOfExeInDevCppMakefile(Paths.GET_NAME_OF_EXE_IN_DEVCPP_MAKEFILE + "Makefile2.win");

        String EO = "BTL.exe";
        Assert.assertEquals(EO, makefileName);
    }
}
