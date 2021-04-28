<template lang="">
   <div class="room">
      <div class="deskList">
         <div class="deskItem" v-for="key in 100" :key="key">
            <room-desk></room-desk>
         </div>
      </div>
   </div>
</template>
<script>
import RoomDesk from '@/components/RoomDesk';

export default {
   name: 'Room',
   components: { RoomDesk },
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

.room {
   margin-top: $HeaderHeight;
   /* padding-bottom: $footerHeight; */

   width: 100%;
   height: calc(100% - #{$HeaderHeight});
   background-color: rgb(255, 253, 190);

   overflow-y: auto;

   .deskList {
      display: flex;
      flex-wrap: wrap;
      justify-content: center;

      .deskItem {
         width: 25vw;
         min-width: 150px;
         min-height: 200px;
         margin: 5px;
      }
   }
}
</style>
