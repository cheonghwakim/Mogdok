<template lang="">
  <div
    @mouseenter="showFooter"
    @mouseleave="hideFooter"
    class="footer floating"
    :class="{ borrow: !isShowFooter }"
  >
    <transition name="fade">
      <div-cam-checker
        v-show="isCamChecker"
        class="cam-check-wrapper"
        @onClickClose="closeCamChecker"
        @onClickStart="doStudy"
        @toggleStartable="toggleCamChecker"
        :startable="isCamChecker"
      ></div-cam-checker>
    </transition>
    <div class="postit-wrapper">
      <div-timer-paper :type="studyType" :timer="studyTimer"></div-timer-paper>
    </div>
    <div class="content">
      <btn-my-desk class="btnMyDesk-wrapper" @click="goToMyDesk"></btn-my-desk>
      <btn-command
        class="btnCommand-wrapper"
        :label="btnLabel"
        @click="btnClickEvent"
      ></btn-command>
      <btn-leave-desk class="btnLeaveDesk-wrapper" @click="leaveSeat"></btn-leave-desk>
    </div>
    <div class="img-wrapper">
      <img src="@/assets/img/noteLong.svg" alt="" />
    </div>
  </div>
</template>
<script>
import DivTimerPaper from '@/components/ui/DivTimerPaper';
import BtnCommand from '@/components/ui/BtnCommand';
import BtnMyDesk from '@/components/ui/BtnMyDesk';
import BtnLeaveDesk from '@/components/ui/BtnLeaveDesk';
import DivCamChecker from '@/components/ui/DivCamChecker';
import { mapState } from 'vuex';
import {
  ROOM_STUDY_TYPE_NO_ACTION,
  ROOM_STUDY_TYPE_PAUSE,
  ROOM_STUDY_TYPE_START,
} from '../../store/modules/room';
// import from 'vue-editor-bridge';

