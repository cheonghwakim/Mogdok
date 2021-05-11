<template lang="">
   <div v-dragscroll="true" class="desk">
      <div>
         <button @click="editComplete">편집완료(저장)</button>
         <button @click="$store.dispatch('CREATE_MEMO', moveableState)">메모 생성</button>
         <button v-show="selectedMemoIdx >= 0" @click="$store.commit('REMOVE_MEMO')">
            이 메모 삭제하기
         </button>
         <button v-show="selectedMemoIdx >= 0" @click="$store.commit('SET_MEMO_EDIT_DIALOG', true)">
            내용 작성하기
         </button>
         <button v-for="index in 5" :key="'color' + index" v-show="editable && selectedMemoIdx >= 0" @click="$store.commit('SET_SELECTED_MEMO_COLOR', index)">색상-{{ index }}로 변경</button>
      </div>
      <!-- <div class="info borrow">
         <div class="info-content">
            <btn-close class="btnClose" @onClick="exitDesk"></btn-close>
            <p class="userName kyoboHand">편집화면</p>
            <p class="cate kyoboHand">#편집</p>
         </div>
         <div-banner></div-banner>
      </div> -->
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
               @mousedown.native="$store.dispatch('UPDATE_SELECTED_MEMO_UI_BY_INDEX', index)"
               :style="{ zIndex, transform }"
            >
               <svg-memo :text="content" :color="color"></svg-memo>
            </vue-moveable>
         </div>
         <svg-desk></svg-desk>
      </div>

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
// import DivBanner from '@/components/ui/DivBanner'; DivBanner
// import BtnClose from '@/components/ui/BtnClose'; BtnClose
import { mapState } from 'vuex';

export default {
   name: 'Desk',
   components: { SvgDesk, VueMoveable, SvgMemo },
   props: {},
   data() {
      return {
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
         dialog: (state) => state.deskedit.memoEditDialog, // 내용 변경을 위한 모달용
         removedMemoList: (state) => state.deskedit.removedMemoList, //삭제된 메모리스트
      }),
   },
   //lifecycle area
   mounted() {
      console.log('> DeskEdit : mounted');

      // 책상 편집 화면 초기 셋팅
      this.initDeskEdit();
   },
   methods: {
      // 책상 편집 초기 셋팅
      initDeskEdit: async function() {
         // 컨테이너를 넣을 요소를 객체에 할당
         const elem = document.getElementsByClassName('desk-draw-area')[0];
         this.moveableState.container = elem;

         // 편집하는 책상의 정보들을 VUEX에 셋팅
         await this.$store.dispatch('GET_DESK_ALL_MEMO_4_EDIT', this.moveableState);

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
