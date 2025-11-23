package tetris.ui.services;

import java.util.ArrayList;
import java.util.List;

/**
 * 메뉴 네비게이션 서비스
 * - 메뉴 항목 선택 관리
 * - 방향키 네비게이션 로직
 */
public class MenuNavigationService {
    
    private int currentIndex = 0;
    private int menuItemCount = 0;
    
    /**
     * 메뉴 항목 개수 설정
     * @param count 메뉴 항목 개수
     */
    public void setMenuItemCount(int count) {
        this.menuItemCount = count;
        if (currentIndex >= count) {
            currentIndex = 0;
        }
    }
    
    /**
     * 현재 선택된 인덱스 설정
     * @param index 선택할 인덱스
     * @return 실제로 설정된 인덱스 (범위 검증 후)
     */
    public int setCurrentIndex(int index) {
        if (index < 0 || index >= menuItemCount) {
            return currentIndex;
        }
        currentIndex = index;
        return currentIndex;
    }
    
    /**
     * 이전 메뉴 항목으로 이동
     * @return 새로운 인덱스
     */
    public int navigateToPrevious() {
        if (menuItemCount == 0) {
            return 0;
        }
        currentIndex = (currentIndex - 1 + menuItemCount) % menuItemCount;
        return currentIndex;
    }
    
    /**
     * 다음 메뉴 항목으로 이동
     * @return 새로운 인덱스
     */
    public int navigateToNext() {
        if (menuItemCount == 0) {
            return 0;
        }
        currentIndex = (currentIndex + 1) % menuItemCount;
        return currentIndex;
    }
    
    /**
     * 첫 번째 메뉴 항목으로 이동
     * @return 새로운 인덱스
     */
    public int navigateToFirst() {
        currentIndex = 0;
        return currentIndex;
    }
    
    /**
     * 마지막 메뉴 항목으로 이동
     * @return 새로운 인덱스
     */
    public int navigateToLast() {
        currentIndex = Math.max(0, menuItemCount - 1);
        return currentIndex;
    }
    
    /**
     * 현재 선택된 인덱스 가져오기
     * @return 현재 인덱스
     */
    public int getCurrentIndex() {
        return currentIndex;
    }
    
    /**
     * 메뉴 항목 개수 가져오기
     * @return 메뉴 항목 개수
     */
    public int getMenuItemCount() {
        return menuItemCount;
    }
    
    /**
     * 초기화
     */
    public void reset() {
        currentIndex = 0;
    }
}
