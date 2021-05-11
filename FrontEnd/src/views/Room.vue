<template>
  <div class="room">
    <transition name="slide-left">
      <div v-show="$store.state.desk.isOpenProfile" class="profile-wrapper">
        <div-profile :clickedDesk="$store.state.desk.desk"></div-profile>
      </div>
    </transition>
    <div class="deskList">
      <div class="deskItem" v-for="(seat, index) in seatList" :key="seat + index">
        <room-desk v-if="!seat" @click="clickDesk(seat, index)"></room-desk>
        <room-desk
          v-else
          :seat="seat"
          :stream-manager="seat.subscriber"
          @click="clickDesk(seat, index)"
        ></room-desk>
      </div>
    </div>
    <div-bgm-player></div-bgm-player>
    <div class="bottom-shader"></div>
  </div>
</template>
<script>
import RoomDesk from '@/components/RoomDesk';
import DivProfile from '@/components/ui/DivProfile';
import DivBgmPlayer from '@/components/ui/DivBgmPlayer';
import { mapState } from 'vuex';

export default {
  name: 'Room',
  components: {
    RoomDesk,
    DivProfile,
    DivBgmPlayer,
  },
  props: {},
  data() {
    return {};
  },
  computed: {
    ...mapState({
      roomInfo: (state) => state.room.roomInfo,
      seatList: (state) => state.room.seatList,
      selectedSeatInfo: (state) => state.room.selectedSeatInfo,
      userInfo: (state) => state.user.userInfo,
      videoSourceList: (state) => state.user.videoSourceList,
      videoSourceIdx: (state) => state.user.videoSourceIdx,
      OV: (state) => state.openvidu.OV,
      session: (state) => state.openvidu.session,
      publisher: (state) => state.openvidu.publisher,
      subscribers: (state) => state.openvidu.subscribers,
    }),
  },
  watch: {
    subscribers: {
      immediate: true,
      handler() {
        console.log('%cRoom.vue line:58 subscribers에 변경이 감지됨!', 'color: #007acc;');
        this.$store.dispatch('CONNECT_ROOM_WITH_OPENVIDU');
      },
    },
  },
  //lifecycle area
  created() {
    this.joinSession();
  },
  methods: {
    // 세선 참여
    async joinSession() {
      // TODO : joinSession 작업이 진행되는 동안 '로딩중' 필요
      try {
        // OpenVidu 서버 연결
        // --- 오픈바이두 객체 생성 ---
        if (!this.OV) this.$store.commit('CREATE_OPENVIDU');
        // 사용 가능한 비디오 소스 확인
        this.$store.dispatch('SET_VIDEO_SOURCE_LIST');
        // 최대 이용 인원 수 설정
        this.$store.commit('SET_SUBSCRIBERS', this.roomInfo.limitUserCount);
        // --- 세션 초기화 ---
        this.$store.commit('SET_SESSION', this.OV.initSession());
        // 세션에 이벤트 지정
        this.$store.dispatch('INIT_OV_SESSION_EVENT');
        // 세션에 유저 연결
        this.$store.dispatch('CONNECT_USER_TO_SESSION', this.userInfo);
        window.addEventListener('beforeunload', this.leaveSession);

        // Room 서버 연결
        await this.$store.dispatch('CONNECT_ROOM_SERVER');
        // Room 서버의 현재 좌석 정보 받기
        await this.$store.dispatch('GET_SEAT_INFO');
        // 현재 받은 좌석에 openvidu subscriber 옮기기
        this.$store.dispatch('CONNECT_ROOM_WITH_OPENVIDU');
        // Room 서버 구독
        this.$store.dispatch('SUBSCRIBE_ROOM_SERVER');
      } catch (e) {
        alert(`${this.roomInfo.name}에 접속을 실패했습니다. ${e}`);
        this.$router.replace({ path: '/' });
        return;
      }
    },
    leaveSession() {
      // 현재 접속 중인 세션을 나간다. (disconnect)
      // TODO: close버튼을 클릭하는게 아니라 이 부분에서도 자원해제를 해야하는지 판단후 적용
      window.removeEventListener('beforeunload', this.leaveSession);
    },
    async clickDesk(seat, index) {
      if (seat) {
        this.$store.commit('SET_SELECTED_SEAT_INFO', seat);
        alert(`${this.selectedSeatInfo.userName} 클릭되었습니다.`);
        // TODO: 신분증 나오도록
      } else {
        if (confirm(`${index + 1}번 좌석에 앉으시겠습니까?`)) {
          // room 서버에 해당 좌석에 앉았음을 알림
          try {
            await this.$store.dispatch('SEND_SEAT_ALLOCATED', { seatNo: index + 1 });
          } catch (error) {
            alert(error);
          }
        } else {
          console.log('%cRoom.vue line:121 앉지않음', 'color: #007acc;');
        }
      }
    },
  },
};
</script>
<style scoped lang="scss">
@import 'src/assets/css/common';

.room {
  margin-top: $HeaderHeight;
  margin-bottom: 56px;

  width: 100%;
  height: calc(100% - #{$HeaderHeight});
  /* background-color: rgb(255, 253, 190); */

  overflow-y: auto;

  .profile-wrapper {
    position: fixed;

    top: 10vh;
    right: 0px;

    /* transform 으로 위치 변경시 애니메이션 오류 */

    /* border: 1px dashed red; */
    z-index: 10;

    width: 45vmax;
    min-width: 300px;
    max-width: 500px;
    height: auto;
  }

  .deskList {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;

    .deskItem {
      width: 20vw;
      min-width: 150px;
      min-height: 200px;

      margin: 10px; // 책상 사이 간격 조정
    }
  }

  .bottom-shader {
    position: fixed;
    bottom: 0;
    width: 100vw;
    height: 100px;

    z-index: 10;

    background-image: linear-gradient(
      0deg,
      rgb(255, 255, 255),
      rgba(255, 255, 255) 40%,
      rgba(255, 255, 255, 0)
    );
  }
}

.slide-left-enter-active,
.slide-left-leave-active {
  transition: all 1s ease;
  /* animation-fill-mode: forwards; */
}
.slide-left-enter,
.slide-left-leave-to {
  /* opacity: 0; */
  transform: translate(200px, -1000px) rotate(20deg);
}
</style>
