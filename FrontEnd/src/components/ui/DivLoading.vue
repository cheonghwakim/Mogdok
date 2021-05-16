<template lang="">
   <div class="loading">
      <v-lottie-player :width="'100%'" :height="'100%'" loop path="https://assets6.lottiefiles.com/private_files/lf30_fzfz6t0n.json" />
      <p class="kyoboHand">...LOADING...</p>
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

   width: 300px;
   height: 347px;
   padding-bottom: 30px;
   /* height: 100vh; */

   background-color: rgba(0, 0, 0, 0.2);
   border-radius: 20px;
   z-index: 100;

   display: flex;
   flex-direction: column;
   align-items: center;

   p {
      font-weight: bold;
      font-size: 12pt;
   }
}
</style>
