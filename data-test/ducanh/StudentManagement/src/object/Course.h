/*
 * Course.h
 *
 *  Created on: Jan 8, 2018
 *      Author: Duc Anh Nguyen
 */

#ifndef SRC_COURSE_H_
#define SRC_COURSE_H_

#include <iostream>
#include <string>
#include <list>
using namespace std;

class Course {
private:
	string courseName;
	int credit;
	std::list<Course> prerequisites;
	string courseDescription;

public:
	Course();
	virtual ~Course();

	const string& getCourseDescription() const {
		return courseDescription;
	}

	void setCourseDescription(const string& courseDescription) {
		this->courseDescription = courseDescription;
	}

	const string& getCourseName() const {
		return courseName;
	}

	void setCourseName(const string& courseName) {
		this->courseName = courseName;
	}

	int getCredit() const {
		return credit;
	}

	void setCredit(int credit) {
		this->credit = credit;
	}

	const std::list<Course>& getPrerequisites() const {
		return prerequisites;
	}

	void setPrerequisites(const std::list<Course>& prerequisites) {
		this->prerequisites = prerequisites;
	}
};

#endif /* SRC_COURSE_H_ */
