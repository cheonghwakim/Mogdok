<template lang="">
  <div class="enterance">
    <div class="container">
      <div class="content">
        <svg-sagam></svg-sagam>
        <div class="roomList">
          <div class="roomItem" v-for="(room, idx) in roomList" :key="idx">
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

export default {
  name: 'Enterance',
  components: { SvgSagam, Logo, EnteranceDoor },
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
        // Todo: 서버에서 룸 리스트 정보 받아와야 함
        {
          roomNumber: 1,
          roomTitle: '자유 열람 1실',
          roomCurrent: 12,
          roomLimit: 16,
          sessionId: 'sessionA',
        },
        {
          roomNumber: 2,
          roomTitle: '자유 열람 2실',
          roomCurrent: 34,
          roomLimit: 20,
          sessionId: 'sessionB',
        },
        {
          roomNumber: 3,
          roomTitle: '고삼 전용',
          roomCurrent: 4,
          roomLimit: 16,
          sessionId: 'sessionC',
        },
      ];
    },
    enterRoom(room) {
      this.$store.commit('SET_ROOMINFO', room);
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

    /* border: 1px solid red; */

    .content {
      margin-top: 5vh;
      width: inherit;
      display: inherit;
      flex-direction: column;
      align-items: center;

      // roomList가 모여있는 공간
      .roomList {
        margin-top: 20px;

        /* border: 1px solid blue; */

        // 방 갯수가 넘쳤을 경우, 가로 스크롤 제공
        overflow-x: auto;
        overflow-y: hidden;

        /* padding: 0 50px; */

        width: 100%;
        height: 220px;

        display: flex;
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
