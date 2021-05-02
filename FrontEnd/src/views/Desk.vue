<template lang="">
   <div v-dragscroll="true" class="desk">
      <div class="info">
         <div class="info-content">
            <btn-close class="btnClose" @onClick="exitDesk"></btn-close>
            <p class="userName kyoboHand">안양불빠따</p>
            <p class="cate kyoboHand">#취업준비생</p>
         </div>
         <div-banner></div-banner>
      </div>
      <div class="desk-wrapper">
         <div class="desk-draw-area"></div>
         <svg-desk></svg-desk>
      </div>
   </div>
</template>
<script>
import { dragscroll } from 'vue-dragscroll';
import SvgDesk from '@/components/svg/SvgDesk';
import DivBanner from '@/components/ui/DivBanner';
import BtnClose from '@/components/ui/BtnClose';

export default {
   name: 'Desk',
   components: { SvgDesk, DivBanner, BtnClose },
   props: {},
   data() {
      return {};
   },
   directives: {
      dragscroll,
   },
   computed: {},
   watch: {},
   //lifecycle area
   methods: {
      exitDesk: function() {
         let isExit = confirm(`책상을 떠나시겠습니까?`);

         if (isExit) {
            this.$router.push('/room');
         }
      },
   },
};
</script>
<style scoped lang="scss">
@import 'src/assets/css/common';

/* $desk-width: 1280px; */

.desk {
   /* margin-top: $HeaderHeight; */
   margin-bottom: 60px;

   width: 100%;
   height: calc(100% - #{$HeaderHeight});

   display: flex;
   flex-direction: column;

   overflow: hidden;
   /* overflow-x: auto; // hidden으로 해도, npm에서 드래그 제공 */

   .info {
      position: fixed;
      top: 1vmax;
      left: 50%;
      transform: translate(-50%, -5vmax);

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

   .desk-wrapper {
      cursor: grab; // 드래그 영역에선 grap으로 표시

      position: relative;
      border: 1px solid red;

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
         border: 1px solid blue;
      }
   }
}
</style>
