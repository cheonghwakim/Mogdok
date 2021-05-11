<template>
  <div class="room">
    <transition name="slide-left">
      <div v-show="$store.state.desk.isOpenProfile" class="profile-wrapper">
        <div-profile :clickedDesk="$store.state.desk.desk"></div-profile>
      </div>
    </transition>
    <div class="deskList">
      <div class="deskItem" v-for="(desk, idx) in deskList" :key="subscribers[idx]">
        <room-desk :desk="desk" :stream-manager="subscribers[idx]"></room-desk>
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
    return {
      deskList: [],
    };
  },
  computed: {
    ...mapState({
      roomInfo: (state) => state.room.roomInfo,
      userInfo: (state) => state.user.userInfo,
      videoSourceList: (state) => state.user.videoSourceList,
      videoSourceIdx: (state) => state.user.videoSourceIdx,
      OV: (state) => state.openvidu.OV,
      session: (state) => state.openvidu.session,
      publisher: (state) => state.openvidu.publisher,
      subscribers: (state) => state.openvidu.subscribers,
    }),
  },
  watch: {},
  //lifecycle area
  created() {
    // this.sampleData(); // Sample Data 시작하자마 삽입
    this.joinSession();
    // this.$store.dispatch('CAMERA_ON');
  },
  methods: {
    // Sample Data 삽입
    // sampleData: function() {
    //   this.deskList = [
    //     {
    //       deskNumber: 1,
    //       userName: '안양불빠따',
    //       userTimer: '04:23:11',
    //       userStatus: 'study',
    //     },
    //     {
    //       deskNumber: 2,
    //       userName: '박영철',
    //       userTimer: '00:13:11',
    //       userStatus: 'rest',
    //     },
    //     {
    //       deskNumber: 3,
    //       userName: '몽실히',
    //       userTimer: '14:23:11',
    //       userStatus: 'study',
    //     },
    //     {
    //       deskNumber: 4,
    //       userName: '',
    //       userTimer: '',
    //       userStatus: 'empty',
    //     },
    //     {
    //       deskNumber: 5,
    //       userName: '',
    //       userTimer: '',
    //       userStatus: 'empty',
    //     },
    //     {
    //       deskNumber: 6,
    //       userName: '성실히',
    //       userTimer: '00:01:11',
    //       userStatus: 'study',
    //     },
    //   ];
    // },
    // 세선 참여
    joinSession() {
      // --- 오픈바이두 객체 생성 ---
      if (!this.OV) this.$store.commit('CREATE_OPENVIDU');
      // 사용 가능한 비디오 소스 확인
      this.$store.dispatch('SET_VIDEO_SOURCE_LIST');
      // 최대 이용 인원 수 설정
      this.$store.commit('SET_SUBSCRIBERS', this.roomInfo.roomLimit);
      // --- 세션 초기화 ---
      this.$store.commit('SET_SESSION', this.OV.initSession());
      // 세션에 이벤트 지정
      this.$store.dispatch('INIT_OV_SESSION_EVENT');
      // 세션에 유저 연결
      this.$store.dispatch('CONNECT_USER_TO_SESSION', this.userInfo);
      window.addEventListener('beforeunload', this.leaveSession);
    },
    leaveSession() {
      // 현재 접속 중인 세션을 나간다. (disconnect)
      window.removeEventListener('beforeunload', this.leaveSession);
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
