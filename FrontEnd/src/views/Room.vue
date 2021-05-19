<template>
  <div class="room">
    <transition name="fade">
      <div-guide v-if="GuideShown == null" @onClick="closeGuide"></div-guide>
    </transition>
    <transition name="slide-left">
      <div v-show="$store.state.desk.isOpenProfile" class="profile-wrapper">
        <div-profile
          :seat="selectedSeatInfo"
          :stream-manager="selectedSeatInfo ? selectedSeatInfo.subscriber : undefined"
        ></div-profile>
      </div>
    </transition>
    <div class="deskList">
      <div class="deskItem" v-for="(seat, index) in seatList" :key="`${seat}${index}`">
        <room-desk
          v-if="!seat"
          :timer="timeList[index]"
          @click="clickDesk(seat, index)"
        ></room-desk>
        <room-desk
          v-else
          :seat="seat"
          :stream-manager="seat.subscriber"
          :timer="timeList[index]"
          @click="clickDesk(seat, index)"
        ></room-desk>
      </div>
    </div>
    <div-bgm-player></div-bgm-player>
    <transition name="fade">
      <div
        v-show="$store.state.desk.isOpenProfile"
        class="black-screen"
        @click="closeProfile"
      ></div>
    </transition>
    <div class="bottom-shader"></div>
  </div>
</template>
<script>
import RoomDesk from '@/components/RoomDesk';
import DivProfile from '@/components/ui/DivProfile';
import DivBgmPlayer from '@/components/ui/DivBgmPlayer';
import DivGuide from '@/components/ui/DivGuide';
import { mapState } from 'vuex';

export default {
  name: 'Room',
  components: {
    RoomDesk,
    DivProfile,
    DivBgmPlayer,
    DivGuide,
  },
  props: {},
  data() {
    return {
      GuideShown: null,
      selectedSeatIdx: -1,
      time: [],
    };
  },
  computed: {
    ...mapState({
      socket: (state) => state.room.socket,
      stomp: (state) => state.room.stomp,
      roomInfo: (state) => state.user.roomInfo,
      seatList: (state) => state.room.seatList,
      timeList: (state) => state.room.timeList,
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
        this.$store.dispatch('CONNECT_ROOM_WITH_OPENVIDU');
      },
    },
  },
  //lifecycle area
  created() {
    if (!this.seatList || !this.timeList) {
      this.$store.commit('INIT_ROOM', this.roomInfo);
    }
    if (!this.socket || !this.stomp || !this.session) {
      this.joinSession();
    }
    this.GuideShown = localStorage.getItem('GuideShown');
  },
  mounted() {
    window.addEventListener('beforeunload', this.leaveSession);
  },
  beforeDestroy() {
    window.removeEventListener('beforeunload', this.leaveSession);
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
        // await this.$store.dispatch('SET_VIDEO_SOURCE_LIST');
        // 최대 이용 인원 수 설정
        // this.$store.commit('SET_SUBSCRIBERS', this.roomInfo.limitUserCount);
        // --- 세션 초기화 ---
        this.$store.commit('SET_SESSION', this.OV.initSession());
        // 세션에 이벤트 지정
        this.$store.dispatch('INIT_OV_SESSION_EVENT');
        // 세션에 유저 연결
        this.$store.dispatch('CONNECT_USER_TO_SESSION', this.userInfo);

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
    leaveSession(e) {
      e = e || window.event;
      if (e) {
        e.returnValue = '자리에서 떠나시겠습니까?'; //old browsers
      }
      return '자리에서 떠나시겠습니까?'; //safari, chrome(chrome ignores text)
    },
    async clickDesk(seat, index) {
      if (seat) {
        try {
          this.selectedSeatIdx = index;
          await this.$store.dispatch('GET_SELECTED_SEAT_USER_INFO', seat);
        } catch (error) {
          alert('유저 정보를 불러오는데 실패했어요.' + error);
          this.selectedSeatIdx = -1;
          return;
        }
        this.$store.commit('TOGGLE_PROFILE');
      } else {
        if (confirm(`🪑[ ${index + 1} ]번 좌석에 앉을까요?`)) {
          // room 서버에 해당 좌석에 앉았음을 알림
          try {
            await this.$store.dispatch('SEND_SEAT_ALLOCATED', { seatNo: index + 1 });
          } catch (error) {
            alert(error);
          }
        }
      }
    },
    closeProfile() {
      this.$store.commit('TOGGLE_PROFILE');
      this.$store.commit('CLEAR_DESK');
    },
    closeGuide() {
      localStorage.setItem('GuideShown', 'Y'); // 이미 가이드가 보였음
      this.GuideShown = 'Y';
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

  /* 스크롤 가능 버젼 */
  overflow-y: auto;

  .profile-wrapper {
    position: fixed;

    top: 10vh;
    right: 0px;

    /* border: 1px dashed red; */
    z-index: 50;

    width: 45vmax;
    min-width: 300px;
    max-width: 500px;
    height: auto;
  }

  .deskList {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;

    /* border: 1px dashed red; */

    /* 스크롤 불가능 버젼 */
    width: 100%;

    .deskItem {
      /* border: 1px dashed rgb(0, 21, 255); */
      width: 20vw;
      min-width: 150px;
      max-width: 250px;
      height: calc(20vw * 4 / 3);
      min-height: 200px;
      max-height: 334px;

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

  .black-screen {
    /* cursor: pointer; */
    width: 100vw;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.45);

    position: fixed;
    top: 0;
    left: 0;

    z-index: 30;
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
