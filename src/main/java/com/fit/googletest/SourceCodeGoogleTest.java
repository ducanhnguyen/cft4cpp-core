package com.fit.googletest;

import java.util.ArrayList;
import java.util.List;

public class SourceCodeGoogleTest implements ISourceCodeGoogleTest {
    /**
     * Name of file will save source code
     */
    private String fileName;

    /**
     * Headers
     */
    private List<String> includes = new ArrayList<>();

    /**
     * Source code except for headers
     */
    private String sourcecode;

    public SourceCodeGoogleTest(String fileName, List<String> includes, String sourcecode) {
        this.fileName = fileName;
        this.includes = includes;
        this.sourcecode = sourcecode;
    }

    @Override
    public String getSourceCode() {
        return sourcecode;
    }

    @Override
    public void setSourceCode(String code) {
        sourcecode = code;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<String> getIncludes() {
        return includes;
    }

    @Override
    public void setIncludes(List<String> include) {
        includes = include;
    }
}
