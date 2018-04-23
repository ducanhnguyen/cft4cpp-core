#include <iostream>
using namespace std;

/** 

Parameter:
 - b_w: weight
 - he: height

Return list
 - UNDERWEIGHT: 0
 - Perfect Shape: 1
 - OVERWEIGHT: 2
 - A VICTIM OF OBESITY: 3
 - Out of believeable: -1
 
*/
int bmi(float b_w, float he)
{
	double c;
	c = (b_w / (he * he / 10000));
	
	{
		if (c < 19) {
			return 0;
		}

		else if (c >= 19 && c < 25) {
			return 1;

		}

		else if (c >= 25 && c < 30) {
			return 2;
		}

		else if (c >= 30) {
			return 3;

		} else {
			// (infeasible)
			return -1;
		}
	}
}

struct Date{
	int date;
	int month;
	int year;
};

Date calculateAge(Date born, Date today)
{	
	int x, y, z;
	Date result;

	int date = today.date;
	int month = today.month;
	int year = today.year;

	int date1 = born.date;
	int month1 = born.month;
	int year1 = born.year;	

	x = ((year) - (year1 + 1));
	{
		if ((month >= month1) && (date >= date1)) {
			{
				y = (month - month1);
				x = x + 1;
				z = (date - date1);
			}
		} else if (((month > month1) && (date < date1)) && ((month == 5)  || (month == 7) || (month == 10)  || (month == 12))) {
			{
				y = ((month - month1) - 1);
				x = x + 1;
				z = ((30 - date1) + date);
			}
		} else if (((month > month1) && (date < date1)) && ((month == 4) || (month == 1) || (month == 2) || (month == 6) || (month == 8) || (month == 9) || (month == 11))) {
			{
				y = ((month - month1) - 1);
				x = x + 1;
				z = ((31 - date1) + date);
			}
		}

		else if ((month > month1) && (date < date1) && (month == 3)) {
			{
				y = ((month - month1) - 1);
				x = x + 1;
				z = ((28 - date1) + date);
			}
		}

		else if (((month == month1) && (date < date1)) && ((month == 1) || (month == 2) || (month == 4)  || (month == 6) || (month == 8) || (month == 9) || (month == 11))) {
			{
				y = 11;
				z = ((31 - date1) + date);
			}
		}

		else if (((month == month1) && (date < date1)) && ((month == 5) || (month == 7) || (month == 10)  || (month == 12))) {
			{
				z = ((30 - date1) + date);
			}
		}

		else if (month == month1 && date < date1 && month == 3) {
			{
				z = ((28 - date1) + date);
			}
		}

		else if (((month < month1) && (date > date1)) && ((month == 1) || (month == 3) || (month == 5)  || (month == 7) || (month == 8) || (month == 10) || (month == 12))) {
			{
				y = ((12 - month1) + month);
				z = date - date1;
			}
		}

		else if (((month < month1) && (date > date1)) && ((month == 4) || (month == 6) || (month == 9)  || (month == 11))) {
			{
				y = ((12 - month1) + month);
				z = date - date1;
			}
		}

		else if ((month < month1) && (date > date1) && (month == 2)) {
			{
				y = ((12 - month1) + month);
				z = date - date1;
			}
		}

		else if (((month < month1) && (date < date1)) && ((month == 1) || (month == 2) || (month == 4)  || (month == 6) || (month == 8) || (month == 9) || (month == 11))) {
			{
				y = ((12 - month1) + month - 1);
				z = ((31 - date1) + date);
			}
		}

		else if (((month < month1) && (date < date1)) && ((month == 5) || (month == 7) || (month == 10)  || (month == 12))) {
			{
				y = ((12 - month1) + month - 1);
				z = ((30 - date1) + date);
			}
		} else if ((month < month1) && (date < date1) && (month == 3)) {
			{
				y = ((12 - month1) + month - 1);
				z = ((28 - date1) + date);
			}
		}

		else {
			x = y = z = -1;
		}
	}

	result.date = z;
	result.month = y;
	result.year = x;
	
	return result;
}

