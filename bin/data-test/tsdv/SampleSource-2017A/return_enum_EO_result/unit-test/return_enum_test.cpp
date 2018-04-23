#include "gtest/gtest.h"
#include "./return_enum/return_enum.h"
#include "./return_enum/main.cpp"
#include "./return_enum/return_enum.cpp"

TEST(SimpleTest, testpath1) {

	Color c=BLUE;  
	Color_ReturnType  EO_returnVar=BLUE_TYPE;
	
	Color_ReturnType  returnVar=SimpleTest(c);
	
	EXPECT_EQ(EO_returnVar,returnVar);

}

TEST(SimpleTest, testpath2) {

	Color c=GREEN;  
	Color_ReturnType  EO_returnVar=GREEN_TYPE;
	
	Color_ReturnType  returnVar=SimpleTest(c);
	
	EXPECT_EQ(EO_returnVar,returnVar);

}


TEST(SimpleTest, testpath3) {

	Color c=RED;  
	Color_ReturnType  EO_returnVar=RED_TYPE;
	
	Color_ReturnType  returnVar=SimpleTest(c);
	
	EXPECT_EQ(EO_returnVar,returnVar);

}


TEST(SimpleTest, testpath4) {

	Color c=5;  
	Color_ReturnType  EO_returnVar=NONE;
	
	Color_ReturnType  returnVar=SimpleTest(c);
	
	EXPECT_EQ(EO_returnVar,returnVar);

}

