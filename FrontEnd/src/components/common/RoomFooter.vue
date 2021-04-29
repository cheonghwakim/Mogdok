<template lang="">
   <div @mouseenter="showFooter" @mouseleave="hideFooter" class="footer floating">
      <div class="postit-wrapper">
         <div-post-it :type="'study'" :timer="'10:30:20'"></div-post-it>
      </div>
      <div class="content">
         <btn-my-desk class="btnMyDesk-wrapper"></btn-my-desk>
         <btn-command class="btnCommand-wrapper" :label="'공부 하자!'"></btn-command>
         <btn-leave-desk class="btnLeaveDesk-wrapper"></btn-leave-desk>
      </div>
      <div class="img-wrapper">
         <img src="@/assets/img/noteLong.svg" alt="" />
      </div>
   </div>
</template>
<script>
import DivPostIt from '@/components/ui/DivPostIt';
import BtnCommand from '@/components/ui/BtnCommand';
import BtnMyDesk from '@/components/ui/BtnMyDesk';
import BtnLeaveDesk from '@/components/ui/BtnLeaveDesk';

export default {
   name: 'Footer',
   components: { DivPostIt, BtnCommand, BtnMyDesk, BtnLeaveDesk },
   props: {},
   data() {
      return {
         isOpen: false,
      };
   },
   computed: {},
   // watch: {},
   //lifecycle area
   methods: {
      showFooter: function() {
         var footer = document.querySelector('.footer.floating');
         console.log('마우스가 위에');

         if (footer.classList.contains('borrow')) {
            footer.classList.remove('borrow');
         }
         footer.classList.add('jumping');
      },

      hideFooter: function() {
         var footer = document.querySelector('.footer.floating');
         console.log('마우스가 범위 밖으로');

         if (footer.classList.contains('jumping')) {
            footer.classList.remove('jumping');
         }
         footer.classList.add('borrow');
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

   /* animation: burrowing-reverse 2s 1s ease forwards; */
   /* background-color: rgb(173, 199, 254); */
   /* height: $footerHeight; */

   width: 50vw;
   max-width: 600px;

   height: auto;

   .postit-wrapper {
      position: absolute;
      top: -10%;
      left: 50%;
      transform: translateX(-50%);

      width: 15vw;
      min-width: 100px;
      max-width: 160px;
   }

   .content {
      /* background-color: blue; */
      width: 100%;
      position: absolute;
      top: 30%;

      .btnMyDesk-wrapper {
         position: absolute;
         left: 10%;
         width: 7vw;

         min-width: 50px;
         max-width: 70px;
      }

      .btnCommand-wrapper {
         position: absolute;
         left: 50%;
         transform: translateX(-50%);

         width: 15vw;
         min-width: 100px;
         max-width: 150px;
      }

      .btnLeaveDesk-wrapper {
         position: absolute;
         right: 10%;
         width: 10vw;

         min-width: 50px;
         max-width: 100px;
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
   animation: burrowing 2s 1s ease forwards;
}

.jumping {
   animation: burrowing-reverse 2s 1s ease forwards;
}
</style>
