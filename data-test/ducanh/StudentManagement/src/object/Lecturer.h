/*
 * Lecturer.h
 *
 *  Created on: Jan 8, 2018
 *      Author: Duc Anh Nguyen
 */

#ifndef SRC_LECTURER_H_
#define SRC_LECTURER_H_

#include <iostream>
#include <string>
using namespace std;

class Lecturer {
private:
	string lecturerName;
	string degree;
	string major;
public:
	Lecturer();
	virtual ~Lecturer();

	const string& getDegree() const {
		return degree;
	}

	void setDegree(const string& degree) {
		this->degree = degree;
	}

	const string& getLecturerName() const {
		return lecturerName;
	}

	void setLecturerName(const string& lecturerName) {
		this->lecturerName = lecturerName;
	}

	const string& getMajor() const {
		return major;
	}

	void setMajor(const string& major) {
		this->major = major;
	}
};

#endif /* SRC_LECTURER_H_ */
