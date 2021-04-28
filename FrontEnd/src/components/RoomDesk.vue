<template lang="">
   <div class="RoomDesk">
      <!-- 헤더 : 회원 닉네임이 들어감 -->
      <div class="header">
         <h3>안양불빠따</h3>
      </div>

      <!-- 본문 : 화면과 책상이 기록되는 공간 -->
      <div class="content">
         <!-- 자리의 상태에 따라 캠, 휴식, 빈 공간이 표시 -->
         <div class="cam">
            사용자 화면
         </div>
         <div class="resting">
            <sleeping class="character"></sleeping>
         </div>
         <img src="@/assets/img/desk.svg" />
      </div>

      <!-- 푸터 : 하단에 시간이 기록되는 공간 -->
      <div class="footer">
         <p class="mark">🕳</p>
         <p class="timer">00:00:00</p>
      </div>
   </div>
</template>
<script>
import Sleeping from '@/components/svg/Sleeping.vue';
export default {
   components: { Sleeping },
   props: {},
};
</script>
<style scoped lang="scss">
$deskWidth: 20vw;

// 책상의 비율은 3:4
// 높이는 4/3*너비 로 구함

.RoomDesk {
   position: relative;
   width: inherit;
   min-width: inherit;
   height: calc(#{$deskWidth} * 4 / 3);
   min-height: inherit;

   /* border: 1px solid red; */

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

         width: 100%;
         height: 50%;
         border-radius: 20px;
         background-color: rgba(62, 62, 62, 0.516);

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