int calculateZodiac(Date born){

	int date1 = born.date;
	int month1 = born.month;
	int year1 = born.year;	
	
	int t;
	
	{

		if (((month1 == 3) && (date1 >= 21) && (date1 <= 31)) || ((month1 == 4) && (date1 <= 19)))

		{
			cout << "\n\n\t\t\tYour Zodiac sign is ARIES";
			cout << "\n\n 2012 would be a good year overall. You will experience a rise in financial luck and inflows";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 1;
		}

		else if (((month1 == 4) && (date1 >= 20) && (date1 <= 30)) || ((month1 == 5) && (date1 <= 20))) {
			cout << "\n\n\t\tYour Zodiac sign is TAURUS";
			cout << "\n\n A very eventful year. Career would be on a high growth trajectory and bring in major progress and achievements.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 2;
		}

		else if (((month1 == 5) && (date1 >= 21) && (date1 <= 31)) || ((month1 == 6) && (date1 <= 20))) {
			cout << "\n\n\t\tYour Zodiac sign is GEMINI";
			cout << "\n\nA very positive year. Income & professional growth would be immense. You will find the ability to make some very profitable deals now.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 3;
		}

		else if (((month1 == 6) && (date1 >= 21) && (date1 <= 30)) || ((month1 == 7) && (date1 <= 22))) {
			cout << "\n\n\t\tYour Zodiac sign is CANCER";
			cout << "\n\nA very eventful year, although negative thoughts and unnecessary pessimism could spoil your spirit and bring in unnecessary tension.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 4;
		}

		else if (((month1 == 7) && (date1 >= 23) && (date1 <= 31)) || ((month1 == 8) && (date1 <= 22))) {
			cout << "\n\n\t\tYour Zodiac sign is LEO";
			cout << "\n\nAn exceptional year again. You will see a rise in status and expansion in career this year too. Luck will favor you throughout.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 5;
		}

		else if (((month1 == 8) && (date1 >= 23) && (date1 <= 31)) || ((month1 == 9) && (date1 <= 22))) {
			cout << "\n\n\t\tYour Zodiac sign is VIRGO";
			cout << "\n\nA brilliant & positive year, where you will make things happen on your own strength, rather than seeking support of others.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 6;
		}

		else if (((month1 == 9) && (date1 >= 23) && (date1 <= 30)) || ((month1 == 10) && (date1 <= 22))) {
			cout << "\n\n\t\tYour Zodiac sign is LIBRA";
			cout << "\n\nA powerful phase will be in operation this month. You will find your role as defined by nature will change and all efforts and activities carried out by you will assume higher importance and effectiveness.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 7;
		}

		else if (((month1 == 10) && (date1 >= 23) && (date1 <= 31)) || ((month1 == 11) && (date1 <= 21))) {
			cout << "\n\n\t\tYour Zodiac sign is SCORPIO";
			cout << "\n\nPositive period would continue, although you need to be careful that throwing good money after bad money is not a great idea.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 8;
		}

		else if (((month1 == 11) && (date1 >= 22) && (date1 <= 31)) || ((month1 == 12) && (date1 <= 21))) {
			cout << "\n\n\t\tYour Zodiac sign is SAGITTARIUS";
			cout << "\n\n2012 brings in promise and progress. New ideas and cerebral approach to matters will bring in much progress this year. You will be at your creative best till May 2012 and thereafter dynamic activity will take you along.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 9;
		}

		else if (((month1 == 12) && (date1 >= 22) && (date1 <= 30)) || ((month1 == 1) && (date1 <= 19))) {
			cout << "\n\n\t\tYour Zodiac sign is CAPRICORN";
			cout << "\n\nA very positive year for you. You would be at your creative best and luck related peak in most of the works you get into.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 10;
		}

		else if (((month1 == 1) && (date1 >= 20) && (date1 <= 31)) || ((month1 == 2) && (date1 <= 18))) {
			cout << "\n\n\t\tYour Zodiac sign is AQUARIUS";
			cout << "\n\nA much better year in comparison to 2010 & 2011. You will feel a surge in your luck, productivity and general sense of positive outlook.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 11;
		}

		else if (((month1 == 2) && (date1 >= 19) && (date1 <= 29)) || ((month1 == 3) && (date1 <= 20))) {
			cout << "\n\n\t\tYour Zodiac sign is PISCES";
			cout << "\n\nSome amount of struggle and hurdles could come about in life this year. You will have a positive and gainful period till May 2012.";
			cout << "\n\n\t\t Best of luck for Your Future ";
			t = 12;
		}

		else {
			t = -1;
		}

	}
	
	return t;
}


