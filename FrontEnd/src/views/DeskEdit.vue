<template lang="">
  <div v-dragscroll="true" class="desk">
    <!-- 최상단 에디팅 관련 DIV -->
    <div class="top-editor-wrapper">
      <div class="first-row">
        <!-- 메모 생성 -->
        <div class="btn btn-add" @click="$store.dispatch('CREATE_MEMO', moveableState)"></div>
        <span>Desk Editor</span>
        <!-- 메모 저장 -->
        <div class="btn btn-save" @click="editComplete"></div>
      </div>
      <transition name="memo-down">
        <div v-show="selectedMemoIdx >= 0" class="second-row">
          <!-- 메모 글 작성 -->
          <div class="btn btn-write" @click="toggleModal"></div>
          <div
            class="btn btn-color"
            v-for="(item, index) in memoColor"
            :key="'color' + index"
            :style="{ 'background-color': item.code }"
            @click="$store.commit('SET_SELECTED_MEMO_COLOR', index)"
          ></div>
          <!-- 메모 삭제 -->
          <div class="btn btn-delete" @click="$store.commit('REMOVE_MEMO')"></div>
        </div>
      </transition>
    </div>

    <!-- 메모가 들어가는 영역 -->
    <div class="desk-wrapper">
      <div class="desk-draw-area">
        <vue-moveable
          v-for="({ memoId, content, zIndex, moveable, transform, color }, index) in memoList"
          :key="'memo' + memoId + index"
          class="moveable-container"
          v-bind="moveable"
          @drag="handleDrag"
          @renderEnd="handleRenderEnd(index, ...arguments)"
          @rotate="handleRotate"
          @scale="handleScale"
          @dblclick.native="toggleModal"
          @mousedown.native="$store.dispatch('UPDATE_SELECTED_MEMO_UI_BY_INDEX', index)"
          @touchstart.native="$store.dispatch('UPDATE_SELECTED_MEMO_UI_BY_INDEX', index)"
          :style="{ zIndex, transform }"
        >
          <svg-memo :text="content" :color="color"></svg-memo>
        </vue-moveable>
        <div class="calendar-box">STUDY CALENDAR</div>
      </div>
      <svg-desk></svg-desk>
    </div>

    <!-- 글 작성 모달 -->
    <div class="memoInputModal" v-show="isOpenModal">
      <div
        v-if="memoList[selectedMemoIdx]"
        class="memo-Modal-content"
        :style="{ 'background-color': memoColor[memoList[selectedMemoIdx].color].code }"
      >
        <p class="title">POST-IT</p>
        <textarea
          v-model="memoList[selectedMemoIdx].content"
          type="text"
          class="kyoboHand"
          placeholder="메모 내용을 작성해주세요"
        />
        <p class="desc">메모 내용이 실시간으로 작성됩니다 😛</p>
        <div v-wave class="btn-close" @click="toggleModal">
          CLOSE
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { dragscroll } from 'vue-dragscroll';
import VueMoveable from 'vue-moveable';
import SvgDesk from '@/components/svg/SvgDesk';
import SvgMemo from '@/components/svg/SvgMemo';
import { mapState } from 'vuex';

