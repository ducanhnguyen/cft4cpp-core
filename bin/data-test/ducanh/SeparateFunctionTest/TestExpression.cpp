#include <iostream>
#include "Header.h"
#include "Source.cpp"


//Loi goi ham trong If
//Loi goi ham trong bieu thuc
int test5() {
	int a = 5;
	if ( add(a + add(a)) + 6 > 5) {
		if(add(a) > 6)
			return 1;
	} else {
		return 0;
	}
	
}

//Loi goi ham trong for, while, do {} while
int test6() {
	int i = 5;
	for (; i < add(6);)
	{
		a++;
	}
	while (sub(a) > 5) {
		a--;
	}
	return a;
}
int test7() {
	int a = add(3);
	a = add(5);
	add(4);
	int b = add(a+add(a));
	c = 2 + add(a);
}
