#include "gtest/gtest.h"
// other include 

TEST(global_variable_test, testpath1) {
	bool EO_flag = true;
	int EO_returnVar=0;
	int returnVar=global_variable_test(p);
	
	EXPECT_EQ(EO_flag,flag);
	EXPECT_EQ(EO_returnVar,returnVar);
	EXPECT_EQ(gp.x,p.x);
	EXPECT_EQ(gp.y,p.y);
	EXPECT_EQ(gp.z,p.z);

}



