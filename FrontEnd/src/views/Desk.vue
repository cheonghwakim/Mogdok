<template lang="">
   <div class="desk">
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
import SvgDesk from '@/components/svg/SvgDesk';

export default {
   name: 'Desk',
   components: { SvgDesk },
   props: {},
   data() {
      return {};
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
   align-items: center;
   justify-content: center;

   overflow-x: auto;
   overflow-y: hidden;

   .info {
      position: fixed;
      top: $HeaderHeight;
      left: 50%;
      transform: translateX(-50%);

      border: 1px dashed red;

      width: 320px;
      height: 100px;

      text-align: center;

      .cate {
         margin-top: 5px;
         color: rgb(150, 150, 150);
      }
   }

   .desk-wrapper {
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