export default {
  name: 'Desk',
  components: { SvgDesk, VueMoveable, SvgMemo },
  props: {},
  data() {
    return {
      // 메모 글 작성을 위한 모달
      isOpenModal: false,

      // 포스트잇 색상 리스트 (with SvgMemo)
      memoColor: [
        {
          title: 'white',
          code: '#FFFFFF',
        },
        {
          title: 'yelloe',
          code: '#FAFFDC',
        },
        {
          title: 'red',
          code: '#FFE2E2',
        },
        {
          title: 'blue',
          code: '#DAEBFF',
        },
      ],

      // 움직일 수 있는 상태 (=편집 모드 접근 상태)
      moveableState: {
        draggable: true, // changed
        scalable: true, // changed
        rotatable: true, // changed
        resizable: false,
        pinchable: true, // changed
        throttleDrag: 0,
        keepRatio: true,
        throttleScale: 0,
        throttleRotate: 0,
        origin: false,
        zoom: 1,
        className: 'moveable', // changed
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
      deskId: (state) => state.desk.deskId, // 보고있는 책상의 ID
      memoList: (state) => state.deskedit.memoList, //  책상의 메모들
      ddayList: (state) => state.deskedit.ddayList, //  책상의 디데이들
      boardList: (state) => state.deskedit.boardList, // 책상의 방명록(쪽지)
      selectedMemoIdx: (state) => state.deskedit.selectedMemoIdx, // 현재 선택한 메모의 인덱스
      editable: (state) => state.deskedit.editable, // 편집 가능한 상태여부
      removedMemoList: (state) => state.deskedit.removedMemoList, //삭제된 메모리스트
    }),
  },
  //lifecycle area
  mounted() {
    window.addEventListener('beforeunload', this.leaveSession);
    this.initDeskEdit();
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
    // 책상 편집 초기 셋팅
    initDeskEdit: async function() {
      // 컨테이너를 넣을 요소를 객체에 할당
      const elem = document.getElementsByClassName('desk-draw-area')[0];
      this.moveableState.container = elem;

      // 편집하는 책상의 정보들을 VUEX에 셋팅
      await this.$store.dispatch('GET_DESK_ALL_MEMO_4_EDIT', {
        nickname: this.$route.params.userName,
        objectState: this.moveableState,
      });

      // #JS : 이 부분은 desk.js action 내에서 동기적으로 처리해도 좋을듯
      // #JS : (하지만 VUEX를 유지할지 안할지 몰라서 일단 이곳에 작성)
      this.$store.commit('SET_EDIT_STATE', true); // 편집을 가능 상태로 변경
      this.$store.commit('SET_REMOVED_MEMO_LIST', []); // 삭제했던 리스트 초기화
      this.$store.commit('SET_SELECTED_MEMO_IDX', -1); // 클릭된 메모 없음 상태로 변경
    },
    // 편집완료 처리
    editComplete() {
      this.saveMemo(); // 메모 저장
      this.$store.commit('SET_EDIT_STATE', false); // 편집 불가능 상태로 변경
      this.$store.commit('SET_REMOVED_MEMO_LIST', []); // 삭제했던 리스트 초기화
      this.$store.commit('SET_SELECTED_MEMO_IDX', -1); // 클릭된 메모 없음 상태로 변경
      this.$router.replace({ name: 'Desk' });
    },
    saveMemo() {
      // TODO: 비동기처리 필수
      this.$store.dispatch('SAVE_MEMO_LIST');
      this.$store.dispatch('DELETE_MEMO_LIST');
    },

    // 글 작성용 모달
    toggleModal: function() {
      this.isOpenModal = !this.isOpenModal;
    },

    // ======================================================
    // Moveable 제어용 메소드
    handleDrag({ target, transform }) {
      target.style.transform = transform;
    },
    handleRotate({ target, transform }) {
      target.style.transform = transform;
    },
    handleScale({ target, transform }) {
      target.style.transform = transform;
    },
    handleRenderEnd(index, event) {
      this.$store.commit('SET_MEMO_TRANSFORM_BY_INDEX', {
        index,
        transform: event.target.style.transform,
      });
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

  /* 에디팅 관련 영역 */
  .top-editor-wrapper {
    position: fixed;
    top: 1vmax;
    left: 50%;
    transform: translate(-50%, 1vmax);

    width: 300px;
    height: 90px;

    border-radius: 50px;
    background-color: white;
    box-shadow: 0px 9px 20px 0px #56565629;
    z-index: 30;

    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    padding: 0px 20px;

    .first-row {
      width: 80%;
      display: flex;
      align-items: center;
      justify-content: space-around;

      span {
        font-size: 10pt;
        font-weight: 600;
        margin-bottom: 7px;
      }
    }

    .second-row {
      width: 90%;
      display: flex;
      align-items: center;
      justify-content: space-around;
      margin-top: 10px;

      background-color: rgba(227, 227, 227, 0.495);
      border-radius: 30px;
      padding: 5px;
    }

    .btn {
      width: 25px;
      height: 25px;
      cursor: pointer;

      &.btn-add {
        background-image: url('../assets/img/emoji/pencil.png');
        background-repeat: no-repeat;
        background-size: cover;
      }
      &.btn-delete {
        background-image: url('../assets/img/emoji/wastebasket.png');
        background-repeat: no-repeat;
        background-size: cover;
      }
      &.btn-write {
        background-image: url('../assets/img/emoji/write.png');
        background-repeat: no-repeat;
        background-size: cover;
      }
      &.btn-color {
        width: 20px;
        height: 20px;
        border: 1px solid rgb(159, 159, 159);
        border-radius: 50%;
        margin: 3px;
      }
      &.btn-save {
        background-image: url('../assets/img/emoji/save.png');
        background-repeat: no-repeat;
        background-size: cover;
      }
    }

    /* .color-box {
         width: 70px;
         display: flex;
         flex-wrap: wrap;
      } */
  }

  /* 메모 들어가는 영역 */
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

      .calendar-box {
        position: absolute;

        top: 25px;
        left: 15px;

        color: rgb(58, 58, 58);
        font-size: 10pt;
        font-weight: 600;
        letter-spacing: 5px;
        text-align: center;
        line-height: 300px;

        width: 240px;
        height: 300px;

        border-radius: 20px;
        border: 1px solid #707070;
        background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAQUlEQVQoU2NkIAIUFBTMZCSkDqRowoQJ6QQVwgzCqRBmEl6F6IpAijFMxKYIq0JcnoObiMskFDcSUgS2mhhFIIUA1IId9JIePAcAAAAASUVORK5CYII=)
          repeat;

        z-index: -1;
      }
    }
  }
}

