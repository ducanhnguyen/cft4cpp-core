/*
 * Module.h
 *
 *  Created on: Jan 8, 2018
 *      Author: Duc Anh Nguyen
 */

#ifndef SRC_MODULE_H_
#define SRC_MODULE_H_

#include <iostream>
#include <string>
#include <list>
#include "Course.h"
using namespace std;

class Module {
private:
	string moduleName;
	std::list<Course> courses;
public:
	Module();
	virtual ~Module();

	const string& getModuleName() const {
		return moduleName;
	}

	void setModuleName(const string& moduleName) {
		this->moduleName = moduleName;
	}
};

#endif /* SRC_MODULE_H_ */
