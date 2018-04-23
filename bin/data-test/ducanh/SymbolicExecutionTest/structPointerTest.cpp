struct Date1{
	int ngay;
	int thang;
	int nam;
};

int testDate0(Date1* d){
	if (d->ngay == 1)
		return 1;
	else
		return 0;
}

int testDate1(Date1* d){
	if (d->ngay == 1 && d->thang == 12 && d->nam == 2020)
		return 1;
	else
		return 0;
}

int testDate2(Date1* d){
	if (d->ngay == 1 || d->thang == 12 || d->nam == 2020)
		return 1;
	else
		return 0;
}

int testDate3(Date1* d){
	d->ngay = 1;
	if (d->ngay == 10)
		return 1;
	else
		return 0;
}

int testDate4(Date1* d){
	d->ngay = d->thang;
	if (d->ngay == 10)
		return 1;
	else
		return 0;
}

