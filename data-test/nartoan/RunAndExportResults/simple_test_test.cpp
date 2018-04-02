#include "gtest/gtest.h"
#include "C:/Users/DucToan/Documents/GitHub/ava/Source code/CppProjectTesting/data-test/ducanh/SymbolicExecutionTest/simple_test.cpp"
TEST(symbolic_execution21, testpath1) {
	int a=1;int b=0;
	
	int EO_returnVar = 1;

	int AO = symbolic_execution2(a,b);
EXPECT_EQ(EO_returnVar,AO);

}

TEST(symbolic_execution21, testpath2) {
	int a=0;int b=0;
	
	int EO_returnVar = 0;

	int AO = symbolic_execution2(a,b);
EXPECT_EQ(EO_returnVar,AO);

}


TEST(symbolic_execution32, testpath1) {
	int a=1;int b=0;
	
	int EO_returnVar = 1;

	int AO = symbolic_execution3(a,b);
EXPECT_EQ(EO_returnVar,AO);

}

TEST(symbolic_execution32, testpath2) {
	int a=0;int b=0;
	
	int EO_returnVar = 0;

	int AO = symbolic_execution3(a,b);
EXPECT_EQ(EO_returnVar,AO);

}



