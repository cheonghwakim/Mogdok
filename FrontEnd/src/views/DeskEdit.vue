<template lang="">
  <div v-dragscroll="true" class="desk">
    <div>
      <button @click="editComplete">편집완료(저장)</button>
      <button @click="$store.dispatch('CREATE_MEMO')">메모 생성</button>
      <button v-show="selectedMemoIdx >= 0" @click="$store.commit('REMOVE_MEMO')">
        이 메모 삭제하기
      </button>
      <button v-show="selectedMemoIdx >= 0" @click="$store.commit('SET_MEMO_EDIT_DIALOG', true)">
        내용 작성하기
      </button>
      <button
        v-for="index in 5"
        :key="'color' + index"
        v-show="editable && selectedMemoIdx >= 0"
        @click="$store.commit('SET_SELECTED_MEMO_COLOR', index)"
      >
        색상-{{ index }}로 변경
      </button>
    </div>
    <transition name="fade"> </transition>
    <div class="info borrow">
      <div class="info-content">
        <btn-close class="btnClose" @onClick="exitDesk"></btn-close>
        <p class="userName kyoboHand">편집화면</p>
        <p class="cate kyoboHand">#편집</p>
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
      :key="'memo' + memoId"
      class="moveable-container"
      v-bind="moveable"
      @drag="handleDrag"
      @renderEnd="handleRenderEnd(index, ...arguments)"
      @rotate="handleRotate"
      @scale="handleScale"
      @mousedown.native="$store.dispatch('UPDATE_SELECTED_MEMO_UI_BY_INDEX', index)"
      :style="{ zIndex, transform }"
    >
      <svg-memo :text="content" :color="color"></svg-memo>
    </vue-moveable>
    <div class="modal" v-show="dialog">
      <div v-if="memoList[selectedMemoIdx]" class="modal-content">
        <textarea v-model="memoList[selectedMemoIdx].content" type="text" class="input-box" />
        <span class="close" @click="$store.commit('SET_MEMO_EDIT_DIALOG', false)">닫기</span>
      </div>
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
import { mapState } from 'vuex';

export default {
  name: 'Desk',
  components: { SvgDesk, DivBanner, BtnClose, VueMoveable, SvgMemo },
  props: {},
  data() {
    return {};
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
      selectedMemoIdx: (state) => state.deskedit.selectedMemoIdx,
      editable: (state) => state.deskedit.editable,
      dialog: (state) => state.deskedit.memoEditDialog,
      removedMemoList: (state) => state.deskedit.removedMemoList,
    }),
  },
  watch: {},
  //lifecycle area
  created() {
    this.edit();
  },
  methods: {
    handleDrag({ target, transform }) {
      target.style.transform = transform;
    },
    handleRenderEnd(index, event) {
      this.$store.commit('SET_MEMO_TRANSFORM_BY_INDEX', {
        index,
        transform: event.target.style.transform,
      });
    },
    handleRotate({ target, transform }) {
      target.style.transform = transform;
    },
    handleScale({ target, transform }) {
      target.style.transform = transform;
    },
    edit() {
      this.$store.commit('SET_EDIT_STATE', true); // 편집을 가능 상태로 변경
      this.$store.commit('SET_REMOVED_MEMO_LIST', []); // 삭제했던 리스트 초기화
      this.$store.commit('SET_SELECTED_MEMO_IDX', -1); // 클릭된 메모 없음 상태로 변경
      // 모든 메모지를 이동가능한 상태로 업데이트
      for (let i = 0; i < this.memoList.length; i++) this.setMemoState(i, true);
    },
    editComplete() {
      this.saveMemo(); // 메모 저장
      this.$store.commit('SET_EDIT_STATE', false); // 편집 불가능 상태로 변경
      this.$store.commit('SET_REMOVED_MEMO_LIST', []); // 삭제했던 리스트 초기화
      this.$store.commit('SET_SELECTED_MEMO_IDX', -1); // 클릭된 메모 없음 상태로 변경
      // 모든 메모지를 움직일 수 없는 상태로 업데이트
      for (let i = 0; i < this.memoList.length; i++) this.setMemoState(i, false);
      this.$router.replace({ name: 'Desk' });
    },
    setMemoState(index, state) {
      // 메모지 상태 업데이트
      this.$store.commit('SET_MEMO_CLASSNAME_BY_INDEX', {
        index,
        className: state ? 'moveable' : 'movedisable',
      });
      this.$store.commit('SET_MEMO_MOVEABLE_STATUS_BY_INDEX', { index, status: state });
    },
    saveMemo() {
      // TODO: 비동기처리 필수
      this.$store.dispatch('SAVE_MEMO_LIST');
      this.$store.dispatch('DELETE_MEMO_LIST');
    },
    exitDesk() {
      console.log('exitDesk?');
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

.fade-enter-active,
.fade-leave-active {
  transition: all 1s ease;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}

/* The Modal (background) */
.modal {
  position: fixed; /* Stay in place */
  z-index: 10000; /* Sit on top */
  padding-top: 100px; /* Location of the box */
  left: 0;
  top: 0;
  width: 100%; /* Full width */
  height: 100%; /* Full height */
  overflow: auto; /* Enable scroll if needed */
  background-color: rgb(0, 0, 0); /* Fallback color */
  background-color: rgba(0, 0, 0, 0.8); /* Black w/ opacity */
}

/* Modal Content */
.modal-content {
  background-color: #fefefe;
  margin: auto;
  padding: 30px;
  border: 1px solid #888;
  width: 80%;
}

/* The Close Button */
.close {
  color: #aaaaaa;
  float: right;
  font-size: 20px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: #000;
  text-decoration: none;
  cursor: pointer;
}

textarea {
  border: 2px solid;
  border-radius: 5px;
  width: 100%;
  min-height: 10vh;
}
</style>
