/*
 * GradeReport.h
 *
 *  Created on: Jan 8, 2018
 *      Author: Duc Anh Nguyen
 */

#ifndef SRC_GRADEREPORT_H_
#define SRC_GRADEREPORT_H_

#include "Student.h"
#include "Course.h"
/**
 * Represent a grade report
 */
class GradeReport {
private:
	float grade;
	Student* student;
	Course* course;
public:
	GradeReport();
	virtual ~GradeReport();

	float getGrade() const {
		return grade;
	}

	void setGrade(float grade) {
		this->grade = grade;
	}
};

#endif /* SRC_GRADEREPORT_H_ */
