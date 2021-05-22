<template lang="">
   <div class="div-temp">
      <div v-wave class="btn-temp-wrapper" @click="goURL">
         <img src="../../assets/img/emoji/bell.png" alt="" />
      </div>
   </div>
</template>
<script>
export default {
   methods: {
      goURL() {
         window.open('https://forms.gle/Ggp8dWPia4jfHthU7', '_blank');
      },
   },
};
</script>
<style scoped lang="scss">
.div-temp {
   position: fixed;
   top: 130px;
   left: 30px;

   /* 반응형 크기 */
   @media all and (max-width: 321px) {
      & {
         left: 10px;
      }
   }
}

.btn-temp-wrapper {
   cursor: pointer;
   z-index: 20;

   width: 45px;
   height: 45px;
   border-radius: 50%;
   background-color: rgb(255, 255, 255);
   box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;

   img {
      width: 40%;
      height: 40%;
   }
}
</style>
