Bước 1. Tìm lõi kiểu biến
	TH 1: Kiểu biến được định nghĩa qua typedef.
	 		TH1.1: Định nghĩa typedef đặt trong cùng một tệp
	 		TH1.2: Định nghĩa typedef đặt trong tệp header khác
	 		TH1.3: Định nghĩa typedef đặt trong class
	
	TH 2: Kiểu biến là kiểu danh sách
	 		TH2.1: vector
	
	TH 3: Kiểu biến là kiểu con trỏ
	 		TH3.1: Con trỏ một mức
	 		TH3.2: Con trỏ nhiều mức
			TH3.3: Địa chỉ
			
	TH 4: Kiểu biến là kiểu mảng
	 		TH4.1: Mảng một chiều
	 		TH4.2: Mảng nhiều chiều
	
	TH 5: Kiểu biến là kiểu struct
	
	TH 6: Kiểu biến là kiểu class
	
	___________________________
Bước 2. Xây dựng không gian tìm kiếm
	Độ ưu tiên (giảm dần):
	- Không gian tìm kiếm class gần nhất, thừa kế class
	- Không gian tìm kiếm tệp hiện tại
	- Không gian tìm kiếm những tệp included
	___________________________