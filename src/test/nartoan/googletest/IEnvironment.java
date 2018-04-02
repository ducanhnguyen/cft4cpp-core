package test.nartoan.googletest;

import com.fit.googletest.UnitTestProject;

import java.io.File;

public interface IEnvironment {

    /**
     * Chuẩn bị môi trường để biên dịch và chạy
     *
     * @param unitTestFolder    folder project test cần chạy
     * @param compliationFolder Thư mục chứa Dev-Cpp
     * @throws Exception
     */
    public void setEnvironment(UnitTestProject unitTestProject, File compliationFolder, File z3Solver) throws Exception;
}
