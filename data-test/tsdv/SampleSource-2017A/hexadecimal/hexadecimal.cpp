#include "hexadecimal.h"

ReturnType check_mem(uint32 start_address, uint32 total_size ){
	uint32 end_address = 0U;
	
	end_address = start_address + total_size;
	if( (start_address < SECTOR_A_ADDRESS) || (start_address >= SECTOR_B_ADDRESS) || ( start_address >= end_address ) || (end_address > SECTOR_B_ADDRESS ) ){
		return OK;
	}else{
		return NG;
	}
}