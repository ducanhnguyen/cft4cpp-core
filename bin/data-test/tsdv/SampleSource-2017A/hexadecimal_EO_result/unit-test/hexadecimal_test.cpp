#include "gtest/gtest.h"
// other include 

TEST(check_mem, testpath1) {
	unsigned int start_address=0x0001300;
	unsigned int total_size=100;

	ReturnType EO_returnVar=OK;
	ReturnType returnVar=check_mem(start_address,total_size);
	
	EXPECT_EQ(EO_returnVar,returnVar);

}

TEST(check_mem, testpath2) {
	unsigned int start_address=0x0001000;
	unsigned int total_size=100000;

	ReturnType EO_returnVar=NG;
	ReturnType returnVar=check_mem(start_address,total_size);
	
	EXPECT_EQ(EO_returnVar,returnVar);

}



