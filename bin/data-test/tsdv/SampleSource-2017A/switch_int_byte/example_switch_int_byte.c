#include "main_struct.h"

BYTE switch_case_switch_case(BYTE var1, DWORD dword1)
{
    BYTE var2 = type1;

    switch (var1)
    {
    case type2:
        var2 = type5;
        break;

    case type3:
        switch (dword1)
        {
        case type14:
        case type15:
            var2 = type7;
            break;

        case type16:
            var2 = type8;
            break;
        case type17:
        case type20:
        case type22:
            var2 = type9;
            break;
        case type23:
            var2 = type10;
            break;

        default:
            var2 = type6;
            break;
    	}
    	var2 = type6;
        break;

    case type4:
        var2 = type6;
        break;

    default:
        var2 = type6;
        break;
    }
    return var2;
}

BYTE set_expected_byte(BYTE var3, BYTE var4, BYTE var5)
{
    BYTE var6 = type9;

    if (var3 == type23)
    {
        var6 = type10;
    }
    else if (var4 == type3)
    {
        var6 = type1;
    }
    else if (var5 == type3)
    {
        var6 = type12;
    }
    else
    {
        
    }
    return var6;
}

INT set_expected_int(INT int1)
{
    INT int2 = type1;

    if (int1 >= (type3 - 1))
    {
        int2 = 0;
    }
    else
    {
        int2 = int1 + 1;
    }
    return int2;
}