/*
 * Department.h
 *
 *  Created on: Jan 8, 2018
 *      Author: Duc Anh Nguyen
 */

#ifndef SRC_DEPARTMENT_H_
#define SRC_DEPARTMENT_H_

#include <iostream>
#include <string>
#include "Module.h"
#include "Lecturer.h"
using namespace std;

class Department {
private:
	string departmentName;
	std::list<Module> modules;
	std::list<Lecturer> lecturers;
public:
	Department();
	virtual ~Department();

	const string& getDepartmentName() const {
		return departmentName;
	}

	void setDepartmentName(const string& departmentName) {
		this->departmentName = departmentName;
	}

	const std::list<Lecturer>& getLecturers() const {
		return lecturers;
	}

	void setLecturers(const std::list<Lecturer>& lecturers) {
		this->lecturers = lecturers;
	}

	const std::list<Module>& getModules() const {
		return modules;
	}

	void setModules(const std::list<Module>& modules) {
		this->modules = modules;
	}
};

#endif /* SRC_DEPARTMENT_H_ */
