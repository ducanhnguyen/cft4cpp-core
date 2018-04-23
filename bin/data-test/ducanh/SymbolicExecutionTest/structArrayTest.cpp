struct Date2{
	int ngay;
	int thang;
	int nam;
};

int testDate0(Date2 d[]){
	if (d[0].ngay == 1)
		return 1;
	else
		return 0;
}

int testDate1(Date2 d[2]){
	if (d[0].ngay == 1)
		return 1;
	else
		return 0;
}