export default {
  name: 'Footer',
  components: { DivTimerPaper, BtnCommand, BtnMyDesk, BtnLeaveDesk, DivCamChecker },
  props: {},
  data() {
    return {
      isOpen: false,
      isStudy: false,
      isCamChecker: false,
      isShowFooter: false,
    };
  },
  computed: {
    ...mapState({
      userInfo: (state) => state.user.userInfo,
      userRoomState: (state) => state.room.userRoomState,
      timeList: (state) => state.room.timeList,
      userSeatIndex: (state) => state.room.userSeatIndex,
    }),
    btnLabel() {
      switch (this.userRoomState) {
        case ROOM_STUDY_TYPE_NO_ACTION:
          return '공부 하자!';
        case ROOM_STUDY_TYPE_PAUSE:
          return '공부 하자!';
        case ROOM_STUDY_TYPE_START:
          return '쉬기';
      }
      return '';
    },
    studyType() {
      switch (this.userRoomState) {
        case ROOM_STUDY_TYPE_NO_ACTION:
          return 'rest';
        case ROOM_STUDY_TYPE_PAUSE:
          return 'rest';
        case ROOM_STUDY_TYPE_START:
          return 'study';
      }
      return 'rest';
    },
    studyTimer() {
      return this.userSeatIndex === -1 || !this.timeList[this.userSeatIndex]
        ? '00:00:00'
        : this.timeList[this.userSeatIndex];
    },
  },
  // watch: {},
  //lifecycle area
  created: function() {
    window.addEventListener('scroll', this.scrollListener);
  },
  beforeDestroy: function() {
    window.removeEventListener('scroll', this.scrollListener);
  },
  mounted() {
    // 최초 로드 시 2초 이후 자동으로 버로우
    setTimeout(() => {
      this.hideFooter();
    }, 2000);
  },
  methods: {
    // 푸터 표시하기
    showFooter: function() {
      // console.log('마우스 진입');
      this.isShowFooter = true;
    },

    // 푸터 감추기
    hideFooter: function() {
      // console.log('마우스가 범위 밖으로');
      if (this.isCamChecker) this.isShowFooter = true;
      else this.isShowFooter = false;
    },

    scrollListener: function() {
      console.log('스크롤중');
    },

    // 공부시작 커맨드 버튼 클릭 시, 캠 체커 띄우기
    showCamChecker: async function() {
      await this.$store.dispatch('SET_VIDEO_SOURCE_LIST');
      await this.$store.dispatch('CAMERA_ON');
      this.isCamChecker = true;
    },

    doRest: function() {
      this.$store.dispatch('CAMERA_OFF');
      this.isStudy = false;
    },

    // CamChecker를 닫기
    closeCamChecker: function() {
      this.isCamChecker = false;
      this.$store.dispatch('CAMERA_OFF');
      this.hideFooter(); // 푸터 닫기
    },

    // CamChecker에서 진짜 공부 시작
    doStudy: async function() {
      try {
        await this.$store.dispatch('SEND_STUDY_START');
        await this.$store.dispatch('PUBLISH_VIDEO_TO_SESSION');
        this.$store.commit('SET_USER_ROOM_STATE', ROOM_STUDY_TYPE_START);
        this.isCamChecker = false;
        this.isStudy = true;
      } catch (error) {
        alert('공부를 시작하지 못했어요. ' + error);
      }
    },

    goToMyDesk: function() {
      this.$router.replace({ path: `/desk/${this.userInfo.userName}` });
    },

    async btnClickEvent() {
      console.log(
        '%cRoomFooter.vue line:163 this.userRoomState',
        'color: #007acc;',
        this.userRoomState
      );
      switch (this.userRoomState) {
        case ROOM_STUDY_TYPE_NO_ACTION:
          alert('공부를 시작하려면 자리에 앉아야 합니다. 좌석을 클릭해서 자리에 앉아주세요.');
          break;
        case ROOM_STUDY_TYPE_PAUSE:
          this.showCamChecker();
          break;
        case ROOM_STUDY_TYPE_START:
          // 공부를 멈췄을 때
          try {
            await this.$store.dispatch('SEND_STUDY_PAUSE');
            await this.closeCamChecker();
          } catch (error) {
            alert('휴식모드로 돌아가지 못했어요. 다시 시도해주세요.' + error);
          }
          // 타이머 시작, 캠 처리
          break;
      }
    },

    async leaveSeat() {
      // 자리를 벗어남. openvidu, socket 연결은 끊기지 않음
      if (confirm('정말 자리를 떠나실건가요?')) {
        try {
          await this.$store.dispatch('SEND_SEAT_END');
        } catch (error) {
          alert(error);
        }
      }
    },

    toggleCamChecker(value) {
      this.isCamChecker = value;
    },
  },
};
</script>
<style scoped lang="scss">
@import 'src/assets/css/common';

.footer {
  bottom: 0;
  left: 50%;
  transform: translate(-50%, 20%);

  // 애니메이션
  transition: transform 1s ease;

  width: 50vw;
  max-width: 600px;

  height: auto;

  .cam-check-wrapper {
    position: absolute;
    bottom: 60%;
    left: 50%;
    transform: translateX(-50%);
  }

  .postit-wrapper {
    position: absolute;
    top: -10%;
    left: 50%;

    transform: translateX(-50%);
  }

  .content {
    /* background-color: blue; */
    width: 100%;
    position: absolute;
    top: 30%;

    .btnMyDesk-wrapper {
      position: absolute;
      left: 10%;
    }

    .btnCommand-wrapper {
      position: absolute;
      left: 50%;
      transform: translateX(-50%);
    }

    .btnLeaveDesk-wrapper {
      position: absolute;
      right: 10%;
    }
  }

  //flex-shrink: 0; // flex에 영향 없는 고정 요소 셋팅
  .img-wrapper {
    overflow: hidden;
    img {
      width: 100%;
      height: auto;

      object-fit: cover;
      object-position: 0px 0px;
    }
  }
}

@include mobile {
  .footer {
    /* border: 2px solid red; */
    width: 70vw;
  }
}

@include tinyMobile {
  .footer {
    /* border: 2px solid blue; */
    width: 100vw;
  }
}

// 가운데 정렬 되어 있는 요소를 버로우 시키기
@keyframes burrowing {
  0% {
    transform: translate(-50%, 20%);
  }
  100% {
    transform: translate(-50%, 75%);
  }
}

@keyframes burrowing-reverse {
  0% {
    transform: translate(-50%, 75%);
  }
  100% {
    transform: translate(-50%, 20%);
  }
}

.borrow {
  transform: translate(-50%, 75%);
}
</style>
