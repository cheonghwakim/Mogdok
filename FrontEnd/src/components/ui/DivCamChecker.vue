<template lang="">
   <div class="camChecker">
      <!-- 최상단 닫기 버튼 -->
      <btn-close class="btnClose" @onClick="closeCam"></btn-close>

      <!-- 내용들이 들어가는 내부 공간 -->
      <div class="content">
         <div class="cam-wrapper">
            <div-tape></div-tape>
            <div class="cam"></div>
         </div>
         <!-- 안내 문구 -->
         <p class="info-wrapper">
            열정있는 스터디 문화를 위해 <br />
            공부하는 🖐을 보여주세요
         </p>

         <select name="camSelect">
            <option value="">카메라를 선택하세요</option>
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
         </select>

         <!-- 하단 버튼 -->
         <btn-underline :label="'START'" @onClick="startStudy"></btn-underline>
      </div>

      <!-- 바탕 이미지 -->
      <img src="@/assets/img/bubble.svg" alt="" />
   </div>
</template>
<script>
import btnUnderline from '@/components/ui/BtnUnderline';
import DivTape from '@/components/ui/DivTape';
import BtnClose from '@/components/ui/BtnClose';
export default {
   components: { btnUnderline, DivTape, BtnClose },
   methods: {
      closeCam: function() {
         this.$emit('onClickClose');
      },
      startStudy: function() {
         this.$emit('onClickStart');
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
   }

   .content {
      position: absolute;
      top: 30px;
      left: 10px;

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
            border: 1px solid black;
            background-color: rgba(0, 0, 0, 0.201);
         }
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
