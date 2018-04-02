/*
 * CourceClass.h
 *
 *  Created on: Jan 8, 2018
 *      Author: Duc Anh Nguyen
 */

#ifndef SRC_OBJECT_COURCECLASS_H_
#define SRC_OBJECT_COURCECLASS_H_

#include "Student.h"
#include "CourseClassInterface.h"
#include <list>
/**
 * For example: K56CLC
 */
class CourceClass {
private:
	list<Student*> students;
public:
	CourceClass();
	virtual ~CourceClass();

	const list<Student*>& getStudents() const {
		return students;
	}

	void setStudents(const list<Student*>& students) {
		this->students = students;
	}

	list<Student*> findStudent(string studentID, int searchType);
};

#endif /* SRC_OBJECT_COURCECLASS_H_ */
