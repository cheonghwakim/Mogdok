<template lang="">
  <div class="header floating">
    <div class="section">
      <h2>{{ roomInfo.name }}</h2>
      <div-num-person
        class="NumOfPerson"
        :current="roomUserCount"
        :limit="roomInfo.limitUserCount"
      ></div-num-person>
    </div>
    <btn-close class="btnClose" @onClick="exitRoom"></btn-close>
  </div>
</template>
<script>
import DivNumPerson from '@/components/ui/DivNumPerson';
import BtnClose from '@/components/ui/BtnClose';
import { mapState } from 'vuex';

export default {
  name: 'Header',
  components: { DivNumPerson, BtnClose },
  props: {},
  data() {
    return {
      roomCurrent: 20,
      roomLimit: 30,
    };
  },
  computed: {
    ...mapState({
      roomInfo: (state) => state.user.roomInfo,
      roomUserCount: (state) => state.room.roomUserCount,
    }),
  },
  watch: {},
  //lifecycle area
  methods: {
    exitRoom: async function() {
      let isExit = confirm(`🚪 방을 나갈까요?`);

      if (isExit) {
        // TODO : 로딩중필요
        await this.$store.dispatch('LEAVE_SESSION');
        this.$store.commit('CLEAR_CONNECT');
        this.$router.replace('/');
      }
    },
  },
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
    right: 30px;

    width: 20px;
    height: auto;
  }
}
</style>
