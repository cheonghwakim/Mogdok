<template lang="">
  <div class="room">
    <transition name="slide-left">
      <div v-show="$store.state.desk.isOpenProfile" class="profile-wrapper">
        <div-profile :clickedDesk="$store.state.desk.desk"></div-profile>
      </div>
    </transition>
    <div class="deskList">
      <div class="deskItem" v-for="(desk, idx) in deskList" :key="idx">
        <room-desk :desk="desk"></room-desk>
      </div>
    </div>
    <div class="bottom-shader"></div>
  </div>
</template>
<script>
import { OpenVidu } from 'openvidu-browser';
import RoomDesk from '@/components/RoomDesk';
import DivProfile from '@/components/ui/DivProfile';
import { createSession, createToken } from '../api/openvidu';
// import { mapState } from 'vuex';

const USER_MAX_NUMBER = 16;

export default {
  name: 'Room',
  components: { RoomDesk, DivProfile },
  props: {},
  data() {
    return {
      deskList: [],
      videoSourceList: [],
      subscribers: new Array(USER_MAX_NUMBER),
      sessionId: 'SessionA',
    };
  },
  computed: {
    // ...mapState(['isOpenProfile']),
  },
  watch: {
    // Enterance로부터 [최대정원, 세션id, ]전달받기
  },
  //lifecycle area
  created() {
    this.sampleData(); // Sample Data 시작하자마 삽입
  },
  methods: {
    // Sample Data 삽입
    sampleData: function() {
      this.deskList = [
        {
          deskNumber: 1,
          userName: '안양불빠따',
          userTimer: '04:23:11',
          userStatus: 'study',
        },
        {
          deskNumber: 2,
          userName: '박영철',
          userTimer: '00:13:11',
          userStatus: 'rest',
        },
        {
          deskNumber: 3,
          userName: '몽실히',
          userTimer: '14:23:11',
          userStatus: 'study',
        },
        {
          deskNumber: 4,
          userName: '',
          userTimer: '',
          userStatus: 'empty',
        },
        {
          deskNumber: 5,
          userName: '',
          userTimer: '',
          userStatus: 'empty',
        },
        {
          deskNumber: 6,
          userName: '성실히',
          userTimer: '00:01:11',
          userStatus: 'study',
        },
      ];
    },
    // 세선 참여
    joinSession() {
      // --- Get an OpenVidu object ---
      if (!this.OV) this.OV = new OpenVidu();
      // 사용 가능한 비디오 소스 확인
      this.OV.getDevices().then((res) => {
        this.videoSourceList = res.filter((e) => {
          return e.kind === 'videoinput' && e.label;
        });
      });
      // --- Init a session ---
      this.session = this.OV.initSession();
      // --- Specify the actions when events take place in the session ---
      // On every new Stream received...
      this.session.on('streamCreated', ({ stream }) => {
        const subscriber = this.session.subscribe(stream);
        this.subscribers.push(subscriber);
      });
      // On every Stream destroyed...
      this.session.on('streamDestroyed', ({ stream }) => {
        const index = this.subscribers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.subscribers.splice(index, 1);
        }
      });
      // 메시지 시그널
      // this.session.on('signal:message', (signalEvent) => {
      //   this.receivedMessages.push(signalEvent);
      // });
      // --- Connect to the session with a valid user token ---
      this.getToken(this.sessionId).then((token) => {
        this.session
          .connect(token, `aaaaaa##${this.myUserName}`)
          .then(() => {
            // 세션에 성공적으로 입장
            console('세션에 참가했습니다');
          })
          .catch((error) => {
            console.log('There was an error connecting to the session:', error.code, error.message);
          });
      });
      window.addEventListener('beforeunload', this.leaveSession);
    },
    getToken(sid) {
      return this.createSession(sid).then((sessionId) => this.createToken(sessionId));
    },
    createSession(sessionId) {
      return new Promise((resolve, reject) => {
        createSession(
          { sessionId },
          (res) => res.data,
          (data) => resolve(data.id),
          (error) => {
            if (error.response.status === 409) {
              resolve(sessionId);
            } else {
              // 에러처리
              alert('createSession ERROR');
              reject(error.response);
            }
          }
        );
      });
    },
    createToken(sessionId) {
      return new Promise((resolve, reject) => {
        createToken(
          { sessionId },
          (res) => res.data,
          (data) => resolve(data.token),
          (error) => reject(error.response)
        );
      });
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
