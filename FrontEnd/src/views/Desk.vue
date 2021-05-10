<template lang="">
  <div v-dragscroll="true" class="desk">
    <button @click="$router.replace({ name: 'DeskEdit' })">편집하기</button>
    <transition name="fade">
      <div v-show="isFirst" class="caution covering">
        <img src="@/assets/img/discover.gif" alt="" />
        <p class="desc kyoboHand">드래그를 하면 책상을 <span>탐닉</span>할 수 있어요!</p>
      </div>
    </transition>
    <div class="info borrow">
      <div class="info-content">
        <btn-close class="btnClose" @onClick="exitDesk"></btn-close>
        <p class="userName kyoboHand">안양취준생</p>
        <p class="cate kyoboHand">#취업준비생</p>
      </div>
      <div-banner></div-banner>
    </div>
    <div class="desk-shader"></div>
    <div class="desk-wrapper">
      <div class="desk-draw-area"></div>
      <svg-desk></svg-desk>
    </div>
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
</template>
<script>
import { dragscroll } from 'vue-dragscroll';
import VueMoveable from 'vue-moveable';
import SvgDesk from '@/components/svg/SvgDesk';
import SvgMemo from '@/components/svg/SvgMemo';
import DivBanner from '@/components/ui/DivBanner';
import BtnClose from '@/components/ui/BtnClose';
import { mapState } from 'vuex';

export default {
  name: 'Desk',
  components: { SvgDesk, DivBanner, BtnClose, VueMoveable, SvgMemo },
  props: {},
  data() {
    return {
      isFirst: true,
    };
  },
  directives: {
    dragscroll,
  },
  computed: {
    ...mapState({
      deskId: (state) => state.desk.deskId,
      memoList: (state) => state.deskedit.memoList,
      ddayList: (state) => state.deskedit.ddayList,
      boardList: (state) => state.deskedit.boardList,
    }),
  },
  watch: {},
  //lifecycle area
  created() {
    // n초 뒤 몽실이 안내 화면이 사라짐
    setTimeout(() => {
      this.isFirst = false;
    }, 3000);
    // this.$store.dispatch('GET_DESK_INFO', userName 입력);
    this.$store.dispatch('GET_DESK_INFO');
  },
  methods: {
    exitDesk: function() {
      let isExit = confirm(`책상을 떠나시겠습니까?`);

      if (isExit) {
        this.$router.push('/room');
      }
    },
    clickMemo(index) {
      console.log(index + '번째 메모 클릭');
    },
  },
};
</script>
<style scoped lang="scss">
@import 'src/assets/css/common';

/* $desk-width: 1280px; */

.desk {
  /* margin-top: $HeaderHeight; */
  /* margin-bottom: 60px; */

  width: 100%;
  /* height: calc(100% - #{$HeaderHeight}); */

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
    top: 1vmax;
    left: 50%;
    transform: translate(-50%, -2vmax);

    transition: transform 1s ease;

    z-index: 11;

    width: 280px;

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

  /* .desk-shader {
      position: fixed;
      bottom: 50px;
      width: 100vw;
      height: 50px;
      background-color: red;
      background-image: linear-gradient(270deg, var(--white), var(--white) 40%, rgba(var(--white-rgb), 0));
   } */

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
</style>
