#include "struct_parameter.h"

int CheckStructParameters(
    COMPLEX_STRUCT cstr,
    BIG_INTEGER off,
    size_t2 Length
    )
{    
    if( cstr.Info.Size < Length 
		|| off < 0 
		|| (off > (cstr.Info.Size - Length))
    
	) {
        return FALSE;
    }
    return TRUE;
}

