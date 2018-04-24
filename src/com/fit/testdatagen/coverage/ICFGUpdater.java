package com.fit.testdatagen.coverage;

import com.fit.cfg.ICFG;
import com.fit.cfg.testpath.ITestpathInCFG;

/**
 * Update visited statement in CFG
 *
 * @author ducanhnguyen
 */
public interface ICFGUpdater {
	/**
	 * Update visited nodes in CFG
	 */
	void updateVisitedNodes();

	String[] getTestpath();

	void setTestpath(String[] testpath);

	ICFG getCfg();

	void setCfg(ICFG cfg);

	ITestpathInCFG getUpdatedCFGNodes();

	void setUpdatedCFGNodes(ITestpathInCFG updatedCFGNodes);

	/**
	 * Reset the state by removing the states of the latest test path
	 */
	public void unrollChangesOfTheLatestPath();
}