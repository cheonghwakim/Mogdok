<template lang="">
   <div class="loading">
      <div class="lottie-wrapper">
         <v-lottie-player :width="'100%'" :height="'100%'" loop path="https://assets6.lottiefiles.com/private_files/lf30_fzfz6t0n.json" />
         <p class="kyoboHand">...LOADING...</p>
      </div>
   </div>
</template>
<script>
export default {};
</script>
<style scoped lang="scss">
.loading {
   position: fixed;
   top: 50%;
   left: 50%;
   transform: translate(-50%, -50%);

   /* width: 300px; */
   /* height: 347px; */
   width: 100vw;
   height: 100vh;

   background-color: rgba(0, 0, 0, 0.3);
   z-index: 100;

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;

   .lottie-wrapper {
      width: 150px;
      text-align: center;
   }

   p {
      font-weight: bold;
      font-size: 14pt;
      color: white;
   }
}
</style>
