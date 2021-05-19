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
        <div class="bottom-shader"></div>
        <p class="btnFAQ" @click="toggleModalFAQ">
          <i class="fas fa-question-circle"></i> 자주 묻는 질문
        </p>
        <modal-faq :isOpenFAQ="isOpenFAQ" @onClick="toggleModalFAQ"></modal-faq>
      </div>
      <div class="grey-btn" @click="getAllRooms">새로고침</div>
      <div class="grey-btn" @click="logout">로그아웃</div>
      <logo></logo>
    </div>
  </div>
</template>
<script>
import SvgSagam from '@/components/svg/SvgSagam';
import Logo from '@/components/ui/Logo';
import ModalFaq from '@/components/ui/ModalFaq';
import EnteranceDoor from '@/components/EnteranceDoor';
import { mapState } from 'vuex';

export default {
  name: 'Enterance',
  components: { SvgSagam, Logo, EnteranceDoor, ModalFaq },
  props: {},
  data() {
    return {
      isOpenFAQ: false,
    };
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
  async created() {
    await this.getAllRooms();
  },
  methods: {
    async getAllRooms() {
      try {
        await this.$store.dispatch('GET_ALL_ROOMS');
      } catch (error) {
        alert('열람실 목록을 가져오는데 실패했어요. ' + error);
      }
    },
    enterRoom(room) {
      this.$store.dispatch('ENTER_ROOM', room);
      this.$router.replace({ name: 'Room' });
    },
    toggleModalFAQ() {
      this.isOpenFAQ = !this.isOpenFAQ;
    },
    logout() {
      this.$store.dispatch('LOGOUT');
      this.$router.replace({ path: '/login' });
    },
  },
};
</script>
<style scoped lang="scss">
.grey-btn {
  font-size: 10pt;
  font-weight: 600;
  padding: 15px;
  border-radius: 10px;
  cursor: pointer;
  color: grey;
  text-decoration: underline;
  letter-spacing: 2px;
}
.grey-btn:hover {
  font-size: 10pt;
  font-weight: 600;
  padding: 15px;
  border-radius: 10px;
  cursor: pointer;
  color: black;
  background-color: rgb(243, 243, 243);
  text-decoration: underline;
  letter-spacing: 2px;
}
.grey-btn:active {
  font-size: 10pt;
  font-weight: 600;
  padding: 15px;
  border-radius: 10px;
  cursor: pointer;
  color: black;
  background-color: rgb(228, 228, 228);
  text-decoration: underline;
  letter-spacing: 2px;
}

.enterance {
  width: 100%;
  height: 100%;

  display: flex;
  align-items: center;
  justify-content: center;

  /* border: 1px solid red; */

  .container {
    display: inherit;
    flex-direction: column;
    justify-content: space-between;
    align-items: center;
    width: 70vw;
    height: 90vh;

    /* border: 1px solid rgb(34, 0, 255); */

    .content {
      /* margin-top: 5vh; */
      width: inherit;
      position: relative;
      display: inherit;
      flex-direction: column;
      align-items: center;
      /* border: 1px solid rgb(255, 68, 0); */

      .character {
        max-width: 200px;
        width: 60%;
      }

      // roomList가 모여있는 공간
      .roomList {
        margin-top: 20px;
        position: relative;

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

      .bottom-shader {
        /* border: 1px solid blue; */
        position: absolute;
        bottom: 0px;
        width: 100%;
        height: 60px;

        z-index: 50;

        background-image: linear-gradient(
          0deg,
          rgb(255, 255, 255),
          rgba(255, 255, 255) 40%,
          rgba(255, 255, 255, 0)
        );
      }

      .btnFAQ {
        position: absolute;
        bottom: -10px;
        z-index: 51;

        cursor: pointer;
        font-size: 10pt;
        font-weight: 600;
        text-align: center;
        letter-spacing: 2px;
        color: rgb(176, 176, 176);

        margin-top: 12px;
      }
    }
  }
}
</style>
