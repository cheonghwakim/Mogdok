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
               <div class="btn btn-write" @click="$store.commit('SET_MEMO_EDIT_DIALOG', true)"></div>
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
               @mousedown.native="$store.dispatch('UPDATE_SELECTED_MEMO_UI_BY_INDEX', index)"
               :style="{ zIndex, transform }"
            >
               <svg-memo :text="content" :color="color"></svg-memo>
            </vue-moveable>
         </div>
         <svg-desk></svg-desk>
      </div>

      <!-- 글 작성 모달 -->
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
