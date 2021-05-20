<template lang="">
   <div class="guide">
      <div class="bgm">
         <img src="../../assets/img/about/guide_bgm.png" alt="" />
      </div>
      <div class="controller">
         <img src="../../assets/img/about/guide_controller.png" alt="" />
      </div>
      <div class="desk-guide-wrapper">
         <div class="img-wrapper">
            <img src="../../assets/img/about/guide_desk.png" alt="" style="margin-right:30px" />
            <img src="../../assets/img/about/guide_desk2.png" alt="" />
         </div>
         <p @click="closeGuide">닫기</p>
      </div>
   </div>
</template>
<script>
export default {
   methods: {
      closeGuide: function() {
         this.$emit('onClick');
      },
   },
};
</script>
<style scoped lang="scss">
.guide {
   position: fixed;
   top: 0;
   left: 0;
   width: 100%;
   height: 100%;
   background-color: rgba(0, 0, 0, 0.5);
   z-index: 100;

   img {
      width: 100%;
   }

   .bgm {
      position: fixed;
      top: 95px;
      left: 55px;

      width: 150px;
   }

   .controller {
      position: fixed;
      left: 45%;
      bottom: -50px;
      width: 200px;
   }

   .desk-guide-wrapper {
      position: fixed;
      top: 45%;
      left: 50%;
      transform: translate(-50%, -50%);

      width: 280px;
      padding: 10px;
      border-radius: 20px;

      display: flex;
      flex-direction: column;
      align-items: center;

      background-color: white;
      box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);

      p {
         padding: 15px 0px;
         font-size: 10pt;
         font-weight: 600;
         color: rgb(48, 48, 48);
         text-decoration: underline;
         cursor: pointer;

         &:hover {
            color: black;
         }
      }

      .img-wrapper {
         display: flex;
      }

      img {
         width: 45%;
      }
   }
}
</style>
