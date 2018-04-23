/*
 * Faculty.h
 *
 *  Created on: Jan 8, 2018
 *      Author: Duc Anh Nguyen
 */

#ifndef SRC_FACULTY_H_
#define SRC_FACULTY_H_
#include <iostream>
#include <string>
#include "Department.h"
#include "list"
using namespace std;

class Faculty {
private:
	string facultyName;
	std::list<Department> departments;
public:
	Faculty();
	virtual ~Faculty();

	const string& getFacultyName() const {
		return facultyName;
	}

	void setFacultyName(const string& facultyName) {
		this->facultyName = facultyName;
	}

	const std::list<Department>& getDepartments() const {
		return departments;
	}

	void setDepartments(const std::list<Department>& departments) {
		this->departments = departments;
	}
};

#endif /* SRC_FACULTY_H_ */
