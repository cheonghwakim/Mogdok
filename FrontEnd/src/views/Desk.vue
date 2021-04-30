<template lang="">
   <div class="desk">
      <div class="desk-wrapper">
         <svg-desk></svg-desk>
         <div class="desk-draw-area"></div>
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

$desk-width: 100vmax;

.desk {
   margin-top: $HeaderHeight;
   margin-bottom: 60px;

   width: 100%;
   height: calc(100% - #{$HeaderHeight});
   /* background-color: rgb(255, 253, 190); */

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;

   .desk-wrapper {
      position: relative;
      border: 1px solid red;

      width: $desk-width;
      height: auto;

      .desk-draw-area {
         position: absolute;

         top: 47%;
         left: 50%;
         transform: translate(-50%, -50%);

         width: calc(#{$desk-width} * 0.77);
         height: calc(#{$desk-width} * 0.77 * 5 / 9);
         border: 1px solid blue;
      }
   }
}
</style>
