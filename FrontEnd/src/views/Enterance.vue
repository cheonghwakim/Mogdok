<template lang="">
   <div class="enterance">
      <div class="container">
         <div class="content">
            <sagam></sagam>
            <div class="roomList">
               <div class="roomItem" v-for="(room, idx) in roomList" :key="idx">
                  <enterance-door :room="room"></enterance-door>
               </div>
            </div>
         </div>
         <logo></logo>
      </div>
   </div>
</template>
<script>
import Sagam from '@/components/svg/Sagam';
import Logo from '@/components/ui/Logo';
import EnteranceDoor from '@/components/EnteranceDoor';

export default {
   name: 'Enterance',
   components: { Sagam, Logo, EnteranceDoor },
   props: {},
   data() {
      return {
         roomList: [],
      };
   },
   computed: {},
   watch: {},
   //lifecycle area
   created() {
      this.sampleData(); // Sample Data 시작하자마 삽입
   },
   methods: {
      // Sample Data 삽입
      sampleData: function() {
         this.roomList = [
            {
               roomNumber: 1,
               roomTitle: '자유 열람 1실',
               roomCurrent: 12,
               roomLimit: 30,
            },
            {
               roomNumber: 2,
               roomTitle: '자유 열람 2실',
               roomCurrent: 34,
               roomLimit: 50,
            },
            {
               roomNumber: 3,
               roomTitle: '고삼 전용',
               roomCurrent: 4,
               roomLimit: 30,
            },
         ];
      },
   },
};
</script>
<style scoped lang="scss">
.enterance {
   width: 100%;
   height: 100%;

   display: flex;
   align-items: center;
   justify-content: center;

   .container {
      display: inherit;
      flex-direction: column;
      justify-content: space-between;
      align-items: center;
      width: 70vw;
      height: 90vh;

      border: 1px solid red;

      .content {
         margin-top: 5vh;
         width: inherit;
         display: inherit;
         flex-direction: column;
         align-items: center;

         .roomList {
            border: 1px solid blue;

            width: 100%;
            height: 200px;

            display: flex;
            justify-content: center;

            .roomItem {
               margin: 0px 5px;
            }
         }
      }
   }
}
</style>
