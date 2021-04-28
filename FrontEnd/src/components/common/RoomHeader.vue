<template lang="">
   <div class="header floating">
      <div class="section">
         <h2>자유 열람 1실</h2>
         <num-person class="NumOfPerson" :current="this.roomCurrent" :limit="this.roomLimit"></num-person>
      </div>
      <close class="btnClose"></close>
   </div>
</template>
<script>
import NumPerson from '@/components/ui/NumPerson';
import Close from '@/components/ui/Close';

export default {
   name: 'Header',
   components: { NumPerson, Close },
   props: {},
   data() {
      return {
         roomCurrent: 20,
         roomLimit: 30,
      };
   },
   computed: {},
   watch: {},
   //lifecycle area
   methods: {},
};
</script>
<style scoped lang="scss">
@import 'src/assets/css/common';

.header {
   top: 0;
   background-color: rgb(255, 255, 255);
   width: 100%;
   height: $HeaderHeight;

   //flex-shrink: 0; // flex에 영향 없는 고정 요소 셋팅

   .section {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      height: $HeaderHeight;

      h2 {
         margin-bottom: 6px;
      }
   }

   .btnClose {
      position: absolute;
      top: 20px;
      right: 20px;
   }
}
</style>
