<template lang="">
   <div class="enterance">
      <div class="container">
         <div class="content">
            <svg-sagam class="character"></svg-sagam>
            <div class="roomList">
               <div v-dragscroll="true" class="roomItem" v-for="(room, idx) in roomList" :key="idx">
                  <enterance-door :room="room" @click="enterRoom(room)"></enterance-door>
               </div>
            </div>
         </div>
         <logo></logo>
      </div>
   </div>
</template>
<script>
import SvgSagam from '@/components/svg/SvgSagam';
import Logo from '@/components/ui/Logo';
import EnteranceDoor from '@/components/EnteranceDoor';
import { mapState } from 'vuex';

export default {
   name: 'Enterance',
   components: { SvgSagam, Logo, EnteranceDoor },
   props: {},
   data() {
      return {};
   },
   computed: {
      ...mapState({
         OV: (state) => state.openvidu.OV,
         roomList: (state) => state.room.roomList,
         roomInfo: (state) => state.room.roomInfo,
         userInfo: (state) => state.user.userInfo,
      }),
   },
   watch: {},
   //lifecycle area
   created() {
      this.$store.dispatch('GET_ALL_ROOMS');
   },
   methods: {
      enterRoom(room) {
         // TODO : 입장 전 해당 방의 openvidu session과 room서버 연결 완료하고 페이지 이동
         this.$store.commit('SET_ROOM_INFO', room);
         this.$router.replace({ name: 'Room' });
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

      .content {
         margin-top: 5vh;
         width: inherit;
         display: inherit;
         flex-direction: column;
         align-items: center;
         /* border: 1px solid rgb(255, 68, 0); */

         .character {
            max-width: 200px;
            width: 70%;
         }

         // roomList가 모여있는 공간
         .roomList {
            margin-top: 20px;

            /* border: 1px solid blue; */

            // 방 갯수가 넘쳤을 경우, 가로 스크롤 제공
            overflow-y: auto;
            overflow-x: hidden;

            width: 100%;
            height: 220px;

            display: flex;
            flex-wrap: wrap;
            justify-content: center;

            // 개별 room (=door)
            .roomItem {
               margin: 0px 5px;
            }
         }
      }
   }
}
</style>