/* 메모 입력 모달 */
.memoInputModal {
  position: fixed; /* Stay in place */
  z-index: 100; /* Sit on top */

  width: 100%; /* Full width */
  height: 100%; /* Full height */
  overflow: auto; /* Enable scroll if needed */

  background-color: rgb(0, 0, 0); /* Fallback color */
  background-color: rgba(0, 0, 0, 0.5); /* Black w/ opacity */

  /* 본 CONTENTS */
  .memo-Modal-content {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);

    width: 320px;
    height: 480px;

    background-color: white;
    border-radius: 20px;
    box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.25);

    display: flex;
    flex-direction: column;
    align-items: center;

    p.title {
      font-weight: 600;
      font-size: 12pt;
      margin-top: 30px;
      margin-bottom: 20px;
    }
    p.detail {
      font-size: 6pt;
      color: gray;
    }
    p.desc {
      margin-top: 10px;
      font-size: 8pt;
      color: rgb(84, 84, 84);
    }

    textarea {
      width: 100%;
      height: 300px;
      padding: 16px;

      resize: none;
      outline: none;
      border: none;
      border-top: 1px solid rgb(216, 216, 216);
      border-bottom: 1px solid rgb(216, 216, 216);
      background-color: transparent;

      font-size: 16pt;
      line-height: 30px;
      letter-spacing: 1.5px;

      &:focus {
        outline: none;
      }
      &::placeholder {
        color: #2d2d2d;
      }
    }

    .btn-close {
      position: absolute;
      bottom: 0;

      width: 100%;
      height: 60px;
      cursor: pointer;

      background-color: rgba(0, 0, 0, 0.4);
      border-radius: 0 0 20px 20px;

      font-weight: 600;
      color: white;
      letter-spacing: 8px;
      line-height: 60px;
      text-align: center;
    }
  }
}

/* 트랜지션 */
.memo-down-enter-active {
  transition: all 0.5s ease;
}
.memo-down-leave-active {
  transition: all 0.2s ease;
}
.memo-down-enter,
.memo-down-leave-to {
  transform: translateY(10px);
  opacity: 0;
}
</style>
