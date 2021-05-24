<template lang="">
   <div class="camChecker">
      <!-- 최상단 닫기 버튼 -->
      <btn-close v-wave class="btnClose" @onClick="closeCam"></btn-close>

      <!-- 내용들이 들어가는 내부 공간 -->
      <div class="content">
         <div class="cam-wrapper" v-if="publisher">
            <ov-video class="cam" :streamManager="publisher"></ov-video>
         </div>
         <!-- 캠이 안보일 경우 -->
         <div class="cam-wrapper" v-else>
            <div class="cam"></div>
         </div>
         <!-- 안내 문구 -->
         <p class="info-wrapper">
            열정있는 스터디 문화를 위해 <br />
            공부하는 🖐을 보여주세요
         </p>

         <select name="camSelect" v-model="changeVideoSource">
            <option v-for="(videoSource, index) in videoSourceList" :key="videoSource.deviceId + index" :value="videoSource.deviceId">{{ videoSource.label }}</option>
         </select>

         <!-- 하단 버튼 -->
         <!-- TODO : startable을 이용해서 클릭가능/불가능 UI 변경 -->
         <btn-underline :color="startable ? 'black' : 'lightgrey'" :label="'공부 시작'" @onClick="startStudy"></btn-underline>
      </div>
      <!-- 바탕 이미지 -->
      <img src="@/assets/img/bubble.svg" alt="" />
   </div>
</template>
<script>
import btnUnderline from '@/components/ui/BtnUnderline';
import BtnClose from '@/components/ui/BtnClose';
import OvVideo from '../common/OvVideo';
import { mapState } from 'vuex';
export default {
   components: { btnUnderline, BtnClose, OvVideo },
   props: {
      startable: Boolean,
   },
   computed: {
      ...mapState({
         videoSourceList: (state) => state.user.videoSourceList,
         videoSource: (state) => state.user.videoSource,
         publisher: (state) => state.openvidu.publisher,
      }),
      changeVideoSource: {
         get() {
            return this.videoSource;
         },
         async set(value) {
            this.$emit('toggleStartable', false);
            // this.startable = false;
            this.$store.commit('SET_VIDEO_SOURCE', value);
            await this.$store.dispatch('CHANGE_CAMERA'); // 비디오 소스를 변경하면 카메라를 끄고 다시 켬
            this.$emit('toggleStartable', true);
            // this.startable = true;
         },
      },
   },
   methods: {
      closeCam: function() {
         this.$emit('onClickClose');
      },
      startStudy: function() {
         if (this.startable) this.$emit('onClickStart');
      },
   },
};
</script>
<style scoped lang="scss">
.camChecker {
   width: 320px;
   height: 450px;
   z-index: 1;

   .btnClose {
      position: absolute;
      top: 20px;
      right: 25px;

      width: 40px;
      height: 40px;

      background-color: white;
      border: 2px solid black;
      padding: 8px;
      border-radius: 50%;
   }

   .content {
      position: absolute;
      top: 30px;
      left: 15px;

      /* border: 1px solid blue; */

      width: 90%;
      height: 80%;

      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: space-between;

      .cam-wrapper {
         position: relative;
         width: 280px;
         height: calc(280px * 0.75);

         .cam {
            width: 100%;
            height: 100%;
            border-radius: 10px;
            background-image: url('../../assets/img/Spin-loading.gif');
            background-repeat: no-repeat;
            background-position: center;
            background-size: 60px 60px;
         }
      }

      select {
         width: 250px;
         outline: none;
      }

      .info-wrapper {
         text-align: center;
         font-size: 10pt;
         color: rgb(60, 60, 60);
         line-height: 14pt;
      }
   }
}
</style>
