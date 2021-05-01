<template lang="">
   <div v-dragscroll="true" class="desk">
      <div class="info">
         <h2 class="userName kyoboHand">안양불빠따</h2>
         <h3 class="cate kyoboHand">#취업준비생</h3>
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

export default {
   name: 'Desk',
   components: { SvgDesk },
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
   methods: {},
};
</script>
<style scoped lang="scss">
@import 'src/assets/css/common';

/* $desk-width: 1280px; */

.desk {
   margin-top: $HeaderHeight;
   margin-bottom: 60px;

   width: 100%;
   height: calc(100% - #{$HeaderHeight});

   display: flex;
   flex-direction: column;

   overflow: hidden;
   /* overflow-x: auto; // hidden으로 해도, npm에서 드래그 제공 */

   .info {
      position: fixed;
      top: $HeaderHeight;
      left: 50%;
      transform: translateX(-50%);

      background-color: white;
      border-radius: 20px;
      border: 2px solid black;

      z-index: 11;

      width: 320px;
      height: 50px;

      text-align: center;

      .cate {
         margin-top: 5px;
         color: rgb(150, 150, 150);
      }
   }

   .desk-wrapper {
      cursor: grab; // 드래그 영역에선 grap으로 표시

      position: relative;
      border: 1px solid red;

      width: 1280px;
      height: auto;

      margin: 0 auto;

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
