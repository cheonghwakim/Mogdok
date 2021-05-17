<template lang="">
  <div v-dragscroll="true" class="desk">
    <!-- v-if로 ID 필터링해서 본인 계정일 경우에만 보이도록 -->
    <btn-rounded
      v-if="isUserDesk"
      class="br-wrapper"
      :label="'Desk Editor'"
      :color="'yellow'"
      :type="'floating'"
      @onClick="goEditPage"
    ></btn-rounded>

    <!-- 몽실이 안내 화면 -->
    <transition name="fade">
      <div v-show="isFirst" class="caution covering">
        <img src="@/assets/img/discover.gif" alt="" />
        <p class="desc kyoboHand">드래그를 하면 책상을 <span>탐닉</span>할 수 있어요!</p>
      </div>
    </transition>

    <!-- 최상단 어떤 책상인지 INFO DISPLAY -->
    <div class="info">
      <div class="info-content">
        <btn-close class="btnClose" @onClick="exitDesk"></btn-close>
        <p class="userName kyoboHand">{{ profileUserName }}</p>
        <p class="cate kyoboHand">#{{ profile.category }}</p>
      </div>
      <div-banner></div-banner>
    </div>

    <!-- 메모가 표시는 책상 -->
    <div class="desk-wrapper">
      <div class="desk-draw-area">
        <vue-moveable
          v-for="({ memoId, content, zIndex, moveable, transform, color }, index) in memoList"
          :key="'memo' + index + memoId"
          class="moveable-container"
          v-bind="moveable"
          @click.native="clickMemo(index)"
          :style="{ zIndex, transform }"
        >
          <svg-memo :text="content" :color="color"></svg-memo>
        </vue-moveable>
      </div>
      <svg-desk></svg-desk>
    </div>
  </div>
</template>
<script>
import { dragscroll } from 'vue-dragscroll';
import VueMoveable from 'vue-moveable';
import SvgDesk from '@/components/svg/SvgDesk';
import SvgMemo from '@/components/svg/SvgMemo';
import DivBanner from '@/components/ui/DivBanner';
import BtnClose from '@/components/ui/BtnClose';
import BtnRounded from '@/components/ui/BtnRounded';
import { mapState } from 'vuex';

export default {
  name: 'Desk',
  components: { SvgDesk, DivBanner, BtnClose, VueMoveable, SvgMemo, BtnRounded },
  props: {},
  data() {
    return {
      isFirst: true,
      profileUserName: '',
      // Desk.vue 에는 고정된 메모 요소만 삽입
      moveFixedState: {
        draggable: false,
        scalable: false,
        rotatable: false,
        resizable: false,
        pinchable: false,
        throttleDrag: 0,
        keepRatio: true,
        throttleScale: 0,
        throttleRotate: 0,
        origin: false,
        zoom: 1,
        className: 'moveDisable',
        snappable: true,
        bounds: { left: 0, top: 0, right: 1000, bottom: 600 }, // 메모가 움직이는 최대 범위
        container: null, // 어느 요소 밑으로 넣을지 결정
      },
    };
  },
  directives: {
    dragscroll,
  },
  computed: {
    ...mapState({
      userInfo: (state) => state.user.userInfo,
      deskId: (state) => state.desk.deskId, // 보고있는 책상의 ID
      memoList: (state) => state.deskedit.memoList, //  책상의 메모들
      ddayList: (state) => state.deskedit.ddayList, //  책상의 디데이들
      boardList: (state) => state.deskedit.boardList, // 책상의 방명록(쪽지)
      profile: (state) => state.room.selectedSeatInfo,
    }),
    isUserDesk() {
      return this.userInfo.userName === this.$route.params.userName;
    },
  },
  watch: {
    '$route.params.userName': {
      immediate: true,
      async handler() {
        await this.getDeskInfo();
      },
    },
  },
  //lifecycle area
  mounted() {
    window.addEventListener('beforeunload', this.leaveSession);
    // 몽실이 안내 화면 : n초 뒤 화면 사라짐
    setTimeout(() => {
      this.isFirst = false;
    }, 3000);
    // 책상 화면 초기 셋팅
    this.initDesk();
  },
  beforeDestroy() {
    window.removeEventListener('beforeunload', this.leaveSession);
  },
  methods: {
    leaveSession(e) {
      e = e || window.event;
      if (e) {
        e.returnValue = '자리에서 떠나시겠습니까?'; //old browsers
      }
      return '자리에서 떠나시겠습니까?'; //safari, chrome(chrome ignores text)
    },
    async getDeskInfo() {
      try {
        await this.$store.dispatch('GET_SELECTED_SEAT_USER_INFO', {
          userName: this.$route.params.userName,
        });
        this.profileUserName = this.$route.params.userName;
        await this.$store.dispatch('GET_DESK_INFO', {
          nickname: this.profileUserName,
          objectState: this.moveFixedState,
        });
      } catch (error) {
        alert('사용자 정보를 불러오는데 실패했어요. ' + error);
      }
    },
    // 최초 데스크 셋팅(서버 내 메모 셋팅 등)
    initDesk: function() {
      // 컨테이너를 넣을 요소를 객체에 할당
      const elem = document.getElementsByClassName('desk-draw-area')[0];
      this.moveFixedState.container = elem;
    },

    // 편집 화면으로 이동
    goEditPage: function() {
      this.$router.replace({ name: 'DeskEdit' });
    },

    exitDesk: function() {
      let isExit = confirm(`책상을 떠나시겠습니까?`);

      if (isExit) {
        this.$router.push('/room');
      }
    },
    clickMemo: function(index) {
      console.log(index + '번째 메모 클릭');
    },
  },
};
</script>
<style scoped lang="scss">
@import 'src/assets/css/common';

.desk {
  width: 100%;

  display: flex;
  flex-direction: column;

  overflow: hidden;
  /* overflow-x: auto; // hidden으로 해도, npm에서 드래그 제공 */

  // 몽실이가 안내하는 화면
  .caution {
    width: 300px;
    height: 200px;

    background-color: rgb(255, 255, 255);
    box-shadow: 0px 9px 20px 0px #56565629;
    border-radius: 20px;

    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    img {
      width: 40%;
    }

    .desc {
      color: rgb(43, 43, 43);
      font-size: 12pt;

      span {
        font-family: inherit;
        color: rgb(247, 85, 85);
        font-weight: bold;
      }
    }
  }

  /* 상단에 표시되는 책상의 이름 안내 요소 */
  .info {
    position: fixed;
    top: -1.3vmax;
    left: 50%;
    transform: translateX(-50%);

    width: 280px;

    transition: transform 1s ease;
    z-index: 11;
    text-align: center;

    .info-content {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%);

      width: 90%;

      .btnClose {
        position: absolute;
        top: 20%;
        right: 10px;

        width: 20px;
        height: auto;
      }

      .userName {
        font-size: 18pt;
      }

      .cate {
        font-size: 14pt;
        margin-top: 5px;
        color: rgb(150, 150, 150);
      }
    }
  }

  .desk-wrapper {
    cursor: grab; // 드래그 영역에선 grap으로 표시

    position: relative;
    /* border: 1px solid red; */

    width: 1280px;
    height: auto;

    margin: 0 auto;
    margin-top: 10vmin;

    .desk-draw-area {
      position: absolute;

      top: 47%;
      left: 50%;
      transform: translate(-50%, -50%);

      width: 1000px;
      height: 600px;
      /* border: 1px solid blue; */
    }
  }
}

.br-wrapper {
  position: fixed;
  top: 120px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 50;

  width: 180px;
}
</style>
