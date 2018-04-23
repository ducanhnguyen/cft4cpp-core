#include "gtest/gtest.h"
#include "./return_bool/return_bool.h"
#include "./return_bool/main.cpp"
#include "./return_bool/return_bool.cpp"
TEST(return_bool_test0, testpath1) {
	int ns=0;

	bool EO_returnVar=false;
	
	bool returnVar=return_bool_test(ns);
	
	EXPECT_EQ(EO_returnVar,returnVar);

}

TEST(return_bool_test0, testpath2) {
	int ns=1;

	bool EO_returnVar=true;
	
	bool returnVar=return_bool_test(ns);
	
	EXPECT_EQ(EO_returnVar,returnVar);

}



