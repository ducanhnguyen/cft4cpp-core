
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>


#define MAX_NUM 10

struct exp_struct {
	int index;
	bool dev;
};

int struct_array( int dev, exp_struct exp_struct[] )
{
	int i;
	int offset = -1;

	for ( i = 0 ; i < MAX_NUM ; i++ )
	{
		if ( dev == exp_struct[ i ].dev )
		{
			offset = exp_struct[ i ].index ;
			break;
		}
	}
	
	return offset;
}

int int_array( int dev, int devs[], bool indexs[] )
{
	int i;
	int offset = -1;

	for ( i = 0 ; i < MAX_NUM ; i++ )
	{
		if ( dev == devs[i] )
		{
			offset = indexs[i] ;
			break;
		}
	}
	
	return offset;
}
