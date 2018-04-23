
union RGBA{
	int color;
	int aliasColor;
};

int SimpleTest(RGBA s){
	if (s.color > 10 && s.aliasColor > 12)
		return 1;
	else
		return 0;
}

int SimpleTestWithKeyword(union RGBA s){
	if (s.color > 10 && s.aliasColor > 12)
		return 1;
	else
		return 0;
}
