#include "return_enum.h"

Color_ReturnType SimpleTest(Color color){
	if (color == RED)
		return RED_TYPE;
	else if (color == GREEN)
		return GREEN_TYPE;
	else if (color == BLUE)
		return BLUE_TYPE;
	else
		return NONE ;
}	
