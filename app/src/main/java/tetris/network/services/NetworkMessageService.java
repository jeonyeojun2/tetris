package tetris.network.services;

import tetris.network.NetworkMessage;

/**
 * 네트워크 메시지 처리 및 검증 서비스
 * GameServer와 GameClient에서 사용하는 비즈니스 로직을 분리
 */
public class NetworkMessageService {

    /**
     * 메시지가 PING 타입인지 확인
     */
    public boolean isPingMessage(Object message) {
        if (!(message instanceof NetworkMessage)) {
            return false;
        }
        return ((NetworkMessage) message).getType() == NetworkMessage.MessageType.PING;
    }

    /**
     * 메시지가 PONG 타입인지 확인
     */
    public boolean isPongMessage(Object message) {
        if (!(message instanceof NetworkMessage)) {
            return false;
        }
        return ((NetworkMessage) message).getType() == NetworkMessage.MessageType.PONG;
    }

    /**
     * RTT(Round Trip Time) 계산
     * @param sendTimestamp PING을 보낸 시간 (밀리초)
     * @param receiveTimestamp PONG을 받은 시간 (밀리초)
     * @return RTT (밀리초)
     */
    public long calculateRTT(long sendTimestamp, long receiveTimestamp) {
        return receiveTimestamp - sendTimestamp;
    }

    /**
     * PONG 메시지에서 RTT 계산
     * @param pongMessage PONG 메시지
     * @param currentTimeMillis 현재 시간 (밀리초)
     * @return RTT (밀리초), 유효하지 않으면 -1
     */
    public long extractRTTFromPong(NetworkMessage pongMessage, long currentTimeMillis) {
        if (pongMessage == null || pongMessage.getType() != NetworkMessage.MessageType.PONG) {
            return -1;
        }
        
        Object data = pongMessage.getData();
        if (!(data instanceof Long)) {
            return -1;
        }
        
        long sendTime = (Long) data;
        return calculateRTT(sendTime, currentTimeMillis);
    }

    /**
     * RTT에 따른 네트워크 품질 평가
     * @param rttMillis RTT (밀리초)
     * @return 네트워크 품질 등급 (EXCELLENT, GOOD, FAIR, POOR, TERRIBLE)
     */
    public NetworkQuality evaluateNetworkQuality(long rttMillis) {
        if (rttMillis < 0) {
            return NetworkQuality.UNKNOWN;
        } else if (rttMillis < 50) {
            return NetworkQuality.EXCELLENT;
        } else if (rttMillis < 150) {
            return NetworkQuality.GOOD;
        } else if (rttMillis < 300) {
            return NetworkQuality.FAIR;
        } else if (rttMillis < 500) {
            return NetworkQuality.POOR;
        } else {
            return NetworkQuality.TERRIBLE;
        }
    }

    /**
     * 네트워크 품질 등급
     */
    public enum NetworkQuality {
        EXCELLENT("매우 좋음", "#00ff00"),
        GOOD("좋음", "#90ee90"),
        FAIR("보통", "#ffff00"),
        POOR("나쁨", "#ffa500"),
        TERRIBLE("매우 나쁨", "#ff0000"),
        UNKNOWN("알 수 없음", "#808080");

        private final String displayName;
        private final String colorCode;

        NetworkQuality(String displayName, String colorCode) {
            this.displayName = displayName;
            this.colorCode = colorCode;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getColorCode() {
            return colorCode;
        }
    }

    /**
     * PING 메시지 생성
     * @param timestamp 현재 타임스탬프 (밀리초)
     * @return PING NetworkMessage
     */
    public NetworkMessage createPingMessage(long timestamp) {
        return new NetworkMessage(NetworkMessage.MessageType.PING, timestamp);
    }

    /**
     * PONG 메시지 생성
     * @param originalPingData PING 메시지의 데이터 (타임스탬프)
     * @return PONG NetworkMessage
     */
    public NetworkMessage createPongMessage(Object originalPingData) {
        return new NetworkMessage(NetworkMessage.MessageType.PONG, originalPingData);
    }

    /**
     * 메시지 타입이 유효한지 확인
     */
    public boolean isValidMessageType(NetworkMessage message) {
        if (message == null) {
            return false;
        }
        return message.getType() != null;
    }

    /**
     * 게임 상태 메시지인지 확인
     */
    public boolean isGameStateMessage(Object message) {
        if (!(message instanceof NetworkMessage)) {
            return false;
        }
        NetworkMessage netMsg = (NetworkMessage) message;
        return netMsg.getType() == NetworkMessage.MessageType.GAME_STATE;
    }

    /**
     * 게임 시작 메시지인지 확인
     */
    public boolean isGameStartMessage(Object message) {
        if (!(message instanceof NetworkMessage)) {
            return false;
        }
        NetworkMessage netMsg = (NetworkMessage) message;
        return netMsg.getType() == NetworkMessage.MessageType.GAME_START;
    }

    /**
     * 게임 종료 메시지인지 확인
     */
    public boolean isGameOverMessage(Object message) {
        if (!(message instanceof NetworkMessage)) {
            return false;
        }
        NetworkMessage netMsg = (NetworkMessage) message;
        return netMsg.getType() == NetworkMessage.MessageType.GAME_OVER;
    }
}
