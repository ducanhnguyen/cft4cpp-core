package com.fit.googletest;

import java.util.List;

public interface ISourceCodeGoogleTest {

    String getSourceCode();

    void setSourceCode(String code);

    String getFileName();

    void setFileName(String fileName);

    List<String> getIncludes();

    void setIncludes(List<String> includes);

}