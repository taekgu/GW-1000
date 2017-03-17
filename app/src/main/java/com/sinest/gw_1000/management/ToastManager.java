package com.sinest.gw_1000.management;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Jinwook on 2017-02-22.
 *
 * 어플리케이션에서 사용되는 Toast 메시지 관리
 */

public class ToastManager {

    /* Toast 메시지 목록

    toastMsgs[Application_manager.m_language][msg number]

    0: "4개의 모드를 선택하세요"
    1: "4개 초과 선택할 수 없습니다"
    2: "하나 이상의 패턴을 사용해야합니다"
    3: "25~40 사이의 값을 입력해주세요"
    4: "잠시 후 다시 시도해주세요"
    5: "동작 시간을 1~90분 사이로 설정해야합니다"
    6: "1~90 사이의 값을 입력해주세요"
    7: "0~90 사이의 값을 입력해주세요"
    8: "서버 연결 완료"
    9: "WIFI 연결 완료"
    10: "WIFI 연결 해제"
    11: "시간을 다시 확인해주세요"
    12: "관리자 권한을 받아오지 못했습니다"
    13: "관리자 권한을 받았습니다"

     */

    private static String toastMsgs[][] = {

            {
                    "4개의 모드를 선택하세요", "4개 초과 선택할 수 없습니다", "하나 이상의 패턴을 사용해야합니다", "25~40 사이의 값을 입력해주세요", "잠시 후 다시 시도해주세요",
                    "동작 시간을 1~90분 사이로 설정해야합니다", "1~90 사이의 값을 입력해주세요", "0~90 사이의 값을 입력해주세요",
                    "서버 연결 완료", "WIFI 연결 완료", "WIFI 연결 해제", "시간을 다시 확인해주세요", "관리자 권한을 받아오지 못했습니다", "관리자 권한을 받았습니다"
            },

            {
                    "Please select 4 modes", "Can not select more than 4 modes", "Must use more than 1 pattern", "Please enter a value between 25 and 40", "Please try again",
                    "Working time should be set 1~90 minutes", "Please enter a value between 1 and 90", "Please enter a value between 0 and 90",
                    "Server connection complete", "Wifi connection complete", "Disconnection Wifi", "Check the time again", "Failed to get administrator privileges", "Succeed to get administrator privileges"
            },

            {
                    "请选择4个模式", "请勿超过选择4个模式", "请使用一个以上模式", "请输入25~40之间的温度", "请稍等重试",
                    "请设置1~90分钟之间的运行时间", "请输入1~90之间的数字", "请输入0~90之间的数字",
                    "成功连接服务器", "成功连接WIFI", "解除WIFI连接", "请重新检查时间", "取得管理员权限失败", "成功取得管理员权限"
            }
    };

    public ToastManager() {

    }

    /**
     * 현재 화면에 선택 토스트 메시지 출력
     * @param msg_num 토스트 메시지 번호
     */
    public static void popToast(int msg_num) {

        Toast.makeText(Application_manager.getContext(), toastMsgs[Application_manager.m_language][msg_num], Toast.LENGTH_SHORT).show();
    }

    /**
     * 현재 화면에 "'min' ~ 'max' 사이의 값을 입력해주세요" 메시지 출력
     * @param min 입력 가능한 최솟값
     * @param max 입력 가능한 최댓값
     */
    public static void popToast_time_range(int min, int max) {

        String msg = "";
        if (Application_manager.m_language == 0) {

            msg = "" + min + "~" + max + " 사이의 값을 입력해주세요";
        }
        else if (Application_manager.m_language == 1) {

            msg = "Please enter a value between " + min + " and " + max;
        }
        else if (Application_manager.m_language == 2) {

            msg = "请输入" + min + "~" + max + "之间的数字";
        }

        Toast.makeText(Application_manager.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
