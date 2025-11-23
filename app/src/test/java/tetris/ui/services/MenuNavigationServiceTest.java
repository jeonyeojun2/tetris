package tetris.ui.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MenuNavigationServiceTest {
    
    private MenuNavigationService service;
    
    @BeforeEach
    void setUp() {
        service = new MenuNavigationService();
    }
    
    @Test
    void testSetMenuItemCount() {
        service.setMenuItemCount(5);
        assertEquals(5, service.getMenuItemCount());
        assertEquals(0, service.getCurrentIndex());
    }
    
    @Test
    void testSetMenuItemCountResetsIndexIfOutOfBounds() {
        service.setMenuItemCount(5);
        service.setCurrentIndex(4);
        
        service.setMenuItemCount(3); // 인덱스 4가 범위를 벗어남
        
        assertEquals(0, service.getCurrentIndex());
    }
    
    @Test
    void testSetCurrentIndex() {
        service.setMenuItemCount(5);
        
        int result = service.setCurrentIndex(2);
        
        assertEquals(2, result);
        assertEquals(2, service.getCurrentIndex());
    }
    
    @Test
    void testSetCurrentIndexOutOfBounds() {
        service.setMenuItemCount(5);
        service.setCurrentIndex(2);
        
        int result = service.setCurrentIndex(10); // 범위 초과
        
        assertEquals(2, result); // 변경되지 않음
        assertEquals(2, service.getCurrentIndex());
    }
    
    @Test
    void testSetCurrentIndexNegative() {
        service.setMenuItemCount(5);
        service.setCurrentIndex(2);
        
        int result = service.setCurrentIndex(-1); // 음수
        
        assertEquals(2, result); // 변경되지 않음
        assertEquals(2, service.getCurrentIndex());
    }
    
    @Test
    void testNavigateToNext() {
        service.setMenuItemCount(5);
        
        assertEquals(1, service.navigateToNext());
        assertEquals(2, service.navigateToNext());
        assertEquals(3, service.navigateToNext());
    }
    
    @Test
    void testNavigateToNextWrapsAround() {
        service.setMenuItemCount(5);
        service.setCurrentIndex(4); // 마지막 항목
        
        int result = service.navigateToNext();
        
        assertEquals(0, result); // 첫 번째 항목으로 순환
        assertEquals(0, service.getCurrentIndex());
    }
    
    @Test
    void testNavigateToPrevious() {
        service.setMenuItemCount(5);
        service.setCurrentIndex(3);
        
        assertEquals(2, service.navigateToPrevious());
        assertEquals(1, service.navigateToPrevious());
        assertEquals(0, service.navigateToPrevious());
    }
    
    @Test
    void testNavigateToPreviousWrapsAround() {
        service.setMenuItemCount(5);
        service.setCurrentIndex(0); // 첫 번째 항목
        
        int result = service.navigateToPrevious();
        
        assertEquals(4, result); // 마지막 항목으로 순환
        assertEquals(4, service.getCurrentIndex());
    }
    
    @Test
    void testNavigateToFirst() {
        service.setMenuItemCount(5);
        service.setCurrentIndex(3);
        
        int result = service.navigateToFirst();
        
        assertEquals(0, result);
        assertEquals(0, service.getCurrentIndex());
    }
    
    @Test
    void testNavigateToLast() {
        service.setMenuItemCount(5);
        service.setCurrentIndex(1);
        
        int result = service.navigateToLast();
        
        assertEquals(4, result);
        assertEquals(4, service.getCurrentIndex());
    }
    
    @Test
    void testNavigateToLastWithZeroItems() {
        service.setMenuItemCount(0);
        
        int result = service.navigateToLast();
        
        assertEquals(0, result);
    }
    
    @Test
    void testNavigateWithZeroItems() {
        service.setMenuItemCount(0);
        
        assertEquals(0, service.navigateToNext());
        assertEquals(0, service.navigateToPrevious());
    }
    
    @Test
    void testNavigateWithOneItem() {
        service.setMenuItemCount(1);
        
        assertEquals(0, service.navigateToNext());
        assertEquals(0, service.navigateToPrevious());
    }
    
    @Test
    void testReset() {
        service.setMenuItemCount(5);
        service.setCurrentIndex(3);
        
        service.reset();
        
        assertEquals(0, service.getCurrentIndex());
        assertEquals(5, service.getMenuItemCount()); // 카운트는 유지
    }
    
    @Test
    void testMultipleNavigations() {
        service.setMenuItemCount(7);
        
        service.navigateToNext(); // 1
        service.navigateToNext(); // 2
        service.navigateToPrevious(); // 1
        service.navigateToNext(); // 2
        service.navigateToNext(); // 3
        
        assertEquals(3, service.getCurrentIndex());
    }
}
