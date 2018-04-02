#ifndef QLSV_H
#define QLSV_H
#include <iostream>
#include <fstream>

using namespace std;

typedef class thisinh TS;

class thisinh
{
private:
    char id[20], ho[20], ten[20], tendem[20];
    char gt;
    char ns[5];
    char qq[20];
    float toan, ly, hoa, tongdiem, tienganh;
    int n;
    int kq;// do - truot
    TS *next, * head, * last;
public:
    // ham tao
    thisinh();

    // ham in du lieu ra File
    void inFile(ofstream& fo, TS* student);
    void inFileAll(ofstream& fo);

    // ham thao tac xau ki tu
    void StrDel(char s[],int k,int h);
    void StrIns(char s[],int pos, char s1[]);
    void StrIns(char s[],int pos, char ch);
    void xoa_dau_cach_thua(char s[]);
    void chuan_hoa(char* s);

    // ham di chuyen con tro
    void gotoxy(short x, short y);

    // ham lua chon menu
    int choose(int cot, int dong, int n);

    // ham xoa man hinh
    void xoa_dong(int dong);
    void xoa_dong(int a, int b);

    // ham in du lieu thi sinh ra man hinh
    void inScreen(TS* student, int opi);

    // ham lay cac thong tin thi sinh tu xau s
    void xu_li(TS* student, char* s);

    // ham nhap chuan du lieu
    int nhap_so();
    char* nhap_xau();

    //
    void trao_doi(int& a, int& b);
    void trao_doi(char& a, char& b);
    void trao_doi(float& a, float& b);
    void trao_doi(char a[],char b[]);
    void trao_doi(TS* a,TS* b);

    // ham nhap du lieu thi sinh
    void nhap_qua_File(ifstream& fi);
    void nhap_qua_ban_phim();
    void nhap_diem_tieng_anh(ifstream& fi);

    void sap_xep_ten();
    void sap_xep_diem_thi();
    void tinh_tong_diem();

    void in_TS_do(ofstream& fo);
    void in_danh_sach_sap_xep_ten(ofstream& fo);
    void in_CLC(ofstream& fo);
    void in_danh_sach_thi_sinh_len_man_hinh();
    void in_thi_sinh_do_len_man_hinh();
    void in_thi_sinh_CLC_len_man_hinh();
    void inFileTiengAnh(ofstream& fo, TS* student);

    void phuc_khao();

    void tac_gia();

    //
    void textcolor(int mau);
};

#endif


