<template lang="">
   <div class="RoomDesk">
      <!-- 헤더 : 회원 닉네임이 들어감 -->
      <div class="header" v-if="seat">
         <h3>{{ seat.userName }}</h3>
      </div>

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

      <!-- 푸터 : 하단에 시간이 기록되는 공간 -->
      <div class="footer">
         <!-- <p class="mark">🕳</p> -->
         <p class="timer" v-if="timer">{{ timer }}</p>
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
$deskWidth: 20vw;

// 책상의 비율은 3:4
// 높이는 4/3*너비 로 구함

.RoomDesk {
   position: relative;
   width: 100%;
   height: 100%;
   /* width: inherit; */
   /* min-width: inherit; */
   /* height: calc(#{$deskWidth} * 4 / 3); */
   /* min-height: inherit; */

   border: 1px solid magenta;

   display: flex;
   flex-direction: column;
   align-items: center;

   .header {
      margin-top: 5%;
   }

   .content {
      width: 100%;
      height: 100%;

      .cam {
         position: absolute;
         top: 13%;
         left: 50%;
         transform: translateX(-50%);

         z-index: 4;

         width: 88.9%;
         height: 50%;
         border-radius: 10px;
         background-image: url('../assets/img/discover.gif');
         background-repeat: no-repeat;
         background-size: cover;
         background-color: rgba(150, 150, 150, 0.516);

         /* 임시용 */
         color: white;
         text-align: center;
         font-size: 1rem;
      }

      .resting {
         position: absolute;
         bottom: 35%;
         left: 48%;
         transform: translateX(-50%);
         z-index: 4;

         .character {
            margin-left: 18%;
            display: inline;
            /* background-color: red; */
         }
      }

      img {
         cursor: pointer;

         position: absolute;
         bottom: 0;
         left: 50%;
         transform: translateX(-50%);

         width: 80%;
         height: auto;
      }
   }

   .footer {
      position: absolute;
      bottom: 0px;

      display: flex;
      flex-direction: column;
      align-items: center;

      .mark {
         width: 90px;
         text-align: center;
         margin-bottom: 5px;
         font-size: 0.7rem;
      }
      .timer {
         font-size: 0.7rem;
         opacity: 1;
      }
   }
}
</style>
