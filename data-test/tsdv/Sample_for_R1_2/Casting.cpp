
int SimpleIntCharCast(int x){
	if (x > 127 || x < -128)
		return -1;
		
	char c = (char)x;
	
	if (c == 'A')
		return 1;
	else
		return 0;
}

int StaticCast(int x){
	if (x > 127 || x < -128)
		return -1;
	
	char c = static_cast<char>(x); // Just same as (char)x
	
	if (c == 'A')
		return 1;
	else
		return 0;
}


