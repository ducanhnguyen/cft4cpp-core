package com.fit.normalizer;

import interfaces.INormalizer;

import java.io.File;

/**
 * Normalize make file of Dev-Cpp, Visual Studio, etc.
 *
 * @author ducanhnguyen
 */
public interface IMakefileNormalizer extends INormalizer {
    /**
     * Get the path of make file
     *
     * @return
     */
    File getMakefilePath();

    /**
     * Set make file path
     *
     * @param makefilePath Represent the path of make file
     */
    void setMakefilePath(File makefilePath);
}
