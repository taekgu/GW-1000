#include <iostream>
#include <vector>
#include <sstream>
#include <string>
#include <iso646.h>
//#include <exception>

#include <signal.h>

//#include <opencv2/opencv.hpp>
//#include <opencv2/core/core.hpp>
//#include <opencv2/highgui/highgui.hpp>

#include "ServerSocket.h"
#include "SocketException.h"

using namespace std;
//using namespace cv;

#define uchar unsigned char
#define PORT 20002

#define SIZE_OF_MSG_IN	17
#define SIZE_OF_MSG_OUT	21

uchar calcCheckSum(uchar msg[], int size);

uchar ack_list[7] = {0x00, 0x01, 0x02, 0x0a, 0x0b, 0x0c, 0x0d};
uchar state_list[6] = {0x00, 0x10, 0x20, 0x30, 0x40, 0x50};
uchar msg_in[50];
uchar msg_out[SIZE_OF_MSG_OUT] = {

		0xfd, // STX
		0x00, // 상태
		0x18, // 수온
		0x18, 0x18,	0x18, 0x18, // 내부온도
		0x00, 0x08, 0x00, 0x08, // 산소농도
		0x00, 0x00, 0x00, 0x00, // 습도
		0x00, // 현재노즐위치
		0x00, // 도어상태
		0x00, // RFID
		0x00, // 비상정지스위치
		0x00, // 체크섬
		0xfe  // ETX
};

int main (void) {

	signal(SIGPIPE, SIG_IGN);

	ServerSocket server(PORT);
	ServerSocket client;

	cout << "Waiting client ..." << endl;

	server.accept(client);
	cout << "Connected" << endl;

	int socket = client.getSock();

	int val = 1;
	int size = 0;

	int i_ack = 0;
	int i_state = 0;

	uchar cmd;

	while (true) {

		try {

			size = 0;
			recv(socket, msg_in, SIZE_OF_MSG_IN, 0);

			cout << "msg_in: " << endl;
			for (int i = 0; i < SIZE_OF_MSG_IN; i++) {

				cout << hex << (int) msg_in[i] << " ";
				size++;
				if ((int) msg_in[i] == 254) break;
			}
			cout << endl;

			if (calcCheckSum(msg_in, size) == msg_in[size - 2]) {

				cout << "checkSum is correct" << endl;

				cmd = msg_in[1];
				msg_out[1] = cmd | state_list[i_state++];
				if (i_state == 6) i_state = 0;

				//msg_out[1] = 0x20;

				//usleep(1000000);

				msg_out[SIZE_OF_MSG_OUT - 2] = calcCheckSum(msg_out, SIZE_OF_MSG_OUT);
				send(socket, msg_out, SIZE_OF_MSG_OUT, 0);

				val++;
				if (val == 15) val = 1;
				msg_out[15] = (uchar) val;

			} else {

				cout << "checkSum is wrong" << endl;
			}

			/*
			 cout << "msg_out: " << endl;
			 for (int i=0; i<sizeof(msg_out); i++) {

			 cout << hex << (int)msg_out[i] << " ";
			 }
			 cout << endl;
			 */
		} catch (int e) {

			cout << "Exception was caught" << endl;
		}
	}


	return 0;
}

uchar calcCheckSum(uchar msg[], int size) {

	uchar res = msg[1];

	for (int i = 2; i < size - 2; i++) {

		res = (uchar)(res ^ msg[i]);
	//	cout << hex << (int) res << " ";
	}

	return res;
}
