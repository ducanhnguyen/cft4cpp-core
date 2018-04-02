/*
 * Student.h
 *
 *  Created on: Jan 8, 2018
 *      Author: Duc Anh Nguyen
 */

#ifndef SRC_STUDENT_H_
#define SRC_STUDENT_H_

#include <iostream>
#include <string>
#include <list>

using namespace std;

class Student {
private:
	string studentName;
	string studentID;

public:
	Student();
	virtual ~Student();

	const string& getStudentId() const {
		return studentID;
	}

	void setStudentId(const string& studentId) {
		studentID = studentId;
	}

	const string& getStudentName() const {
		return studentName;
	}

	void setStudentName(const string& studentName) {
		this->studentName = studentName;
	}
};

#endif /* SRC_STUDENT_H_ */
