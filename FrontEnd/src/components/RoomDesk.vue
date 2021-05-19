<template lang="">
   <div class="RoomDesk">
      <!-- 본문 : 화면과 책상이 기록되는 공간 -->
      <div class="content">
         <!-- 자리의 상태에 따라 캠, 휴식, 빈 공간이 표시 -->
         <div v-if="streamManager">
            <ov-video class="cam" :stream-manager="streamManager"></ov-video>
         </div>
         <div class="resting" v-show="seat && !streamManager">
            <svg-sleeping class="character"></svg-sleeping>
         </div>
         <img src="@/assets/img/desk.svg" @click="$emit('click')" />
      </div>

      <!-- 헤더 : 회원 닉네임이 들어감 -->
      <div class="header" v-if="seat">
         {{ seat.userName }}
      </div>
      <!-- 푸터 : 하단에 시간이 기록되는 공간 -->
      <div class="footer">
         <!-- <p class="mark">🕳</p> -->
         <p class="timer" v-if="timer" :class="{ 'rest-time': !seat.isRunning }"><i class="fas fa-stopwatch"></i>{{ timer }}</p>
      </div>
   </div>
</template>
<script>
import SvgSleeping from '@/components/svg/SvgSleeping.vue';
import OvVideo from '@/components/common/OvVideo';
export default {
   components: { SvgSleeping, OvVideo },
   props: {
      seat: Object,
      streamManager: Object,
      timer: String,
   },
   data() {
      return {
         time: '',
      };
   },
   computed: {},
   methods: {},
};
</script>
<style scoped lang="scss">
// 책상의 비율은 3:4
// 높이는 4/3*너비 로 구함

* {
   /* border: 1px dashed red; */
}

.RoomDesk {
   position: relative;
   width: 100%;
   height: 100%;
   /* width: inherit; */
   /* min-width: inherit; */
   /* min-height: inherit; */

   /* border: 1px solid magenta; */

   display: flex;
   flex-direction: column;
   align-items: center;

   .content {
      width: 100%;
      height: 100%;

      .cam {
         position: absolute;
         top: 3%;
         left: 50%;
         transform: translateX(-50%);

         z-index: 4;

         width: 89%;
         height: 50%;
         border-radius: 10px;
         background-image: url('../assets/img/Spin-loading.gif');
         background-repeat: no-repeat;
         background-position: center;
         background-size: 60px 60px;
         /* background-color: rgba(150, 150, 150, 0.516); */
         /* border: 1px solid black; */
      }

      .resting {
         position: absolute;
         bottom: 45%;
         left: 48%;
         transform: translateX(-50%);
         z-index: 4;

         .character {
            margin-left: 18%;
            display: inline;
            /* background-color: red; */
         }
      }

      /* 책상 이미지 */
      img {
         cursor: pointer;

         position: absolute;
         bottom: 10%;
         left: 50%;
         transform: translateX(-50%);

         width: 80%;
         height: auto;
      }
   }

   .header {
      font-weight: 600;
      font-size: 11pt;
      letter-spacing: 1px;
      color: rgb(41, 41, 41);
   }

   .footer {
      position: absolute;
      bottom: 18px;

      display: flex;
      flex-direction: column;
      align-items: center;

      .mark {
         width: 90px;
         text-align: center;
         margin-bottom: 5px;
         font-size: 8pt;
      }
      .timer {
         font-size: 9pt;
         font-weight: 600;
         letter-spacing: 2px;
         opacity: 1;
         color: #cd0b0b;

         &.rest-time {
            color: #074f10;
            -webkit-animation-name: blinker;
            -webkit-animation-iteration-count: infinite;
            -webkit-animation-duration: 2.5s;
         }
      }
   }
}

@-webkit-keyframes blinker {
   0% {
      opacity: 0.2;
   }
   50% {
      opacity: 1;
   }
   100% {
      opacity: 0.2;
   }
}
</style>
